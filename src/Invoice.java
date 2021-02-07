

import java.util.List;

public class Invoice {
    public String orderId;
    public String personId;
    public String orderDate;
    public String totalPrice;
    public List<String> products;

    public Invoice(String orderId, String personId, String orderDate, String totalPrice, List<String> products) {
        this.orderId = orderId;
        this.personId = personId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "orderId='" + orderId + '\'' +
                ", personId='" + personId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", products=" + products +
                '}';
    }
}
