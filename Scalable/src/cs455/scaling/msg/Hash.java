package cs455.scaling.msg;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	public static String toHash(byte[] bytes) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {}
		byte[] hash = digest.digest(bytes);
		BigInteger hashInt = new BigInteger(1, hash);
		return hashInt.toString(16);
	}
}
