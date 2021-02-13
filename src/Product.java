import redis.clients.jedis.Jedis;

import java.util.HashMap;

public class Product {

	public String asin;
	public String price;
	public String title;
	public String imgUrl;
	public String brand;
	
	public Product(String asin, String price, String title, String imgUrl, String brand) {
		this.asin = asin;
		this.price = price;
		this.title = title;
		this.imgUrl = imgUrl;
		this.brand = brand;

	}

	public Product() {

	}

	@Override
	public String toString() {
		return "Product{" +
				"asin='" + asin + '\'' +
				", price='" + price + '\'' +
				", title='" + title + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", brand='" + brand + '\'' +
				'}';
	}

	public static void ajoutProduct(Jedis jedis, String ...strings ) {
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
	}

	





}
