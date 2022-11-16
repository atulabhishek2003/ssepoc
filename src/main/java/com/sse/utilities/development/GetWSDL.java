package com.sse.utilities.development;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import com.sse.utilities.credentials.SecretHandler;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Test class attempt to get Enterprise WSDL from Salesforce (as Chrome often runs out of memory)
 * @author mitchella3
 *
 */
@SuppressWarnings("javadoc")
public class GetWSDL {
	private static URL url = null;
	private static String sessionId;

	/*
	 * If you get out-of-memory issues in Chrome, paste the (Staging) URL from the failed window into the URL below, and run it.
	 * This will write the output WSDL into whatever file you specify below. 
	 */
	private static String targetFileName = "C:\\temp\\wsdl.txt";
	
	static  {
		try {
			url = new URL("https://unity--staging.my.salesforce.com/soap/wsdl.jsp?type=*&ver_appirio_core=2020.200&ver_Apttus=9.432&ver_Apttus_Approval=9.15&ver_Apttus_Billing=5.158&ver_Apttus_CMConfig=11.120&ver_Apttus_Config2=11.1552&ver_Apttus_CPQApi=10.91&ver_Apttus_CQApprov=9.0&ver_Apttus_Proposal=9.187&ver_Apttus_QPAsset=6.14&ver_Apttus_QPComply=8.44&ver_Apttus_QPConfig=11.288&ver_Apttus_XAAdmin=1.0&ver_Apttus_XApps=4.40&ver_Apttus_XAppsDS=1.9&ver_APXTConga4=8.174&ver_cas=1.32&ver_CLZV6=1.5&ver_CLZV6BP=1.6&ver_DNBoptimizer=1.6103&ver_ffc=4.1&ver_ffct=2.7&ver_fferpcore=2020.202&ver_ffirule=2019.200&ver_ffr=2020.201&ver_Field_Trip=1.3&ver_FLODocs=3.981&ver_highspot=2.62&ver_iSell=2.2&ver_LEXMAGICMOVER=4.15&ver_LID=3.17&ver_ob1=1.19&ver_psa_m=1.49&ver_psa_report=1.12&ver_pse=2020.200&ver_sf_chttr_apps=1.19&ver_sf_com_apps=1.7&ver_spi_map=1.122&ver_success=8.45&ver_sumokpi=2.13&ver_XactlyCorp=9.84");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	//Uses the current SOAP URL to login just to get a session id for use in the cookie to retrieve the new WSDL.
	public static void main(String[] args) {
			try {
				login("automationsysadmin@cirium.datastage.com.staging",
						SecretHandler.decrypt("E755C3293E7F7AB13C27"),
						"https://test.salesforce.com/services/Soap/c/52.0/0DF3L0000008OOw");
				
				getWSDL();
				
			} catch (ConnectionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	

	private static void getWSDL() throws IOException {
		System.out.println("Getting WSDL from " + url);
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("Cookie", "sid=" + sessionId);
		connection.connect();
		InputStream is = (InputStream)(connection.getContent());
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			Vector<String> v = new Vector<>();

			while ((line = br.readLine()) != null) {
				v.add(line);
			}
			
			writeVectortoFile(v);
/*
			if (urlConnection instanceof HttpURLConnection) {
				System.out.println("ResponseCode: "+((HttpURLConnection)urlConnection).getResponseCode());
				System.out.println("ResponseMessage: "+((HttpURLConnection)urlConnection).getResponseMessage());
			}
			System.out.println("ContentEncoding: "+urlConnection.getContentEncoding());
			System.out.println("ContentLength: "+urlConnection.getContentLength());
			System.out.println("ContentType: "+urlConnection.getContentType());
			System.out.println("Date: "+urlConnection.getDate());
			System.out.println("Server: "+urlConnection.getHeaderField("Server"));
			System.out.println("\n\n");
*/
		}
		
	}



	private static EnterpriseConnection login(String userName, String password, String endpoint) throws ConnectionException {
		final ConnectorConfig config = new ConnectorConfig();
		config.setUsername(userName);
		config.setPassword(password);
		config.setAuthEndpoint(endpoint);

		EnterpriseConnection connection = Connector.newConnection(config); // this sets the URL within it.
		System.out.println("Logging in with username : " + userName);
		System.out.println("Auth EndPoint: " + config.getAuthEndpoint());
		System.out.println("Service EndPoint: " + config.getServiceEndpoint());
		System.out.println("Username: " + config.getUsername());
		System.out.println("SessionId: " + config.getSessionId());
		sessionId = config.getSessionId();

		return connection;
	}
	
	private static void writeVectortoFile(Vector<String> lines) throws IOException {
		System.out.println("Writing WSDL to " + targetFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(targetFileName,false)));) {
			
			String line;
			for (int i = 0; i < lines.size(); i++) {
				line = lines.elementAt(i);
				pw.println(line);
			}
		}
	}	
}

