package nanobets;

import java.math.BigInteger;
import java.math.BigDecimal;

public class UserObject {

    private String id;

    private String fingerprint;

    private String ip;

    private String nano_address;

    private String balance;

    private String walletId;

	private String qr_code;

	private String decimal_balance;

    	public UserObject(String id, String fingerprint, String ip, String nano_address, String balance, String walletId, String qr_code, String decimal_balance) {
            this.id = id;
		    this.fingerprint = fingerprint;
		    this.ip = ip;
            this.nano_address = nano_address;
            this.balance = balance;
            this.walletId = walletId;
		    this.decimal_balance = decimal_balance;
		    this.qr_code = qr_code;
	}
        
        public String getFp() {
        return fingerprint;
        }

        public String getIp() {
        return ip;
        }

        public String getId() {
        return id;
        }

        public String getWalletId() {
        return walletId;        
        }        

        public String getNanoAddress() {
        return nano_address;
        }        

        public BigInteger getBalance() {
        BigInteger big = new BigInteger(balance);
        return big;
        }        

        public BigDecimal getDecimalBalance() {
        //string to dec and return
        BigDecimal dec = new BigDecimal(decimal_balance);	
        return dec;
        }	
        public String getQrCode() {
        return qr_code;
        }	
}





