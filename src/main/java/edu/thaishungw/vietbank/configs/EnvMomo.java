package edu.thaishungw.vietbank.configs;

public class EnvMomo {
    private String accessKey;
    private String secretKey;
    private String partnerCode;
    private String redirectUrl;
    private String ipnUrl;
    private String requestType;
    private String orderInfo;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getIpnUrl() {
        return ipnUrl;
    }

    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public EnvMomo() {
        this.accessKey = "F8BBA842ECF85";
        this.secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
        this.partnerCode = "MOMO";
        this.redirectUrl = "http://localhost:8080/";
        this.ipnUrl = "http://localhost:8080/payment-notification";
        this.requestType = "captureWallet";
        this.orderInfo = "pay with MoMo";
    }

}
