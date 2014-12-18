package org.boreas.common.login.captcha;

import java.awt.image.RenderedImage;

/**
 * A entity bean contains captcha information.
 * 
 * @author boreas
 *
 */
public class Captcha {
	private String code;
	private RenderedImage image;

	public void setCode(String code) {
		this.code = code;
	}

	public void setImage(RenderedImage image) {
		this.image = image;
	}

	public String getCode() {
		return code;
	}

	public RenderedImage getImage() {
		return image;
	}

}
