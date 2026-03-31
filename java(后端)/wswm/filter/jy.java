package wswm.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class jy implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest qq = (HttpServletRequest) servletRequest;
        HttpServletResponse xy = (HttpServletResponse) servletResponse;
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
