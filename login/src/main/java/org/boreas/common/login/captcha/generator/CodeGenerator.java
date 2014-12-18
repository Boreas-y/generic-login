package org.boreas.common.login.captcha.generator;

/**
 * This interface specifies a code generator which can generate a string.
 * 
 * @author boreas
 *
 */
public interface CodeGenerator {
	/**
	 * Generate a string in a specified method.
	 * 
	 * @return
	 */
	String generateCode();
}
