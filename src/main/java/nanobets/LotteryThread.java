package nanobets; 

//jnano
import uk.oczadly.karl.jnano.callback.BlockCallbackListener;
import uk.oczadly.karl.jnano.callback.BlockData; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance; 
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance; 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend; 
import uk.oczadly.karl.jnano.rpc.exception.RpcException; 
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestPasswordEnter; 


//java
import java.net.InetAddress; 
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList; import java.sql.DriverManager;
import java.lang.StringBuilder; 
import java.net.MalformedURLException;
import java.io.IOException;
import java.util.Stack;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

//sql2o
import org.sql2o.Sql2o;
import org.sql2o.*;

//native
import static nanobets.Main.*;
import static nanobets.BlockTools.getPocketedBalance;
import static nanobets.BlockTools.getPendingBalance;
import static nanobets.BlockTools.checkMatch;
import static nanobets.BlockTools.randomStringGenerator;
import static nanobets.BlockTools.update;
import static nanobets.PojoBuilder2.*;

//httpcore
import uk.oczadly.karl.jnano.rpc.HttpRequestExecutor; 

public class LotteryThread extends Thread {

	private String target;
	private InetAddress node;
	private List<UserObject> all_users; 
	private UserObject user;
	private String block_account_address;
	private BigInteger raw_pocketed;
	private BigInteger raw_pending;
	private RpcQueryNode nodeu = new RpcQueryNode();


	private List<GambleObject> gamble_list;
	private GambleObject lottery_object; 	
	private String epoch_draw; 
	private Long epoch_draw_long = new Long(0);
	private long epoch_current;
	private String outcome;
	private long entry_time_epoch;
	private String user_id;
	private int winners = 0;
	private Stack<String> winners_stack = new Stack<String>();

  	public LotteryThread(){
    	}

	/**
	 * Runs user thread
	 */
	public void run()  {

		System.out.println("Lottery thread started");

		//get current lottery jackpot
		List<AdminObject> jackpot_list = PojoBuilder.getLotteryJackpot();
		AdminObject admin = jackpot_list.get(0);
		double jackpotAdmin = admin.getBalance();	
		if(jackpotAdmin <= 0) {
			double new_jackpot = 50;
			PojoBuilder.updateLotteryJackpot(new_jackpot);
		}
		//lottery id
		String lottery_id = "bb469234a7698fc5baf8d983303472e78b9430880344effc9c57b9c4a4de71ae";
		try {
			//get next lottery object
			gamble_list =  PojoBuilder.getLotteryObject(lottery_id);
			lottery_object = gamble_list.get(0); 	

			String result_hash = lottery_object.getResultHash();
			System.out.println(result_hash);
			//get lottery_draw_time_epoch
			epoch_draw = lottery_object.getLotteryEpoch();

			epoch_draw_long = Long.valueOf(epoch_draw).longValue();

			System.out.println("draw time: " +  epoch_draw_long);

			//get current epoch time
			epoch_current = Instant.now().getEpochSecond();
			System.out.println("current time: " +  epoch_current);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	
		if(epoch_current > epoch_draw_long) {
			
			System.out.println("current time is greater than lottery draw time, so it's ready to reveal");

			try {
				/*reveal numbers  */
				Map<String, List<String>> result_map = lottery_object.getHashMapResult();
				List<String> result_numbers_list = result_map.get("5");
				//lottery result number
				String result_numbers = result_numbers_list.toString();
				String filtered = result_numbers.replaceAll("\\W+","");
				int result_numbers_int = Integer.parseInt(filtered);
				/* end */
				System.out.println("resultNumbers: " + result_numbers);
				
				EntryObject entry_object;	
				
				//check for winners
				List<EntryObject> entryObjectList = PojoBuilder.getPendingEntryObjects();				
				//entry size
				int entry_size = entryObjectList.size();	
				/*Cycle through entry size and check */
				for(int i = 0; i < entry_size; i++ ) {
					entry_object = entryObjectList.get(i);		
					//
					String chosen_numbers = entry_object.getChosenNumbers();	
					entry_time_epoch = entry_object.getEntryTimeEpoch();
					user_id = entry_object.getUserId();

					//convert into int
					int chosen_numbers_int = Integer.parseInt(chosen_numbers);
				
					if(chosen_numbers_int == result_numbers_int) {
						//Match found
						//add counter to winners and push id to winners stack.
						winners ++;
						winners_stack.push(user_id);
						System.out.println("Match found");
						System.out.println(result_numbers_int);
						//change outcome to win
						outcome = "WIN" ;  
						PojoBuilder.updateEntryOutcome(outcome, entry_time_epoch, user_id);	
						//updates gamble/lottery record
					}
					else {
						System.out.println("chosen numbers: " + chosen_numbers_int + " not a match");
						outcome = "LOSS" ;  
						PojoBuilder.updateEntryOutcome(outcome, entry_time_epoch, user_id);	
						//change outcome to loss
					}
				}
				//Check winners and give prizes here.
				//work out split prize
				//get current jackpot
				List<AdminObject> admin_list = PojoBuilder.getLotteryJackpot();
				AdminObject jackpotObject = admin_list.get(0);
				double jackpot = jackpotObject.getBalance();	
				System.out.println(jackpot);
				System.out.println(jackpot);
				System.out.println(jackpot);
				int split =  winners_stack.size();
				double user_reward = jackpot/split;

				/*Cycle through and reward winners*/				
				for(String id : winners_stack)
				{
					//get user
					List<UserObject> user_list = PojoBuilder.getUserObject(id);
					UserObject user = user_list.get(0);
					//update user balance if won
					PojoBuilder.updateDbBalance(user, user_reward);
				}
				if(winners > 0) {
					//clear jackpot
					PojoBuilder.updateLotteryJackpot(0);
				}

				String winners_str = Integer.toString(winners);
				PojoBuilder.updateLotteryRecord(lottery_object, winners_str);
				System.out.println("Lottery record updated.");

				winners_stack.clear();
				winners = 0;

			} 
			
			catch(Exception e) {
				e.printStackTrace();

			}
		}
		else {
			System.out.println("current time is less than lottery draw time, so it's not ready to reveal");
		}		
					

	}
}
