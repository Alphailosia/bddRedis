import redis.clients.jedis.Jedis;

import java.util.HashMap;

public class Post {

	public String id;
    public String imageFile;
    public String creationDate;
    public String locationIP;
    public String browserUsed;
    public String language;
    public String content;
    public String length;
    
	public Post(String id, String imageFile, String creationDate, String locationIP, String browserUsed,
			String language, String content, String length) {
		super();
		this.id = id;
		this.imageFile = imageFile;
		this.creationDate = creationDate;
		this.locationIP = locationIP;
		this.browserUsed = browserUsed;
		this.language = language;
		this.content = content;
		this.length = length;
	}
    
    
	@Override
    public String toString() {
        return "Invoice{" +
                "orderId='" + id + '\'' +
                ", personId='" + imageFile + '\'' +
                ", orderDate='" + creationDate + '\'' +
                ", totalPrice='" + locationIP + '\'' +
                ", products=" + browserUsed + '\'' +
                ", products=" + language + '\'' +
                ", products=" + content + '\'' +
                '}';
    }

	/*public static void ajoutProduct(Jedis jedis, String ...strings ) {
		jedis = new Jedis("localhost");
		HashMap<String,String> hm = new HashMap<>();
		hm.put("asin", strings[0]);
		hm.put("title", strings[1]);
		hm.put("price", strings[2]);
		hm.put("imgUrl", strings[3]);
		hm.put("brand", strings[4]);
		jedis.hmset("product_"+strings[0],hm);
		System.out.println(jedis.hmget("product_B000003NUS", "price"));
		System.out.println("produit ajouté");
	}

	public static void deleteProduct(Jedis jedis, String id) {
		jedis.del("product_"+id);
		System.out.println(jedis.get("product_"+id));
		System.out.println("produit supprimé");
	}*/
}
