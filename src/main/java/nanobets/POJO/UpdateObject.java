package nanobets;

public class UpdateObject {

    	private UserObject user;

        private double balance_update;

    	public UpdateObject(UserObject user, double balance_update) {
                this.balance_update = balance_update;
		this.user = user;
	}
        
    	public UserObject getUser() {
        	return user;
    	}
        
	public double getAmount() {
		return balance_update;
	}
}





