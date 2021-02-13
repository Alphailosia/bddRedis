import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;


public class Vendor {

    public String vendor;
    public String country;
    public String industry;


    public Vendor(String vendor, String country, String industry) {
        this.vendor = vendor;
        this.country = country;
        this.industry = industry;
    }

    public Vendor() {

    }

    @Override
    public String toString() {
        return "Vendor{" +
                "vendor='" + vendor + '\'' +
                ", country='" + country + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }

    public void ajoutVendor(Jedis jedis, String ...strings ) {
        jedis = new Jedis("localhost");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Vendor", strings[0]);
        hm.put("Country", strings[1]);
        hm.put("Industry", strings[2]);
        jedis.hmset("vendor_"+strings[0],hm);
        System.out.println("vendor ajouté");
    }

    public static void deleteVendor(Jedis jedis, String id) {
        jedis.del("vendor_"+id);
        System.out.println("vendor supprimé");
    }

    public static void updateVendor(Jedis jedis,String id, String key, String value) {
        Map<String, String> m = jedis.hgetAll("vendor_"+id);
        m.put(key, value);
        jedis.hmset("vendor_"+id, m);
        System.out.println("vendor modifié");
    }

}
