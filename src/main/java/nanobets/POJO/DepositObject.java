package nanobets;

public class DepositObject {

    	private String destination;

    	private String amount;

    	private String block_height;

        private String block_hash;

        private String time;

        private String epoch_time_used;

        private String user_id;

    	public DepositObject(String destination, String amount, String block_hash, String time, String epoch_time_used, String user_id, String block_height) {
		this.destination = destination;
                this.block_hash = block_hash;
                this.block_height = block_height;
		this.time = time;
		this.epoch_time_used = epoch_time_used;
                this.amount = amount;
                this.user_id = user_id;
	}


        public String getBlockCount() {
                return block_height;
        }

}

	
