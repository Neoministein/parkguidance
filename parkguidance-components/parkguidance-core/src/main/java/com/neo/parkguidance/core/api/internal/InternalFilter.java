package com.neo.parkguidance.core.api.internal;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class InternalFilter extends HttpFilter {

    private Set<String> localAddresses = new HashSet<>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            localAddresses.add(InetAddress.getLocalHost().getHostAddress());
            for (InetAddress inetAddress : InetAddress.getAllByName("localhost")) {
                localAddresses.add(inetAddress.getHostAddress());
            }
        } catch (IOException e) {
            throw new ServletException("Unable to lookup local addresses");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (localAddresses.contains(request.getRemoteAddr())) {
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {
        //Need to be implemented, but there no use in the context
    }
}
