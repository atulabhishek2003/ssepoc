package com.sse.utilities.credentials;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class used to encrypt and decrypt passwords from properties files (or other String data if required).
 * This currently uses AES encryption in conjunction with conversion from bytes to a String hex representation
 * to enable 'readable' and easily-enterable envrypted password into properties files etc.
 * @author mitchella3
 *
 */
public final class SecretHandler {

	/**
	 * To obtain the encoding key (which is NOT stored in the codebase), a hex representation of ths key is stored in an
	 * environment variable in the operating system (or runtime) configuration. The name of the variable in which this key is stored is the value
	 * of the constant below.
	 */
	public static final String ENVIRONMENT_VARIABLE_HOLDING_KEY = "L2I_KEY";

	private static Logger log = LogManager.getLogger(SecretHandler.class);
	private static final boolean DEBUG = false; //set to true in the event of issues to gain more debugging output.

	private static boolean entitiesInitialised = false;
	private static Cipher cipher;
	private static SecretKeySpec secretKeySpec;
	private static IvParameterSpec ivSpec;
	private static final String CHAR_ENCODING = "ISO-8859-1";

	private SecretHandler() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}		
	
	/**
	 * Encrypts the passed String and returns a hex representation of the encoded byte array resulting from the AES encryption
	 * @param inputString the String to be encrypted
	 * @return a hex representation of the encoded byte array resulting from the AES encryption
	 * @throws InvalidKeyException a possible issue with the encryption processing
	 * @throws InvalidAlgorithmParameterException a possible issue with the encryption processing
	 * @throws UnsupportedEncodingException a possible issue with the encryption processing
	 * @throws ShortBufferException a possible issue with the encryption processing
	 * @throws IllegalBlockSizeException a possible issue with the encryption processing
	 * @throws BadPaddingException a possible issue with the encryption processing
	 * @throws NoSuchPaddingException a possible issue with the initialisation of the encryption objects
	 * @throws NoSuchAlgorithmException a possible issue with the initialisation of the encryption objects
	 */
	public static String encrypt(String inputString) throws InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		initialiseEntities();
		byte[] input = inputString.getBytes(CHAR_ENCODING);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
		byte[] encrypted= new byte[cipher.getOutputSize(input.length)];
		int encLen = cipher.update(input, 0, input.length, encrypted, 0);
		encLen += cipher.doFinal(encrypted, encLen);

		String convertedString = HexBytesConversion.bytesToHex(encrypted);
		if (DEBUG) {
			log.info("Encrypted bytes for input string " + inputString + " with encoding length : " + encLen + " :");
			printDebugInfoForByteArray(encrypted);
			log.info("About to return string : " + convertedString);
		}
		return convertedString;
	}

	/**
	 * Decrypts the passed encoded String (which is in the form of a hex representation of the encoded byte array) and returns the decrypted String
	 * <p>Any exceptions are rethrown as a java.lang.Error as they will be fatal to the continuation of the suite.
	 * @param inputString the String to be decrypted
	 * @return a hex representation of the encoded byte array resulting from the AES decryption
	 */
	public static String decrypt(String inputString) {
		String convertedString;
		try {
			initialiseEntities();
			byte[] encrypted = HexBytesConversion.hexToBytes(inputString);
			int encLen = encrypted.length;
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
			byte[] decrypted = new byte[cipher.getOutputSize(encLen)];
			int decLen = cipher.update(encrypted, 0, encLen, decrypted, 0);
			decLen += cipher.doFinal(decrypted, decLen);

			convertedString = new String(decrypted,CHAR_ENCODING);
			if (DEBUG) {
				log.info("Decrypted bytes for input string " + inputString + " with decoding length : " + decLen + " :");
				printDebugInfoForByteArray(decrypted);
				log.info("About to return string : " + convertedString);
			}
		}
		catch (UnsupportedEncodingException | IllegalBlockSizeException | ShortBufferException | BadPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalArgumentException e) {
			// All exceptions will cause failures, so convert into Error....
			String message = "Exception occurred during encryption";
			Error fatalError = new Error(message, e);
			log.fatal(message, fatalError);
			throw fatalError;
		}
		return convertedString;
	}

	private static synchronized void initialiseEntities() throws NoSuchAlgorithmException, NoSuchPaddingException {
		String key = System.getenv(ENVIRONMENT_VARIABLE_HOLDING_KEY);
		if (key==null) {
			String message = "*** YOU NEED TO HAVE AN ENVIRONMENT VARIABLE "
					+ ENVIRONMENT_VARIABLE_HOLDING_KEY + " SET TO THE CORRECT VALUE TO RUN THIS APPLICATION ***";
			Error fatalError = new Error(message);
			log.fatal(message,fatalError);
			throw fatalError;
		}
		if (!entitiesInitialised) {
			log.info("About to initialise encryption objects");
			cipher = Cipher.getInstance("AES/CTR/NoPadding");

			 /* This "initialisation vector" key was initially created by running this code once and storing the bytes generated:
					cipher = Cipher.getInstance("AES/CTR/NoPadding");
					SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
					ivBytes = new byte[cipher.getBlockSize()];
					randomSecureRandom.nextBytes(ivBytes);
			 */
			byte[] ivBytes = HexBytesConversion.hexToBytes("1519F7475ADC2A782AA63DFF052799E4");
			ivSpec = new IvParameterSpec(ivBytes);

			 /* This encyption key was initially created by running this code once and capturing the bytes generated:
					KeyGenerator keyGen = KeyGenerator.getInstance("AES");
					keyGen.init(256);
					SecretKey secretKey = keyGen.generateKey();
					byte[] encodedKey = secretKey.getEncoded();

				The hex representation of this key is stored in an environment variable SSE_KEY on the machine running this suite.
			  */
			byte[] encodedKey = HexBytesConversion.hexToBytes(key);
			secretKeySpec = new SecretKeySpec(encodedKey, "AES");
			entitiesInitialised = true;
			log.info("Encryption objects initialised successfully");
		}
	}

	private static void printDebugInfoForByteArray(byte[] bytes) {
		log.info(HexBytesConversion.bytesToHex(bytes));

		byte[] bytesToOutput = bytes;
		for (int i=0; i < 2; i++) {
			boolean firstTimeinLoop = true;
			StringBuffer sb = new StringBuffer();
			for (byte b : bytesToOutput) {
				sb.append((firstTimeinLoop ? "" : ",") + b);
				firstTimeinLoop = false;
			}
			log.info(sb.toString());
			//repeat for result of byte[]->hex->byte[] to confirm reversibility of hex/byte conversion.
			//The same lines should appear twice.
			bytesToOutput = HexBytesConversion.hexToBytes(HexBytesConversion.bytesToHex(bytes));
		}
	}
}
