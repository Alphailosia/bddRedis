

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class Main {

    private Jedis jedis = new Jedis("localhost");
    private String lien = System.getProperty("user.dir") + "/src/data/";

    public static void main(String[] args) throws IOException {
        Main m = new Main();
        Jedis jedis = m.jedis;
        String lien = m.lien;
        //jedis.flushAll();

        System.out.println("connexion reussi");

        //m.query1("19791209300458");
        //m.query1("4145");

        m.query2("B000KKEPJ2", LocalDate.of(2020, 10, 1), LocalDate.of(2022, 1, 1));


/*
        // ajout des invoices
        m.importInvoiceXML();
        System.out.println("ajout des invoices");
        //m.getAllInvoices();

        // initialisation de la list de HashMap a mettre dans la bdd via un fichier Json
     	ArrayList<HashMap<String,String>> listUser = readCsvProduit(lien + "product/Product.csv",",");
     	
     	for(int i=0;i<listUser.size();i++) {

			jedis.hmset("product_"+listUser.get(i).get("asin"),listUser.get(i) );

		}
     	
     	System.out.println("ajout des produits");


        // ajout des marques

        BufferedReader reader = new BufferedReader(new FileReader (lien+"product/BrandByProduct.csv"));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        try {
            while((line = reader.readLine()) != null) {
                String[] tab = line.split(",");
                
                tab[1] =  tab[1].replaceAll("B005UND3CY","B00168NK9S");
                tab[1] =  tab[1].replaceAll("B007M2S52E","B00I1WVRBK");
                tab[1] =  tab[1].replaceAll("B000UUTAZQ","B002APDC0I");
                
                List<String> marque = jedis.hmget("product_"+tab[1], "asin","title", "price", "imgUrl");
                marque.add(tab[0]);
                HashMap<String, String> hm = new HashMap<>();
                hm.put("asin", marque.get(0));
                hm.put("title", marque.get(1));
                hm.put("price", marque.get(2));
                hm.put("imgUrl", marque.get(3));
                hm.put("brand", marque.get(4));
                
                jedis.hmset("product_"+tab[1], hm);
            }
        } finally {
            reader.close();
        }

        System.out.println("ajout des marques dans les produits");

        
        // ajout des customers
     	
        listUser = readCsv(lien + "customer/person_0_0.csv","\\|");
     	
     	for(int i=0;i<listUser.size();i++) {

			jedis.hmset("customer_"+listUser.get(i).get("id"),listUser.get(i) );

		}
     	
     	System.out.println("ajout des customers");
     	

     	// ajout des feedback

        listUser = readCsvFeedback(lien + "feedback/Feedback.csv","\\|");
     	
     	for(int i=0;i<listUser.size();i++) {

			jedis.hmset("feedback_"+listUser.get(i).get("asin")+"_"+listUser.get(i).get("id"),listUser.get(i) );

		}
     	
     	System.out.println("ajout des feedback");


     	// ajout des vendor

        listUser = readCsv(lien + "vendor/Vendor.csv",",");

        for(int i=0;i<listUser.size();i++) {

            jedis.hmset("vendor_"+listUser.get(i).get("Vendor"),listUser.get(i) );

        }

        System.out.println("ajout des vendor");


        // ajout des person_hasInterest_tag

        listUser = readCsv(lien + "socialNetwork/person_hasInterest_tag_0_0.csv","\\|");

        for(int i=0;i<listUser.size();i++) {

            jedis.hmset("person_hasInterest_tag_"+listUser.get(i).get("Person.id"),listUser.get(i) );

        }

        System.out.println("ajout des person_hasInterest_tag");


        // ajout des person_knows_person

        listUser = readCsv(lien + "socialNetwork/person_knows_person_0_0.csv","\\|");

        for(int i=0;i<listUser.size();i++) {

            jedis.hmset("person_knows_person_"+listUser.get(i).get("Person.id"),listUser.get(i) );

        }

        System.out.println("ajout des person_knows_person");


        // ajout des post

        listUser = readCsv(lien + "socialNetwork/post_0_0.csv","\\|");

        for(int i=0;i<listUser.size();i++) {

            jedis.hmset("post_"+listUser.get(i).get("id"),listUser.get(i) );

        }

        System.out.println("ajout des post");


        // ajout des post_hasCreator_person

        listUser = readCsv(lien + "socialNetwork/post_hasCreator_person_0_0.csv","\\|");

        for(int i=0;i<listUser.size();i++) {

            jedis.hmset("post_hasCreator_person_"+listUser.get(i).get("Post.id"),listUser.get(i) );

        }

        System.out.println("ajout des post_hasCreator_person");


        // ajout des post_hasTag_tag

        listUser = readCsv(lien + "socialNetwork/post_hasTag_tag_0_0.csv","\\|");

        for(int i=0;i<listUser.size();i++) {

            jedis.hmset("post_hasTag_tag_"+listUser.get(i).get("Post.id"),listUser.get(i) );

        }

        System.out.println("ajout des post_hasTag_tag");
        
        
        // ajout des commande

        ArrayList<HashMap<String,String>> listOskour = readJson(lien+"order/Order.json");
        
        for(int i=0;i<listOskour.size();i++) {

            jedis.hmset("order_"+listOskour.get(i).get("OrderId"),listOskour.get(i) );

        }

        System.out.println("ajout des commandes");


        ///Ajout d'un produit
        Product p = new Product();
        p.ajoutProduct(jedis, "B000003NUS", "Black Mountain Products Single Band", "81.02", "http://ecx.images-amazon.com/images/I/41g2Pz8o8KL._SX342_.jpg", "chanel");

        ///modification d'un produit
        p.updateProduct(jedis, "B000003NUS", "price", "201");

        ///Suppression d'un produit
        p.deleteProduct(jedis, "B000003NUS");


        ///Ajout d'un feedback
        Feedback f = new Feedback();
        f.ajoutFeedback(jedis, "B005FUKW6M", "17592186053220", "'5.0,Finally found a good dart cabinet without some crap logo on the front, or some fake antiquated dart pub artwork!random words:reccoreckonrclameroadworkrootiragglerestamprussellrhombus'");

        ///Modification d'un feedback

        ///Suppression d'un feedback
        f.deleteFeedback(jedis, "B005FUKW6M_17592186053220");


        ///Ajout d'un post
        Post po = new Post();
        po.ajoutPost(jedis, "1236950581248", "", "2011-09-15T00:45:16.684+0000", "192.101.113.232", "Internet Explorer", "uz", "bout Armasight Spark CORE Multi-Purpose Night Vision Monocular, # 62 on October 8, 2007, and his career-high doub", "95");

        ///Modification d'un post

        ///Suppression d'un post
        po.deletePost(jedis, "1236950581248");


        ///Ajout d'un order
        Order o = new Order();
        o.ajoutOrder(jedis, "016f6a4a-ec18-4885-b1c7-9bf2306c76d8", "10995136278715", "2018-09-22", "133.53", "La liste des produits");

        ///Modification d'un order

        ///Suppression d'un order
        o.deleteOrder(jedis, "016f6a4a-ec18-4885-b1c7-9bf2306c76d8");


        ///Ajout d'un vendor
        Vendor v = new Vendor();
        v.ajoutVendor(jedis, "Nomas", "South_Korea", "Sports");

        ///Modification d'un vendor
        v.updateVendor(jedis, "Nomas", "Country", "US");

        ///Suppression d'un vendor
        v.deleteVendor(jedis, "Nomas");

*/
        ///Ajout d'un customer
        Customer c = new Customer();
        c.ajoutCustomer(jedis, "2199025266270", "Mimi", "Cheh", "female", "1989-01-18", "2010-04-06T22:43:26.134+0000","27.129.140.209", "Chrome", "421");

        ///Modification d'un customer
        c.updateCustomer(jedis, "2199025266270", "place", "420");

        ///Suppression d'un customer
        c.deleteCustomer(jedis, "2199025266270");
/*
*/



    }


    public void importInvoiceXML() {
        try {
            File fXmlFile = new File(lien + "invoice/Invoice.xml");
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

                    NodeList productsId = eElement.getElementsByTagName("Orderline");
                    String products = "";
                    for(int i=0; i<productsId.getLength(); i++) {
                        if(products.length() != 0) products += "~é";
                        String produits = productsId.item(i).getTextContent();
                        produits = produits.replaceFirst("            ", "");
                        produits = produits.replaceAll("\n", "");
                        produits = produits.replaceAll("            ", "~#");
                        produits = produits.replaceFirst("        ", "");
                        products += produits;
                    }

                    params.put("products", products);
                    jedis.hmset("invoice"+orderId, params);
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }

    public List<Invoice> getAllInvoices() {
        ScanParams scanParams = new ScanParams().match("*").count(100000);
        List<String> results = jedis.scan("0", scanParams).getResult();
        List<Invoice> invoices = new ArrayList<>();
        for(int i=0; i<results.size();i++) {
            if(results.get(i).contains("invoice")) {
                List<String> res = jedis.hmget(results.get(i), "orderId", "personId", "orderDate", "totalPrice", "products");
                List<String> products = Arrays.asList(res.get(4).split("~é"));
                List<Product> produits = new ArrayList<>();
                for(int j=0; j<products.size(); j++) {
                    List<String> p = Arrays.asList(products.get(j).split("~#"));
                    produits.add(new Product(p.get(1), p.get(3), p.get(2), "", p.get(4)));
                }
                Invoice inv = new Invoice(res.get(0), res.get(1), res.get(2), res.get(3), produits);
                invoices.add(inv);
            }
        }
        return invoices;
    }
    
    public static ArrayList<HashMap<String,String>> readCsvProduit(String file, String splitter) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader (file));
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		HashMap<String, String> user = new HashMap<String,String>();
		ArrayList<String> key = new ArrayList<>();
		int count = 0;
		try {
			while((line = reader.readLine()) != null) {
				if (count==0){
					for(String s : line.split(splitter)) {
						key.add(s);
					}
					count++;
				}
				else{
					user=new HashMap<String,String>();	

					String[] oui = traitementProduit(line,splitter,key.size());
					
					for(int i=0;i<oui.length;i++) {
						user.put(key.get(i), oui[i]);
					}
					result.add(user);
				}
			}
			return result;
		} finally {
			reader.close();
		}
	}

    public static ArrayList<HashMap<String,String>> readCsvFeedback(String file, String splitter) throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader (file));
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		HashMap<String, String> user = new HashMap<String,String>();
		ArrayList<String> key = new ArrayList<>();

		key.add("asin");
		key.add("id");
		key.add("feedback");
		try {
			while((line = reader.readLine()) != null) {
				
					user=new HashMap<String,String>();	

					String[] oui = traitementProduit(line,splitter,key.size());
					
					for(int i=0;i<oui.length;i++) {
						user.put(key.get(i), oui[i]);
					}
					result.add(user);
			}
			return result;
		} finally {
			reader.close();
		}
	}
    
	private static String[] traitementProduit(String line,String split, int size) {
		if(line.split(split).length==size) {
			return line.split(split);
		}
		else {
			String[] traitement = new String[size];
			String[] tabAtraiter = line.split(split);
			
			traitement[0] = tabAtraiter[0];
			traitement[3] = tabAtraiter[tabAtraiter.length-1];
			traitement[2] = tabAtraiter[tabAtraiter.length-2];
			String s = "";
			for(int i=1;i<tabAtraiter.length-2;i++) {
				s+=tabAtraiter[i];
			}
			traitement[1]=replaceComma(s);
			return traitement;
		}
	}
	
	private static ArrayList<HashMap<String,String>> readCsv(String file, String splitter) throws IOException{
		
		BufferedReader reader = new BufferedReader(new FileReader (file));
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		HashMap<String, String> user = new HashMap<String,String>();
		ArrayList<String> key = new ArrayList<>();
		int count = 0;
		try {
			while((line = reader.readLine()) != null) {
				if (count==0){
					for(String s : line.split(splitter)) {
						key.add(s);
					}
					count++;
				}
				else{
					user=new HashMap<String,String>();	

					String[] oui = line.split(splitter);
					for(int i=0;i<oui.length;i++) {
						user.put(key.get(i), oui[i]);
					}
					
					result.add(user);
				}
			}
			return result;
		} finally {
			reader.close();
		}
	}
	
	private static ArrayList<HashMap<String,String>> readJson(String file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader (file));
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		try {
            while((line = reader.readLine()) != null) {
            	line = line.replaceAll("\\[Watch\\]","");
                line = line.replaceAll("\\[2014 MODEL\\]","");
            	
                int indexTab = line.indexOf("[");
                int finTab = line.indexOf("]");
                
                String tabProduct = replaceComma(line.substring(indexTab-12, finTab+1)); // le tableau des produits commander
                line = line.substring(0, indexTab-14)+line.substring(finTab+1);
                
                // traitement du reste de la ligne 
                line = line.substring(1,line.length()-1);
                String[] resteLigne = line.split(",");
                HashMap<String,String> hmOrder = new HashMap<String,String>();
                
                for(int i=0;i<resteLigne.length;i++) {
                	String[] ui = resteLigne[i].split(":");
                	hmOrder.put(ui[0].substring(1,ui[0].length()-1), ui[1]);
                }
                
                // traitement du tableau de produits
                
                String key = tabProduct.substring(1, 10);
                String[] produits = tabProduct.substring(12).substring(1,tabProduct.substring(12).length()-1).split(",");
                ArrayList<HashMap<String,String>> products = new ArrayList<>();
                
                for(int i=0;i<produits.length;i++) {
                	
                	String[] s = produits[i].split(",");
                	HashMap<String,String> hmp = new HashMap<>();
                	
                	for(int j=0;j<s.length;j++) {
                		String[] jpp = s[j].split(":");
                		hmp.put(jpp[0].substring(1,jpp[0].length()-1),jpp[1]);
                	}
                	
                	products.add(hmp);
                	
                }
                
                hmOrder.put(key, products.toString());
                result.add(hmOrder);
            }
            return result;
        } finally {
            reader.close();
        }
	}

	public static String replaceComma(String toParse){
        String result ="";
        String rpl1 = "MMMM"; //stand for ","
        String rpl2 = "NNNN"; //stand for ,"
        String rpl3 = "OOOO"; //stand for },

        result = toParse.replaceAll("\",\"",rpl1);
        result = result.replaceAll(",\"",rpl2);
        result = result.replaceAll("},",rpl3);
        result = result.replaceAll(",","");

        result = result.replaceAll(rpl3,"},");
        result = result.replaceAll(rpl2,",\"");
        result = result.replaceAll(rpl1,"\",\"");
        

        return result;
    }

    public List<Post> getAllPosts() {
        ScanParams scanParams = new ScanParams().match("*").count(1000000);
        List<String> results = jedis.scan("0", scanParams).getResult();
        List<Post> posts = new ArrayList<>();
        for(int i=0; i<results.size();i++) {
            if(results.get(i).contains("post_")) {
                List<String> res = jedis.hmget(results.get(i), "id", "imageFile", "creationDate", "locationIP", "browserUsed", "language", "content", "length");
                Post p = new Post(res.get(0), res.get(1), res.get(2), res.get(3), res.get(4), res.get(5), res.get(6), res.get(7));
                posts.add(p);
            }
        }
        return posts;
    }


    public void query1(String idCustomer) {
        System.out.println("Query 1 (ID Customer: "+idCustomer+"):");

        //find profile
        List<String> cust = jedis.hmget("customer_" + idCustomer,
                "id", "firstName", "lastName", "gender", "birthday", "creationDate", "locationIP", "browserUsed", "place");
        System.out.println("Profile :");

        Customer customer = new Customer(cust.get(0), cust.get(1), cust.get(2), cust.get(3), cust.get(4), cust.get(5), cust.get(6), cust.get(7), cust.get(8));
        System.out.println(customer);
        System.out.println("");

        //find orders/invoices
        System.out.println("Invoices/orders (in the last month):");
        List<Invoice> allInvoices = getAllInvoices();
        for(int i=0 ; i<allInvoices.size(); i++) {
            if(allInvoices.get(i).personId.equals(customer.id) && lastMonth(allInvoices.get(i).orderDate) ) {
                System.out.println(allInvoices.get(i));
            }
        }
        System.out.println("");


        //find feedback
        ScanParams scanParams = new ScanParams().match("*").count(100000);
        List<String> results = jedis.scan("0", scanParams).getResult();
        List<String> postsIds = new ArrayList<>();

        System.out.println("Feedbacks :");
        for(int i=0; i<results.size();i++) {
            if(results.get(i).contains("feedback_")) {
                List<String> res = jedis.hmget(results.get(i), "asin", "id", "feedback");
                Feedback f = new Feedback(res.get(0), res.get(1), res.get(2));
                if(f.id.equals(customer.id)) System.out.println(f);
            }
        }
        System.out.println("");


        //find posts
        for(int i=0; i<results.size();i++) {
            if(results.get(i).contains("post_hasCreator_person_")) {
                List<String> res = jedis.hmget(results.get(i), "Post.id", "Person.id");
                if(res.get(1).equals(customer.id)) postsIds.add(res.get(0));
            }
        }
        System.out.println("Posts (in the last month):");
        for(int i=0; i<postsIds.size();i++) {
            List<String> res = jedis.hmget("post_"+postsIds.get(i), "id", "imageFile", "creationDate", "locationIP", "browserUsed", "language", "content", "length");
            Post p = new Post(res.get(0), res.get(1), res.get(2), res.get(3), res.get(4), res.get(5), res.get(6), res.get(7));
            if(lastMonth(p.creationDate)) {
                System.out.println(p);
            }
        }
        System.out.println("");

        //find the category in which he has bought the largest number of product
        HashMap<String, Integer> listProduits = new HashMap<>();
        for(int i=0 ; i<allInvoices.size(); i++) {
            if(allInvoices.get(i).personId.equals(customer.id)) {
                List<Product> prods = allInvoices.get(i).products;
                for(int j =0; j<prods.size();j++) {
                    String key = prods.get(j).brand;
                    if(listProduits.containsKey(key)) listProduits.put(key, listProduits.get(key) + 1);
                    else listProduits.put(key, 1);
                }
            }
        }

        String highest = "";
        int highestNumber = 0;
        for(Map.Entry<String, Integer> entry : listProduits.entrySet()) {
            if(entry.getValue() > highestNumber) {
                highest = entry.getKey();
                highestNumber = entry.getValue();
            }
        }
        System.out.println("The category in which he has bought the largest number of product: "+highest + " (" + highestNumber + ").");

        System.out.println("\nEND query 1");
        System.out.println("--------------------\n\n");
    }

    public void query2(String idProduct, LocalDate d1, LocalDate d2) {

        List<String> personBougthIt = new ArrayList<>();
        List<Invoice> invoices = getAllInvoices();
        for(Invoice inv : invoices) {
            if(dateBetween(inv.orderDate, d1, d2)) {
                for(Product product : inv.products) {
                    if(product.asin.equals(idProduct)) personBougthIt.add(inv.personId);
                }
            }
        }

        System.out.println("Persons who bought the product ("+idProduct+"):");
        for(String personId : personBougthIt) {
            List<String> cust = jedis.hmget("customer_" + personId, "firstName", "lastName");
            System.out.println(cust.get(0) + " " + cust.get(1));
        }

    }

    public boolean lastMonth(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate aMonth = LocalDate.now();
        aMonth = aMonth.minusMonths(1);
        LocalDate today = LocalDate.now();
        LocalDate dateTime = LocalDate.parse(date.substring(0, 10), formatter);
        return (dateTime.compareTo(aMonth) >= 0 && dateTime.compareTo(today) <= 0);
    }

    public boolean dateBetween(String date, LocalDate d1, LocalDate d2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(date.substring(0, 10), formatter);
        return (dateTime.compareTo(d1) >= 0 && dateTime.compareTo(d2) <= 0);
    }

}
