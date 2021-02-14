package mainClass;

import spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.List;
import spark.ResponseTransformer;
import com.google.gson.Gson;

import static spark.Spark.get;
import static mainClass.JsonUtil.json;
import static spark.Spark.post;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.sql2o.*;

import spark.Request;
import spark.Response;

import java.sql.DriverManager;
import spark.staticfiles.*;


public class AjaxTest {


        public void ajaxTest() {
                staticFiles.location("/public"); 
                //externalStaticFileLocation("src/test/resources/public");
                Sql2o sql2o = new Sql2o("jdbc:mysql://remotemysql.com:3306/ZQyF5HE8GQ", "ZQyF5HE8GQ", "wl8z9bP3vz");        
                UserPojo userPojo = new UserPojo(sql2o);


                
                post("/api/sum", (request, response) -> {

                        List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());

                        Map<String, String> params = toMap(pairs);

                        try {
                                Integer a = Integer.parseInt(params.get("a"));
                                Integer b = Integer.parseInt(params.get("b"));
                                return a + b;
                        }
                        catch (Exception e){
                                return "Error: " + e.getMessage();
                        }
                });          
        }        
        
                
}        
