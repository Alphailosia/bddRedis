package product;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class Main {

    private Jedis jedis = new Jedis("localhost");
    private String lien = System.getProperty("user.dir") + "/src/product/";

    public static void main(String[] args) throws IOException {
        Main m = new Main();
        Jedis jedis = m.jedis;
        String lien = m.lien;
        jedis.flushAll();

        System.out.println("connection reussi");


        // Product

        // initialisation de la list de HashMap a mettre dans la bdd via un fichier Json
        ArrayList<HashMap<String,String>> listUser = readFile(lien + "product.json");

        // boucle pour mettre dans la bdd et afficher les users
        for(int i=0;i<listUser.size();i++) {
            System.out.println("\nAjout du product "+(i+1)+" \n");

            jedis.hmset("product"+i,listUser.get(i) );

            System.out.println("asin : "+jedis.hmget("product"+i, "asin"));
            System.out.println("title : "+jedis.hmget("product"+i, "title"));
            System.out.println("price "+jedis.hmget("product"+i, "price"));
            System.out.println("imgUrl "+jedis.hmget("product"+i, "imgUrl"));
            System.out.println("brand "+jedis.hmget("product"+i, "brand"));

        }

        // Customer

        // initialisation de la list de HashMap a mettre dans la bdd via un fichier Json
        ArrayList<HashMap<String,String>> listCustomer = readFile(lien + "customer.json");

        System.out.println(listCustomer.size());
        // boucle pour mettre dans la bdd et afficher les users
        for(int i=0;i<listCustomer.size();i++) {
            System.out.println("\nAjout du customer "+(i+1)+" \n");

            jedis.hmset("customer"+i,listCustomer.get(i) );

            System.out.println("personId : "+jedis.hmget("customer"+i, "personId"));
            System.out.println("firstName : "+jedis.hmget("customer"+i, "firstName"));
            System.out.println("lastName "+jedis.hmget("customer"+i, "lastName"));
            System.out.println("gender "+jedis.hmget("customer"+i, "gender"));
            System.out.println("birthday "+jedis.hmget("customer"+i, "birthday"));
            System.out.println("createDate "+jedis.hmget("customer"+i, "createDate"));
            System.out.println("location "+jedis.hmget("customer"+i, "location"));
            System.out.println("browserUsed "+jedis.hmget("customer"+i, "browserUsed"));
            System.out.println("place "+jedis.hmget("customer"+i, "place"));

        }

        // FeedBack

        // initialisation de la list de HashMap a mettre dans la bdd via un fichier Json
        ArrayList<HashMap<String,String>> listFeedBack = readFile(lien + "feedback.json");

        // boucle pour mettre dans la bdd et afficher les users
        for(int i=0;i<listFeedBack.size();i++) {
            System.out.println("\nAjout du feedback "+(i+1)+" \n");

            jedis.hmset("feedback"+i,listFeedBack.get(i) );

            System.out.println("asin : "+jedis.hmget("feedback"+i, "asin"));
            System.out.println("personId : "+jedis.hmget("feedback"+i, "personId"));
            System.out.println("feedback "+jedis.hmget("feedback"+i, "feedback"));

        }

        m.getAllProducts();
        m.importInvoiceXML();
        m.getAllInvoices();
    }

    public void getAllProducts() {
        ScanParams scanParams = new ScanParams().match("*").count(10);
        List<String> results = jedis.scan("0", scanParams).getResult();
        List<Product> products = new ArrayList<>();
        for(int j=0; j<results.size();j++) {
            if(results.get(j).contains("product")) {
                List<String> res = jedis.hmget(results.get(j), "asin", "price", "title", "imgUrl", "brand");
                Product p = new Product(results.get(j), res.get(0), res.get(1), res.get(2), res.get(3), res.get(4));
                products.add(p);
            }
        }
        System.out.println("\nAll products :");
        System.out.println("----------------------");
        for(int j=0; j<products.size();j++) {
            System.out.println(products.get(j));
        }
        System.out.println("----------------------");
    }

    public static ArrayList<HashMap<String,String>> readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader (file));
        ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        HashMap<String, String> user = new HashMap<String,String>();
        try {
            while((line = reader.readLine()) != null) {
                if (line.equals("]") || line.equals("[")){

                }
                else if(line.equals("},") || line.equals("}") ){
                    result.add(user);
                }
                else if(line.equals("{")){
                    user = new HashMap<String,String>();
                }
                else{
                    String[] tab = line.split(":");
                    if(tab[1].charAt(tab[1].length()-1)==',') {
                        user.put(tab[0].substring(1,tab[0].length()-1),tab[1].substring(1,tab[1].length()-2));
                    }
                    else {
                        user.put(tab[0].substring(1,tab[0].length()-1),tab[1].substring(1,tab[1].length()-1));
                    }
                }
            }
            return result;
        } finally {
            reader.close();
        }
    }

    public void importInvoiceXML() {
        try {
            File fXmlFile = new File(lien + "Invoice.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Invoice.xml");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    HashMap<String, String> params = new HashMap<>();

                    String orderId = eElement.getElementsByTagName("OrderId").item(0).getTextContent();
                    params.put("orderId", orderId);

                    String personId = eElement.getElementsByTagName("PersonId").item(0).getTextContent();
                    params.put("personId", personId);

                    String orderDate = eElement.getElementsByTagName("OrderDate").item(0).getTextContent();
                    params.put("orderDate", orderDate);

                    String totalPrice = eElement.getElementsByTagName("TotalPrice").item(0).getTextContent();
                    params.put("totalPrice", totalPrice);


                    NodeList productsId = eElement.getElementsByTagName("productId");
                    String products = "";
                    for(int i=0; i<productsId.getLength(); i++) {
                        if(products.length() != 0) products += ",";
                        products += productsId.item(i).getTextContent();
                    }

                    params.put("products", products);
                    jedis.hmset("invoice"+orderId, params);
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }

    public void getAllInvoices() {
        ScanParams scanParams = new ScanParams().match("*").count(10);
        List<String> results = jedis.scan("0", scanParams).getResult();
        List<Invoice> invoices = new ArrayList<>();
        for(int i=0; i<results.size();i++) {
            if(results.get(i).contains("invoice")) {
                List<String> res = jedis.hmget(results.get(i), "orderId", "personId", "orderDate", "totalPrice", "products");
                List<String> products = Arrays.asList(res.get(4).split(","));
                Invoice inv = new Invoice(res.get(0), res.get(1), res.get(2), res.get(3), products);
                invoices.add(inv);
            }
        }
        System.out.println("\nAll invoices (cap 10) :");
        System.out.println("----------------------");
        for(int j=0; j<invoices.size();j++) {
            System.out.println(invoices.get(j));
        }
        System.out.println("----------------------");
    }
}
