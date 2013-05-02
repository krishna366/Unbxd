package models;


public class Query {
	private String query;
	private String timestamp;
	private int productId;
	
	public Query(String str) {
		this.parse(str);
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
