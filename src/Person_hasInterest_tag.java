import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person_hasInterest_tag {

    public String personId;
    public String tagId;

    public Person_hasInterest_tag (String personId, String tagId) {
        this.personId = personId;
        this.tagId = tagId;
    }

    public Person_hasInterest_tag() {

    }

    @Override
    public String toString() {
        return "Person_hasInterest_tag{" +
                "personId='" + personId + '\'' +
                ", tagId='" + tagId + '\'' +
                '}';
    }

    public void ajoutPerson_hasInterest_tag(Jedis jedis, String ...strings ) {
        jedis = new Jedis("localhost");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Person.id", strings[0]);
        hm.put("Tag.id", strings[1]);
        jedis.hmset("person_hasInterest_tag_"+strings[0],hm);
        System.out.println("person_hasInterest_tag ajouté");
    }

    public static void deletePerson_hasInterest_tag(Jedis jedis, String id) {
        jedis.del("person_hasInterest_tag_"+id);
        System.out.println("person_hasInterest_tag supprimé");
    }

    public static void updatePerson_hasInterest_tag(Jedis jedis,String id, String key, String value) {
        Map<String, String> m = jedis.hgetAll("person_hasInterest_tag_"+id);
        m.put(key, value);
        jedis.hmset("person_hasInterest_tag_"+id, m);
        System.out.println("person_hasInterest_tag_ modifié");
    }

}
