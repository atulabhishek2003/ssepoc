package com.sse.utilities.credentials;

import java.util.Locale;

/**
 * Class of small utilities to convert between a String hex representation of an unsigned byte array, to the byte array.
 * Used to enable byte arrays of credentials to be stored in String form within properties files etc. with no danger of
 * character encoding issues (as characters 0-9 and A-F are generally common between the major 8-byte encoding schemes.
 * @author mitchella3
 *
 */
public final class HexBytesConversion {
	private static byte[] correspondingNibble = new byte['f' + 1];

	private HexBytesConversion() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
    /**
     * Convenience method to convert a byte array to a hex string.
     *
     * @param  data  the byte[] to convert
     * @return String the converted byte[]
     */
    public static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]).toUpperCase(Locale.getDefault()));
        }
        return buf.toString();
    }

    /**
     * Convert a hex string to an unsigned byte array.
     * Permits upper or lower case hex.
     *
     * @param s String must have even number of characters.
     *          and be formed only of digits 0-9 A-F or
     *          a-f. No spaces, minus or plus signs.
     *
     * @return corresponding unsigned byte array.
     */
    public static byte[] hexToBytes(String s) {
        int stringLength = s.length();
        if (stringLength % 2 != 0) {
            throw new IllegalArgumentException("String " + s + " requires an even number of hex characters");
        }
        byte[] bytes = new byte[stringLength/2];

        for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
            int high = charToNibble(s.charAt(i));
            int low = charToNibble(s.charAt(i + 1));
            // You can store either unsigned 0..255 or signed -128..127 bytes in a byte type.
            bytes[j] = (byte) ((high << 4) | low);
        }
        return bytes;
    }

    /* PRIVATE METHODS BELOW */

    /**
     * Utility to convert a single byte to a hex string.
     *
     * @param  data  the byte to convert
     * @return String the converted byte
     */
    private static String byteToHex(byte data) {
        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar((data >>> 4) & 0x0F));
        buf.append(toHexChar(data & 0x0F));
        return buf.toString();
    }

    /**
     * Convenience method to convert an int (<b>between 0 and 15 inclusive </b>) to a hex char.
     * <p>Used internally to this class, but made public in case it's useful.
     *
     * @param  i  the int to convert
     * @return char the converted char
     */
    private static char toHexChar(int i) {
        if (0 <= i && i <= 9) {
            return (char) ('0' + i);
        }
        return (char) ('a' + (i - 10));
    }

    static
        {
        // only 0..9 A..F a..f have meaning. rest are errors.
        for (int i = 0; i <= 'f'; i++) {
            correspondingNibble[i] = -1;
        }
        for (int i = '0'; i <= '9'; i++) {
            correspondingNibble[i] = (byte) (i - '0');
        }
        for (int i = 'A'; i <= 'F'; i++) {
            correspondingNibble[i] = (byte) (i - 'A' + 10);
        }
        for (int i = 'a'; i <= 'f'; i++) {
            correspondingNibble[i] = (byte) (i - 'a' + 10);
        }
    }

    /**
     * Convert  a single char to corresponding nibble using a precalculated array.
     *
     * @param c char to convert. must be 0-9 a-f A-F, no
     *          spaces, plus or minus signs.
     *
     * @return corresponding integer  0..15
     * @throws IllegalArgumentException on invalid c.
     */
    private static int charToNibble(char c) {
        if (c > 'f')
            throw new IllegalArgumentException("Invalid hex character: " + c);
        int nibble = correspondingNibble[c];
        if (nibble < 0)
            throw new IllegalArgumentException("Invalid hex character: " + c);
        return nibble;
	}

}
