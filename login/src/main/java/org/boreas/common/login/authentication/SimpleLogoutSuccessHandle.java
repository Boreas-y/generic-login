package org.boreas.common.login.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import org.boreas.common.login.Attributes;

/**
 * Remove user information from session while logging out.
 * 
 * @author boreas
 * @see SimpleAuthenticationSuccessHandle
 */
public class SimpleLogoutSuccessHandle extends SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		session.removeAttribute(Attributes.LOGIN_USER);
		super.handle(request, response, authentication);
	}

}
