package nanobets; 


//java
import java.math.BigInteger;
import java.math.BigDecimal;
import java.lang.Thread; 
import java.util.List;
import java.io.IOException;
import java.util.Random;

//nanobets
import static nanobets.Main.*;
import static nanobets.BlockTools.getPocketedBalance;
import static nanobets.PojoBuilder2.*;

//jnano
import uk.oczadly.karl.jnano.callback.BlockCallbackListener;
import uk.oczadly.karl.jnano.callback.BlockData; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance; 
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance; 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend; 
import uk.oczadly.karl.jnano.rpc.exception.RpcException; 




public class AdminThread extends Thread {

	public BigInteger pending_balance;
	private UserObject user;
	private BigInteger raw_pending;
	private String pk_id;
	private BigInteger big_balance;

	/**
	 * Checks and updates admin balance record in admin table.
	 */
	public synchronized void run() {
		System.out.println("Admin thread started");

		String admin_hot_wallet = "nano_3z1z144ggdujyypjmrm9dn37a5jimur6g5poshxzz5ryr6jjf8hufjgq16yr";

		try {

			big_balance = getPocketedBalance(admin_hot_wallet);		
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		BigDecimal dec_balance = new BigDecimal(big_balance);
		PojoBuilder2.updateAdminBalance(big_balance, dec_balance);
 	}
}
