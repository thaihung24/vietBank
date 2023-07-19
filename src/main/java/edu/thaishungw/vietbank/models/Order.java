package edu.thaishungw.vietbank.models;

public class Order {
    private String orderId;
    private String productName;
    private int price;
    private int count;
    private long total;

    public Order() {
    }

    public Order(String orderId, String productName, int price, int count, long total) {
        this.orderId = orderId;
        this.productName = productName;
        this.price = price;
        this.count = count;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
