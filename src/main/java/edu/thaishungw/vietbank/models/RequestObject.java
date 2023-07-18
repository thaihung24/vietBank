package edu.thaishungw.vietbank.models;

public class RequestObject {
    private String partnerCode;
    private String partnerName;

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    private String storeId;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    private String requestId;
    private long amount;
    private String orderId;
    private String orderInfo;
    private String redirectUrl;
    private String ipnUrl;
    private String requestType;
    private String extraData;
    private Boolean autoCapture;

    public Boolean getAutoCapture() {
        return autoCapture;
    }

    public void setAutoCapture(Boolean autoCapture) {
        this.autoCapture = autoCapture;
    }

    private String lang;
    private String signature;

    public RequestObject() {

    }

    public RequestObject(String partnerCode, String partnerName, String storeId, String requestId, long amount,
            String orderId,
            String orderInfo, String redirectUrl, String ipnUrl, String requestType, String extraData,
            Boolean autoCapture, String signature,
            String lang) {
        this.partnerCode = partnerCode;
        this.partnerName = partnerName;
        this.storeId = storeId;
        this.requestId = requestId;
        this.amount = amount;
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.redirectUrl = redirectUrl;
        this.ipnUrl = ipnUrl;
        this.requestType = requestType;
        this.extraData = extraData;
        this.autoCapture = autoCapture;
        this.signature = signature;
        this.lang = lang;
    }

    public RequestObject(String partnerCode, String requestId, String orderId, String signature, String lang) {
        this.partnerCode = partnerCode;
        this.requestId = requestId;
        this.orderId = orderId;
        this.signature = signature;
        this.lang = lang;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
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

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
