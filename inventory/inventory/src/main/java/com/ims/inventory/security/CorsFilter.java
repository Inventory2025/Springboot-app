package com.ims.inventory.security;

import com.ims.inventory.constants.ImsConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {
    static final String ORIGIN = "Origin";

    static final String ORIGIN_VALUE = "http://localhost:4200";

	 /* @Value("${origin.value}")
		private String originValue;	*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String origin = request.getHeader(ORIGIN);

        System.out.println("originValue--------"+origin);

        response.setHeader("Access-Control-Allow-Origin", origin); // originValue //* or origin as u prefer
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers","*"); //authtoken,exprationmessage,authkey
        response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET");//, OPTIONS, DELETE
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(ImsConstants.SC_OK);
        }
        chain.doFilter(request, response);

    }
}
