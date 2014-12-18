package org.boreas.common.login.userdetail;

/**
 * Custom user bean must implement this interface for security reason.
 * 
 * @author boreas
 *
 */
public interface PasswordProtector {
	/**
	 * User-defined user bean will be stored in session. This method provide a
	 * way to protect password. A simple implementation is to set password to
	 * null. If you still need to store password, please encode the password.
	 * 
	 * @author boreas
	 */
	void protectPassword();
}
