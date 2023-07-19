package edu.thaishungw.vietbank.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Formatter;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("restriction")
public class Encoder {
    public static String generateSignature(String data, String secretKey) {
        try {
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            Formatter formatter = new Formatter();
            for (byte b : rawHmac) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Hàm chuyển Map thành chuỗi JSON
    public static String convertMapToJson(Map<String, Object> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public static String convertRawSignatureToJson(String rawSignature) {
        // Tách các cặp key-value từ chuỗi rawSignature
        String[] keyValuePairs = rawSignature.split("&");

        // Tạo một TreeMap để sắp xếp các tham số theo thứ tự từ điển
        TreeMap<String, String> sortedParams = new TreeMap<>();

        for (String pair : keyValuePairs) {
            // Tách key và value từ cặp key-value
            String[] entry = pair.split("=");

            if (entry.length == 2) {
                // Thêm key-value vào TreeMap
                sortedParams.put(entry[0], entry[1]);
            }
        }

        // Chuyển TreeMap thành đối tượng JsonObject
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }

        // Chuyển đối tượng JsonObject thành chuỗi JSON
        return gson.toJson(jsonObject);
    }
}
