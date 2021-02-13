import redis.clients.jedis.Jedis;

import java.util.HashMap;


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


}
