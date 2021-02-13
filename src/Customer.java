import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {

    public String id;
    public String firstName;
    public String lastName;
    public String gender;
    public String birthday;
    public String creationDate;
    public String locationIP;
    public String browserUsed;
    public String place;

    public Customer(String id, String firstName, String lastName, String gender, String birthday, String creationDate, String locationIP, String browserUsed, String place) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.creationDate = creationDate;
        this.locationIP = locationIP;
        this.browserUsed = browserUsed;
        this.place = place;
    }

    public Customer() {

    }

    @Override
    public String toString() {
        return "Customer{" + "\n" +
                "\tid='" + id + '\'' + ",\n" +
                "\tfirstName='" + firstName + '\'' + ",\n" +
                "\tlastName='" + lastName + '\'' + ",\n" +
                "\tgender='" + gender + '\'' + ",\n" +
                "\tbirthday='" + birthday + '\'' + ",\n" +
                "\tcreationDate='" + creationDate + '\'' + ",\n" +
                "\tlocationIP='" + locationIP + '\'' + ",\n" +
                "\tbrowserUsed='" + browserUsed + '\'' +",\n" +
                "\tplace='" + place + '\'' + "\n" +
                '}';
    }

    public static void ajoutCustomer(Jedis jedis, String ...strings ) {
        jedis = new Jedis("localhost");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("id", strings[0]);
        hm.put("firstName", strings[1]);
        hm.put("lastName", strings[2]);
        hm.put("gender", strings[3]);
        hm.put("birthday", strings[4]);
        hm.put("creationDate", strings[5]);
        hm.put("locationIP", strings[6]);
        hm.put("browserUsed", strings[7]);
        hm.put("place", strings[8]);
        jedis.hmset("customer_"+strings[0],hm);
        System.out.println("customer ajouté");
    }

    public static void deleteCustomer(Jedis jedis, String id) {
        jedis.del("customer_"+id);
        System.out.println("customer supprimé");
    }

    public static void updateCustomer(Jedis jedis,String id, String key, String value) {
        Map<String, String> m = jedis.hgetAll("customer_"+id);
        m.put(key, value);
        jedis.hmset("customer_"+id, m);
        System.out.println("customer modifié");
    }

}
