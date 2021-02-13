import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class Feedback {
	
	public String asin;
	public String id;
	public String feedback;
	
	public Feedback(String asin, String id, String feedback) {
		this.asin = asin;
		this.id = id;
		this.feedback = feedback;
	}

	public Feedback() {

	}

	@Override
	public String toString() {
		return "Feedback{" +
				"asin='" + asin + '\'' +
				", id='" + id + '\'' +
				", feedback='" + feedback + '\'' +
				'}';
	}

	public static void ajoutFeedback(Jedis jedis, String ...strings ) {
		jedis = new Jedis("localhost");
		HashMap<String,String> hm = new HashMap<>();
		hm.put("asin", strings[0]);
		hm.put("id", strings[1]);
		hm.put("feedback", strings[2]);
		jedis.hmset("feedback_"+strings[0]+"_"+strings[1],hm);
		System.out.println("feedback ajouté");
	}

	public static void deleteFeedback(Jedis jedis, String id) {
		jedis.del("feedback_"+id);
		System.out.println("feedback supprimé");
	}

	public static void updateFeedback(Jedis jedis,String id, String key, String value) {
		Map<String, String> m = jedis.hgetAll("feedback_"+id);
		m.put(key, value);
		jedis.hmset("feedback_"+id, m);
		System.out.println("feedback modifié");
	}
	
}
