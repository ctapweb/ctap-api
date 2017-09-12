package com.ctapweb.api.utils.unused;

import org.mindrot.jbcrypt.BCrypt;

public class UserUtils {

	/**
	 * Create a Bcrypt-encrypted passwd.
	 * 
	 * <pre>
	 * {@code
	 * // Hash a password for the first time * String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
	 * // Check that an unencrypted password matches one that has
	 * // previously been hashed
	 * if (BCrypt.checkpw(candidate, hashed))
	 *	 	System.out.println("It matches");
	 * else 
	 * 		System.out.println("It does not match");
	 * }
	 * </pre>
	 * 
	 * @see <a href="http://www.mindrot.org/projects/jBCrypt/">http://www.mindrot.org/projects/jBCrypt/</a> 
	 * @param plainPasswd
	 * @return
	 */
	public static String encryptPasswd(String plainPasswd) {
		return BCrypt.hashpw(plainPasswd, BCrypt.gensalt());
	}
	
	public static boolean checkPasswd(String plainPasswd, String encryptedPasswd) {
		return BCrypt.checkpw(plainPasswd, encryptedPasswd);
	}
}
