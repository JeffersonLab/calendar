package org.jlab.atlis.calendar.presentation.filter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ryans
 */
@WebFilter(
    filterName = "AuditFilter",
    urlPatterns = {"/*"},
    dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class AuditFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    AuditContext context = new AuditContext();

    String ip = request.getRemoteAddr();

    String xForwardedFor = httpRequest.getHeader("X-Forwarded-For");

    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      String[] ipArray = xForwardedFor.split(",");
      ip = ipArray[0].trim(); // first one, if more than one
    }

    String username = httpRequest.getRemoteUser();

    context.setIp(ip);
    context.setUsername(username);

    AuditContext.setCurrentInstance(context);

    try {
      chain.doFilter(request, response);
    } finally {
      AuditContext.setCurrentInstance(null);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void destroy() {}
}
