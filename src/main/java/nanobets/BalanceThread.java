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
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestWalletLocked; 
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletLocked; 
import uk.oczadly.karl.jnano.rpc.request.node.RequestPending;
import uk.oczadly.karl.jnano.rpc.response.ResponsePending; 
import uk.oczadly.karl.jnano.rpc.response.ResponsePending.PendingBlock; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestReceive; 
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash; 
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo; 

//java
import java.util.Map;
import java.net.InetAddress; 
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList; import java.sql.DriverManager;
import java.lang.StringBuilder; 
import java.net.MalformedURLException;
import java.lang.InterruptedException;
import java.io.IOException;
import java.util.Stack;
import java.util.HashMap;
import java.net.URL;
import java.util.concurrent.TimeUnit;

//sql2o
import org.sql2o.Sql2o;
import org.sql2o.*;

//nanobets
import static nanobets.Main.*;
import static nanobets.BlockTools.getPocketedBalance;
import static nanobets.BlockTools.getPendingBalance;
import static nanobets.BlockTools.getPendingBlocks;
import static nanobets.BlockTools.receiveDeposit;
import static nanobets.BlockTools.moveToMainAccount;
import static nanobets.BlockTools.checkMatch;
import static nanobets.BlockTools.randomStringGenerator;
import static nanobets.BlockTools.update;
import static nanobets.BlockTools.getAccountInfo;
import static nanobets.BlockTools.moveToMainAccount;
import static nanobets.BlockTools.getPendingMap;
import static nanobets.PojoBuilder2.*;
import static nanobets.PojoBuilder.*;

//httpcore
import uk.oczadly.karl.jnano.rpc.HttpRequestExecutor; 



public class BalanceThread extends Thread {

	private String target;
	private InetAddress node;
	private List<UserObject> all_users; 
	private UserObject user;
	private String block_account_address;
	private BigInteger raw_pocketed;
	private BigInteger raw_pending;
	private RpcQueryNode nodeu = new RpcQueryNode();
	private HashMap<String, String> pending_map  = new HashMap<String, String>();

  	public BalanceThread(){
    }
	/**
	 * Updates user balances and inserts deposit records.
	 */
	public void run()  {
		balanceUpdater();
	}
	public void balanceUpdater()  {

            pending_map = getPendingMap();
            System.out.println("Balance thread started");
            all_users = PojoBuilder2.getAllUserObjects();
            int users_size = all_users.size(); 
            System.out.println(users_size);
            for (int i = 0; i < users_size; i++) { 
            try {
                user = all_users.get(i);
                String user_id = user.getId();
                String db_account_address = user.getNanoAddress();
                raw_pending = getPendingBalance(db_account_address);
                if(raw_pending.signum() == 1) {
                    //PojoBuilder2.updateDbBalance(user, raw_pending);
                    //PojoBuilder2.insertDepositRecord(db_account_address, user_id,  raw_pending);
                }
                Map<HexData, ResponsePending.PendingBlock> pendingMap =  getPendingBlocks(db_account_address);
                for (Map.Entry<HexData, ResponsePending.PendingBlock> entry : pendingMap.entrySet()) {
                    HexData hex = entry.getKey();
                    String pending_block = hex.toString();
                    RequestReceive receive = new RequestReceive("07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4", db_account_address, pending_block);
                    receiveDeposit(receive);
                    raw_pocketed = getPocketedBalance(db_account_address);
                    PojoBuilder2.updateDbBalance(user, raw_pocketed);
                    PojoBuilder2.insertDepositRecord(db_account_address, user_id,  raw_pocketed);
                }

        }
        catch(Exception e) {
                e.printStackTrace();
}
}
}
}
