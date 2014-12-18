package org.boreas.common.login.captcha.generator;

import java.awt.image.RenderedImage;

import org.springframework.util.Assert;

import org.boreas.common.login.captcha.Captcha;

/**
 * Base captcha generator for generating {@code Captcha} object, contains an
 * {@code ImageGenerator} and a {@code CodeGenerator}.
 * 
 * @author boreas
 *
 */
public abstract class AbstractCaptchaGenerator {

	private final ImageGenerator imageGenerator;
	private final CodeGenerator codeGenerator;

	public AbstractCaptchaGenerator(ImageGenerator imageGenerator,
			CodeGenerator codeGenerator) {
		Assert.notNull(imageGenerator, "imageGenerator must not be null.");
		this.imageGenerator = imageGenerator;
		Assert.notNull(codeGenerator, "codeGenerator must not be null.");
		this.codeGenerator = codeGenerator;
	}

	/**
	 * Generate a {@code Captcha} object using {@code ImageGenerator} and
	 * {@code CodeGenerator}.
	 * 
	 * @return
	 */
	public abstract Captcha generateCaptcha();

	protected final RenderedImage generateImage(String content) {
		return imageGenerator.generateImage(content);
	}

	protected final String generateCode() {
		return codeGenerator.generateCode();
	}

}
