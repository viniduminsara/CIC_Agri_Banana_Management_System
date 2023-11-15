package module;

public class AddOrderTable {

    private String orderId;
    private String orderDate;
    private int orderQty;
    private String customerName;

    public AddOrderTable(String orderId, String orderDate, int orderQty, String customerName) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderQty = orderQty;
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }
}
