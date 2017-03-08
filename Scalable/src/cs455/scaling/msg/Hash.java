package cs455.scaling.msg;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	
	final static public int size = 40;

	public static String toHash(byte[] bytes) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {}
		byte[] hash = digest.digest(bytes);
		BigInteger hashInt = new BigInteger(1, hash);
		String ret = hashInt.toString(16);
		if (ret.length() == size)
			return ret;
		for (int i = ret.length(); i < size; i++)
			ret = "0" + ret;
		return ret;
	}
}
