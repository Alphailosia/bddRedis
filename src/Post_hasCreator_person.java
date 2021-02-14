import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post_hasCreator_person {

    public String postId;
    public String personId;

    public Post_hasCreator_person (String postId, String personId) {
        this.postId = postId;
        this.personId = personId;
    }

    public Post_hasCreator_person() {

    }

    @Override
    public String toString() {
        return "Post_hasCreator_person{" +
                "postId='" + postId + '\'' +
                ", personId='" + personId + '\'' +
                '}';
    }

    public void ajoutPost_hasCreator_person(Jedis jedis, String ...strings ) {
        jedis = new Jedis("localhost");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Post.id", strings[0]);
        hm.put("Person.id", strings[1]);
        jedis.hmset("post_hasCreator_person_"+strings[0],hm);
        System.out.println("post_hasCreator_person ajouté");
    }

    public static void deletePost_hasCreator_person(Jedis jedis, String id) {
        jedis.del("post_hasCreator_person_"+id);
        System.out.println("post_hasCreator_person supprimé");
    }

    public static void updatePost_hasCreator_person(Jedis jedis,String id, String key, String value) {
        Map<String, String> m = jedis.hgetAll("post_hasCreator_person_"+id);
        m.put(key, value);
        jedis.hmset("post_hasCreator_person_"+id, m);
        System.out.println("post_hasCreator_person modifié");
    }

}
