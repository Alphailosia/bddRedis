package product;

public class Product {

	public String id;
	public String asin;
	public String price;
	public String title;
	public String imgUrl;
	public String brand;

	public Product(String id, String asin, String price, String title, String imgUrl, String brand) {
		this.id = id;
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
}
