package org.boreas.common.login.captcha;

import javax.servlet.Filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * A utility class for configure whether using {@code CaptchaFilter} or not
 * because {@code security:custom-filter} tag cannot set disabled for a filter. <br>
 * Current solution is that add {@code CaptchaFilter} into filter chain first.
 * If the configuration indicates that the filter should be disabled, this class
 * will remove the {@code CaptchaFilter} from filter chain.
 * 
 * @author boreas
 *
 */
public class CaptchaFilterHelper implements DisposableBean {

	private static final Log log = LogFactory.getLog(CaptchaFilterHelper.class);

	/**
	 * If the {@code CaptchaFilter} is turned off, remove it from filter chain.
	 * 
	 * @param filterChainProxy
	 * @param on
	 *            whether the {@code CaptchaFilter} is turned on or not
	 */
	public CaptchaFilterHelper(FilterChainProxy filterChainProxy, boolean on) {
		if (!on) {
			SecurityFilterChain filterChain = filterChainProxy
					.getFilterChains().get(0);
			for (Filter filter : filterChain.getFilters()) {
				if (filter instanceof CaptchaFilter) {
					filterChain.getFilters().remove(filter);
					log.info("captchaFilter has been removed from filter chain.");
					break;
				}
			}
		}
	}

	@Override
	public void destroy() throws Exception {
	}

}
