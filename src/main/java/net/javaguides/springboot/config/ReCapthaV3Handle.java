package net.javaguides.springboot.config;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.h2.security.auth.H2AuthConfig;

public class ReCapthaV3Handle {
    private String recaptchaSecretKey = "6LdAQNwdAAAAAP_D6n_gQFgPU1MWIM8F6wGdmlxo" ;
    private String recaptchaServerURL = "https://www.google.com/recaptcha/api/siteverify" ;

    public float verify(String recaptchaFormResponse) throws InvalidCaptchaTokenException {
        System.out.println("ReCaptaV3 Handle called...");
        System.out.println("g-recaptcha-response: " + recaptchaFormResponse);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new HttpHeaders();
        map.add("secret", recaptchaSecretKey);
        map.add("response", recaptchaFormResponse);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        ReCaptchaResponse response = restTemplate.postForObject(recaptchaServerURL, request, ReCaptchaResponse.class);
        System.out.println("Recaptcha Response: \n");
        System.out.println(response.isSuccess());
        System.out.println(response.getAction());
        System.out.println(response.getScore());
        System.out.println(response.getChallenge_ts());
        System.out.println(response.getHostname());
        if(response.getErrorCodes() != null){
            System.out.println("Error Codes: ");
            for(String errorCode : response.getErrorCodes()){
                System.out.println(errorCode);
            }
        }
        
        if(!response.isSuccess()){
            throw new InvalidCaptchaTokenException("Invalid Captcha Token");
        }
        // return 0.4f;
        return Float.parseFloat(response.getScore());
    }
}
