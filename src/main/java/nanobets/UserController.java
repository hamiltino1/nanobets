package nanobets;

//java 
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.charset.Charset;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.lang.IndexOutOfBoundsException;
import java.lang.RuntimeException;
import java.time.Instant;


//google 
import com.google.gson.Gson;

//spark 
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.halt;
import spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.ResponseTransformer;

//apache 
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.commons.lang3.exception.ExceptionUtils; 

//nanobets
import static nanobets.JsonUtil.json;
import static nanobets.Tools.getId;
import static nanobets.Main.*;
import static nanobets.Tools.withdraw;
import static nanobets.Tools.checkValidBet;
import static nanobets.Tools.randomStringGenerator;
import static nanobets.Tools.getUserGambleObject;
import static nanobets.Tools.*;
import static nanobets.Tools.toMap;
import static nanobets.PojoBuilder.*;

//jnano 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.exception.RpcUnrecognizedException; 
import uk.oczadly.karl.jnano.rpc.exception.RpcInvalidArgumentException; 
import uk.oczadly.karl.jnano.rpc.exception.RpcInvalidRequestJsonException; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountPublicKey; 
import uk.oczadly.karl.jnano.rpc.response.ResponseKey; 


//jackson 
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.core.JsonProcessingException; 

public class UserController {

	private UserObject userx;
	private double exceptionAmount;
	private String session_id_catch = "";

                public UserController(final RpcQueryNode node) throws JsonProcessingException {
			
			get("/", (request, response) -> {
                    response.type("text/html");
    				response.redirect("index.htm#gamble"); 
				return null;
			});
			
			/**
			 * Public gamble history post api.
			 **/
			get("/api/gamble/history", (request, response) -> {
                response.type("application/json");
				List<GambleObject> gambleObject;
				try {
					gambleObject  = PojoBuilder.getPublicGambleHistory();
                                	return gambleObject;
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                }, json());
			/**
			 * Get current lottery jackpot.
			 **/
			get("/api/lottery/jackpot", (request, response) -> {
                response.type("application/json");
				List<AdminObject> adminObject_list;
				try {
					adminObject_list  = PojoBuilder.getLotteryJackpot();
					AdminObject adminObject  = adminObject_list.get(0);
					double balance = adminObject.getBalance();
                                	return balance;
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                        }, json());
			/**
			 * Public future lottery draws  post api.
			 **/
			get("/api/lottery/future", (request, response) -> {
                                response.type("application/json");
				List<GambleObject> gambleObject;
				try {
					gambleObject  = PojoBuilder.getPublicLotteryFutures();
                    return gambleObject;
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                                
            }, json());

			/**
			 * entry history post api.
			 **/
			post("/api/user/history/entries", (request, response) -> {
				List<EntryObject> EntryObjectList;
                response.type("application/json");
				try {

					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String user_id = params.get("session_id");

					EntryObjectList  = PojoBuilder.getEntryHistory(user_id);
					return EntryObjectList;
				}
				catch(Exception e) {
					response.status(400);
					String errorMsg = "Error: " + e.getMessage();
					return errorMsg;
				}
                                
            }, json());
			/**
			 * entry history post api.
			 **/
			get("/api/user/history/entries/:user_id", (request, response) -> {
				List<EntryObject> entryObjectList;
                                response.type("application/json");
				try {
					String user_id = request.params("user_id");
					entryObjectList  = PojoBuilder.getEntryHistory(user_id);
					return entryObjectList;
				}
				catch(Exception e) {
					response.status(400);
					String errorMsg = "Error: " + e.getMessage();
					return errorMsg;
				}
                                
                        }, json());

			/**
			 * Public gamble history post api.
			 **/
			get("/api/lottery/history", (request, response) -> {
                                response.type("application/json");
				List<GambleObject> gambleObject;
				try {
					gambleObject  = PojoBuilder.getPublicLotteryHistoryLimit();
                                	return gambleObject;
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                                
                        }, json());
			/**
			 * Public gamble history get request method.
			 **/
			get("/api/lottery/history/:result_hash", (request, response) -> {
                                response.type("application/json");
				String result_hash = request.params(":result_hash");
				List<GambleObject> gambleObject;
				try {
					gambleObject  = PojoBuilder.getLotteryObjectByHash(result_hash);
                                	return gambleObject;
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                                
                        }, json());
			/**
			 * Public lottery future get request method.
			 **/
			get("/api/lottery/:result_hash", (request, response) -> {
                                response.type("application/json");
				String result_hash = request.params(":result_hash");
				List<GambleObject> gambleObject;
				try {
					gambleObject = PojoBuilder.getUnusedLotteryObjectByHash(result_hash);
					return gambleObject;
				}
				catch(IndexOutOfBoundsException ex) {
					response.status(400);	
					return "Could not find gamble record";
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                        }, json());
			/**
			 * Public gamble history post api.
			 **/
			get("/api/gamble/:result_hash", (request, response) -> {
                                response.type("application/json");
				String result_hash = request.params(":result_hash");
				List<GambleObject> gambleObject;
				List<GambleObject> gambleObject2;
				try {
					gambleObject = PojoBuilder.getGambleObjectByHashNoJsonResult(result_hash);
                                	gambleObject2 = PojoBuilder.getGambleObjectByHashNoJsonResult2(result_hash);
					gambleObject.addAll(gambleObject2);
					return gambleObject;
				}
				catch(IndexOutOfBoundsException ex) {
					response.status(400);	
					return "Could not find gamble record";
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                        }, json());
			/**
			 * Public gamble history post api.
			 **/
			get("/api/withdraw/:block_hash", (request, response) -> {
                                response.type("application/json");
				String block_hash = request.params(":block_hash");
				List<WithdrawObject> withdrawObject;
				try {
					withdrawObject = PojoBuilder.getWithdrawObject(block_hash);
					return withdrawObject;
				}
				catch(IndexOutOfBoundsException ex) {
					response.status(400);	
					return "Could not find withdraw record";
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                        }, json());

			/**
			 * Withdraw history post api.
			 **/
			post("/api/user/history/withdraw", (request, response) -> {
				List<WithdrawObject> withdrawObjectList;
                                response.type("application/json");
				try {

					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String user_id = params.get("session_id");

					withdrawObjectList  = PojoBuilder.getWithdrawHistory(user_id);
					return withdrawObjectList;
				}
				catch(Exception e) {
					response.status(400);
					String errorMsg = "Error: " + e.getMessage();
					return errorMsg;
				}
                                
                        }, json());

			/**
			 * deposit history
			 **/
			post("/api/user/history/deposit", (request, response) -> {
				List<DepositObject> depositObjectList;
                                response.type("application/json");
				try {
					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String user_id = params.get("session_id");

					depositObjectList = PojoBuilder.getDepositHistory(user_id);
					return depositObjectList;
				}
				catch(Exception e) {
					response.status(400);
					String errorMsg = "Error: " + e.getMessage();
					return errorMsg;
				}
                                
                        }, json());

			/**
			 * Private gamble history post api.
			 **/
			post("/api/user/history", (request, response) -> {
				List<GambleObject> gambleObject;
                                response.type("application/json");
				try {

					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String user_id = params.get("session_id");

					gambleObject  = PojoBuilder.getPrivateGambleHistory(user_id);

				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                                
                                return gambleObject;

                        }, json());
			/**
			 * Get user balance.
			 **/
			post("/api/user/balance", (request, response) -> {
				List<UserObject> balanceObject;
                                response.type("application/json");
				try {

					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String user_id = params.get("session_id");

					balanceObject = PojoBuilder.getBalanceObject(user_id);

				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                                
                                return balanceObject;

                        }, json());
			/**
			 * Private gamble history post api.
			 **/
			post("/api/user/hashes", (request, response) -> {
				List<GambleObject> gambleObject;
                                response.type("application/json");
				try {
					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String user_id = params.get("session_id");

					gambleObject  = PojoBuilder.getGambleObject(user_id);

				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                                
                                return gambleObject;

                        }, json());
			/**
			 *
			 **/
			post("/api/user/deposit", (request, response) -> {
				List<UserObject> userDepositObject;
                                response.type("application/json");
				try {

					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String user_id = params.get("session_id");

					userDepositObject = PojoBuilder.getDepositObject(user_id);
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
                                
                                return userDepositObject;

                        }, json());

			
			/**
			 * Post api for login.
			 **/
                        post("/api/login", (request, response) -> {
                                response.type("application/json");
				UserObject user;
				try {

					List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
					Map<String, String> params = toMap(pairs);
					String id = params.get("id");
					List<UserObject> user_list  = PojoBuilder.getUserObject(id);
					user = user_list.get(0);
				}
				catch(IndexOutOfBoundsException ex) {
					response.status(400);	
					return "Login id not found";
				}
				catch(Exception e) {
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);

				}

                                return user.getId();

                        }, json());
                        
			/**
			 * Post api for registration.
			 **/
                        post("/api/register", (request, response) -> {
                                List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
                                Map<String, String> params = toMap(pairs);
				UserObject user;
				String id = "";
                                
                                try {
                                        //String ip = request.ip();
					String ip = request.headers("CF-Connecting-IP");
					//String ip = "127.0.0.1";
					if(ip==null) {
						throw new java.lang.RuntimeException("IP not valid");
					}
					
                                        //System.out.println(ip);
                                        String fingerprint = params.get("canvas_fp");
                       			id = generateId(ip, fingerprint);
					System.out.println("id created " + id);

					System.out.println("id created " + id);
					System.out.println("id created " + id);

					if(idMap.containsKey(id)) {
						return false;
					}
					else {
						idMap.put(id, id);
					}

                                	List<UserObject> check = PojoBuilder.checkExists(id); 

					System.out.println("size check " + check.size());

				       	if(check.isEmpty() && Tools.checkIpCount(ip) == true) {	
						HashMap<String, String> hmap = createNanoAccount(node);
						System.out.println("nano account created: ");
						String nano_address = hmap.get("accountAddress");
						String qr_code = generateQr(nano_address);	

                                        	if(PojoBuilder.insertUserObject(ip, node, hmap, qr_code, id, fingerprint) == true) {
							System.out.println("User created");
						}		
						else {
							System.out.println("insert User failed");	
							System.out.println("insert User failed");	
							System.out.println("insert User failed");	
						}
                                        	List<UserObject> user_list = PojoBuilder.getUserObject(id);
						user = user_list.get(0);
							//assign 20 gamble objects to user
							List<GambleObject> gamble_object_list = PojoBuilder.getEmptyGambleObject();
							for(GambleObject gamble_object : gamble_object_list) {
								PojoBuilder.updateGambleUserObjectId(gamble_object, user);
							}
					idMap.remove(id);
					//double startingBalance = 0.01;
					//PojoBuilder.updateDbBalance(user, startingBalance);
					return id;
					}
                                }
				catch(RuntimeException e) {
					response.status(400);
					idMap.remove(id);
					return e.getMessage();
				}
                                catch(Exception e){
                                        String errorMsg = "Error: " + e.getMessage();;
                                        //return halt(500, errorMsg);
					idMap.remove(id);
					e.printStackTrace();
                                }
				idMap.remove(id);
				return id;
                        }, json());        

			/**
			 * Post api for withdraw request.   
			 **/                        
                        
                post("/api/withdraw", (request, response) -> {
                response.type("application/json");
                    String withdraw_id = randomStringGenerator();
                    List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
                    Map<String, String> params = toMap(pairs);                                
                    String strAmount;
                    String session_id = params.get("session_id");
				    double doubleAmount;
				    UserObject user;
				    String destinationAccount = params.get("destination_account");
				try { 
					RequestAccountPublicKey key = new RequestAccountPublicKey(destinationAccount);
					ResponseKey rkey = node.processRequest(key);	
								}
				catch(Exception e) {
					return "Invalid nano address " + destinationAccount;
				}
				if(destinationAccount.length() > 65 ) {
					response.status(401);	
					return "Invalid nano address + " + destinationAccount;
				}

				if(withdrawMap.containsKey(session_id)) {
					response.status(401);	
					return "Already processing withdraw";
				}	
				else {
					long time = System.currentTimeMillis();
					withdrawMap.put(session_id, time);
				}
				try {
                    //Get query Params in request body
					strAmount = params.get("amount");
					strAmount = strAmount.replaceAll("\n", "");
					doubleAmount = Double.valueOf(strAmount);

					//quick conversion into bigInt
					BigDecimal decAmount = new BigDecimal(strAmount);
					BigDecimal decAmountb = decAmount.movePointRight(30);
					BigInteger big_amount = decAmountb.toBigInteger();
					exceptionAmount = doubleAmount;
					
                    //Get user object with session_id
                    List<UserObject> newUserObject = PojoBuilder.getUserObject(session_id);                
                    user =  newUserObject.get(0);
					userx = user;
                    String sourceAccount = user.getNanoAddress();

					//Get user balance
					BigDecimal dec_balance = user.getDecimalBalance();
					BigInteger balance = user.getBalance();

					double min = 0.05;
					if(Double.compare(doubleAmount, min) == -1) {
						System.out.println("request to low");
						withdrawMap.remove(session_id);
						response.status(405);
						return "request to low";
					}
					String responsestr = null;

					//If balance => sendAmount  process, else return false(error)
					if(dec_balance.compareTo(decAmount) > -1) {

                        int size = withdraw_stack.size();
                        if(withdraw(big_amount, balance, destinationAccount, user, strAmount, withdraw_id) == true) {													
                            int size2 = withdraw_stack.size();
                            if(size == size2) {
                                responsestr =  "withdraw complete";
                                withdrawMap.remove(session_id);
                            }
                            else {
                                responsestr =  "Added to withdraw request stack. Withdraw id: " + withdraw_id;
                                PojoBuilder.insertWithdrawRequestRecord(params.get("session_id"), withdraw_id, params.get("amount"), params.get("destination_account"));
                                logger.info(withdraw_id + " " + strAmount);

                            }

                            response.status(200);
                            return responsestr;
                        }
						else {
                            response.status(409);
                            withdrawMap.remove(session_id);
                            throw new java.lang.RuntimeException("Withdraw fail");
						}
                    }

					else {
							withdrawMap.remove(session_id);
							response.status(411);
							throw new java.lang.RuntimeException("Error not enough balance");
					}
				}
				catch(RpcInvalidArgumentException e) {
					PojoBuilder.updateDbBalance(userx, exceptionAmount);
					exceptionAmount = 0;
                    String errorMsg = "Error: " + e.getMessage() + " try again";
					withdrawMap.remove(session_id);
					return errorMsg;
				}
				catch(RpcUnrecognizedException e) {
					PojoBuilder.updateDbBalance(userx, exceptionAmount);
					exceptionAmount = 0;
                    String errorMsg = "Error: " + e.getMessage() + " try again";
					withdrawMap.remove(session_id);
					return errorMsg;
				}
				catch(RpcInvalidRequestJsonException e) {
					PojoBuilder.updateDbBalance(userx, exceptionAmount);
					exceptionAmount = 0;
                    String errorMsg = "Error: " + e.getMessage() + " try again";
					withdrawMap.remove(session_id);
					return errorMsg;
				}
                catch(Exception e){
					//PojoBuilder.updateDbBalance(userx, exceptionAmount);
					userx = null; 
					exceptionAmount = 0;
					response.status(415);
                    e.printStackTrace();
					String stacktrace = ExceptionUtils.getStackTrace(e);
                    String errorMsg = "Error: " + e.getMessage();
					withdrawMap.remove(session_id);
					logger.info(stacktrace);
					return errorMsg;
                }                                

            }, json());
			/**
			 * Post api for gamble request and balance update.
			 */
			post("/api/gamble", (request, response) -> {
				response.type("application/json");
				String session_id = "";
                List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
                Map<String, String> params = toMap(pairs);                                

				//Initiate variables and objects before try-catch block for scope.
				String profit_str;
				GambleObject gamble_object;
				UserObject user;

				try {
					//Get user bet amount.
				 	String bet_amount = params.get("bet_amount");
					//Get user's session id, this should be the same as the users login id.
                                        session_id = params.get("session_id");
					//Get user's dice roll amount.
					String chosen_odds = params.get("chosen_odds");
					//Get the users winning roll choice.
					String chosen_number = params.get("chosen_number");
					//Another session id initailization for catch scope. 
					session_id_catch = session_id;
					/* Check if user has a pending withdrawl request. */
					if(withdrawMap.containsKey(session_id)) {
						response.status(401);
						return "Can't gamble while withdraw request is processing";
					}
					/* Check if user has a gamble attempt already in progress. */
					if(gamble_map.containsKey(session_id)) {
						response.status(401);	
						return "roll already in progress";
					}	
					else {
						long epoch_current = Instant.now().getEpochSecond();
						gamble_map.put(session_id, epoch_current);
					}
					//Attempt to get user object. 
					List<UserObject> user_list = PojoBuilder.getUserObject(session_id);

					//Get double value of user's bet amount.
					double double_bet_amount = Double.valueOf(bet_amount);
					//Get the user's winning roll choice. 
					int int_chosen_number = Integer.parseInt(chosen_number);
					//Get the integer value of users dice roll choice.
					int int_chosen_odds = Integer.parseInt(chosen_odds); 
					
					/*check if user input is valid. */	
					if(checkValidBet(params, user_list) == true) {
						user = user_list.get(0);
						String user_id = user.getId();
						gamble_object = getUserGambleObject(user);
						double profit = Tools.getProfit(gamble_object, params);

						/* Update gamble and user's balance record */
						if(PojoBuilder.updateGambleRecord(gamble_object, params, profit) == true) {
							PojoBuilder.updateDbBalance(user, profit);
						}
						else {
							gamble_map.remove(session_id);
							response.status(500);
							return "gamble record update failed"; 
						}	
					}
					else {

						gamble_map.remove(session_id);
						response.status(400);
						return "invalid bet";
					}
				} 
				catch(IndexOutOfBoundsException e) {
					gamble_map.remove(session_id_catch);
					String method_name = e.getStackTrace()[0].getMethodName();
					System.out.println(method_name);
					String errorMsg = "IndexOutOfBounds Could not find an unused gamble object";
					return halt(500, errorMsg);
				}
				catch(NullPointerException e) {
					gamble_map.remove(session_id_catch);
					String method_name = e.getStackTrace()[0].getMethodName();
					System.out.println(method_name);
					e.printStackTrace();
					String errorMsg = " NullPointer Could not find an unused gamble object";
					return halt(500, errorMsg);
				}
				catch(Exception e) {
					gamble_map.remove(session_id_catch);
					e.printStackTrace();
					String method_name = e.getStackTrace()[0].getMethodName();
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
				//Update and return gamble object
				List<GambleObject> gamble_list = PojoBuilder.getGambleObjectByHashNoJsonResult(gamble_object.getResultHash());
				gamble_object = gamble_list.get(0);
                HashMap<String, String> hmap = new HashMap<String, String>();

				//assign new gamble record for user
				List<GambleObject> gamble_object_list = PojoBuilder.getEmptyGambleObject();
				GambleObject gamble_object_assign = gamble_object_list.get(0);
				PojoBuilder.updateGambleUserObjectId(gamble_object_assign, user);

				//hmap to json
				gamble_map.remove(session_id_catch);
				return gamble_object;
						
			}, json());
			/**
			 * Post api for gamble request and balance update.
			 */
			post("/api/gamble", (request, response) -> {
				response.type("application/json");
				String session_id = "";
                List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
                Map<String, String> params = toMap(pairs);                                

				//Initiate variables and objects before try-catch block for scope.
				String profit_str;
				GambleObject gamble_object;
				UserObject user;

				try {
					//get query paramaters
				 	String bet_amount = params.get("bet_amount");
                    session_id = params.get("session_id");
					String chosen_odds = params.get("chosen_odds");
					String chosen_number = params.get("chosen_number");
					session_id_catch = session_id;

					if(withdrawMap.containsKey(session_id)) {
						response.status(401);
						return "Can't gamble while withdraw request is processing";
					}
					if(gamble_map.containsKey(session_id)) {
						response.status(401);	
						return "roll already in progress";
					}	
					else {
						long epoch_current = Instant.now().getEpochSecond();
						gamble_map.put(session_id, epoch_current);
					}

					//Store user in list. List will be empty if user does not exist.
					List<UserObject> user_list = PojoBuilder.getUserObject(session_id);

					//convert a few of the query params to new type
					double double_bet_amount = Double.valueOf(bet_amount);
					int int_chosen_number = Integer.parseInt(chosen_number);
					int int_chosen_odds = Integer.parseInt(chosen_odds); 
					
					//check if bet is valid	
					if(checkValidBet(params, user_list) == true) {
						user = user_list.get(0);
						String user_id = user.getId();
					
						gamble_object = getUserGambleObject(user);

						//System.out.println(gamble_object);
						//System.out.println(gamble_object);

						double profit = Tools.getProfit(gamble_object, params);

						//Update user and gamble record.
						if(PojoBuilder.updateGambleRecord(gamble_object, params, profit) == true) {
							PojoBuilder.updateDbBalance(user, profit);
						}
						else {
							gamble_map.remove(session_id);
							response.status(500);
							return "gamble record update failed"; 
						}	
					}
					else {

						gamble_map.remove(session_id);
						response.status(400);
						return "invalid bet";
					}
				} 
				catch(IndexOutOfBoundsException e) {
					gamble_map.remove(session_id_catch);
					String method_name = e.getStackTrace()[0].getMethodName();
					System.out.println(method_name);
					String errorMsg = "IndexOutOfBounds Could not find an unused gamble object";
					return halt(500, errorMsg);
				}
				catch(NullPointerException e) {
					gamble_map.remove(session_id_catch);
					String method_name = e.getStackTrace()[0].getMethodName();
					System.out.println(method_name);
					e.printStackTrace();
					String errorMsg = " NullPointer Could not find an unused gamble object";
					return halt(500, errorMsg);
				}
				catch(Exception e) {
					gamble_map.remove(session_id_catch);
					e.printStackTrace();
					String method_name = e.getStackTrace()[0].getMethodName();
					String errorMsg = "Error: " + e.getMessage();;
					return halt(500, errorMsg);
				}
				//Update and return gamble object
				List<GambleObject> gamble_list = PojoBuilder.getGambleObjectByHashNoJsonResult(gamble_object.getResultHash());
				gamble_object = gamble_list.get(0);
      				HashMap<String, String> hmap = new HashMap<String, String>();

				//assign new gamble record for user
				List<GambleObject> gamble_object_list = PojoBuilder.getEmptyGambleObject();
				GambleObject gamble_object_assign = gamble_object_list.get(0);
				PojoBuilder.updateGambleUserObjectId(gamble_object_assign, user);


				//hmap to json
				gamble_map.remove(session_id_catch);
				return gamble_object;
						
			}, json());
			/**
			 * Post api for lottery entry.
			 */
			post("/api/entry", (request, response) -> {
				response.type("application/json");
                List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());
                Map<String, String> params = toMap(pairs);                                

				/*Initiate variables and objects before try-catch block for scope. */
				String session_id = "";
				String profit_str;
				GambleObject gamble_object;
				UserObject user;
				/*end*/

				try {
					//store user's login id.
                    session_id = params.get("session_id");
					//store user's chosen entry numbers.
					String chosen_number = params.get("chosen_numbers");
					//Another session id object for catch scope. 
					session_id_catch = session_id;
					/*Check if the user has a pending withdraw request */
					if(withdrawMap.containsKey(session_id)) {
						response.status(401);
						return "Can't enter while withdraw request is processing";
					}
					/*Check if the user has a entry or dice roll request that is already processing.*/
					if(gamble_map.containsKey(session_id)) {
						response.status(401);	
						return "lottery entry or dice roll already in progress";
					}	
					else {
						long epoch_current = Instant.now().getEpochSecond();
						gamble_map.put(session_id, epoch_current);
					}

					//Get UserObject by id.
					List<UserObject> user_list = PojoBuilder.getUserObject(session_id);
					//error found here
					user = user_list.get(0);

					AdminObject jackpotObject = null;	
					/* check if entry is valid, update user balance and add an entry record if true */
					if(checkValidEntry(params, user) == true) {
						//update user balance.
						double entryCost = -0.07;
						PojoBuilder.updateDbBalance(user, entryCost);
						PojoBuilder.insertEntry(user, params);
						//get current lottery jackpot
						List<AdminObject> admin_list = PojoBuilder.getLotteryJackpot();
						jackpotObject = admin_list.get(0);
						
						//double jackpot = jackpotObject.getBalance();	
						//double new_jackpot = jackpot + 0.125;
						//PojoBuilder.updateLotteryJackpot(new_jackpot);

						gamble_map.remove(session_id);
						return true;
					}
					else {
						gamble_map.remove(session_id);
						response.status(400);
						return "invalid entry";
					}
				} 
				catch(IndexOutOfBoundsException e) {
					gamble_map.remove(session_id_catch);
					String method_name = e.getStackTrace()[0].getMethodName();
					System.out.println(method_name); 
					String errorMsg = "Error: " + e.getMessage();
					return halt(500, errorMsg);
				}
				catch(NullPointerException e) {
					gamble_map.remove(session_id_catch);
					String method_name = e.getStackTrace()[0].getMethodName();
					System.out.println(method_name);
					e.printStackTrace();
					String errorMsg = " NullPointer Could not find an unused gamble object";
					return halt(500, errorMsg);
				}
				catch(RuntimeException e) {
					gamble_map.remove(session_id_catch);
					e.printStackTrace();
					String method_name = e.getStackTrace()[0].getMethodName();
					String errorMsg = "Error: " + e.getMessage();
					//return halt(400, errorMsg);
					response.status(420);
					return errorMsg;
				}
				catch(Exception e) {
					gamble_map.remove(session_id_catch);
					e.printStackTrace();
					String method_name = e.getStackTrace()[0].getMethodName();
					String errorMsg = "Error: " + e.getMessage();
					return halt(500, errorMsg);
				}
				
						
			}, json());

		}
}