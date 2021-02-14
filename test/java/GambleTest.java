package nanobets;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.sql2o.*;
import java.util.List;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import spark.staticfiles.*;
import static spark.Spark.post;
import org.apache.http.client.utils.URLEncodedUtils;
import java.nio.charset.Charset;
import org.apache.http.NameValuePair;
import java.util.stream.IntStream;
import java.util.function.Predicate;


import uk.oczadly.karl.jnano.util.WalletUtil;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.request.node.RequestVersion;
import uk.oczadly.karl.jnano.rpc.QueryCallback;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import java.net.MalformedURLException; 
import uk.oczadly.karl.jnano.rpc.request.wallet.*; 
import java.io.IOException;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount; 
import uk.oczadly.karl.jnano.rpc.response.RpcResponse; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestAccountCreate; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend; 
import java.math.BigInteger;
import java.util.HashMap;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Random;

import uk.oczadly.karl.jnano.callback.BlockCallbackListener;
import uk.oczadly.karl.jnano.callback.BlockData; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance; 
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletInfo; 
import uk.oczadly.karl.jnano.rpc.response.ResponseLedger.AccountInfo; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance; 
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance; 
import java.net.MalformedURLException; 


public class GambleTest {

	@Test
	public void getObjectTest() {

		List<UserObject> object = getUserObject("4325c92c4e114c9dffb9b864723b097eedb4eece116c252b5d7763510447916c");	

		System.out.println(object.size());
	}

/*
	public static List<GambleObject> getEmptyGambleObject() {

                Sql2o sql2o = new Sql2o("jdbc:sqlite:nanodb.db", null, null);
		String used = "0";
		try(Connection con = sql2o.open()) {
			final String query =
			"SELECT result_hash, json_result " +
			"FROM gamble WHERE used = :used limit 1";

			return con.createQuery(query)
				.addParameter("used", used)
				.executeAndFetch(GambleObject.class);
		}	
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}        
	}
*/


	/**
	 * Returns UserObject object with specified username
	 */
	@Test
	public static List<UserObject> getUserObject(String id) {

		List<UserObject> list = null;

                Sql2o sql2o = new Sql2o("jdbc:sqlite:nanodb.db", null, null);
		try(Connection con = sql2o.open()) {
				final String query =
				"SELECT id " +
				"FROM users WHERE id = :id";

				list = con.createQuery(query)
					.addParameter("id", id)
					.executeAndFetch(UserObject.class);
			}	
			catch (Exception e) {
				e.printStackTrace();
			}        
			finally {
				return list;
			}

			
	}
	@Test
	public void streamTest() {
	
		List<Integer> int_list = new ArrayList<Integer>();
		int_list.add(1);
		IntStream stream = int_list.stream().mapToInt(Integer::intValue);
		if(stream.anyMatch(n -> n == 1)) {
			System.out.println("true");
		}
		else {
			System.out.println("false");
		}

	}
}

