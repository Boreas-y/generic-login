package org.boreas.common.login.captcha.generator;

import org.boreas.common.login.captcha.Captcha;

/**
 * A simple, default implement of captcha generator.
 * 
 * @author boreas
 *
 */
public class DefaultCaptchaGenerator extends AbstractCaptchaGenerator {

	public DefaultCaptchaGenerator(ImageGenerator imageGenerator,
			CodeGenerator codeGenerator) {
		super(imageGenerator, codeGenerator);
	}

	@Override
	public Captcha generateCaptcha() {
		Captcha captcha = new Captcha();
		captcha.setCode(generateCode());
		captcha.setImage(generateImage(captcha.getCode()));
		return captcha;
	}

}
