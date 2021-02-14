package nanobets; 

//jnano
import uk.oczadly.karl.jnano.callback.BlockCallbackListener;
import uk.oczadly.karl.jnano.callback.BlockData; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance; 
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance; 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend; 
import uk.oczadly.karl.jnano.rpc.exception.RpcException; 


//jnano
import uk.oczadly.karl.jnano.util.WalletUtil;
import uk.oczadly.karl.jnano.rpc.request.node.RequestVersion;
import uk.oczadly.karl.jnano.rpc.QueryCallback;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.wallet.*;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestAccountCreate;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;
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

//java
import java.net.InetAddress; 
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList; 
import java.sql.DriverManager;
import java.lang.StringBuilder; 
import java.net.MalformedURLException;
import java.io.IOException;
import java.util.Stack;
import org.apache.commons.lang3.RandomStringUtils; 
import java.util.HashMap;
import java.util.Map;


//sql2o
import org.sql2o.Sql2o;
import org.sql2o.*;

//nanobets
import static nanobets.Main.*;
import static nanobets.BlockTools.getPocketedBalance;
import static nanobets.BlockTools.checkMatch;
import static nanobets.Tools.randomStringGenerator;
import static nanobets.BlockTools.update;
import static nanobets.PojoBuilder2.*;

public class GmapThread  extends Thread {

	/**
	 * Runs gamble_map removal thread
	 */
	public void run() {
				System.out.println("Withdraw map removal thread started");
				try {
					if(gamble_map.isEmpty() == false) {
						for(Map.Entry<String, Long> entry : gamble_map.entrySet()) {
							long time = entry.getValue();	
							String id = entry.getKey();
							//check if wmap entry has been in the gmap for over 5 minutes
							long current_time = System.currentTimeMillis();
							long check_against = time + 60000;
							System.out.println("current time: " + current_time + "check_against time: " + check_against);
							if(current_time > check_against) {
								//go ahead and delete gmap entry
								gamble_map.remove(id);
							}
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
	}
}
