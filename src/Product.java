

public class Product {

	public String asin;
	public String price;
	public String title;
	public String imgUrl;

	public Product(String asin, String price, String title, String imgUrl) {
		this.asin = asin;
		this.price = price;
		this.title = title;
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "Product{" +
				"asin='" + asin + '\'' +
				", price='" + price + '\'' +
				", title='" + title + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				'}';
	}
}
