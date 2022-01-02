package net.javaguides.springboot.config;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import net.javaguides.springboot.service.UserServiceImpl;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Repository;

@Component
public class BeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserServiceImpl userServiceImpl;

    public BeforeAuthenticationFilter() {
        super.setUsernameParameter("username");
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
    }
    @Override
    
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String email = request.getParameter("username");
        System.out.println("attemptAuthentication: "+ email);
        User user = userRepository.findByEmail(email);
        if(user != null){
            float spamScore = getGoogleReCaptchaScore();
            if (spamScore < 0.5) {
                if(user.isOTPRequired()){
                    return super.attemptAuthentication(request, response);
                }
                try {
                    UserServiceImpl.generateOneTimePassword(user);
                    throw new InsufficientAuthenticationException("OTP");
                } catch (MessagingException | UnsupportedEncodingException ex) {
                    throw new AuthenticationServiceException(
                                "Error while sending OTP email.");
                }
                 
            }
        }

        return super.attemptAuthentication(request, response);
    }
    private float getGoogleReCaptchaScore(){
        return 0.9f;
    }
    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }
    @Autowired
    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }
    

    


}
