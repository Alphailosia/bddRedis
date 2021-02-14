import org.w3c.dom.*;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;

import java.io.*;
import java.time.LocalDate;
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
        System.out.println("Connexion réussi !");

        //Reset Database
        //m.jedis.flushAll();

        //Import des fichiers dans la database
        //m.importAll();

        //Queries
        m.query9();

        //m.query1("8796093025356");
        //m.query1("10995116280191");
        //m.query2("B000KKEPJ2", LocalDate.of(2020, 10, 1), LocalDate.of(2022, 1, 1));
        //m.query3("B001C74GM8", LocalDate.of(2012, 10, 1), LocalDate.of(2016, 1, 1));
        //m.query4();
        //m.query6("4398046519185", "4398046519477");
        //m.query7("Penalty_(sports_manufacturer)");

        // Ajouts, modifications et supressions
        //m.addMutDel();

    }

    public void importAll() throws IOException {

        System.out.println("Importations ...");
        
        // initialisation de la list de HashMap a mettre dans la bdd via un fichier Json
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        
        // ajout des invoices
        importInvoiceXML();
        System.out.println("Invoices ajoutés");

        // ajout des products
        list = readCsvProduit(lien + "product/Product.csv",",");
     	for(int i=0;i<list.size();i++) {
			jedis.hmset("product_"+list.get(i).get("asin"),list.get(i) );
		}
     	System.out.println("Produits ajoutés");


        // ajout des marques
        BufferedReader reader = new BufferedReader(new FileReader (lien+"product/BrandByProduct.csv"));
        String line = null;
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
        System.out.println("Marques ajoutées dans les produits");


        // ajout des customers
        list = readCsv(lien + "customer/person_0_0.csv","\\|");
     	for(int i=0;i<list.size();i++) {
			jedis.hmset("customer_"+list.get(i).get("id"),list.get(i) );
		}
     	System.out.println("Customers ajoutés");


     	// ajout des feedback
        list = readCsvFeedback(lien + "feedback/Feedback.csv","\\|");
     	for(int i=0;i<list.size();i++) {
			jedis.hmset("feedback_"+list.get(i).get("asin")+"_"+list.get(i).get("id"),list.get(i) );
		}
     	System.out.println("Feedback ajoutés");
     	

     	// ajout des vendor
        list = readCsv(lien + "vendor/Vendor.csv",",");
        for(int i=0;i<list.size();i++) {
            jedis.hmset("vendor_"+list.get(i).get("Vendor"),list.get(i) );
        }
        System.out.println("Vendors ajoutés");

        
        // ajout des person_hasInterest_tag
        list = readCsv(lien + "socialNetwork/person_hasInterest_tag_0_0.csv","\\|");
        for(int i=0;i<list.size();i++) {
            jedis.hmset("person_hasInterest_tag_"+list.get(i).get("Person.id"),list.get(i) );
        }
        System.out.println("person_hasInterest_tag ajoutés");


        // ajout des person_knows_person
        list = readCsvKnows(lien + "socialNetwork/person_knows_person_0_0.csv","\\|");
        for(int i=0;i<list.size();i++) {
            jedis.hmset("person_knows_person_"+list.get(i).get("Person.id")+list.get(i).get("Person2.id"),list.get(i) );
        }
        System.out.println("person_knows_person ajoutés");


        // ajout des post
        list = readCsv(lien + "socialNetwork/post_0_0.csv","\\|");
        for(int i=0;i<list.size();i++) {
            jedis.hmset("post_"+list.get(i).get("id"),list.get(i) );
        }
        System.out.println("Posts ajoutés");


        // ajout des post_hasCreator_person
        list = readCsv(lien + "socialNetwork/post_hasCreator_person_0_0.csv","\\|");
        for(int i=0;i<list.size();i++) {
            jedis.hmset("post_hasCreator_person_"+list.get(i).get("Post.id"),list.get(i) );
        }
        System.out.println("post_hasCreator_person ajoutés");


        // ajout des post_hasTag_tag
        list = readCsv(lien + "socialNetwork/post_hasTag_tag_0_0.csv","\\|");
        for(int i=0;i<list.size();i++) {
            jedis.hmset("post_hasTag_tag_"+list.get(i).get("Post.id"),list.get(i) );
        }
        System.out.println("post_hasTag_tag ajoutés");

        
        // ajout des commandes
        list = readJson(lien+"order/Order.json");
        for(int i=0;i<list.size();i++) {
            jedis.hmset("order_"+list.get(i).get("OrderId"),list.get(i) );
        }
        System.out.println("Commandes ajoutés");

        System.out.println("FIN des importations");
        System.out.println("--------------------\n\n");

    }

    public void addMutDel() {

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
        f.updateFeedback(jedis, "B005FUKW6M_17592186053220", "feedback", "'6.0,Finally found a good dart cabinet'");

        ///Suppression d'un feedback
        f.deleteFeedback(jedis, "B005FUKW6M_17592186053220");


        ///Ajout d'un post
        Post po = new Post();
        po.ajoutPost(jedis, "1236950581248", "", "2011-09-15T00:45:16.684+0000", "192.101.113.232", "Internet Explorer", "uz", "bout Armasight Spark CORE Multi-Purpose Night Vision Monocular, # 62 on October 8, 2007, and his career-high doub", "95");

        ///Modification d'un post
        po.updatePost(jedis, "1236950581248", "length", "96");

        ///Suppression d'un post
        po.deletePost(jedis, "1236950581248");


        ///Ajout d'un order
        Order o = new Order();
        o.ajoutOrder(jedis, "016f6a4a-ec18-4885-b1c7-9bf2306c76d8", "10995136278715", "2018-09-22", "133.53", "La liste des produits");

        ///Modification d'un order
        o.updateOrder(jedis, "016f6a4a-ec18-4885-b1c7-9bf2306c76d8", "totalPrice", "140.15");

        ///Suppression d'un order
        o.deleteOrder(jedis, "016f6a4a-ec18-4885-b1c7-9bf2306c76d8");


        ///Ajout d'un vendor
        Vendor v = new Vendor();
        v.ajoutVendor(jedis, "Nomas", "South_Korea", "Sports");

        ///Modification d'un vendor
        v.updateVendor(jedis, "Nomas", "Country", "US");

        ///Suppression d'un vendor
        v.deleteVendor(jedis, "Nomas");


        ///Ajout d'un customer
        Customer c = new Customer();
        c.ajoutCustomer(jedis, "2199025266270", "Mimi", "Cheh", "female", "1989-01-18", "2010-04-06T22:43:26.134+0000","27.129.140.209", "Chrome", "421");

        ///Modification d'un customer
        c.updateCustomer(jedis, "2199025266270", "place", "420");

        ///Suppression d'un customer
        c.deleteCustomer(jedis, "2199025266270");

        ///Ajout d'un invoice
        Invoice i = new Invoice();
        i.ajoutInvoice(jedis, "6711da51-dee6-452a-a7b7-f79a1cbb9437", "10995146278715", "2020-09-01", "171", "[{6485,B000HIE4WC,Topeak Dual Touch Bike Storage Stand, 51, Derbi},[8512,B000HJE4WC,Sportlock Leatherlock, 120, Chanel}]");

        ///Modification d'un invoice
        i.updateInvoice(jedis, "6711da51-dee6-452a-a7b7-f79a1cbb9437", "totalPrice", "150");

        ///Suppression d'un invoice
        i.deleteInvoice(jedis, "6711da51-dee6-452a-a7b7-f79a1cbb9437");
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
                    jedis.hmset("invoice_"+orderId, params);
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }

    public List<Invoice> getAllInvoices() {
        ScanParams scanParams = new ScanParams().match("*").count(300000);
        List<String> results = jedis.scan("0", scanParams).getResult();
        List<Invoice> invoices = new ArrayList<>();
        for(int i=0; i<results.size();i++) {
            if(results.get(i).contains("invoice_")) {
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
    
    public static ArrayList<HashMap<String,String>> readCsvKnows(String file, String splitter) throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader (file));
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		HashMap<String, String> user = new HashMap<String,String>();
		ArrayList<String> key = new ArrayList<>();

		key.add("Person.id");
		key.add("Person2.id");
		key.add("creationDate");
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
                String produits = tabProduct.substring(12).substring(1,tabProduct.substring(12).length()-1);
                               
                hmOrder.put(key, produits);
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
        ScanParams scanParams = new ScanParams().match("*").count(2000000);
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

        System.out.println("Query 1 (ID Customer: "+idCustomer+"):\n");

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
        for(Invoice invoice : allInvoices) {
            if(invoice.personId.equals(customer.id) && lastMonth(invoice.orderDate)) {
                System.out.println(invoice);
            }
        }
        System.out.println("");


        //find feedback
        ScanParams scanParams = new ScanParams().match("*").count(1000000);
        List<String> results = jedis.scan("0", scanParams).getResult();
        System.out.println("Feedbacks :");
        for(String r : results) {
            if(r.contains("feedback_")) {
                List<String> res = jedis.hmget(r, "asin", "id", "feedback");
                Feedback f = new Feedback(res.get(0), res.get(1), res.get(2));
                if(f.id.equals(customer.id)) System.out.println(f);
            }
        }
        System.out.println("");


        //find posts
        List<String> postsIds = new ArrayList<>();
        for(String r : results) {
            if(r.contains("post_hasCreator_person_")) {
                List<String> res = jedis.hmget(r, "Post.id", "Person.id");
                if(res.get(1).equals(customer.id)) postsIds.add(res.get(0));
            }
        }
        System.out.println("Posts (in the last month):");
        for(String postId : postsIds) {
            List<String> res = jedis.hmget("post_"+postId, "id", "imageFile", "creationDate", "locationIP", "browserUsed", "language", "content", "length");
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
        System.out.println("");

        //Tags
        HashMap<String, Integer> tags = new HashMap<>();
        for(int i=0; i<postsIds.size();i++) {
            List<String> res = jedis.hmget("post_hasTag_tag_"+postsIds.get(i), "Post.id", "Tag.id");
            String tag = res.get(1);
            if(tag != null) {
                if(tags.containsKey(tag)) tags.put(tag, tags.get(tag) + 1);
                else tags.put(tag, 1);
            }
        }
        String highestTag = "";
        int highestTagNumber = 0;
        for(Map.Entry<String, Integer> entry : tags.entrySet()) {
            if(entry.getValue() > highestTagNumber) {
                highestTag = entry.getKey();
                highestTagNumber = entry.getValue();
            }
        }
        System.out.println("The tag which he has engaged the greatest times in the posts: "+highestTag + " (" + highestTagNumber + ").");

        System.out.println("\nEND query 1");
        System.out.println("--------------------\n\n");
    }
    
    public void query2(String idProduct, LocalDate d1, LocalDate d2) {
        System.out.println("Query 2 (ID product: "+idProduct+"):");

        List<String> personBougthIt = new ArrayList<>();
        List<Invoice> invoices = getAllInvoices();
        for(Invoice inv : invoices) {
            if(dateBetween(inv.orderDate, d1, d2)) {
                for(Product product : inv.products) {
                    if(product.asin.equals(idProduct)) personBougthIt.add(inv.personId);
                }
            }
        }

        System.out.println("Persons who bought the product ("+idProduct+") between "+d1+" and "+d2+":");
        for(String personId : personBougthIt) {
            List<String> cust = jedis.hmget("customer_" + personId, "firstName", "lastName");
            System.out.println(cust.get(0) + " " + cust.get(1));
        }

        System.out.println("\nEND query 2");
        System.out.println("--------------------\n\n");
    }

    public void query3(String idProduct, LocalDate d1, LocalDate d2) {
        System.out.println("Query 3 (ID product: "+idProduct+"):");

        ScanParams scanParams = new ScanParams().match("*").count(100000);
        List<String> results = jedis.scan("0", scanParams).getResult();

        System.out.println("Comments with bad sentiments of the product ("+idProduct+") :");
        for(String r : results) {
            if(r.contains("feedback_"+idProduct)) {
                List<String> res = jedis.hmget(r, "asin", "id", "feedback");
                Feedback f = new Feedback(res.get(0), res.get(1), res.get(2));
                int note;
                if(f.feedback.substring(1, 2).equals("'")) {
                    note = Integer.parseInt(f.feedback.substring(2, 3));
                } else {
                    note = Integer.parseInt(f.feedback.substring(1, 2));
                }
                if(note < 3) {
                    int pos = f.feedback.indexOf(",");
                    System.out.print("Note: "+ note+"/5, ");
                    System.out.println("feedback: "+ f.feedback.substring(pos+1, f.feedback.length()));
                }
            }
        }

        System.out.println("\nEND query 3");
        System.out.println("--------------------\n\n");
    }

    public void query4() {
        System.out.println("Query 4:");

    	// Trouver les 2 personnes qui ont dépensé le plus
    	
    	ArrayList<String> id = new ArrayList<>();
    	HashMap<String, Float> hmPerson = new HashMap<String, Float>();

        ScanParams scanParams = new ScanParams().match("*").count(800000);
        List<String> results = jedis.scan("0", scanParams).getResult();      
        
        for(String orderId : results) {
        	if(orderId.contains("order_")) {
        		List<String> info = jedis.hmget(orderId, "PersonId","TotalPrice");
            	if(hmPerson.get(info.get(0)) == null) {
            		hmPerson.put(info.get(0), Float.parseFloat(info.get(1)));
            	}
            	else {
            		float prix = Float.parseFloat(info.get(1)) + hmPerson.get(info.get(0));
            		hmPerson.put(info.get(0), prix);
            	}
        	}
        }

        ArrayList<Float> prices = new ArrayList<Float>();
        for(Float f : hmPerson.values()) {
        	prices.add(f);
        }
        prices.sort(null);
        Float tab[] = new Float[2];
        String tab2[] = new String[2];
        tab[0] = prices.get(prices.size()-1);
        tab[1] = prices.get(prices.size()-2);
        System.out.println(tab[0]+ " "+ tab[1]);
        
        for(String s : hmPerson.keySet()) {
        	if(hmPerson.get(s).equals(tab[0])) {
        		tab2[0]=s;
        	}
        	else if(hmPerson.get(s).equals(tab[1])) {
        		tab2[1]=s;
        	}
        }
        
        System.out.println("Classement : ");
        for(String s : tab2) {
        	
        	System.out.println("- "+s);
        }
        
        // on recherche les amis qu'ils ont en commun 
        ArrayList<String> user1Friend = new ArrayList<String>();
        ArrayList<String> user2Friend = new ArrayList<String>();
        ArrayList<String> commonFriend = new ArrayList<String>();
        
        for(String orderId : results) {
        	
        	if(orderId.contains("knows_")) {
        		List<String> info = jedis.hmget(orderId, "Person.id","Person2.id");
            	if(info.get(0).equals(tab2[0])) {
            		user1Friend.add(info.get(1));
            	}
            	else if(info.get(0).equals(tab2[1])){
            		user2Friend.add(info.get(1));
            	}
        	}
        }
        
        for(String f1 : user1Friend) {
        	for(String f2: user2Friend) {
        		if(f1.equals(f2)) {
        			commonFriend.add(f1);
        		}
        	}
        }
        
        System.out.println("L'utilisateur "+tab2[0]+" et  "+tab2[1]+" ont "+commonFriend.size()+" amis en commun !!!!!!!");
        System.out.println("\nEND query 4");
        System.out.println("--------------------\n\n");
    }

    public void query5() {

    }

    public void query6(String idCustomer1, String idCustomer2) {
        System.out.println("Query 6 for customers ("+idCustomer1+", "+idCustomer2+"): \n");
        ScanParams scanParams = new ScanParams().match("*").count(2000000);
        List<String> results = jedis.scan("0", scanParams).getResult();

        //Get all friends of customer1 and customer2
        List<String> friends1 = new ArrayList<>();
        List<String> friends2 = new ArrayList<>();
        for(int i=0; i<results.size();i++) {
            if(results.get(i).contains("person_knows_person_"+idCustomer1)) {
                List<String> res = jedis.hmget(results.get(i), "Person.id", "Person2.id");
                if(res.get(1) != null) friends1.add(res.get(1));
            } else if(results.get(i).contains("person_knows_person_"+idCustomer2)) {
                List<String> res = jedis.hmget(results.get(i), "Person.id", "Person2.id");
                if(res.get(1) != null) friends2.add(res.get(1));
            }
        }

        //get communs friends
        List<String> communs = new ArrayList<>();
        for(String f : friends1) {
            if(friends2.contains(f)) communs.add(f);
        }

        //get number of products of brands from communs friends invoices
        HashMap<String, Integer> brandsNumber = new HashMap<>();
        List<Invoice> invoices = getAllInvoices();
        for(Invoice invoice : invoices) {
            if(communs.contains(invoice.personId)) {
                for (Product p : invoice.products) {
                    if(brandsNumber.containsKey(p.brand)) brandsNumber.put(p.brand, brandsNumber.get(p.brand) + 1);
                    else brandsNumber.put(p.brand, 1);
                }
            }
        }

        //Get the top 3 best seller brands
        ArrayList<String> top3 = new ArrayList<>();
        for(int i=0; i<3; i++) {
            int firstValue = 0;
            String first = "";
            for(Map.Entry<String, Integer> entry : brandsNumber.entrySet()) {
                if(entry.getValue() > firstValue) first = entry.getKey();
            }
            brandsNumber.remove(first);
            top3.add(first);
        }


        System.out.println("TOP 3 best sellers from all these persons purchases:");
        for(String top : top3) System.out.println("- "+top);


        System.out.println("\nEND query 6");
        System.out.println("--------------------\n\n");
    }

    public void query7(String vendor) {
        System.out.println("Query 7 for vendor "+vendor+": \n");

        //get all product of the vendor
        List<Product> products = new ArrayList<>();
        ScanParams scanParams = new ScanParams().match("*").count(2000000);
        List<String> results = jedis.scan("0", scanParams).getResult();
        for(String r : results) {
            if(r.contains("product_")) {
                List<String> res = jedis.hmget(r, "asin", "price", "title", "imgUrl", "brand");
                Product p = new Product(res.get(0), res.get(1), res.get(2), res.get(3), res.get(4));
                if(p.brand != null && p.brand.equals(vendor)) products.add(p);
            }
        }


        //get the number of bad reviews for each products
        HashMap<String, Integer> badReviews = new HashMap<>();
        for(String r : results) {
            for(Product p : products) {
                if(r.contains("feedback_"+p.asin)) {
                    List<String> res = jedis.hmget(r, "asin", "id", "feedback");
                    Feedback f = new Feedback(res.get(0), res.get(1), res.get(2));

                    if(f.feedback != null) {
                        int note;
                        if(f.feedback.substring(1, 2).equals("'")) note = Integer.parseInt(f.feedback.substring(2, 3));
                        else note = Integer.parseInt(f.feedback.substring(1, 2));

                        if(note < 3) {
                            if(badReviews.containsKey(p.asin)) badReviews.put(p.asin, badReviews.get(p.asin) + 1);
                            else badReviews.put(p.asin, 1);
                        }
                    }
                }
            }
        }

        System.out.println("Products from "+vendor+" with bad reviews:");
        for(Map.Entry<String, Integer> entry : badReviews.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " bad reviews");
        }

        System.out.println("\nEND query 7");
        System.out.println("--------------------\n\n");
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

    public void query9() {
    	System.out.println("Query 9:");
    	// 3 plus grandes marques par pays
    	
    	// parcourir les marques et les triés par pays
    	ScanParams scanParams = new ScanParams().match("*").count(8000000);
        List<String> results = jedis.scan("0", scanParams).getResult();
        
        HashMap<String, String> hmBC = new HashMap<String, String>();
        for(String s : results) {
        	if(s.contains("vendor_")) {
        		List<String> info = jedis.hmget(s, "Vendor","Country");
        		hmBC.put(info.get(0),info.get(1));
        	}
        }
        
        // parcour des order pour voir quel marque a le plus vendus
        HashMap<String,Integer> hmCountBrand  = new  HashMap<String, Integer>();
        for(String s : results) {
        	if(s.contains("order_")) {
        		
        		String products = jedis.hgetAll(s).get("Orderline");
        		
        		while(products.length()!=0) {
        			int indexTab = products.indexOf("{");
                    int finTab = products.indexOf("}");
                    
                    String p = products.substring(indexTab+1, finTab);
                    String[] tab = p.split(",");
                    String[] tab2 = tab[tab.length-1].split(":");
                    
                    if(hmCountBrand.get(tab2[1].substring(1, tab2[1].length()-1))!=null) {
                    	int count = hmCountBrand.get(tab2[1].substring(1, tab2[1].length()-1))+1;
                    	hmCountBrand.put(tab2[1].substring(1, tab2[1].length()-1), count);
                    }
                    else {
                    	hmCountBrand.put(tab2[1].substring(1, tab2[1].length()-1), 1);
                    }
                    
                    if(finTab==products.length()-1) {
                    	products="";
                    }
                    else {
                    	products = products.substring(finTab+2);
                    }
        		}
        	}
        }
        
        
        // trier en fonction du pays et du 
        for(String s : hmBC.keySet()) {
        	String pays = hmBC.get(s);
        	Integer sales = hmCountBrand.get(s);
        	
        }
    	
    	// comparer clientèle masculine et féminine
    	
    		// parcourir les order et prenre le personId
    		// trouver dans la bdd le genre de la personne en fontion de ca puis incrémenter le dans la marque
    		// comprarer s'il y a plus de fille ou de garcon
    	
    	// trouver les post les plus récent pour chaque
    		
    		// prendre tous les clients de genre le plus present dans la marque et mettre leur post le plus récent
        
        System.out.println("Fin query 9");
    	
    }
}
