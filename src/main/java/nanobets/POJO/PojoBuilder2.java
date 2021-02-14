package nanobets;

//native 
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.sql.DriverManager;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter; 
import java.time.ZoneOffset; 
import java.time.Instant;

//sql2o 
import org.sql2o.*;

//jnano 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;

//local
import static nanobets.Main.*;

public class PojoBuilder2 {

        public static List<UserObject> emptyList = new ArrayList<>(); 
	
	/**
	 * Returns all user data as user objects inside a list.
	 */
	public static List<UserObject> getAllUserObjects(){
		String sql =
		"SELECT id, nano_address, balance, decimal_balance " +
		"FROM users";

	    	try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(UserObject.class);
	    	}
	}
	/**
	 * Returns user object of given id.
	 */
	public static List<UserObject> getUserObject(String id) {
		try(Connection con = sql2o.open()) {
			final String query =
			"select id, ip, fingerprint, walletid, nano_address, balance, decimal_balance " +
			"from users where id = :id";

			return con.createQuery(query)
				.addParameter("id", id)
				.executeAndFetch(UserObject.class);
		}
		catch (Exception e) {
                        e.printStackTrace();
                        return emptyList;
                }
	}

	
	/**
	 * Updates raw and decimal value of user balance.
	 */
	public synchronized static void updateDbBalance(UserObject user, BigInteger raw_amount) {
		//big int to big dec	
		BigDecimal big_dec = new BigDecimal(raw_amount);
		big_dec = big_dec.movePointLeft(30);
		big_dec = big_dec.stripTrailingZeros();

		//add them
		big_dec = big_dec.add(user.getDecimalBalance());
		raw_amount = raw_amount.add(user.getBalance()); 


		//convert bigint and bigdec to string.
		String dec_str = big_dec.toPlainString();
		String raw_str = raw_amount.toString();

		String id = user.getId();
		String updateSql = "update users set balance = :raw_str, decimal_balance = :dec_str where id = :id";
	
		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql) 
				.addParameter("raw_str", raw_str) 
				.addParameter("dec_str", dec_str) 
				.addParameter("id", id) 
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates balance of admin record in admin table.
	 */
	public static void updateAdminBalance(BigInteger raw_balance, BigDecimal dec_balance) {
		String raw_balance_str = raw_balance.toString();

		dec_balance = dec_balance.movePointLeft(30);
		dec_balance = dec_balance.stripTrailingZeros();
		String dec_balance_str = dec_balance.toPlainString();

		String updateSql = "update admin set balance = :raw_balance, decimal_balance = :dec_balance where id = 1";
	
		try (Connection con = sql2o.open()) {
			con.createQuery(updateSql)
				.addParameter("raw_balance", raw_balance_str)
				.addParameter("dec_balance", dec_balance_str)
				.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Inserts new deposit record. 
	 */
	public static void insertDepositRecord(String destination, String user_id, BigInteger big_amount) {
                                
				long epoch_time = System.currentTimeMillis();
				ZonedDateTime utc = Instant.ofEpochMilli(epoch_time).atZone(ZoneOffset.UTC);
				String time = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(utc);
				

				//convert amount to decimal string
				BigDecimal dec_amount = new BigDecimal(big_amount);	
				dec_amount = dec_amount.movePointLeft(30);
				dec_amount = dec_amount.stripTrailingZeros();
				String amount = dec_amount.toPlainString(); 
                                
				System.out.println(big_amount);

                        try {                        
				String insertQuery =
					"INSERT INTO deposit " + 
					"(destination, user_id, amount, time, epoch_time_used) " +
					"VALUES (:destination, :user_id, :amount, :time, :epoch_time_used)";

					Connection con = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE);  
						con.createQuery(insertQuery)
							.addParameter("destination", destination)
							.addParameter("user_id", user_id)
							.addParameter("amount", amount)
							.addParameter("time", time)
							.addParameter("epoch_time_used", epoch_time)
							.executeUpdate();
						con.commit();
			}
			catch(Exception e) {
                                e.printStackTrace();
                        }        
        }

	/**
	 * Retrieves admin object
	 */
	public static List<AdminObject> getAdminBalance() {

		String sql =
			"SELECT decimal_balance " +
			"FROM admin";

	    	try(Connection con = sql2o.open()) {
			return con.createQuery(sql).executeAndFetch(AdminObject.class);
	    	}
	}


	
}
