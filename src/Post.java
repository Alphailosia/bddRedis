
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
}
