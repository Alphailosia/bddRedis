import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person_knows_person {


    public String person1Id;
    public String person2Id;
    public String creationDate;

    public Person_knows_person (String person1Id, String person2Id, String creationDate) {
        this.person1Id = person1Id;
        this.person2Id = person2Id;
        this.creationDate = creationDate;
    }

    public Person_knows_person() {

    }

    @Override
    public String toString() {
        return "Person_knows_person{" +
                "person1Id='" + person1Id + '\'' +
                "person2Id='" + person2Id + '\'' +
                "creationDate='" + creationDate + '\'' +
                '}';
    }

    public void ajoutPerson_knows_person(Jedis jedis, String ...strings ) {
        jedis = new Jedis("localhost");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Person.id", strings[0]);
        hm.put("Person2.id", strings[1]);
        hm.put("creationDate", strings[2]);
        jedis.hmset("person_knows_person_"+strings[0]+"_"+strings[1],hm);
        System.out.println("person_knows_person ajouté");
    }

    public static void deletePerson_knows_person(Jedis jedis, String id) {
        jedis.del("person_knows_person_"+id);
        System.out.println("person_knows_person supprimé");
    }

    public static void updatePerson_knows_person(Jedis jedis,String id, String key, String value) {
        Map<String, String> m = jedis.hgetAll("person_knows_person_"+id);
        m.put(key, value);
        jedis.hmset("person_knows_person_"+id, m);
        System.out.println("person_knows_person_ modifié");
    }

}
