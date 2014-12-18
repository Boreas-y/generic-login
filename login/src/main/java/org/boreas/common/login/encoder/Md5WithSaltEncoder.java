package org.boreas.common.login.encoder;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Simple salted MD5 encoder implement of {@code PasswordEncoder}
 * 
 * @author boreas
 * @see Md5PasswordEncoder
 */
public class Md5WithSaltEncoder implements PasswordEncoder {

	private Md5PasswordEncoder md5Encoder = new Md5PasswordEncoder();
	private String salt = "";

	public String encode(CharSequence rawPassword) {
		return md5Encoder.encodePassword(rawPassword.toString(), salt);
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(encode(rawPassword));
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
