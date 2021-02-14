package nanobets;

//java
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.io.*;
import java.sql.DriverManager;
import java.net.MalformedURLException;
import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;
import static java.lang.Math.round;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.awt.image.BufferedImage;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Stack;
import java.lang.InterruptedException; 
import java.util.concurrent.ExecutionException;
import uk.oczadly.karl.jnano.model.block.Block; 
import java.util.stream.Collectors; 
import java.util.HashSet;


//sql2o
import org.sql2o.*;

//nanobets
import static nanobets.JsonUtil.json;
import static nanobets.Main.*;
import static nanobets.PojoBuilder.*;

//apache
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;

//jnano
import uk.oczadly.karl.jnano.util.WalletUtil;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.request.node.RequestVersion;
import uk.oczadly.karl.jnano.rpc.QueryCallback;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.wallet.*;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;
import uk.oczadly.karl.jnano.rpc.response.ResponsePending.PendingBlock; 
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestAccountCreate;
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;
import uk.oczadly.karl.jnano.rpc.exception.RpcException; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestBlockHash;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkGenerate; 
import uk.oczadly.karl.jnano.rpc.response.ResponseWork; 
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountInfo; 
import uk.oczadly.karl.jnano.model.NanoAccount; 
import uk.oczadly.karl.jnano.model.work.WorkSolution; 
import uk.oczadly.karl.jnano.rpc.response.ResponseWorkValidation; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkValidate; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkCancel; 
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful; 
import uk.oczadly.karl.jnano.model.work.WorkDifficulty; 
import uk.oczadly.karl.jnano.model.work.WorkSolution; 
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder; 
import uk.oczadly.karl.jnano.model.HexData; 

//jackson
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

//qrcodegen
import io.nayuki.qrcodegen.*;


public class Tools {

	private static String frontBlock;

	
       /**
	* Creates and returns unique id.
	*/
        public static String getId(String fingerprint, String ip) {
                String combine = fingerprint + ip;
                String id = DigestUtils.sha256Hex(combine);
                return id;
        }
        


	/**
	 * Create nano account.
	 */
        public static HashMap<String, String> createNanoAccount(RpcQueryNode node) throws MalformedURLException{
                //WalletUtil walletUtil = new WalletUtil();
                //String seed = walletUtil.generateNewSeed();
                String accountAddress = "";
		//Desktop: 07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4
		//Laptop: 3CE9731B8ED00B9269DA4F539B604B73A7237FE73ED803ABE17466DA1EF0A49B
                String walletId = "07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4";
                System.out.println(walletId);

                try {
                        // Construct the request (and pass query arguments)
                        RequestAccountCreate request = new RequestAccountCreate
				("07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4");
                        // Send request to the node synchronously and retrieve response
                        ResponseAccount response = node.processRequest(request);
                        NanoAccount nanoAccount = response.getAccountAddress();
			accountAddress = nanoAccount.toString();
                        walletId = request.getWalletId();
                }
                catch (RpcException | IOException e) {
                            e.printStackTrace();
                }
                HashMap<String, String> hmap = new HashMap<String, String>();
                System.out.println(accountAddress);
                hmap.put("accountAddress", accountAddress);
                hmap.put("walletId", walletId);
		System.out.println("hmap nano address test: " + hmap.get("accountAddress"));
		System.out.println(hmap.get(walletId));
                return hmap;
        }
	/**
	 * Withdraw method.
	 */
        public static boolean withdraw(BigInteger amount, BigInteger balance, String destinationAccount, UserObject user, String string_amount, String withdraw_id) throws MalformedURLException, IOException, RpcException 	{

			List<UserObject> user_list  = PojoBuilder.getUserObject(user.getId());
			UserObject user1 = user_list.get(0);
			BigInteger bigb = user1.getBalance();

			if(bigb.compareTo(amount) < 0) {
				System.out.println("bigb error");
				throw new java.lang.RuntimeException("bigb error");
			}

			String random_string = RandomStringUtils.randomAlphabetic(10);
			String walletId1 = user.getWalletId();
			String userId1 = user.getId();
			String user_id = user.getId();

			//global scope variables/object references
			//hot wallet
			String sourceAccount = "nano_3z1z144ggdujyypjmrm9dn37a5jimur6g5poshxzz5ryr6jjf8hufjgq16yr";
			ResponseBlockHash response = null;
			String block_hash = null;

			//attempt update withdraw and user table
			Double amount_double = (Double.parseDouble(string_amount)  * - 1);

			try {
				if((PojoBuilder.updateDbBalance(user, amount_double) == true)) {
				}
				else {
					throw new java.lang.RuntimeException("balance update error");
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			//jnano process
			//String randomString = randomStringGenerator();
			String walletId = user.getWalletId();
			String userId = user.getId();
			frontBlock = getFrontier(sourceAccount);

			try {
				if(work_stack.isEmpty() == false) {
					System.out.println("work stack not empty");
					//check if work is valid.
					String work = work_stack.pop();
					//check if frontier is valid
					RequestWorkValidate workValidate = new RequestWorkValidate(work, frontBlock);
					ResponseWorkValidation workResponse = node.processRequest(workValidate);
					if(workResponse.isValid()) {
						RequestSend request = new RequestSend(walletId, sourceAccount, destinationAccount, amount, withdraw_id, work);
						response = node.processRequest(request);
						//Get block hash
						HexData hex = response.getBlockHash();
						block_hash = hex.toString();
						//new balance is balance minus amount
						BigInteger new_raw_balance = balance.subtract(amount);
						String new_balance_string = new_raw_balance.toString();
						PojoBuilder.insertWithdrawRecord(withdraw_id, destinationAccount, userId1, string_amount, block_hash);
						System.out.println("Processed fast withdrawl");
					}
					else {
						System.out.println("Invalid work");
						addToWithdrawStack(walletId, withdraw_id, user_id, user, amount_double, amount, destinationAccount, sourceAccount);	
					}
				}
				else {
					addToWithdrawStack(walletId, withdraw_id, user_id, user, amount_double, amount, destinationAccount, sourceAccount);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return true;
       	}
	public static void addToWithdrawStack(String walletId, String withdraw_id, String user_id, UserObject user, double amount_double, BigInteger amount, String destinationAccount, String sourceAccount) {
		RequestSend request = new RequestSend(walletId, sourceAccount, destinationAccount, amount, withdraw_id);
		withdraw_stack.push(request);
		withdraw_id_stack.push(withdraw_id);
		//May not be nessassary
		global_withdraw_id = withdraw_id;
		id_stack.push(user_id);
		UpdateObject update_object = new UpdateObject(user, amount_double);
		balance_update_stack.push(update_object);
	}
	/**
	 * Returns unique string.
	 */
	public static String randomStringGenerator() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzk123456789".toCharArray();
		StringBuilder sb = new StringBuilder(16);
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
    			char c = chars[random.nextInt(chars.length)];
    			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}
	/**
	* Checks if bet is valid.
	*/
        public static boolean checkValidBet(Map<String, String> params, List<UserObject> user_list) {

		String bet_amount = params.get("bet_amount");
		String session_id = params.get("session_id");
		String chosen_odds = params.get("chosen_odds");
		String chosen_number = params.get("chosen_number");

		//initialize for scope
		Double double_bet_amount = new Double(0.00);
		int chosen_number_int;
		int chosen_odds_int;

		//try-catch block to check for number format exceptions
		try {
			//convert a few of the query params to new type
			double_bet_amount = Double.valueOf(bet_amount);
			final double double_bet_amount_final = double_bet_amount;
			chosen_number_int = Integer.parseInt(chosen_number);
			chosen_odds_int = Integer.parseInt(chosen_odds);
			System.out.println(chosen_number_int);
			System.out.println(chosen_odds_int);

			if(user_list.isEmpty() == true) {
				System.out.println("Empty user");
				return false;
			}
			//check valid odds
			if(IntStream.of(1, 2, 3, 4, 5).anyMatch(i -> i == chosen_odds_int) && (DoubleStream.of(0.1, 0.6, 1.1, 1.6, 2.1).anyMatch(i -> i == double_bet_amount_final))) { 
				//get decimal places
				String[] splitter = double_bet_amount.toString().split("\\.");
				int decimal_places = splitter[1].length();
				if(decimal_places > 6) {
					return false;
			
				}
				
				if(chosen_number_int > 6) {
					return false;
				}


				//get user object and some elements.
				UserObject user = user_list.get(0);
				String user_id = user.getId();
				BigDecimal big_dec = user.getDecimalBalance();

				if(big_dec == null || big_dec.equals(BigDecimal.ZERO)) {
					return false;
				}

				BigDecimal dec_bet_amount = BigDecimal.valueOf(double_bet_amount);

				//Check if bet amount is less or equal to users balance.
				if(big_dec.compareTo(dec_bet_amount) >=0) {
					return true;
				}

			}
			System.out.println("Anymatch failed");
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	* Checks if entry is valid.
	*/
        public static boolean checkValidEntry(Map<String, String> params, UserObject user) {

		String session_id = params.get("session_id");
		String chosen_numbers = params.get("chosen_numbers");

		if(chosen_numbers.length() != 5) {
			return false;
		}

			//get user balance
			BigDecimal balance = user.getDecimalBalance();
			//initiate the cost amount of a single entry
			BigDecimal compare = new BigDecimal(0.07); 
			compare = compare.setScale(2, RoundingMode.DOWN);

			
			//check if user has sufficient funds for entry.
			if(balance.compareTo(compare) >= 0) {

				//Convert string number input into int
				int chosen_numbers_int = Integer.parseInt(chosen_numbers);
				//Convert into an  int array 	
				int[] num_array = new int[chosen_numbers.length()];

				//Cycle through each digit input and check if valid.
				for (int a = 0; a < chosen_numbers.length(); a++) {
					final int number = chosen_numbers.charAt(a) - '0'; 
					if(IntStream.of(1, 2, 3, 4, 5, 6).anyMatch(i -> i == number)) {
						num_array[a] = number;         		
					}	
					else {
						throw new java.lang.RuntimeException("Each digit must be between 1 and 6");
					}
					
				}
				/* if num_array does not contain distinct values, throw an error */
				if(isUnique(num_array) == false) {
					throw new java.lang.RuntimeException("Each digit must be distinct");
				}
			}
			else {
				throw new java.lang.RuntimeException("Insufficient funds.");
			}

		return true;
		}
		
	/**
	 * Returns gamble object for user_id
	 */
	public static GambleObject getUserGambleObject(UserObject user) {

		GambleObject gamble_object = null;
		String user_id = user.getId();
		List<GambleObject> gamble_list;

		try {
			//Get empty or non empty list of type gambleObject.
			gamble_list = PojoBuilder.getGambleObject(user_id);
			System.out.println("list size" + gamble_list.size());

			//---Archived----//
			//If empty, assign new gamble record and initialize gamble object.
			/*if(gamble_list.isEmpty() || gamble_list.size() < 20) {
				System.out.println("got here");
				int to_add = 20 - gamble_list.size();
				gamble_list = PojoBuilder.getEmptyGambleObject();
				for(GambleObject x : gamble_list) {
					PojoBuilder.updateGambleUserObjectId(x, user);
				}
			}
			*/

			//gamble_list = PojoBuilder.getGambleObject(user_id);
			gamble_object = gamble_list.get(0);
		}
		catch(Exception e) {
			e.printStackTrace();
			gamble_list = Collections.emptyList();
		}
		finally {
			return gamble_object;
		}

	}

	/**
	 * Get's profit. 
	 */
	public static double getProfit(GambleObject gamble_object, Map<String, String> params) throws JsonProcessingException {

		double profit;

		//Get user input values
		String bet_amount = params.get("bet_amount");
		String chosen_number_str = params.get("chosen_number");
		String chosen_odds_str = params.get("chosen_odds");
		//conversion
		double double_bet_amount = Double.parseDouble(bet_amount);
		double chosen_number_int = Double.parseDouble(params.get("chosen_number"));
		double chosen_odds_int = Double.parseDouble(params.get("chosen_odds"));
		//get map of object
		Map<String, List<String>> map = gamble_object.getHashMapResult();
//		System.out.println("map " +map);
		List<String> str_list = map.get(chosen_odds_str);
		System.out.println(str_list);
		List<Integer> int_list = new ArrayList<>();
		System.out.println(int_list);
		for(String s : str_list) { 
			System.out.println(s);
			int intt = Integer.valueOf(s);
			int_list.add(intt); 
		}
		for(int x : int_list) {
			if(x == chosen_number_int) {
			 	double multiplier = (6/chosen_odds_int) * 0.985;
			 	profit = ((double_bet_amount * multiplier) - double_bet_amount);
				profit = profit;
        			profit = Math.round(profit * 1000000.0) / 1000000.0;
				return profit;
			}
		}
		profit = (double_bet_amount * -1);
		return profit;
	}

	/**
	 * Converts list into hashmap
	 */
	public static Map<String, String> toMap(List<NameValuePair> pairs) {
		Map<String, String> map = new HashMap<>();
		for(int i=0; i<pairs.size(); i++){
			NameValuePair pair = pairs.get(i);
			map.put(pair.getName(), pair.getValue());
		}
		return map;
	}
	/**
	 * QR generation
	 */
	public static String generateQr(String nano_address) throws IOException  {

		QrCode qr = QrCode.encodeText(nano_address, QrCode.Ecc.MEDIUM);
		BufferedImage img = qr.toImage(4, 10);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", baos);
		baos.flush();
		byte[] bytes  = baos.toByteArray();
		String qr_code = Base64.getEncoder().encodeToString(bytes);
		return qr_code;
	}	
	/**
	 * check IP use count
	 */
	public static boolean checkIpCount(String ip) throws RuntimeException {
		List<UserObject> ip_list = PojoBuilder.getUsedIpCount(ip);
		int ip_list_size = ip_list.size();
		System.out.println("ip count: " + ip_list_size);
		if(ip_list_size >= 4) {
			throw new java.lang.RuntimeException("Your IP address has been used to register to many times, please login instead");
		}
		else {
			return true;
		}
	}

	/**
	 * Return new id
	 */
	public static String generateId(String ip, String fingerprint) {
		String combine = fingerprint + ip;
                String id = DigestUtils.sha256Hex(combine);
		return id;
	}
	/**
	 * get work solutuon of admin frontier
	 */
	public static void getWorkSolution(String frontier) throws IOException, RpcException {
		RequestWorkGenerate request = new RequestWorkGenerate(frontier);
		ResponseWork response = node.processRequest(request);
		WorkSolution workSolution = response.getWorkSolution();
		work_stack.push(workSolution.toString());
		System.out.println(workSolution.toString());
	}
	/**
	 * get frontier
	 * @parm string
	 */
	public static String getFrontier(String account) throws IOException, RpcException {
		RequestAccountInfo request = new RequestAccountInfo(account);
		ResponseAccountInfo response = node.processRequest(request);
		HexData hexFront = response.getFrontierBlockHash();
		return hexFront.toString();
	}
	/* Checks if int array contains all unique digits.*/	
	public static boolean isUnique(int[] nums){
    		Set<Integer> set = new HashSet<>(nums.length);
		/* Can't add duplicate keys to a HashSet, so return false if an attempt is made.*/
	    	for (int a : nums) {
			if (!set.add(a))
		    return false;
	    	}
    		return true;
	}
}
