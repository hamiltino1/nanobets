package nanobets;

//sql2o 
import org.sql2o.*;

//java
import java.util.List;
import java.sql.DriverManager;
import java.net.MalformedURLException; 
import java.io.IOException;
import java.util.Stack;
import java.lang.Thread;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.net.URL;
import java.sql.*;


//java blockcallback
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executor;
import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.Executors; 
import java.math.BigInteger;
import java.net.InetAddress; 
import java.util.Stack;


//spark
import spark.staticfiles.*;
import static spark.Spark.*;

//apache log4j
import org.apache.log4j.Logger;

//nanobets
import nanobets.SparkUtils;
import static nanobets.Tools.*;

//jnano
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkPeerAdd;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful; 

//jnano blockcallback
import uk.oczadly.karl.jnano.callback.BlockCallbackServer; 
import uk.oczadly.karl.jnano.rpc.RpcQueryNode; 
import uk.oczadly.karl.jnano.callback.BlockData; 
import uk.oczadly.karl.jnano.rpc.request.wallet.RequestSend;

//sql2o blockcallback
import org.sql2o.Sql2o;
import org.sql2o.*;

//jackson
import com.fasterxml.jackson.core.JsonProcessingException; 


//httpcore
import uk.oczadly.karl.jnano.rpc.HttpRequestExecutor; 


public class Main {

	public static boolean work_running = false;
	public static HashMap<String, Long> withdrawMap = new HashMap<String, Long>();  
	public static HashMap<String, Long> gamble_map = new HashMap<String, Long>();  
	public static HashMap<String, String> idMap = new HashMap<String, String>();  

	public static HashMap<String, String> pending_map = new HashMap<String, String>();  

	public static String global_withdraw_id = "";

	public static Stack<UpdateObject> balance_update_stack = new Stack<UpdateObject>();	


    public static Sql2o sql2o = new Sql2o("jdbc:sqlite:/root/nano.db", null, null);
    public static Sql2o sql2o2 = new Sql2o("jdbc:sqlite:/root/nanogamble.db", null, null);
    public static RpcQueryNode node; 
	public static Stack<String> work_stack = new Stack<String>();	
	public static Stack<String> id_stack = new Stack<String>();	
	public static Stack<String> withdraw_id_stack = new Stack<String>();	
	public static Stack<RequestSend> withdraw_stack = new Stack<RequestSend>();	
	public static InetAddress inet_node; 
	public static String target;
	public static BlockData block;
	private static final ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
	private static final ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
	private static final ScheduledExecutorService scheduler3 = Executors.newScheduledThreadPool(1);
	private static final ScheduledExecutorService scheduler4 = Executors.newScheduledThreadPool(1);
	private static final ScheduledExecutorService scheduler5 = Executors.newScheduledThreadPool(1);
	public static Stack<BlockData> stack = new Stack<BlockData>();	
	public static MoveThread moveThread = new MoveThread();
	public static BalanceThread balanceThread = new BalanceThread();
	public static WorkThread workThread = new WorkThread();
	public static WmapThread wmapThread = new WmapThread();
	public static Logger logger = Logger.getLogger(Main.class);
	public static String user_id_work = null;

    	public static void main(String[] args) throws MalformedURLException, JsonProcessingException, IOException {
     	    node = new RpcQueryNode(); 
            port(80);
            externalStaticFileLocation("/root/public");
  	    SparkUtils.createServerWithRequestLog(logger);
            new UserController(node);
    	    LotteryThread lotteryThread = new LotteryThread();
            scheduler1.scheduleAtFixedRate(balanceThread, 0, 7, SECONDS);
            scheduler2.scheduleAtFixedRate(moveThread, 0, 10, SECONDS);
            scheduler3.scheduleAtFixedRate(wmapThread, 0, 100, SECONDS);
            scheduler4.scheduleAtFixedRate(lotteryThread, 0, 500, SECONDS);
            scheduler5.scheduleAtFixedRate(workThread, 0, 10, SECONDS);
	}
}
