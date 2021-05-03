package nanobets;

//java 
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.DriverManager;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.awt.image.BufferedImage;
import java.io.FileInputStream; import java.io.File;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import javax.sql.rowset.serial.SerialBlob;
import java.util.Base64;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter; 
import java.time.ZoneOffset; 
import java.time.Instant;

//nanobets 
import static nanobets.JsonUtil.json;
import static nanobets.Tools.createNanoAccount;
import static nanobets.Tools.*;
import static nanobets.Main.*;

//sql2o 
import org.sql2o.*;

//apache 
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.codec.digest.DigestUtils;

//jnano 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;

//qrcodegen
import io.nayuki.qrcodegen.*;

//jackson
import com.fasterxml.jackson.core.JsonProcessingException; 

//PojoBuilder.java class and constructor
public class PojoBuilder {

	//some empty lists for the catch blocks, will go over this method later
        public static List<UserObject> user_emptyList = new ArrayList<>(); 
        public static List<GambleObject> gamble_emptyList = new ArrayList<>(); 
        public static List<EntryObject> entry_emptyList = new ArrayList<>(); 
        public static List<WithdrawObject> withdraw_empty_list = new ArrayList<>(); 
        public static List<DepositObject> deposit_empty_list = new ArrayList<>(); 
	
	//returns all users in a list
	public static List<UserObject> getAllUserObjects(){
		String sql =
		"SELECT id, balance, nano_address " +
		"FROM users limit 1";

	    	try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(UserObject.class);

	    }
	}

	//returns admin record in admin table as object
	public static List<AdminObject> getAdminObjectObject(){
		String sql =
		"SELECT profit " +
		"FROM admin WHERE id = 1";

	    	try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(AdminObject.class);
	    }
	}
	//returns admin record in admin table as object
	public static List<AdminObject> getAdminObject(){
		String sql =
		"SELECT balance " +
		"FROM admin WHERE id = 2";

	    	try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(AdminObject.class);
	    }
	}

	//returns admin record in admin table as object
	public static List<AdminObject> getLotteryJackpot(){
		String sql =
		"SELECT balance " +
		"FROM admin WHERE id = 2";

	    	try(Connection con = sql2o2.open()) {
			return con.createQuery(sql).executeAndFetch(AdminObject.class);
	    }
	}


	//Inserts new user into db
	public static boolean insertUserObject(String ip, RpcQueryNode node, HashMap<String, String> hmap, String qr_code, String new_id, String fingerprint) {

		System.out.println("fingerprint created: " + fingerprint);
        
		try {                        
			String nano_address = hmap.get("accountAddress");;
			String walletId = hmap.get("walletId");

			if(nano_address.isEmpty() == false) {

				final String insertQuery =
				"INSERT INTO users (id, fingerprint, ip, nano_address, walletId, qr_code) " +
				"VALUES (:id, :fingerprint, :ip, :nano_address, :walletId, :qr_code)";


				Connection con = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE);  
					con.createQuery(insertQuery)
						.addParameter("id", new_id)
						.addParameter("fingerprint", fingerprint)
						.addParameter("ip", ip)
						.addParameter("nano_address", nano_address)
						.addParameter("walletId", walletId)
						.addParameter("qr_code", qr_code)
						.executeUpdate();
					con.commit();

			}
			else {
				return false;		
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}        

		return true;
	}	

	/**
	 * Returns UserObject object with specified username
	 */
	public static List<UserObject> getUserObject(String id) {

		try(Connection con = sql2o.open()) {
				final String query =
				"SELECT id, ip, qr_code, decimal_balance, walletId, fingerprint, balance " +
				"FROM users WHERE id = :id OR login_id = :login_id";

				return con.createQuery(query)
					.addParameter("id", id)
					.addParameter("login_id", id)
					.executeAndFetch(UserObject.class);
			}	
			catch (Exception e) {
				e.printStackTrace();
				return user_emptyList;
			}        
	} 
    /**
	 * Returns UserObject object by login id
	 */
	public static List<UserObject> getUserObjectByLoginId(String id) {

		try(Connection con = sql2o.open()) {
				final String query =
				"SELECT id, ip, qr_code, decimal_balance, walletId, fingerprint, balance " +
				"FROM users WHERE login_id = :id";

				return con.createQuery(query)
					.addParameter("id", id)
					.executeAndFetch(UserObject.class);
			}	
			catch (Exception e) {
				e.printStackTrace();
				return user_emptyList;
			}        
	}

	/**
	 * Returns UserObject object with specified username
	 */
	public static List<UserObject> getBalanceObject(String id) {

		try(Connection con = sql2o.open()) {
				final String query =
				"SELECT balance, decimal_balance " +
				"FROM users WHERE id = :id";

				return con.createQuery(query)
					.addParameter("id", id)
					.executeAndFetch(UserObject.class);
			}	
			catch (Exception e) {
				e.printStackTrace();
				return user_emptyList;
			}        
	}
    public static boolean changeLoginId(String user_id, String new_id) {

		String updateSql = "update users set login_id = :login_id where id = :user_id";
		
		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql)
				.addParameter("login_id", new_id)
				.addParameter("user_id", user_id)
				.executeUpdate();
			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}        
	}
	/**
	 * Returns deposit object with specified username.
	 */
	public static List<UserObject> getDepositObject(String id) {

		try(Connection con = sql2o.open()) {
				final String query =
				"SELECT qr_code, nano_address " +
				"FROM users WHERE id = :id";

				return con.createQuery(query)
					.addParameter("id", id)
					.executeAndFetch(UserObject.class);
			}	
			catch (Exception e) {
				e.printStackTrace();
				return user_emptyList;
			}        
	}
	
	/*
	 * checks if user id exists.
	 */	
        public static List<UserObject> checkExists(String id) {
		try(Connection con = sql2o.open()) {
			final String query =
			"SELECT id " +
			"FROM users WHERE id = :id";

			return con.createQuery(query)
				.addParameter("id", id)
				.executeAndFetch(UserObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
                        return user_emptyList;
                }        
        	
        }
	/*
	 * Updates user balance.
	 */
	public synchronized static boolean updateDbBalance(UserObject user, double profit) { 
		String id = "";
		String decimal_balance_str = "";
		String balance_str = "";
		String updateSql = "";
	try {
		System.out.println("inside update db balance");

		id = user.getId();
		// userObject param can be removed in future
		List<UserObject> user_list  = PojoBuilder.getUserObject(id);
		user = user_list.get(0);


		//double profit to bigDec and bigInt
		BigDecimal dec_profit = BigDecimal.valueOf(profit);
		BigDecimal to_big = dec_profit.movePointRight(30);
		BigInteger big_profit = to_big.toBigInteger(); 

		//calculate new values
		BigInteger balance = user.getBalance();
		balance = balance.add(big_profit);
		BigDecimal decimal_balance = user.getDecimalBalance();
		decimal_balance = decimal_balance.add(dec_profit);
		decimal_balance = decimal_balance.stripTrailingZeros();

		//convert to string
		balance_str = balance.toString();
		decimal_balance_str = decimal_balance.toPlainString();

		System.out.println("User balance will be updated to " + decimal_balance_str);
		updateSql = "update users set balance = :balance, decimal_balance = :decimal_balance where id = :id";

	}
	catch(Exception e) {
		e.printStackTrace();
		return false;
	}


		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql)
				.addParameter("balance", balance_str)
				.addParameter("decimal_balance", decimal_balance_str)
				.addParameter("id", id)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	/*
	 * Temp fix for updateDb nullpointer. In future I will switch userobject param to string user_id 
	 */
	public synchronized static boolean updateDbBalance(String id, double profit) { 

		System.out.println("inside update db balance with id");
		System.out.println("inside update db balance with id");
		System.out.println("inside update db balance with id");
		System.out.println("inside update db balance with id");

		String decimal_balance_str = "";
		String balance_str = "";
		String updateSql = "";
		UserObject user;
	try {
		System.out.println("inside update db balance");
		System.out.println("id" + id);

		// userObject param can be removed in future
		List<UserObject> user_list  = PojoBuilder.getUserObject(id);
		user = user_list.get(0);


		//double profit to bigDec and bigInt
		BigDecimal dec_profit = BigDecimal.valueOf(profit);
		BigDecimal to_big = dec_profit.movePointRight(30);
		BigInteger big_profit = to_big.toBigInteger(); 

		//calculate new values
		BigInteger balance = user.getBalance();
		balance = balance.add(big_profit);
		BigDecimal decimal_balance = user.getDecimalBalance();
		decimal_balance = decimal_balance.add(dec_profit);
		decimal_balance = decimal_balance.stripTrailingZeros();

		//convert to string
		balance_str = balance.toString();
		decimal_balance_str = decimal_balance.toPlainString();

		System.out.println("User balance will be updated to: " + decimal_balance_str);
		updateSql = "update users set balance = :balance, decimal_balance = :decimal_balance where id = :id";

	}
	catch(Exception e) {
		e.printStackTrace();
		return false;
	}


		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql)
				.addParameter("balance", balance_str)
				.addParameter("decimal_balance", decimal_balance_str)
				.addParameter("id", id)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	/*
	 * returns an unused gamble object (for lottery) with specified result_hash. 
	 */
	public static List<GambleObject> getLotteryObject(String user_id) {
		String used = "0";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT * " +
			"FROM gamble WHERE used = 0 AND user_id = :user_id ORDER BY lottery_draw_time_epoch LIMIT 1";

			return con.createQuery(query)
				.addParameter("user_id", user_id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	/*
	 * returns an unused gamble object with specified user_id (Should be the lottery_id). 
	 */
	public static List<GambleObject> getPendingLotteryObject(String user_id) {
		String used = "0";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, lottery_draw_time_utc, lottery_draw_time_epoch " +
			"FROM gamble WHERE used = :used AND user_id = :user_id LIMIT 20";

			return con.createQuery(query)
				.addParameter("used", used)
				.addParameter("user_id", user_id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }


	/*
	 * returns an unused gamble object with specified user_id. 
	 */
	public static List<GambleObject> getGambleObject (String user_id) {
		String used = "0";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, result_string, json_result, used, profit, chosen_odds " +
			"FROM gamble WHERE used = :used AND user_id = :user_id LIMIT 20";

			return con.createQuery(query)
				.addParameter("used", used)
				.addParameter("user_id", user_id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }


	/*
	 * Returns user gamble history.
	 */
	public static List<GambleObject> getPrivateGambleHistory(String user_id) {
		String used = "1";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, result_string used, profit, chosen_odds, time_used, chosen_number " +
			"FROM gamble WHERE used = :used AND user_id = :user_id ORDER BY epoch_time_used DESC";

			return con.createQuery(query)
				.addParameter("used", used)
				.addParameter("user_id", user_id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	/*
	 * returns user withdraw history.
	 */
	public static List<WithdrawObject> getWithdrawHistory(String user_id) {
		try(Connection con = sql2o.open()) {
			final String query =
			"select withdraw_id, destination, user_id, amount, dec_amount, time, block_hash " +
			"from withdraw where user_id = :user_id order by epoch_time desc";

			return con.createQuery(query)
				.addParameter("user_id", user_id)
				.executeAndFetch(WithdrawObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return withdraw_empty_list;
                }        
        }
	/*
	 * returns user withdraw history.
	 */
	public static List<WithdrawObject> getWithdrawObject(String block_hash) {
		try(Connection con = sql2o.open()) {
			final String query =
			"select withdraw_id, destination, amount, time, block_hash " +
			"from withdraw where block_hash = :block_hash";

			return con.createQuery(query)
				.addParameter("block_hash", block_hash)
				.executeAndFetch(WithdrawObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return withdraw_empty_list;
                }        
        }

	/*
	 * returns withdraw history.
	 */
	public static List<DepositObject> getDepositHistory(String user_id) {
		try(Connection con = sql2o.open()) {
			final String query =
			"select destination, amount, time, block_hash, user_id, epoch_time_used " +
			"from deposit where user_id = :user_id order by epoch_time_used desc";

			return con.createQuery(query)
				.addParameter("user_id", user_id)
				.executeAndFetch(DepositObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return deposit_empty_list;
                }        
        }
        public static List<DepositObject> getDepositRecord(long block_count) {
                String block_height = String.valueOf(block_count);
		try(Connection con = sql2o.open()) {
			final String query =
			"select destination, amount, time, block_hash, user_id, epoch_time_used, block_height " +
			"from deposit where block_height = :block_height";

			return con.createQuery(query)
				.addParameter("block_height", block_height)
				.executeAndFetch(DepositObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return deposit_empty_list;
                }        
        }


	public static List<GambleObject> getPublicGambleHistory() {
		String used = "1";
		String id = "5dbe850c6211b2bd4d211c3990588683cf0ac0355561ea1e93a130521e05449b";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, result_string, used, profit, chosen_odds, time_used, chosen_number " +
			"FROM gamble WHERE used = :used AND user_id != :lottery_id ORDER BY epoch_time_used DESC LIMIT 100";

			return con.createQuery(query)
				.addParameter("used", used)
				.addParameter("lottery_id", id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	public static List<GambleObject> getPublicLotteryHistory() {
		String used = "1";
		String user_id = "5dbe850c6211b2bd4d211c3990588683cf0ac0355561ea1e93a130521e05449b";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, result_string, result_number, lottery_draw_time_utc, lottery_winners " +
			"FROM gamble WHERE used = :used AND user_id = :user_id ORDER BY lottery_draw_time_epoch DESC";

			return con.createQuery(query)
				.addParameter("used", used)
				.addParameter("user_id", user_id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	public static List<GambleObject> getPublicLotteryHistoryLimit() {
		String used = "1";
		String user_id = "5dbe850c6211b2bd4d211c3990588683cf0ac0355561ea1e93a130521e05449b";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, result_string, result_number, lottery_draw_time_utc, lottery_winners " +
			"FROM gamble WHERE used = :used AND user_id = :user_id ORDER BY lottery_draw_time_epoch DESC LIMIT 5";

			return con.createQuery(query)
				.addParameter("used", used)
				.addParameter("user_id", user_id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }




	public static List<EntryObject> getEntryHistory(String user_id) {
		System.out.println("getting entry history for: " + user_id);
		try(Connection con = sql2o.open()) {
			final String query =
			"SELECT * " +
			"FROM entries WHERE user_id = :user_id ORDER BY entry_time_epoch DESC";

			return con.createQuery(query)
				.addParameter("user_id", user_id)
				.executeAndFetch(EntryObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return entry_emptyList;
                }        
        }
	public static List<GambleObject> getPublicLotteryFutures() {
		String used = "0";
		String user_id = "5dbe850c6211b2bd4d211c3990588683cf0ac0355561ea1e93a130521e05449b";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, lottery_draw_time_utc " +
			"FROM gamble WHERE used = :used AND user_id = :user_id ORDER BY lottery_draw_time_epoch ASC";

			return con.createQuery(query)
				.addParameter("used", used)
				.addParameter("user_id", user_id)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }

	/*
	 * Updates used gamble record.  
	 */
	public static boolean updateGambleRecord(GambleObject gamble_object, Map<String, String> params, double profit) throws JsonProcessingException {
		System.out.println("GAMBLEUPDATE");

		String bet_amount = params.get("bet_amount");
		String chosen_odds = params.get("chosen_odds");
		String chosen_number = params.get("chosen_number");

		String result_hash = gamble_object.getResultHash();
		Map<String, List<String>> result_map = gamble_object.getHashMapResult();
		List<String> result_numbers_list = result_map.get(chosen_odds);
		String result_numbers = result_numbers_list.toString();

			
		String used = "1";
		long used_time = System.currentTimeMillis();
		String used_time_str = String.valueOf(used_time);
		ZonedDateTime used_utc = Instant.ofEpochMilli(used_time).atZone(ZoneOffset.UTC);
		String string_used_utc = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(used_utc);

		String updateSql = 	"update gamble " + 
					"set profit = :profit, used = :used, chosen_number = :chosen_number, " + 
					"chosen_odds = :chosen_odds, result_number = :result_numbers, time_used = :time_used, epoch_time_used = :used_time " + 
					"where result_hash = :result_hash";

		try (Connection con = sql2o2.open()) {
			con.createQuery(updateSql)
				.addParameter("profit", profit)
				.addParameter("used", used)
				.addParameter("chosen_number", chosen_number)
				.addParameter("chosen_odds", chosen_odds)
				.addParameter("result_numbers", result_numbers)
				.addParameter("time_used", string_used_utc)
				.addParameter("result_hash", result_hash)
				.addParameter("used_time", used_time_str)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("DB Update failed");
			return false;
		}

		//update total profit in admin table	
		List<AdminObject> admin_profit_list = getAdminObjectObject();
		AdminObject admin = admin_profit_list.get(0); 
		double admin_profit = Double.valueOf(admin.getProfit());
		admin_profit = (admin_profit + (Double.valueOf(profit) *-1));
		String admin_profit_str = Double.toString(admin_profit); 

		updateAdminObjectTable(admin_profit_str);

		return true;
	}
	/*
	 * Updates used gamble record.   
	 */
	public static boolean updateLotteryRecord(GambleObject lottery_object, String lottery_winners) throws JsonProcessingException {
		System.out.println("LOTTERYUPDATE");
		//result hash object.
		String result_hash = lottery_object.getResultHash();
		
		/*reveal numbers  */
		Map<String, List<String>> result_map = lottery_object.getHashMapResult();
		List<String> result_numbers_list = result_map.get("5");
		//lottery result number
		String result_numbers = result_numbers_list.toString();


		//set used to 1(yes)	
		String used = "1";
		//Leave it to millis for now, in future change to seconds to keep things organized.		
		long used_time = System.currentTimeMillis();
		String used_time_str = String.valueOf(used_time);
		ZonedDateTime used_utc = Instant.ofEpochMilli(used_time).atZone(ZoneOffset.UTC);
		//utc time of used record.
		String string_used_utc = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(used_utc);
		//lottery draw odds is fixed to 5
		final int chosen_odds = 5; 

		String updateSql = 	"update gamble " + 
					"set used = :used, " + 
					"chosen_odds = :chosen_odds, " + 
					"result_number = :result_numbers, " + 
					"time_used = :time_used, " + 
					"lottery_winners = :lottery_winners, " + 
					"epoch_time_used = :used_time "  + 
					"where result_hash = :result_hash";

		try (Connection con = sql2o2.open()) {
			con.createQuery(updateSql)
				.addParameter("used", used)
				.addParameter("chosen_odds", chosen_odds)
				.addParameter("result_numbers", result_numbers)
				.addParameter("time_used", string_used_utc)
				.addParameter("result_hash", result_hash)
				.addParameter("lottery_winners", lottery_winners)
				.addParameter("used_time", used_time_str)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("DB Update failed");
			return false;
		}

		return true;
	}
	/*
	 * Updates empty gamble record with user's id. 
	 */
	public static void updateGambleUserObjectId(GambleObject unassigned_gamble_object, UserObject user) {
		//sql parameters	
		String result_hash = unassigned_gamble_object.getResultHash();
		String user_id = user.getId();
		String used = "0";

		String updateSql = 	"update gamble " +
					"set user_id = :user_id " + 
					"where result_hash = :result_hash " + 
					"AND used = :used";

		try (Connection con = sql2o2.open()) {
			con.createQuery(updateSql)
				.addParameter("user_id", user_id)
				.addParameter("result_hash", result_hash)
				.addParameter("used", used)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}



	/*
	 * returns a single unused gamble object with no user_id set. 
	 */
	public static List<GambleObject> getEmptyGambleObject() {
		String used = "0";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, json_result " +
			"FROM gamble WHERE used = :used AND user_id is NULL ORDER BY RANDOM() LIMIT 20";

			return con.createQuery(query)
				.addParameter("used", used)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }

	/*
	* returns a single gamble object by result_hash 
	*/
	public static List<GambleObject> getGambleObjectByHash(String result_hash) {

		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, json_result, used, profit, chosen_odds, chosen_number, result_string, result_number, time_used " +
			"FROM gamble WHERE result_hash = :result_hash";

			return con.createQuery(query)
				.addParameter("result_hash", result_hash)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	/*
	* returns a single gamble object by result_hash 
	*/
	public static List<GambleObject> getLotteryObjectByHash(String result_hash) {
		String used = "1";

		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, result_string, result_number, lottery_draw_time_utc, lottery_winners " +
			"FROM gamble WHERE used = :used AND result_hash = :result_hash";

			return con.createQuery(query)
				.addParameter("result_hash", result_hash)
				.addParameter("used", used)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	/*
	* returns a single gamble object by result_hash 
	*/
	public static List<GambleObject> getUnusedLotteryObjectByHash(String result_hash) {

		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, lottery_draw_time_utc " +
			"FROM gamble WHERE result_hash = :result_hash";

			return con.createQuery(query)
				.addParameter("result_hash", result_hash)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }

	/*
	* returns a single gamble object by result_hash. No Json_result string.  
	*/
	public static List<GambleObject> getGambleObjectByHashNoJsonResult(String result_hash) {
		String used = "1";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, used, profit, chosen_odds, chosen_number, result_string, result_number, time_used " +
			"FROM gamble WHERE result_hash = :result_hash AND used = 1";

			return con.createQuery(query)
				.addParameter("result_hash", result_hash)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	/*
	* returns a single gamble object by result_hash. No Json_result string.  
	*/
	public static List<GambleObject> getGambleObjectByHashNoJsonResult2(String result_hash) {
		String used = "0";
		try(Connection con = sql2o2.open()) {
			final String query =
			"SELECT result_hash, used, profit, chosen_odds, chosen_number, result_number, time_used " +
			"FROM gamble WHERE result_hash = :result_hash AND used = 0";

			return con.createQuery(query)
				.addParameter("result_hash", result_hash)
				.executeAndFetch(GambleObject.class);
	        }	
                catch (Exception e) {
                        e.printStackTrace();
			return gamble_emptyList;
                }        
        }
	/*
	 * Updates entry outcome. 
	 */
	public static boolean updateEntryOutcome(String outcome, long entry_time_epoch, String user_id) {
		String updateSql = 	"update entries " + 
					"set outcome = :outcome where user_id = :user_id AND entry_time_epoch = :entry_time_epoch";

		try (Connection con = sql2o.open()) {
            		con.getJdbcConnection().setAutoCommit(false);
			con.createQuery(updateSql)
				.addParameter("entry_time_epoch", entry_time_epoch)
				.addParameter("outcome", outcome)
				.addParameter("user_id", user_id)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/*
	 * Updates admin profits. 
	 */
	public static boolean updateAdminObjectTable(String profit) {

		String updateSql = 	"update admin " + 
					"set profit = :profit where id = 1";

		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql)
				.addParameter("profit", profit)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/*
	 * Updates admin profits. 
	 */
	public static boolean updateLotteryJackpot(double new_balance) {

		String updateSql = 	"update admin " + 
					"set balance = :balance where id = 2";

		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql)
				.addParameter("balance", new_balance)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/* Entry insert method. */
	public static boolean insertEntry(UserObject user, Map<String, String> params) {

				//User id object for db insertion
				String user_id = params.get("session_id");

				//Chosen numbers for db insertion
				String chosen_numbers = params.get("chosen_numbers");

 				//Epoch time variable for db insertion.	
				long entry_time_epoch = Instant.now().getEpochSecond();
				ZonedDateTime utc = Instant.ofEpochSecond(entry_time_epoch).atZone(ZoneOffset.UTC);

				//UTC time object for db insertion.
				String entry_time_utc = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(utc);

                        try {                        
				String insertQuery =
					"INSERT INTO entries " + 
					"(entry_time_epoch, entry_time_utc, user_id, chosen_numbers) " +
					"VALUES (:entry_time_epoch, :entry_time_utc, :user_id, :chosen_numbers)";

					//Connection con = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);  
					Connection con = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE);  
						con.createQuery(insertQuery)
							.addParameter("user_id", user_id)
							.addParameter("chosen_numbers", chosen_numbers)
							.addParameter("entry_time_utc", entry_time_utc)
							.addParameter("entry_time_epoch", entry_time_epoch)
							.executeUpdate();
						con.commit();

			}
			catch(Exception e) {
                                e.printStackTrace();
				return false;
                        }        
			return true;
        }

	/*
	 * Insert withdraw record
	 */
	public static boolean insertWithdrawRecord(String withdraw_id, String destination, String user_id, String amount, String block_hash) {
 				
				long epoch_time = System.currentTimeMillis();
				ZonedDateTime utc = Instant.ofEpochMilli(epoch_time).atZone(ZoneOffset.UTC);
				String time = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(utc);

                        try {                        
				String insertQuery =
					"INSERT INTO withdraw " + 
					"(withdraw_id, destination, user_id, amount, dec_amount, time, block_hash, epoch_time) " +
					"VALUES (:withdraw_id, :destination, :user_id, :amount, :dec_amount, :time, :block_hash, :epoch_time)";

					Connection con = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE);  
						con.createQuery(insertQuery)
							.addParameter("withdraw_id", withdraw_id)
							.addParameter("destination", destination)
							.addParameter("user_id", user_id)
							.addParameter("amount", amount)
							.addParameter("dec_amount", amount)
							.addParameter("time", time)
							.addParameter("block_hash", block_hash)
							.addParameter("epoch_time", epoch_time)
							.executeUpdate();
						con.commit();

			}
			catch(Exception e) {
                                e.printStackTrace();
				return false;
                        }        
			return true;
        }
	/*
	 * Insert withdraw record
	 */
	public static boolean insertWithdrawRequestRecord(String withdraw_id, String user_id, String amount, String destination) {

				System.out.println("Adding withdraw request record");
 				
				long epoch_time = System.currentTimeMillis();
				ZonedDateTime utc = Instant.ofEpochMilli(epoch_time).atZone(ZoneOffset.UTC);
				String time = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(utc);

                        try {                        
				String insertQuery =
					"INSERT INTO withdraw_requests " + 
					"(withdraw_id, user_id, amount, time_created, destination) " +
					"VALUES (:withdraw_id, :user_id, :amount, :time_created, :destination)";

					Connection con = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE);  
						con.createQuery(insertQuery)
							.addParameter("withdraw_id", withdraw_id)
							.addParameter("user_id", user_id)
							.addParameter("amount", amount)
							.addParameter("time_created", time)
							.addParameter("destination", destination)
							.executeUpdate();
						con.commit();

			}
			catch(Exception e) {
                                e.printStackTrace();
				return false;
                        }        
			return true;
        }
	public static List<EntryObject> getPendingEntryObjects() {
		String outcome = "pending";
		try(Connection con = sql2o.open()) {
			final String query =
			"SELECT * " +
			"FROM entries WHERE outcome  = :outcome";

			return con.createQuery(query)
				.addParameter("outcome", outcome)
				.executeAndFetch(EntryObject.class);
	        }	
                catch (Exception e) {
			e.printStackTrace();
			throw new java.lang.RuntimeException("Failed to get entry objects.");
                }        
        }

	public static List<UserObject> getUsedIpCount(String ip) {

		try(Connection con = sql2o.open()) {
			final String query =
			"SELECT ip " +
			"FROM users WHERE ip = :ip";

			return con.createQuery(query)
				.addParameter("ip", ip)
				.executeAndFetch(UserObject.class);
	        }	
                catch (Exception e) {
			e.printStackTrace();
			throw new java.lang.RuntimeException("IP count query failed");
                }        
        }
	/*
	 * Updates empty gamble record with user's address.
	 */
	public static void deleteUserAddress(String id) {
		String updateSql = 	"DELETE FROM users " +
					"WHERE id = :id"; 

		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql)
				.addParameter("id", id)
				.executeUpdate();
			System.out.println("deleted user account: " + id + "account was not found in wallet");
			logger.info("deleted user account: " + id + "account was not found in wallet");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}



}
