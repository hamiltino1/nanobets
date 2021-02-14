package nanobets;

//jackson
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException; 

//java
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class GambleObject {
    	private String result_hash;

    	private String user_id;

    	private String result_string;

	private String json_result;

    	private String used;

    	private String epoch_time;

        private String profit;

        private String chosen_odds;

	private String chosen_number;

	private String result_number;

	private String time_used;

	private String epoch_time_used;

	private String lottery_draw_time_utc;

	private String lottery_draw_time_epoch;

	private String id;

	private String lottery_winners;

	private String winners;

    	public GambleObject(String result_hash, String result_string, String used, 
			String profit, String chosen_number, String chosen_odds, String json_result, String result_number, String time_used, String epoch_time, String lottery_draw_time_utc, String lottery_draw_time_epoch, String user_id, String epoch_time_used, String id, String lottery_winners, String winners) {

		this.epoch_time_used = epoch_time_used;
		this.lottery_winners = lottery_winners;
		this.winners = winners;
		this.id = id;
		this.user_id = user_id;
		this.lottery_draw_time_epoch = lottery_draw_time_epoch;
		this.lottery_draw_time_utc = lottery_draw_time_utc;
		this.epoch_time = epoch_time;
                this.result_hash = result_hash;
		this.result_string = result_string;
		this.used = used;
                this.profit = profit;
                this.chosen_odds = chosen_odds;
		this.chosen_number = chosen_number;
		this.json_result = json_result;
		this.result_number = result_number;
		this.time_used = time_used;
	}

    	public String getResultHash() {
        	return result_hash;
    	}
	public String getResultString() {
		return result_string;
	}
	public String getResultNumber() {
		return result_number;
	}
	public Map<String, List<String>> getHashMapResult() throws JsonProcessingException {

		//convert result string to <string, Object> map. Then do a second converion into a <String, String> map.
		Map<String, List<String>> map = new ObjectMapper().readValue(json_result, HashMap.class);
	       	List<String> one   =  map.get("1");
	       	List<String> two   =  map.get("2");
		List<String> three =  map.get("3");
		List<String> four  =  map.get("4");
		List<String> five  =  map.get("5");
		//List<String> six   =  map.get("6");
		//List<String> seven =  map.get("7");
		//List<String> eight =  map.get("8");
		//List<String> nine  =  map.get("9");
		Map<String, List<String>> new_map = new HashMap();

		new_map.put("1", one);
		new_map.put("2", two);
		new_map.put("3", three);
		new_map.put("4", four);
		new_map.put("5", five);
		//new_map.put("6", six);
		//new_map.put("7", seven);
		//new_map.put("8", eight);
		//new_map.put("9", nine);

		return new_map;
	}
	public String getProfit() {
		return profit;
	}
	public String getChosenOdds() {
		return chosen_odds;
	}
	public String getChosenNumber() {
		return chosen_number;
	}
	public String getUsed() {
		return used;
	}
	public String getTimeUsed() {
		return time_used;
	}
	public String getLotteryUTC() {
		return lottery_draw_time_utc;
	}
	public String getLotteryEpoch() {
		return lottery_draw_time_epoch;
	}
	public String getUserId() {
		return user_id;
	}
	public String getWinners() {
		return winners;
	}

}

	
