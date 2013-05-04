package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Query {
	private static BufferedReader br;
	
	private String query;
	private String timestamp;
	private int productId;
	
	public Query(String str) {
		this.parse(str);
	}
	
	public static void initTableScan(String fileName) throws IOException{
		closeTableScan();
		
		File f = new File(fileName);
		if(f.exists()){
			br = new BufferedReader(new FileReader(f));
		}
	}
	
	public static Query fetchNext() throws IOException{
		if (br.ready()) {
		  String line = br.readLine();
		  return new Query(line);
		}
		
		return null;
	}
	
	public static void closeTableScan() throws IOException{
		if(br != null)
			br.close();
	}
	
	public void parse(String str){
		String[] tokens = str.split(", ");
		if(tokens.length > 1){
			this.query = tokens[0].replaceAll("\"", "");
		}
		
		if(tokens.length > 2){
			this.timestamp = tokens[1].replaceAll("\"", "");;
		}
		
		this.productId = Integer.parseInt(tokens[tokens.length - 1]);
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	@Override
	public String toString() {
		return query + "\t| " + timestamp + "\t| " + productId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
