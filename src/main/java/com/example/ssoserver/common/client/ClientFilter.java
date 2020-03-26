package com.example.ssoserver.common.client;

import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Filter基类
 * 
 * @author Joe
 */
public abstract class ClientFilter extends ParamFilter implements Filter {
	
	public abstract boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response)
			throws IOException;

	protected boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	protected void responseJson(HttpServletResponse response, int code, String message) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.OK.value());
		PrintWriter writer = response.getWriter();
		writer.write(new StringBuilder().append("{\"code\":").append(code).append(",\"message\":\"").append(message)
				.append("\"}").toString());
		writer.flush();
		writer.close();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException {
	}

	@Override
	public void destroy() {
	}
}