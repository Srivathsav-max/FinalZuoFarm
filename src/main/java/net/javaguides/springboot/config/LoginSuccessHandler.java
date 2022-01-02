package net.javaguides.springboot.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.javaguides.springboot.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
         
    @Override
    public void onAuthenticationSuccess (HttpServletRequest request,HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
         
        System.out.println("onAuthenticationSucess" );  
        /*UserDetails userDetails= (UserDetails) authentication.getPrincipal();
         
        User user = UserDetails.getCustomer();
 
        if (user.isOTPRequired()) {
            customerService.clearOTP(user);*/
        super.onAuthenticationSuccess(request, response, authentication);
    }
    
    
}
