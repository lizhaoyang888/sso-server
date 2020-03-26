package com.example.ssoserver.common.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.example.ssoserver.common.rpc.AuthenticationRpcService;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Smart容器中心
 * 
 * @author Joe
 */
public class SmartContainer extends ParamFilter implements Filter {
	
	/** 模糊匹配后缀 */
	private static final String URL_FUZZY_MATCH = "/*";

	/** 是否服务端，默认为false */
	private boolean isServer = false;

	/** 忽略URL */
	protected String[] excludeUrls;

	private ClientFilter[] filters;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		if(isServer) {
			ssoServerUrl = filterConfig.getServletContext().getContextPath();
		}
		else if (StringUtils.isEmpty(ssoServerUrl)) {
			throw new IllegalArgumentException("ssoServerUrl不能为空");
		}
		
		String urls = filterConfig.getInitParameter("excludeUrls");
		if (!StringUtils.isEmpty(urls)) {
			excludeUrls = urls.split(",");
		}

		if (authenticationRpcService == null) {
			try {
				authenticationRpcService = (AuthenticationRpcService) new HessianProxyFactory()
						.create(AuthenticationRpcService.class, ssoServerUrl + "/rpc/authenticationRpcService");
			}
			catch (MalformedURLException e) {
				throw new IllegalArgumentException("authenticationRpcService初始化失败");
			}
		}

		if (filters == null || filters.length == 0) {
			throw new IllegalArgumentException("filters不能为空");
		}
		for (ClientFilter filter : filters) {
			filter.setSsoServerUrl(ssoServerUrl);
			filter.setAuthenticationRpcService(authenticationRpcService);

			filter.init(filterConfig);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (isExcludeUrl(httpRequest.getServletPath())) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		for (ClientFilter filter : filters) {
			if (!filter.isAccessAllowed(httpRequest, httpResponse)) {
				return;
			}
		}
		chain.doFilter(request, response);
	}
	
	private boolean isExcludeUrl(String url) {
		if (excludeUrls == null || excludeUrls.length < 1)
			return false;

		Map<Boolean, List<String>> map = Arrays.stream(excludeUrls)
				.collect(Collectors.partitioningBy(u -> u.endsWith(URL_FUZZY_MATCH)));
		List<String> urlList = map.get(false);
		if (urlList.contains(url)) { // 优先精确匹配
			return true;
		}
		urlList = map.get(true);
		for (String matchUrl : urlList) { // 再进行模糊匹配
			if (url.startsWith(matchUrl.replace(URL_FUZZY_MATCH, ""))) {
				return true;
			}
		}
		return false;
	}
	
	public void setIsServer(boolean isServer) {
		this.isServer = isServer;
	}

	@Override
	public void destroy() {
		if (filters == null || filters.length == 0)
			return;
		for (ClientFilter filter : filters) {
			filter.destroy();
		}
	}

	public void setFilters(ClientFilter[] filters) {
		this.filters = filters;
	}

	public ClientFilter[] getFilters() {
		return filters;
	}
}