package edu.thaishungw.vietbank.controllers;

import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.mservice.shared.exception.MoMoException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ch.qos.logback.core.model.Model;

import edu.thaishungw.vietbank.models.HttpResponse;
import edu.thaishungw.vietbank.models.RequestObject;
import edu.thaishungw.vietbank.models.ResponseObject;
import edu.thaishungw.vietbank.utils.AbstractProcess;
import edu.thaishungw.vietbank.utils.Encoder;
import edu.thaishungw.vietbank.utils.Excute;

@Controller
public class VietBankController extends AbstractProcess {

    @RequestMapping("/")
    public String index(Model model) {

        return "index";
    }

    @RequestMapping("/login")
    public String Login(Model model) {

        return "login";
    }

    @RequestMapping("/hi")
    @ResponseBody
    public String Hi() {

        return "Hello world!";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    @ResponseBody
    public RedirectView createCheckout() {
        try {

            // String shippingAddress = (String) request.get("shippingAddress");
            // Double itemsPrice = (Double) request.get("itemsPrice");
            // Double taxPrice = (Double) request.get("taxPrice");
            // Double shippingPrice = (Double) request.get("shippingPrice");
            // Double totalPrice = (Double) request.get("totalPrice");
            // String voucher = (String) request.get("voucher");

            // if (shippingAddress == null) {
            // return ResponseEntity.badRequest().body("shippingAddress not found");
            // }
            // if (shippingPrice == null) {
            // return ResponseEntity.badRequest().body("shippingPrice not found");
            // }

            // Perform your logic here
            String accessKey = "F8BBA842ECF85";
            String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
            String orderInfo = "pay with MoMo";
            String partnerCode = "MOMO";
            String redirectUrl = "http://localhost:3000/order-list";
            String ipnUrl = "https://tlcn-2022-be.onrender.com/api/orders/ipn";
            String requestType = "captureWallet";
            long amount = 50000;
            String orderId = String.valueOf(System.currentTimeMillis());
            String requestId = String.valueOf(System.currentTimeMillis());
            String lang = "vi";
            String extraData = "";

            String rawSignature = new StringBuilder()
                    .append("accessKey").append("=").append(accessKey).append("&")
                    .append("amount").append("=").append(amount).append("&")
                    .append("extraData").append("=").append(extraData).append("&")
                    .append("ipnUrl").append("=").append(ipnUrl).append("&")
                    .append("orderId").append("=").append(orderId).append("&")
                    .append("orderInfo").append("=").append(orderInfo).append("&")
                    .append("partnerCode").append("=").append(partnerCode).append("&")
                    .append("redirectUrl").append("=").append(redirectUrl).append("&")
                    .append("requestId").append("=").append(requestId).append("&")
                    .append("requestType").append("=").append(requestType)
                    .toString();

            // Signature
            String signature = Encoder.generateSignature(rawSignature, secretKey);

            System.out.println("signature " + signature);
            RequestObject request = new RequestObject();
            request.setPartnerCode(partnerCode);
            request.setRequestId(requestId);
            request.setAmount(Long.valueOf(amount));
            request.setOrderId(orderId);
            request.setOrderInfo(orderInfo);
            request.setRedirectUrl(redirectUrl);
            request.setIpnUrl(ipnUrl);
            request.setRequestType(requestType);
            request.setExtraData(extraData);
            request.setSignature(signature);
            request.setLang(lang);
            String payload = getGson().toJson(request, RequestObject.class);
            System.out.println("payload: " + payload);
            Excute excute = new Excute();
            HttpResponse response = excute.sendToMoMo("https://test-payment.momo.vn/v2/gateway/api/create", payload);
            if (response.getStatus() != 200) {
                throw new MoMoException("[PaymentResponse] [" + response.getData() + "] -> Error API");
            }
            ResponseObject responseObject = getGson().fromJson(response.getData(), ResponseObject.class);
            System.out.println(responseObject);
            return new RedirectView(responseObject.getPayUrl());
            // return ResponseEntity.ok(responseObject);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid params capture MoMo Request");
        }
    }

    @PostMapping("/payment-notification")
    public ResponseEntity<?> paymentNotification(@RequestBody Map<String, Object> request) {
        try {
            String partnerCode = (String) request.get("partnerCode");
            String orderId = (String) request.get("orderId");
            String requestId = (String) request.get("requestId");

            if (partnerCode == null || orderId == null || requestId == null) {
                return ResponseEntity.badRequest().body("Missing partnerCode, orderId, or requestId");
            }

            String rawSignature = "accessKey=" + System.getenv("accessKey") +
                    "&orderId=" + orderId +
                    "&partnerCode=" + partnerCode +
                    "&requestId=" + requestId;

            String signature = encode(System.getenv("secretKey"), rawSignature);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("requestId", requestId);
            requestBody.put("orderId", orderId);
            requestBody.put("signature", signature);
            requestBody.put("lang", "vi");

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://test-payment.momo.vn:443/v2/gateway/api/create",
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/query-checkout")
    public ResponseEntity<?> queryCheckout(@RequestBody Map<String, Object> request) {
        try {
            String orderId = (String) request.get("orderId");
            String requestId = (String) request.get("requestId");

            if (orderId == null || requestId == null) {
                return ResponseEntity.badRequest().body("Missed orderId or requestId");
            }

            String accessKey = System.getenv("accessKey");
            String partnerCode = System.getenv("partnerCode");
            String secretKey = System.getenv("secretKey");

            String rawSignature = "accessKey=" + accessKey +
                    "&orderId=" + orderId +
                    "&partnerCode=" + partnerCode +
                    "&requestId=" + requestId;

            String signature = encode(secretKey, rawSignature);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("requestId", requestId);
            requestBody.put("orderId", orderId);
            requestBody.put("signature", signature);
            requestBody.put("lang", "vi");

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://test-payment.momo.vn:443/v2/gateway/api/query",
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    String.class);

            // Update order status or perform other actions based on the response

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    @Override
    public Object execute(Object request) throws MoMoException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}
