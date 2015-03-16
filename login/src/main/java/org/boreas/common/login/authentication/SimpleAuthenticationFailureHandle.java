package org.boreas.common.login.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.Assert;

import org.boreas.common.login.Attributes;

/**
 * Simple implement of {@link AuthenticationFailureHandler}. Provide more
 * features:
 * <ul>
 * <li>Enable error count.</li>
 * <li>Provide more attributes which can user in JSP page.</li>
 * <li>Enable Ajax response, return a {@code JSON} object of failure details.</li>
 * </ul>
 * 
 * @author boreas
 *
 */
public class SimpleAuthenticationFailureHandle extends
		SimpleUrlAuthenticationFailureHandler implements InitializingBean {

	private String loginPageUrl;

	@SuppressWarnings("deprecation")
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String username = ((User) exception.getExtraInformation())
				.getUsername();
		String errorMessage = exception.getMessage();
		int errorCount = updateErrorCount(request.getSession());
		if (request.getHeader(Attributes.AJAX_HEADER) == null) {
			String msg = Attributes.getLoginJsonResult(-1, errorCount,
					errorMessage);
			response.setContentType(Attributes.JSON_CONTENT_TYPE);
			response.getWriter().print(msg);
		} else {
			HttpSession session = request.getSession();
			session.setAttribute(Attributes.SESSION_LOGIN_USERNAME, username);
			session.setAttribute(Attributes.SESSION_LOGIN_ERR_MSG, errorMessage);
			getRedirectStrategy().sendRedirect(request, response, loginPageUrl);
		}
	}

	private int updateErrorCount(HttpSession session) {
		Object countObj = session
				.getAttribute(Attributes.SESSION_LOGIN_ERR_COUNT);
		int count = countObj == null ? 0 : Integer.valueOf(countObj.toString());
		count++;
		session.setAttribute(Attributes.SESSION_LOGIN_ERR_COUNT, count);
		return count;
	}

	public String getLoginPageUrl() {
		return loginPageUrl;
	}

	public void setLoginPageUrl(String loginPageUrl) {
		this.loginPageUrl = loginPageUrl;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(loginPageUrl, "loginPageUrl cannot be null.");
	}

}
