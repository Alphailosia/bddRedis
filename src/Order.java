
public class Order {
	
	public String orderId;
	public String personId;
	public String orderDate;
	public String totalPrice;
	public String orderLine;
	
	public Order(String orderId, String personId, String orderDate, String totalPrice,String orderLine) {
		this.orderId = orderId;
		this.personId = personId;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.orderLine = orderLine;
	}

	@Override
	public String toString() {
		return "Product{" +
				"asin='" + orderId + '\'' +
				", price='" + personId + '\'' +
				", feedback='" + orderDate + '\'' +
				", feedback='" + totalPrice + '\'' +
				", feedback='" + orderLine + '\'' +
				'}';
	}
	
}
