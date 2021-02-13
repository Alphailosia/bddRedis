import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class Post {

	public String id;
    public String imageFile;
    public String creationDate;
    public String locationIP;
    public String browserUsed;
    public String language;
    public String content;
    public String length;
    
	public Post(String id, String imageFile, String creationDate, String locationIP, String browserUsed, String language, String content, String length) {
		this.id = id;
		this.imageFile = imageFile;
		this.creationDate = creationDate;
		this.locationIP = locationIP;
		this.browserUsed = browserUsed;
		this.language = language;
		this.content = content;
		this.length = length;
	}

	public Post(){

	}
    
	@Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", locationIP='" + locationIP + '\'' +
                ", browserUsed=" + browserUsed + '\'' +
                ", language=" + language + '\'' +
                ", content=" + content + '\'' +
				", length=" + length + '\'' +
                '}';
    }

	public static void ajoutPost(Jedis jedis, String ...strings ) {
		jedis = new Jedis("localhost");
		HashMap<String,String> hm = new HashMap<>();
		hm.put("id", strings[0]);
		hm.put("imageFile", strings[1]);
		hm.put("creationDate", strings[2]);
		hm.put("locationIP", strings[3]);
		hm.put("browserUsed", strings[4]);
		hm.put("language", strings[5]);
		hm.put("content", strings[6]);
		hm.put("length", strings[7]);
		jedis.hmset("post_"+strings[0],hm);
		System.out.println("post ajouté");
	}

	public static void deletePost(Jedis jedis, String id) {
		jedis.del("post_"+id);
		System.out.println("post supprimé");
	}

	public static void updatePost(Jedis jedis,String id, String key, String value) {
		Map<String, String> m = jedis.hgetAll("post_"+id);
		m.put(key, value);
		jedis.hmset("post_"+id, m);
		System.out.println("post modifié");
	}
}
