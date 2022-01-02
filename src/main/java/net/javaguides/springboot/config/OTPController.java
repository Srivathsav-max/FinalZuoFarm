package net.javaguides.springboot.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;



    @Controller
    public class OTPController{
        @PostMapping("/otp_login")
        public String viewOTPLoginForm(){

            return "otp_login";
        }
    
        
    }

