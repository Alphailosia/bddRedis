
public class Feedback {
	
	public String asin;
	public String id;
	public String feedback;
	
	public Feedback(String asin, String id, String feedback) {
		this.asin = asin;
		this.id = id;
		this.feedback = feedback;
	}

	@Override
	public String toString() {
		return "Product{" +
				"asin='" + asin + '\'' +
				", price='" + id + '\'' +
				", feedback='" + feedback + '\'' +
				'}';
	}
}
