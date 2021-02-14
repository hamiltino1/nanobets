package nanobets;

public class AdminObject {

	private String profit;
	private String balance;
	private String decimal_balance;

    	public AdminObject(String decimal_balance) {

		this.decimal_balance = decimal_balance;
	}

       	public String getProfit() {
		
		return profit;
	}	
	public double getBalance() {
		double balance_int = Double.parseDouble(balance);	
		return balance_int;
	}
	public String getDecimalBalance() {
		return decimal_balance;
	}
}





