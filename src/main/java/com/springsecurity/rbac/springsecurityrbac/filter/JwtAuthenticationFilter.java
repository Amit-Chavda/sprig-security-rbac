package com.springsecurity.rbac.springsecurityrbac.filter;

import com.springsecurity.rbac.springsecurityrbac.service.UserDetailsServiceImpl;
import com.springsecurity.rbac.springsecurityrbac.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        JwtUtil jwtUtil = new JwtUtil();
        String username = null;
        if (header != null && header.startsWith("Bearer")) {
            String token = header.substring(7);

            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception exception) {
                //SignatureException when different secret key is used
                //JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.
                logger.error(exception.getMessage());
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                throw new ServletException("Token Expired or Invalid token passed...");
            }

        }
        filterChain.doFilter(request, response);
    }

}
