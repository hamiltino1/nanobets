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
import uk.oczadly.karl.jnano.model.HexData; 

//java
import java.net.InetAddress; 
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList; import java.sql.DriverManager;
import java.lang.StringBuilder; 
import java.net.MalformedURLException;
import java.io.IOException;
import java.util.Stack;
import org.apache.commons.lang3.RandomStringUtils; 
import java.util.concurrent.TimeUnit;

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

public class WorkThread extends Thread {

	private String target;
    private String sourceAccount;
	private List<UserObject> all_users; 
	private UserObject user;
	private String block_account_address;
	private BigInteger raw_pocketed;
	private String frontier;
    private ResponseBlockHash response;
    private RequestSend request_send;

  	public WorkThread(){
    	}

	/**
	 * Runs work thread
	 */
	public void run() {
        try {
		    withdrawMethod();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
	}

	public void withdrawMethod() throws IOException {
        if(work_running == false) {
            work_running = true;
            System.out.println("Work thread started");
            System.out.println("Current work stack size: " + work_stack.size());
            System.out.println("Current withdraw stack size: " + withdraw_stack.size());
            System.out.println("Work thread started");
            System.out.println("work running: " + work_running);
            System.out.println("withdraw stack empty check: " + withdraw_stack.isEmpty());

                if(withdraw_stack.isEmpty() == false) {
                    System.out.println("Withdraw stack not empty");
                    String withdraw_id = withdraw_id_stack.pop();
                    //String withdraw_id = global_withdraw_id;;
                    //
                    try {

                        request_send = withdraw_stack.peek();
                        response  = node.processRequest(request_send);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        System.out.println("withdraw stack send error: 56worksend");
                    }

                    //db work
                    HexData hex4 = response.getBlockHash();
                    String block_hash = hex4.toString();
                    BigInteger amount =  request_send.getAmount();
                    BigDecimal dec_amount =  new BigDecimal(amount);
                    dec_amount = dec_amount.movePointLeft(30);
                    dec_amount = dec_amount.stripTrailingZeros();
                    String string_amount = dec_amount.toPlainString();
                    String random_string = RandomStringUtils.randomAlphabetic(10);
                    String destination_account = request_send.getDestinationAccount(); 
                    String source_account = request_send.getSourceAccount();
                    String user_id = id_stack.peek();
                    //new balance is balance minus amount
                    PojoBuilder.insertWithdrawRecord(withdraw_id, destination_account, user_id, string_amount, block_hash);
                    id_stack.pop();
                    withdraw_stack.pop();
                    withdrawMap.remove(user_id);
                    System.out.println("withdraw processed from withdraw stack");
                    //Address contains new frontier, so work_stack needs to be cleared
                    /*
                    if(work_stack.isEmpty() == false) {
                        work_stack.pop();
                    }
                    */
                    work_stack.clear();
                    work_running = false;
                }
                else if(work_stack.isEmpty()) {
                    try {
                        System.out.println("Finding work for stack");
                        sourceAccount = "nano_3z1z144ggdujyypjmrm9dn37a5jimur6g5poshxzz5ryr6jjf8hufjgq16yr";
                        frontier = Tools.getFrontier(sourceAccount);
                        Tools.getWorkSolution(frontier);		
                        System.out.println("new work solution added to stack");
                        work_running = false;
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            
            try {
                TimeUnit.SECONDS.sleep(5);		
                withdrawMethod();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
