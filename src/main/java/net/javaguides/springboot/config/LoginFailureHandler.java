package net.javaguides.springboot.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
     
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");

        System.out.println("onAuthenticationFailure: " + email); 
        String failureRedirectUrl = "/login?error&email="+email;
        if(exception .getMessage().contains("OTP")){
            failureRedirectUrl = "/login?otp=true&email=" + email;
        }
        super.setDefaultFailureUrl(failureRedirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }
 
}