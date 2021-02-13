

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
	

    
    // fonction ajout avec jedis + les différent paramètre + id (product_lenomquetuveux)
    
    /* Partie insertion 
     * 
     * 
     * 
     * ex product , 
     * HashMap<String,String> hm = new HashMap<>();
     * hm.put("asin", "bly");
     * hm.put("title", "blu");
     * hm.put("price", "bli");
     * hm.put("imgUrl", "blo");
     * hm.put("brand", "bla");
     * jedis.hmset("product_lenomquetuveux",hm);
     * 
     */
}
