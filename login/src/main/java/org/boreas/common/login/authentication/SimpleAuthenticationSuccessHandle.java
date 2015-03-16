package org.boreas.common.login.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.boreas.common.login.Attributes;
import org.boreas.common.login.userdetail.ExtendUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

/**
 * Extension of {@link SimpleUrlAuthenticationSuccessHandler}, provide several
 * new features.
 * <ul>
 * <li>
 * Save user information in session.</li>
 * <li>
 * Enable different actions when using different ways to submit. If using
 * <b>HTML form</b>, it will use default actions, redirecting to the previous
 * page or a user-defined page. If using <b>Ajax</b>, response will contain a
 * JSON object of result information.</li>
 * <li>
 * Remove login temporary data stored in session.</li>
 * </ul>
 * 
 * @author boreas
 * @see SimpleLogoutSuccessHandle
 */
public class SimpleAuthenticationSuccessHandle extends
		SimpleUrlAuthenticationSuccessHandler {

	private static final String SUCCESS_JSON_STRING = Attributes
			.getLoginJsonResult(1, 0, "");

	private RequestCache requestCache = new HttpSessionRequestCache();

	/**
	 * <ol>
	 * <li>Clear temporary attributes in session.</li>
	 * <li>Save user information in session.</li>
	 * <li>Handle the response in the required way. If using Ajax, write
	 * {@code JSON} into {@code response}. If using HTML form, redirect to a
	 * defined page.</li>
	 * </ol>
	 * 
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		clearAuthenticationAttrs(request);
		saveUser(request, authentication);
		if (request.getHeader(Attributes.AJAX_HEADER) == null)
			writeJsonResult(response);
		else
			handle(request, response, authentication);
	}

	/**
	 * If there's a {@code SavedRequest} existed, return the URL saved in it.
	 * Otherwise return
	 * {@link AbstractAuthenticationTargetUrlRequestHandler#determineTargetUrl
	 * super.determineTargetUrl}
	 */
	@Override
	protected String determineTargetUrl(HttpServletRequest request,
			HttpServletResponse response) {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String targetUrl;
		if (savedRequest != null) {
			requestCache.removeRequest(request, response);
			targetUrl = savedRequest.getRedirectUrl();
		} else
			targetUrl = super.determineTargetUrl(request, response);
		return targetUrl;
	}

	private void writeJsonResult(HttpServletResponse response)
			throws IOException {
		response.setContentType(Attributes.JSON_CONTENT_TYPE);
		response.getWriter().print(SUCCESS_JSON_STRING);
	}

	private void clearAuthenticationAttrs(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(Attributes.SESSION_LOGIN_USERNAME);
		session.removeAttribute(Attributes.SESSION_LOGIN_ERR_MSG);
		session.removeAttribute(Attributes.SESSION_LOGIN_ERR_COUNT);
	}

	@SuppressWarnings("rawtypes")
	private void saveUser(HttpServletRequest request,
			Authentication authentication) {
		ExtendUser user = (ExtendUser) authentication.getPrincipal();
		request.getSession().setAttribute(Attributes.LOGIN_USER,
				user.getFullDetail());
	}
}
