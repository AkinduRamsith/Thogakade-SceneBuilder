package model;

public class OrderCart {
    private String orderId;
    private String CustomerId;
    private String itemCode;
    private String description;
    private int qty;

    public OrderCart(String orderId, String customerId, String itemCode, String description, int qty) {
        this.orderId = orderId;
        CustomerId = customerId;
        this.itemCode = itemCode;
        this.description = description;
        this.qty = qty;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
