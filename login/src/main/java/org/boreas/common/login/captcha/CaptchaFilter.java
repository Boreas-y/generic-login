package org.boreas.common.login.captcha;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Handle the requests about captcha operations. To be exact, 2 kinds of URL
 * will be handled: fetching/checking captcha. Other URLs will be ignored.
 * 
 * @author boreas
 *
 */
public class CaptchaFilter extends GenericFilterBean {

	private final static String DEFAULT_CAPTCHA_URL = "/captcha";

	/**
	 * {@value}
	 */
	private final static String DEFAULT_CHECK_CAPTCHA_PARAMETER = "check";

	private static final List<String> DEFAULT_AVALIABLE_PARAMETERS = Collections
			.unmodifiableList(Arrays.asList("login"));

	private CaptchaContext captchaContext;

	private List<String> avaliableParameters = DEFAULT_AVALIABLE_PARAMETERS;

	/**
	 * Check if the URL matches captcha request URL pattern. If true, handle it
	 * and escape from filter chain.
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (isCaptchaUrl(request)) {
			Enumeration<String> paramters = request.getParameterNames();
			String parameter;
			while (paramters.hasMoreElements()) {
				parameter = paramters.nextElement();
				// Only generate captcha when the parameter is defined
				if (avaliableParameters.contains(parameter)) {
					captchaContext
							.generateCaptcha(request, response, parameter);
					break;
				} else if (parameter.equals(DEFAULT_CHECK_CAPTCHA_PARAMETER)) {
					captchaContext.checkCaptcha(request, response,
							request.getParameter(parameter));
					break;
				}
			}
			return;
		}
		chain.doFilter(request, response);
	}

	/**
	 * Check if the request matches the defined captcha URL pattern.
	 * 
	 * @param request
	 * @return
	 */
	private boolean isCaptchaUrl(HttpServletRequest request) {
		String uri = request.getRequestURI();

		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			// strip everything after the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}

		if ("".equals(request.getContextPath())) {
			return uri.endsWith(DEFAULT_CAPTCHA_URL);
		}
		return uri.endsWith(request.getContextPath() + DEFAULT_CAPTCHA_URL);
	}

	@Override
	public void afterPropertiesSet() throws ServletException {
		Assert.notNull(captchaContext, "captchaContext must not be null.");
	}

	public CaptchaContext getCaptchaContext() {
		return captchaContext;
	}

	public void setCaptchaContext(CaptchaContext captchaContext) {
		this.captchaContext = captchaContext;
	}

	public List<String> getAvaliableParameters() {
		return avaliableParameters;
	}

	/**
	 * The value equals {@link CaptchaFilter#DEFAULT_CHECK_CAPTCHA_PARAMETER
	 * DEFAULT_CHECK_CAPTCHA_PARAMETER} will be ignored.
	 * 
	 * @param avaliableParameters
	 */
	public void setAvaliableParameters(List<String> avaliableParameters) {
		Assert.notEmpty(avaliableParameters,
				"avaliableParameters cannot be null.");
		HashSet<String> param = new HashSet<String>(avaliableParameters);
		param.remove(DEFAULT_CHECK_CAPTCHA_PARAMETER);
		this.avaliableParameters = Collections
				.unmodifiableList(new ArrayList<String>(param));
	}
}
