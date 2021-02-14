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
import uk.oczadly.karl.jnano.rpc.exception.RpcEntityNotFoundException; 


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
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;


//sql2o
import org.sql2o.Sql2o;
import org.sql2o.*;

//nanobets
import static nanobets.Main.*;
import static nanobets.BlockTools.getPocketedBalance;
import static nanobets.BlockTools.getAccountInfo;
import static nanobets.BlockTools.getPendingMap;
import static nanobets.BlockTools.getPendingBalance;
import static nanobets.BlockTools.getPendingBlocks;
import static nanobets.BlockTools.receiveDeposit;
import static nanobets.BlockTools.moveToMainAccount;
import static nanobets.BlockTools.checkMatch;
import static nanobets.BlockTools.randomStringGenerator;
import static nanobets.BlockTools.update;
import static nanobets.BlockTools.moveToMainAccount;
import static nanobets.PojoBuilder2.*;
import static nanobets.PojoBuilder.*;

//httpcore
import uk.oczadly.karl.jnano.rpc.HttpRequestExecutor; 

public class MoveThread extends Thread {

	private String target;
	private InetAddress node;
	private List<UserObject> all_users; 
	private UserObject user;
	private String block_account_address;
	private BigInteger raw_pocketed;
	private BigInteger raw_pending;
	private RpcQueryNode nodeu = new RpcQueryNode();
	private HashMap<String, String> pending_map  = new HashMap<String, String>();

  	public MoveThread(){
    }
	/**
	 * Thread for deposit work.
	 */
	public void run()  {
    try {
		move();
    }
    catch(Exception e) {
        e.printStackTrace();
    }
	}
	public void move() throws IOException, RpcException  {
        System.out.println("Move thread started");
        all_users = PojoBuilder2.getAllUserObjects();
        int users_size = all_users.size(); 
        for (int i = 0; i < users_size; i++) { 
            UserObject user = all_users.get(i);
            String db_account_address = user.getNanoAddress();
            BigInteger raw_pocketed = getPocketedBalance(db_account_address); 
            if(raw_pocketed.signum() == 1){
                System.out.println("Found a pocketed deposit at " + db_account_address);
                String walletId = "07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4";
                String destinationAccount = "nano_3z1z144ggdujyypjmrm9dn37a5jimur6g5poshxzz5ryr6jjf8hufjgq16yr";
                String randomString = randomStringGenerator();
                moveToMainAccount(walletId, db_account_address, destinationAccount, raw_pocketed, randomString);
            }	
        String main_account = "nano_3z1z144ggdujyypjmrm9dn37a5jimur6g5poshxzz5ryr6jjf8hufjgq16yr";
        BigInteger raw_pending = getPendingBalance(main_account);
        if(raw_pending.signum() == 1) {
        System.out.println("pending deposit in main wallet");
        Map<HexData, ResponsePending.PendingBlock> pendingMap1 =  getPendingBlocks(main_account);
        for (Map.Entry<HexData, ResponsePending.PendingBlock> entry1 : pendingMap1.entrySet()) {
            HexData hex1 = entry1.getKey();
            String pending_block1 = hex1.toString();
            int a = 1;
            Map<HexData, ResponsePending.PendingBlock> mainWalletPending =  getPendingBlocks(db_account_address);
            try {
                a++;
                RequestReceive receive = new RequestReceive("07EE0EBE61BCF1177A8930A843609C3ED01854B7570069507DE787A10AD68DC4", main_account, pending_block1);
                receiveDeposit(receive);
                if (a > 1 ) {
                    break;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
}
}
}
}
}
}


