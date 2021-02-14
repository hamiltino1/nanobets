package nanobets;	

//java
import java.math.BigInteger; 
import java.math.BigDecimal; 
import java.io.IOException;
import java.util.Random;
import java.util.HashMap;
import java.util.Stack;
import java.util.Map;

//jnano
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountInfo; 
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo; 
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance; 
import uk.oczadly.karl.jnano.rpc.exception.RpcException; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend; 
import uk.oczadly.karl.jnano.callback.BlockData; 
import uk.oczadly.karl.jnano.model.NanoAmount; 
import uk.oczadly.karl.jnano.rpc.exception.RpcInvalidResponseException; 
import uk.oczadly.karl.jnano.rpc.exception.RpcEntityNotFoundException; 
import uk.oczadly.karl.jnano.model.HexData; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestPending;
import uk.oczadly.karl.jnano.rpc.response.ResponsePending; 
import uk.oczadly.karl.jnano.rpc.response.ResponsePending; 
import uk.oczadly.karl.jnano.rpc.response.ResponsePending.PendingBlock; 
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestReceive; 
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash; 
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountInfo; 


//nanobets
import static nanobets.Main.*;
import static nanobets.Listener.*;
import static nanobets.PojoBuilder2.*;

public class BlockTools {

	private static RequestAccountBalance request_balance;
	private static int users_size;

	//for account not found catch block. Delete user address on catch
	private static String user_id_catch = null;

	/**
	 * Returns pocketed balance of given address.
	 */
	public static BigInteger getPocketedBalance(String address) throws IOException, RpcException {
		RequestAccountBalance request_pocketed_balance = new RequestAccountBalance(address);	
		ResponseBalance response_balance = node.processRequest(request_pocketed_balance);
		NanoAmount nano_amount = response_balance.getPocketed();
		BigInteger raw_pocketed = nano_amount.getAsRaw();

		return raw_pocketed;

	}	
        /**
         * returns pending_map 
         */
        public static HashMap<String, String> getPendingMap() {
                return pending_map;
        }
	/**
	 * Returns pending balance of given address.
	 */
	public static BigInteger getPendingBalance(String address) throws IOException, RpcException {
		RequestAccountBalance request_pocketed_balance = new RequestAccountBalance(address);	
		ResponseBalance response_balance = node.processRequest(request_pocketed_balance);
		NanoAmount nano_amount = response_balance.getPending();
		BigInteger raw_pending = nano_amount.getAsRaw();
		return raw_pending;
	}	
	public static Map<HexData, ResponsePending.PendingBlock> getPendingBlocks(String address) throws IOException, RpcException {
		RequestPending request_pending = new RequestPending(address, 100);	
		ResponsePending response_pending = node.processRequest(request_pending);
		Map<HexData, ResponsePending.PendingBlock> pendingMap  = response_pending.getPendingBlocks();
		return pendingMap;
	}
	//public static BigInteger getPeningBlockHash(String address) throws IOException, RpcException {
		//RequestPending pending = RequestPending
		// ResponsePending getPendingBlocks
		//get the block hash, then RequestReceieve
	//}
	//

	/**
	 * Returns random string.
	 */
	public static String randomStringGenerator() {
		char[] chars = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();
		StringBuilder sb = new StringBuilder(10);
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
    			char c = chars[random.nextInt(chars.length)];
    			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}

	/**
	 * Updates admin, users and deposit table 
	 * Moves deposited funds to hot wallet
	 * Called from ListenerThread.
	 */	
	public static boolean update(UserObject update_user, BlockData update_block, BigInteger raw_amount) {
		try {
			//Update the db user balance.		
			PojoBuilder2.updateDbBalance(update_user, raw_amount);


                        //update the deposit record

			//get block hash
			//HexData hex3 = update_block.getBlockHash();
			//String block_hash = hex3.toString();

			//set user and some  key/values
			//String update_user_address = update_user.getNanoAddress();		
			//String user_id = update_user.getId();

			//initialize for scope
			//BigInteger raw_pocketed = getPocketedBalance(update_user_address);

					
			//Transfer funds to hot wallet
			//String sourceAccount = update_user_address;
			//String destinationAccount = "nano_3z1z144ggdujyypjmrm9dn37a5jimur6g5poshxzz5ryr6jjf8hufjgq16yr"; 
			//String walletId = "07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4";
			//String randomString = randomStringGenerator();

			//BigDecimal admin_big_dec_balance = new BigDecimal(raw_pocketed);

			//System.out.println(raw_pocketed);
			//System.out.println("raw pocketed in update:" + raw_pocketed);

        		//BigInteger b2 = new BigInteger("0"); 
			/*
			if(raw_pocketed.compareTo(b2) == 0) {
				System.out.println("raw pocketed is 0, not updated");
				return false;
			}
			*/

			//RequestSend request = new RequestSend(walletId, sourceAccount, destinationAccount, raw_pocketed, randomString);    
			//node.processRequest(request);
				
			//clear work stack
			work_stack.clear();

			//confirmation log
			System.out.println("Updated");

			//update admin and insert record in deposit table.
			//request_balance = new RequestAccountBalance(destinationAccount);	
			//ResponseBalance response_balance = node.processRequest(request_balance);
			//NanoAmount nano_amount = response_balance.getPocketed();
			//BigInteger raw_pocketed_admin = nano_amount.getAsRaw();;

			//todec for admin
			//BigDecimal dec_amount = new BigDecimal(raw_pocketed_admin);	
			//updateAdminBalance(raw_pocketed_admin, dec_amount);			
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
		return false;
	}
	public static void receiveDeposit(RequestReceive receive) throws IOException, RpcException {
		ResponseBlockHash response = node.processRequest(receive);
		System.out.println("Received deposit manually");
	}
	/**
	 * Updates admin, users and deposit table 
	 * Moves deposited funds to hot wallet 
	 * Called from UserThread
	 */	
	public static boolean update(UserObject update_user, String address, BigInteger deposit, String pending_block, long block_count) {
		try {

			//set user and some  key/values
			String update_user_address = update_user.getNanoAddress();		
			String user_id = update_user.getId();
			user_id_catch = user_id;

			//Update the db user balance.		
                        System.out.println(deposit);
                        System.out.println(deposit);
                        System.out.println(deposit);
			PojoBuilder2.updateDbBalance(update_user, deposit);
			System.out.println("User balance is updated");
			System.out.println("User balance is updated");
			
			//Transfer funds to hot wallet
			//String sourceAccount = address;
			//String destinationAccount = "nano_3z1z144ggdujyypjmrm9dn37a5jimur6g5poshxzz5ryr6jjf8hufjgq16yr"; 
			//String destinationAccount = "nano_38c4ccb1nce9up47zq5w5ud3kzzffkqho17mdz6r6ogdf1a9f7zawaexhez3"; 
			//String walletId = "07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4";

			//BigDecimal admin_big_dec_balance = new BigDecimal(deposit);

			RequestAccountInfo info_request = new RequestAccountInfo(address);
			ResponseAccountInfo info = node.processRequest(info_request);
			HexData hex5 = info.getFrontierBlockHash();
			String block_hash = hex5.toString();
                        
                        //Map<HexData, ResponsePending.PendingBlock> pendingMap = getPendingBlockHash;

			//confirmation log
			System.out.println("Uncaught deposit found and updated.");

			work_stack.clear();

			//add deposit record	
                        System.out.println("Inserted deposit record");
			return true;
		}
		catch(RpcInvalidResponseException e) {
			e.printStackTrace();
			String response = e.getResponseBody();
			System.out.println("response: " + response);
			return false;
		}
		catch(RpcEntityNotFoundException e) {
			PojoBuilder.deleteUserAddress(user_id_catch);
			return false;

		}
		catch(RpcException e) {
			//System.out.println("rpc exception in update");
			e.printStackTrace();
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
		return false;
	}

	public static void moveToMainAccount(String walletId, String sourceAccount, String destinationAccount, BigInteger deposit, String randomString) throws IOException, RpcException {
		//Moves funds to hot wallet
		System.out.println("moving to hot wallet");
		RequestSend request = new RequestSend(walletId, sourceAccount, destinationAccount, deposit, randomString);    
		node.processRequest(request);
	}

	/**
	 * Checks if block address matches any in users table.
	 */
	public static boolean checkMatch(String db_account_address, String block_account_address) {
		if(block_account_address.equals(db_account_address)) {
			return true;
		}
		else {
			return false;
		}
	}
        public static ResponseAccountInfo getAccountInfo(String account) throws IOException, RpcException {
                RequestAccountInfo request_account = new RequestAccountInfo(account);
                ResponseAccountInfo response = node.processRequest(request_account);
                return response;
        }
}


