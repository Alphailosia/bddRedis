

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoice {
    public String orderId;
    public String personId;
    public String orderDate;
    public String totalPrice;
    public List<Product> products;

    public Invoice(String orderId, String personId, String orderDate, String totalPrice, List<Product> products) {
        this.orderId = orderId;
        this.personId = personId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public Invoice() {

    }

    @Override
    public String toString() {
        return "Invoice{" +
                "orderId='" + orderId + '\'' +
                ", personId='" + personId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", products=" + products + '\'' +
                '}';
    }

    public void ajoutInvoice(Jedis jedis, String ...strings ) {
        jedis = new Jedis("localhost");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("orderId", strings[0]);
        hm.put("personId", strings[1]);
        hm.put("orderDate", strings[2]);
        hm.put("totalPrice", strings[3]);
        hm.put("products", strings[4]);
        jedis.hmset("invoice_"+strings[0],hm);
        System.out.println("Invoice ajouté");
    }

    public static void deleteInvoice(Jedis jedis, String id) {
        jedis.del("invoice_"+id);
        System.out.println("Invoice supprimé");
    }

    public static void updateInvoice(Jedis jedis,String id, String key, String value) {
        Map<String, String> m = jedis.hgetAll("invoice_"+id);
        m.put(key, value);
        jedis.hmset("invoice_"+id, m);
        System.out.println("Invoice modifié");
    }
}
