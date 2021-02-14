package nanobets;

public class WithdrawObject {
    	private String withdraw_id;

    	private String destination;

	private String user_id;

    	private String amount;

    	private String dec_amount;

        private String block_hash;

        private String time;

    	public WithdrawObject(String withdraw_id, String user_id, String destination, String amount, String dec_amount, String block_hash, String time) {

		this.withdraw_id = withdraw_id;
		this.time = time;
                this.user_id = user_id;
		this.destination = destination;
		this.amount = amount;
                this.dec_amount = dec_amount;
                this.block_hash = block_hash;
	}

}

	
