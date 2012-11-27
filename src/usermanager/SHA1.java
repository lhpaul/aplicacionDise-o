package usermanager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
	private MessageDigest md;
	private byte[] buffer, digest;
	private String hash = "";

	/**
	 * Hashes a string with SHA1 algorithm.
	 * @param message string to hash.
	 * @return SHA1 of the string.
	 */
	public String getHash(String message){
		buffer = message.getBytes();
		try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		md.update(buffer);
		digest = md.digest();

		for(byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1) hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}
}
