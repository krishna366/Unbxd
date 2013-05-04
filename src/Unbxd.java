import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import models.Product;
import models.Query;
import tree.BPlusTree;

public class Unbxd {
	public static void main(String[] args) {
		try {
			Unbxd unbxd = new Unbxd();
			unbxd.ask();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final int iMax = 100;
	private final int lMax = 10000;
	
	private String productsFileName;
	private String queriesFileName;
	
	private BPlusTree<Product> productTreeProductIdIndex;
	private BPlusTree<Product> productTreeArtistIndex;
	private BPlusTree<Query> queryTreeProductIdIndex;
	private BPlusTree<Query> queryTreeQueryIndex;
	
	private Scanner in;
	
	enum STATES{
		READ_PRODUCTS,
		READ_QUERIES,
		WHAT_TO_DO,
		SEARCH_PRODUCTS,
		SEARCH_QUERIES,
		QUIT
	}
	
	public STATES status;
	
	public Unbxd() throws IOException {
		in = new Scanner(System.in);
		status = STATES.READ_PRODUCTS;
	}
	
	public void ask() throws IOException{
		if(status == STATES.READ_PRODUCTS){
			System.out.println("Enter the path of the file having products info(Q to quit) : ");
			String input = in.nextLine();
			
			if(input.equals("Q") || input.equals("q")) status = STATES.QUIT;
			else{
				File f = new File(input);
				if(f.exists()){
					productsFileName = input;
					this.buildProductIndexes();
					
					status = STATES.READ_QUERIES;
				}
			}
		}else if(status == STATES.READ_QUERIES){
			System.out.println("Enter the path of the file having queries info(Q to quit) : ");
			String input = in.nextLine();
			
			if(input.equals("Q") || input.equals("q")) status = STATES.QUIT;
			else{
				File f = new File(input);
				if(f.exists()){
					queriesFileName = input;
					this.buildQueryIndexes();
					
					status = STATES.WHAT_TO_DO;
				}
			}
		}else if(status == STATES.WHAT_TO_DO){
			System.out.println("What do you want to do?");
			System.out.println("1) Look for products matching a search string");
			System.out.println("2) Look for search strings matching an artist");
			System.out.println("Q) Quit");
			
			String input = in.nextLine();
			if(input.equals("1")) status = STATES.SEARCH_PRODUCTS;
			if(input.equals("2")) status = STATES.SEARCH_QUERIES;
			if(input.equals("Q") || input.equals("q")) status = STATES.QUIT;
		}else if(status == STATES.SEARCH_PRODUCTS){
			System.out.println("Enter the search string(Q to quit)");
			
			String input = in.nextLine();
			if(input.equals("Q") || input.equals("q")) 
				status = STATES.QUIT;
			else{
				searchProductsForQuery(input);
				status = STATES.WHAT_TO_DO;
			}
		}else if(status == STATES.SEARCH_QUERIES){
			System.out.println("Enter the artist name(Q to quit)");
			
			String input = in.nextLine();
			if(input.equals("Q") || input.equals("q")) 
				status = STATES.QUIT;
			else{
				searchQueriesForArtist(input);
				status = STATES.WHAT_TO_DO;
			}
		}else if(status == STATES.QUIT){
			System.exit(0);
		}
		
		ask();
	}
	
	public void searchProductsForQuery(String searchString){
		System.out.println("Looking for products matching Search Query : " + searchString + " ...");
		long t = new Date().getTime();
		List<Query> queries = queryTreeQueryIndex.search(searchString);
		List<Product> products = new ArrayList<Product>();
		for(Query query : queries){
			int productId = query.getProductId();
			products.addAll(productTreeProductIdIndex.search(productId));
		}
		long queryTime = new Date().getTime() - t;
		
		System.out.println("productId\t| productName\t| artist\t| genre");
		System.out.println("_______________________________________________________");
		for(Product product : products){
			System.out.println(product.toString());
		}
		System.out.println("Rows : " + products.size() +  ", Query Time : " + queryTime + " millisecs\n");
	}
	
	public void searchQueriesForArtist(String artist){
		System.out.println("Looking for queries matching Artist : " + artist + " ...");
		long t = new Date().getTime();
		List<Query> queries = new ArrayList<Query>();
		List<Product> products = productTreeArtistIndex.search(artist);
		for(Product product : products){
			int productId = product.getId();
			queries.addAll(queryTreeProductIdIndex.search(productId));
		}
		long queryTime = new Date().getTime() - t;
		
		System.out.println("query");
		System.out.println("_____");
		for(Query query : queries){
			System.out.println(query.getQuery());
		}
		System.out.println("Rows : " + queries.size() +  ", Query Time : " + queryTime + " millisecs\n");
	}
	
	private void buildProductIndexes() throws IOException{
		System.out.println("Building Product Indexes...");
		long t = new Date().getTime();
		productTreeProductIdIndex = new BPlusTree<Product>(iMax, lMax);
		productTreeArtistIndex = new BPlusTree<Product>(iMax, lMax);
		
		Product.initTableScan(productsFileName);
		Product product = Product.fetchNext();
		while(product != null){
			productTreeProductIdIndex.add(product.getId(), product);
			productTreeArtistIndex.add(product.getArtist(), product);
			
			product = Product.fetchNext();
		}
		Product.closeTableScan();
		long indexTime = new Date().getTime() - t;
		System.out.println("Took " + indexTime + " millisecs to build product indexes\n");
	}
	
	private void buildQueryIndexes() throws IOException{
		System.out.println("Building Query Indexes...");
		long t = new Date().getTime();
		queryTreeProductIdIndex = new BPlusTree<Query>(iMax, lMax);
		queryTreeQueryIndex = new BPlusTree<Query>(iMax, lMax);
		
		Query.initTableScan(queriesFileName);
		Query query = Query.fetchNext();
		while(query != null){
			queryTreeProductIdIndex.add(query.getProductId(), query);
			queryTreeQueryIndex.add(query.getQuery(), query);
			  
			query = Query.fetchNext();
		}
		Query.closeTableScan();
		
		long indexTime = new Date().getTime() - t;
		System.out.println("Took " + indexTime + " millisecs to build query indexes\n");
	}
}
