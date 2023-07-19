package edu.thaishungw.vietbank.controllers;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.mservice.shared.exception.MoMoException;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import edu.thaishungw.vietbank.configs.EnvMomo;
import edu.thaishungw.vietbank.models.CartRequest;
import edu.thaishungw.vietbank.models.HttpResponse;
import edu.thaishungw.vietbank.models.InpMomo;
import edu.thaishungw.vietbank.models.LoginRequest;
import edu.thaishungw.vietbank.models.Order;
import edu.thaishungw.vietbank.models.RequestObject;
import edu.thaishungw.vietbank.models.ResponseObject;
import edu.thaishungw.vietbank.models.Users;
import edu.thaishungw.vietbank.service.OrderService;
import edu.thaishungw.vietbank.service.UserService;
import edu.thaishungw.vietbank.utils.AbstractProcess;
import edu.thaishungw.vietbank.utils.Encoder;
import edu.thaishungw.vietbank.utils.Excute;
import jakarta.servlet.http.HttpSession;

@RestController
public class VietBankController extends AbstractProcess {
    private final UserService userService;
    private final OrderService orderService;

    public VietBankController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @RequestMapping("/users")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody LoginRequest request, HttpSession session) {
        String email = request.getEmail();
        String password = request.getPassword();
        Users user = userService.findByUserEmail(email);
        Users userSession = (Users) session.getAttribute("loggedInUser");
        if (userSession != null) {
            if (userSession.getEmail().equals(user.getEmail())
                    && userSession.getPassword().equals(user.getPassword())) {
                System.out.println("loggedInUser");
                return ResponseEntity.ok("Login successful!"); // Trả về 200 OK nếu đăng nhập thành công
            }
        }
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("miss loggedInUser");
            session.setAttribute("loggedInUser", user);
            return ResponseEntity.ok("Login successful!"); // Trả về 200 OK nếu đăng nhập thành công
        } else {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid credentials");
            // Trả về 401 Unauthorized nếu đăng nhập thất bại
        }
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject createCheckout(@RequestBody CartRequest request) {
        try {

            int price = request.getPrice();
            int count = request.getCount();
            EnvMomo env = new EnvMomo();
            long amount = price * count;
            String orderId = String.valueOf(System.currentTimeMillis());
            String requestId = String.valueOf(System.currentTimeMillis());
            String lang = "vi";
            String extraData = "";
            String rawSignature = new StringBuilder()
                    .append("accessKey").append("=").append(env.getAccessKey()).append("&")
                    .append("amount").append("=").append(amount).append("&")
                    .append("extraData").append("=").append(extraData).append("&")
                    .append("ipnUrl").append("=").append(env.getIpnUrl()).append("&")
                    .append("orderId").append("=").append(orderId).append("&")
                    .append("orderInfo").append("=").append(env.getOrderInfo()).append("&")
                    .append("partnerCode").append("=").append(env.getPartnerCode()).append("&")
                    .append("redirectUrl").append("=").append(env.getRedirectUrl()).append("&")
                    .append("requestId").append("=").append(requestId).append("&")
                    .append("requestType").append("=").append(env.getRequestType())
                    .toString();

            // Signature
            String signature = Encoder.generateSignature(rawSignature, env.getSecretKey());
            RequestObject requestBody = new RequestObject();
            requestBody.setPartnerCode(env.getPartnerCode());
            requestBody.setRequestId(requestId);
            requestBody.setAmount(Long.valueOf(amount));
            requestBody.setOrderId(orderId);
            requestBody.setOrderInfo(env.getOrderInfo());
            requestBody.setRedirectUrl(env.getRedirectUrl());
            requestBody.setIpnUrl(env.getIpnUrl());
            requestBody.setRequestType(env.getRequestType());
            requestBody.setExtraData(extraData);
            requestBody.setSignature(signature);
            requestBody.setLang(lang);
            String payload = getGson().toJson(requestBody, RequestObject.class);
            Excute excute = new Excute();
            HttpResponse response = excute.sendToMoMo("https://test-payment.momo.vn/v2/gateway/api/create", payload);
            if (response.getStatus() != 200) {
                ResponseObject responseObject = new ResponseObject();
                responseObject.setResultCode(String.valueOf(response.getStatus()));
                return responseObject;
            }
            ResponseObject responseObject = getGson().fromJson(response.getData(), ResponseObject.class);

            return responseObject;
            // return ResponseEntity.ok(responseObject);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid params capture MoMo Request");
        }
    }

    @PostMapping(path = "/payment-notification", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public void paymentNotification(InpMomo response) {
        try {
            EnvMomo env = new EnvMomo();
            String rawSignature = new StringBuilder()
                    .append("accessKey").append("=").append(env.getAccessKey()).append("&")
                    .append("amount").append("=").append(response.getAmount()).append("&")
                    .append("extraData").append("=").append(response.getExtraData()).append("&")
                    .append("ipnUrl").append("=").append(env.getIpnUrl()).append("&")
                    .append("orderId").append("=").append(response.getOrderId()).append("&")
                    .append("orderInfo").append("=").append(response.getOrderInfo()).append("&")
                    .append("partnerCode").append("=").append(response.getPartnerCode()).append("&")
                    .append("redirectUrl").append("=").append(env.getRedirectUrl()).append("&")
                    .append("requestId").append("=").append(response.getRequestId()).append("&")
                    .append("requestType").append("=").append(env.getRequestType())
                    .toString();
            String signature = Encoder.generateSignature(rawSignature, env.getSecretKey());
            System.out.println(signature);
            if (signature.equals(response.getSignature())) {
                System.out.println("Xác nhận thanh toán thnahf công ");
                // Xác thực chũ ký thành công
                // --tiến hành xử lý bài toán--
                if (response.getResultCode() == 0) {
                    System.out.println("Xác nhận thanh toán thnahf công , tiến hành sử lý đơn hàng ");
                    // code xử lý đơn hàng
                } else {
                    // Tiến hành xử lý tuỳ theo trạang thái response.getResultCode()
                }
            } else {
                // chữ ký không hợp lệ
                System.out.println("khong hop le");
            }

        } catch (Exception e) {
            System.out.println("Loi roi");

        }
    }

    @PostMapping("/query-checkout")
    public ResponseObject queryCheckout(@RequestBody RequestObject request) {
        try {

            if (request.getOrderId() == null) {
                ResponseObject responseObject = new ResponseObject();
                responseObject.setResultCode("400");
                responseObject.setMessage("Not found");
                return responseObject;
            }
            EnvMomo env = new EnvMomo();
            String accessKey = env.getAccessKey();
            String secretKey = env.getSecretKey();
            String partnerCode = env.getPartnerCode();
            String requestId = String.valueOf(System.currentTimeMillis());
            String rawSignature = "accessKey=" + accessKey +
                    "&orderId=" + request.getOrderId() +
                    "&partnerCode=" + partnerCode +
                    "&requestId=" + requestId;
            ;

            String signature = Encoder.generateSignature(rawSignature, secretKey);

            RequestObject requestBody = new RequestObject();
            requestBody.setPartnerCode(partnerCode);
            requestBody.setRequestId(requestId);
            requestBody.setOrderId(request.getOrderId());
            requestBody.setLang("vi");
            requestBody.setSignature(signature);
            String payload = getGson().toJson(requestBody, RequestObject.class);
            Excute excute = new Excute();
            HttpResponse response = excute.sendToMoMo("https://test-payment.momo.vn/v2/gateway/api/query", payload);
            if (response.getStatus() != 200) {
                ResponseObject responseObject = new ResponseObject();
                responseObject.setResultCode(String.valueOf(response.getStatus()));

                return responseObject;
            }
            ResponseObject responseObject = getGson().fromJson(response.getData(), ResponseObject.class);
            return responseObject;
        } catch (Exception e) {
            ResponseObject responseObject = new ResponseObject();
            responseObject.setResultCode(String.valueOf("500"));
            responseObject.setMessage("Bad Request");
            return responseObject;
        }
    }

    // @RequestMapping(value = "/orders", method = RequestMethod.GET)
    // public List<Order> getAllOrder() {

    // List<Order> orders = orderService.getAllOrder();
    // return orders;
    // }

    @Override
    public Object execute(Object request) throws MoMoException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}
