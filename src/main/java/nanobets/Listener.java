package nanobets;

//jnano
import uk.oczadly.karl.jnano.callback.BlockCallbackListener;
import uk.oczadly.karl.jnano.callback.BlockData; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance; 
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance; 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend; 
import uk.oczadly.karl.jnano.rpc.exception.RpcException; 
import uk.oczadly.karl.jnano.callback.BlockData; 

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


//sql2o
import org.sql2o.Sql2o;
import org.sql2o.*;

//native
import static nanobets.Main.*;
import static nanobets.BlockTools.getPocketedBalance;
import static nanobets.BlockTools.checkMatch;
import static nanobets.BlockTools.randomStringGenerator;
import static nanobets.BlockTools.update;

public class Listener implements BlockCallbackListener {

	public static String block_hash;
	private int users_size;				
	private RequestAccountBalance request_balance;
	private ResponseBalance response_balance;
	private BigDecimal bigDec_nano_account_balance;
	private BigInteger raw_pocketed;

	/**
	 * Run new thread on every new block. 
	 */ 
	@Override
	public void onNewBlock(BlockData block, String target, InetAddress inet_node) {

			stack.push(block);
			//t.run(); 
	}
}
