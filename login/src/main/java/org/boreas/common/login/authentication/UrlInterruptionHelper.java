package org.boreas.common.login.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * A utility class for URL interruption.<br>
 * <p>
 * Enable configure interrupt-urls with a {@code Map} bean instead of
 * security:interrupt-url tag.
 * </p>
 * <p>
 * Use default SpringEL expression {@code hasRole} to define the role hierarchy
 * so that user can define the least principles without repeated {@code hasRole}
 * expressions.
 * </p>
 * 
 * @author boreas
 *
 */
public class UrlInterruptionHelper implements DisposableBean {

	private final String HAS_ROLE_EXP = "hasRole('%s')";

	/**
	 * 
	 * @param filterChainProxy
	 *            Spring security的默认过滤器
	 * @param metadataSource
	 *            包含Ant模式链接和最低权限的Map
	 * @param expressionHandler
	 *            为了方便直接注入，而不是从FilterSecurityInterceptor中获取
	 */
	public UrlInterruptionHelper(FilterChainProxy filterChainProxy,
			Map<String, String> metadataSource,
			SecurityExpressionHandler<FilterInvocation> expressionHandler) {
		SecurityFilterChain filterChain = filterChainProxy.getFilterChains()
				.get(0);
		for (Filter filter : filterChain.getFilters()) {
			if (filter instanceof FilterSecurityInterceptor) {
				FilterSecurityInterceptor fsi = (FilterSecurityInterceptor) filter;
				FilterInvocationSecurityMetadataSource newSource = buildMetadataResource(
						metadataSource, expressionHandler);
				fsi.setSecurityMetadataSource(newSource);
				break;
			}
		}
	}

	protected FilterInvocationSecurityMetadataSource buildMetadataResource(
			Map<String, String> metadataSource,
			SecurityExpressionHandler<FilterInvocation> expressionHandler) {
		int size = metadataSource.size();
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>(
				size);
		Map<String, RequestMatcher> matchers = new HashMap<>(size);
		for (String pattern : metadataSource.keySet()) {
			RequestMatcher matcher;
			Collection<ConfigAttribute> configs;
			if (matchers.containsKey(pattern))
				matcher = matchers.get(pattern);
			else {
				matcher = new AntPathRequestMatcher(pattern);
				matchers.put(pattern, matcher);
			}
			if (requestMap.containsKey(matcher))
				configs = requestMap.get(matcher);
			else {
				configs = new ArrayList<>(size);
				requestMap.put(matcher, configs);
			}
			String config = String.format(HAS_ROLE_EXP,
					metadataSource.get(pattern));
			configs.add(new SecurityConfig(config));
		}
		return new ExpressionBasedFilterInvocationSecurityMetadataSource(
				requestMap, expressionHandler);
	}

	@Override
	public void destroy() throws Exception {
	}

}
