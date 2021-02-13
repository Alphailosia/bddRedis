import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

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

	public Order(){

	}

	@Override
	public String toString() {
		return "Order{" +
				"orderId='" + orderId + '\'' +
				", personId='" + personId + '\'' +
				", orderDate='" + orderDate + '\'' +
				", totalPrice='" + totalPrice + '\'' +
				", orderLine='" + orderLine + '\'' +
				'}';
	}

	public static void ajoutOrder(Jedis jedis, String ...strings ) {
		jedis = new Jedis("localhost");
		HashMap<String,String> hm = new HashMap<>();
		hm.put("orderId", strings[0]);
		hm.put("personId", strings[1]);
		hm.put("orderDate", strings[2]);
		hm.put("totalPrice", strings[3]);
		hm.put("orderLine", strings[4]);
		jedis.hmset("order_"+strings[0],hm);
		System.out.println("order ajouté");
	}

	public static void deleteOrder(Jedis jedis, String id) {
		jedis.del("order_"+id);
		System.out.println("order supprimé");
	}

	public static void updateOrder(Jedis jedis,String id, String key, String value) {
		Map<String, String> m = jedis.hgetAll("order_"+id);
		m.put(key, value);
		jedis.hmset("order_"+id, m);
		System.out.println("order modifié");
	}

}
