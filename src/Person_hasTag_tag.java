
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Person_hasTag_tag {

    public String postId;
    public String tagId;

    public Person_hasTag_tag (String postId, String tagId) {
        this.postId = postId;
        this.tagId = tagId;
    }

    public Person_hasTag_tag() {

    }

    @Override
    public String toString() {
        return "Person_hasTag_tag{" +
                "postId='" + postId + '\'' +
                ", tagId='" + tagId + '\'' +
                '}';
    }

    public void ajoutPerson_hasTag_tag(Jedis jedis, String ...strings ) {
        jedis = new Jedis("localhost");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Post.id", strings[0]);
        hm.put("Tag.id", strings[1]);
        jedis.hmset("person_hasTag_tag_"+strings[0],hm);
        System.out.println(jedis.hmget("person_hasTag_tag_"+strings[0], "Tag.id"));
        System.out.println("person_hasTag_tag ajouté");
    }

    public static void deletePerson_hasTag_tag(Jedis jedis, String id) {
        jedis.del("person_hasTag_tag_"+id);
        System.out.println("person_hasTag_tag supprimé");
    }

    public static void updatePerson_hasTag_tag(Jedis jedis,String id, String key, String value) {
        System.out.println(jedis.hmget("person_hasTag_tag_"+id, "Tag.id"));
        Map<String, String> m = jedis.hgetAll("person_hasTag_tag_"+id);
        m.put(key, value);
        jedis.hmset("person_hasTag_tag_"+id, m);
        System.out.println(jedis.hmget("person_hasTag_tag_"+id, "Tag.id"));
        System.out.println("person_hasTag_tag_ modifié");
    }

}
