package org.boreas.common.login.captcha.generator;

import java.util.Random;

/**
 * A simple implement of {@code CodeGenerator}, can generate a random string
 * with specified length using the defined unconfusable letters and digits(eg.
 * number 1 and letter l is not included).
 * 
 * @author boreas
 *
 */
public class SimpleCodeGenerator implements CodeGenerator {

	private int codeLength = 4;

	private static final String CHARACTERS = "abcdefhjkmnprstuvwxyABCDEFGHJKLMNPRSTVUWXY2345678";

	@Override
	public String generateCode() {
		Random random = new Random();
		char[] codes = new char[codeLength];
		for (int i = 0; i < codeLength; i++) {
			codes[i] = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
		}
		return new String(codes);
	}

	public int getCodeLength() {
		return codeLength;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

}
