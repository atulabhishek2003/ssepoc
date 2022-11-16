package com.sse.utilities.security;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

/**
 * Class to handle a custom KeyStore and the standard JVM keystore
 * <p>Used to workaround unexpected difficulties accessing Salesforce to disable SSO, followed by accessing the Viper API.
 * @author mitchella3
 *
 */
public final class SSLContextGenerator {

	private SSLContextGenerator() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
	
	/**
	 * @param keystore the intended KeyStore object
	 * @param password the KayStore password (usually 'changeit')
	 * @return a SSLContext to be used for the given keystore
	 * @throws NoSuchAlgorithmException if issues with the algorithms occur (e.g. SunX509)
	 * @throws KeyManagementException if a Key-related issue has occurred
	 * @throws UnrecoverableKeyException if a Key-related issue has occurred
	 * @throws KeyStoreException if a Key-related issue has occurred
	 */
	public static SSLContext provideSSLContext(KeyStore keystore, char... password) throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, KeyStoreException {
	  String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
	  X509KeyManager customKeyManager = getKeyManager("SunX509", keystore, password);
	  X509KeyManager jvmKeyManager = getKeyManager(defaultAlgorithm, null, null);
	  X509TrustManager customTrustManager = getTrustManager("SunX509", keystore);
	  X509TrustManager jvmTrustManager = getTrustManager(defaultAlgorithm, null);

	  CompositeX509KeyManager[] keyManagers = { new CompositeX509KeyManager(new ArrayList<X509KeyManager>(Arrays.asList(jvmKeyManager, customKeyManager))) };
	  CompositeX509TrustManager[] trustManagers = { new CompositeX509TrustManager(Arrays.asList(jvmTrustManager, customTrustManager)) };

	  SSLContext context = SSLContext.getInstance("SSL");
	  context.init(keyManagers, trustManagers, null);
	  return context;
	}

	private static X509KeyManager getKeyManager(String algorithm, KeyStore keystore, char... password)
			throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
		KeyManagerFactory factory = KeyManagerFactory.getInstance(algorithm);
		factory.init(keystore, password);

		X509KeyManager result = null;
		for (KeyManager km : factory.getKeyManagers()) {
			if (km instanceof X509KeyManager) {
				result = (X509KeyManager) km;
				break;
			}
		}
		return result;

	}

	private static X509TrustManager getTrustManager(String algorithm, KeyStore keystore)
			throws NoSuchAlgorithmException, KeyStoreException {
		TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
		factory.init(keystore);

		X509TrustManager result = null;
		for (TrustManager tm : factory.getTrustManagers()) {
			if (tm instanceof X509TrustManager) {
				result = (X509TrustManager) tm;
				break;
			}
		}
		return result;

	}
}
