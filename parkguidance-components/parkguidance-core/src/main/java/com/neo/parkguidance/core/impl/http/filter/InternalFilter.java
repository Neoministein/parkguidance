package com.neo.parkguidance.core.impl.http.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * This filter checks if the request IP is internal call such as
 * - localhost
 * - 127.0.0.1
 * - 0:0:0:0:0:0:0:1
 * - The current IP of the machine
 */
public class InternalFilter extends HttpFilter {

    private static final Logger LOGGER = LogManager.getLogger(InternalFilter.class);

    private final Set<String> localAddresses = new HashSet<>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            localAddresses.add(InetAddress.getLocalHost().getHostAddress());
            for (InetAddress inetAddress : InetAddress.getAllByName("localhost")) {
                localAddresses.add(inetAddress.getHostAddress());
            }
        } catch (IOException e) {
            LOGGER.fatal("Unable to lookup local addresses");
            throw new ServletException("Unable to lookup local addresses");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        LOGGER.debug("Checking if HTTP request IP {} is internal", request.getRemoteAddr());
        if (localAddresses.contains(request.getRemoteAddr())) {
            LOGGER.trace("Address is local");
            chain.doFilter(request,response);
        }
        LOGGER.warn("A non local address {} has tried to access a internal only Servlet", request.getRemoteAddr());
    }

    @Override
    public void destroy() {
        //Need to be implemented, but there no use in the context
    }
}
