package org.boreas.common.login.captcha;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import org.boreas.common.login.captcha.generator.AbstractCaptchaGenerator;

/**
 * A context of captcha based on session, provides captcha operations.
 * 
 * @author boreas
 *
 */
public class CaptchaContext implements InitializingBean {

	private static final String CAPTCHA_FORMAT = "jpg";
	private static final String KEY_PATTERN = "_captcha_%s_";
	private static final String PARAMETER_NAME = "captcha";
	private static final String CAPTCHA_ERR_COUNT = "_captchaerr_";

	private AbstractCaptchaGenerator captchaGenerator;

	/**
	 * Generate a new captcha for request in steps:
	 * <ol>
	 * <li>Generate new {@code Captcha} object.</li>
	 * <li>Save the captcha code into session.</li>
	 * <li>Write the image to {@code response}.</li>
	 * </ol>
	 * 
	 * @param request
	 * @param response
	 * @param key
	 *            specify the usage of captcha.
	 * @throws IOException
	 */
	public void generateCaptcha(HttpServletRequest request,
			HttpServletResponse response, String key) throws IOException {
		Captcha captcha = captchaGenerator.generateCaptcha();
		request.getSession().setAttribute(formatKey(key), captcha.getCode());
		writeImage2Response(response, captcha.getImage());
	}

	/**
	 * Check if request contains a correct captcha which is stored in the
	 * parameter named 'captcha'. If correct, captcha will be erased.<br>
	 * A code which indicates the result will be written into response:<br>
	 * 1 : correct<br>
	 * -1 : wrong<br>
	 * 0 : expired<br>
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @throws IOException
	 */
	public void checkCaptcha(HttpServletRequest request,
			HttpServletResponse response, String key) throws IOException {
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession();
		String formatKey = formatKey(key);
		Object obj = session.getAttribute(formatKey);
		String code = request.getParameter(PARAMETER_NAME);
		response.setContentType("application/json;charset=utf-8");
		if (obj == null)
			pw.print("{\"result\":0}");
		else if (obj.toString().equalsIgnoreCase(code)) {
			session.removeAttribute(formatKey);
			session.removeAttribute(CAPTCHA_ERR_COUNT);
			pw.print("{\"result\":1}");
		} else {
			int errCount = updateErrorCount(session);
			pw.print(String.format("{\"result\":-1,\"errCount\":%d}", errCount));
		}
	}

	private void writeImage2Response(HttpServletResponse response,
			RenderedImage image) throws IOException {
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, CAPTCHA_FORMAT, os);
		os.flush();
		os.close();
		response.flushBuffer();
	}

	private String formatKey(String key) {
		return String.format(KEY_PATTERN, key);
	}

	private int updateErrorCount(HttpSession session) {
		Object countObj = session.getAttribute(CAPTCHA_ERR_COUNT);
		int count = countObj == null ? 0 : Integer.valueOf(countObj.toString());
		session.setAttribute(CAPTCHA_ERR_COUNT, ++count);
		return count;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(captchaGenerator, "captchaGenerator must not be null.");
	}

	public void setCaptchaGenerator(AbstractCaptchaGenerator captchaGenerator) {
		this.captchaGenerator = captchaGenerator;
	}
}
