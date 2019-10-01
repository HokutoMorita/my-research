package com.morita.mhcat.servletimpl;
import java.security.*;

/**
 * セッションIDを生成するクラス
 */
class SessionIdGenerator {
	private SecureRandom random;
	
	SessionIdGenerator() {
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
			ex.printStackTrace();
			System.exit(1);
		}
    }

    public String generateSessionId() {
		byte[] bytes = new byte[16];
		this.random.nextBytes(bytes);
		StringBuilder buffer = new StringBuilder();
		
		for (int i = 0; i < bytes.length; i++) {
			buffer.append(Integer.toHexString(bytes[i] & 0xff).toUpperCase());
		}
		return buffer.toString();
    }
}
