package nanobets;

import java.math.BigInteger;
import java.math.BigDecimal;

public class EntryObject {

    	private String user_id;

    	private String chosen_numbers;

    	private String entry_time_epoch;

        private String entry_time_utc;

        private String outcome;

        private String result_hash;

	private String result_string;

    	public EntryObject(String user_id, String entry_time_epoch, String entry_time_utc, String outcome, String result_hash, String result_string, String chosen_numbers) {
                this.user_id = user_id;
		this.entry_time_epoch = entry_time_epoch; 
		this.entry_time_utc = entry_time_utc;
		this.result_hash = result_hash;
		this.outcome = outcome;
		this.result_string = result_string;
		this.chosen_numbers = chosen_numbers;
	}
        
    	public String getUserId() {
        	return user_id;
    	}

	public long getEntryTimeEpoch() {
		//convert to long
		long entry_time_long = Long.parseLong(entry_time_epoch);
		return entry_time_long;
	}

	public String getEntryTimeUtc() {
		return entry_time_utc;
	}
        
        public String getOutcome() {
                return outcome;        
        }        

        public String getResultHash() {
                return result_hash;
        }        

        public String getResultString() {
                return result_string;
        }        
	public String getChosenNumbers() {
                return chosen_numbers;
        }
}





