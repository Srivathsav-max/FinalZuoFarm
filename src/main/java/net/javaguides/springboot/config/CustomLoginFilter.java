package net.javaguides.springboot.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import java.io.IOException;



public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    public CustomLoginFilter(String loginUrl, String httpMethod){
        setUsernameParameter("username");
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(loginUrl,httpMethod));
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");
                System.out.println("Before processing authentication.......................");
                ReCapthaV3Handle handler = new ReCapthaV3Handle();
                
                try{
                    float score = handler.verify(recaptchaResponse);
                    if(score <0.5){
                        request.getRequestDispatcher("otp_login").forward(request, response);
                    }

                }
                catch(InvalidCaptchaTokenException | ServletException |IOException e1){
                    try{
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e1.getMessage());
                }catch(IOException e){
                    e.printStackTrace();
                    }
                }
                
        return super.attemptAuthentication(request, response);
    }
}
