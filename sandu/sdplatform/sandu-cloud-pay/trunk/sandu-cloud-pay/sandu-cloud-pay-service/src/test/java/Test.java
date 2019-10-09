import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Test {

	 public static void main(String args[]) throws Exception {
	        try {
	            URL url = new URL("http://paydev.sanduspace.com:8089/v1/pay/callback/test/notify");
	            InputStream in =url.openStream();
	            InputStreamReader isr = new InputStreamReader(in);
	            BufferedReader bufr = new BufferedReader(isr);
	            String str;
	            while ((str = bufr.readLine()) != null) {
	                System.out.println(str);
	            }
	            bufr.close();
	            isr.close();
	            in.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
