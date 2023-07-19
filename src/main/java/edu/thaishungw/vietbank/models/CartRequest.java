package edu.thaishungw.vietbank.models;

public class CartRequest {
    private int price;
    private int count;
    private String productName;

    public CartRequest() {
    }

    public CartRequest(int price, int count, String productName) {
        this.price = price;
        this.count = count;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
