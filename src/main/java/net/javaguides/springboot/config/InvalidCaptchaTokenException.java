package net.javaguides.springboot.config;

public class InvalidCaptchaTokenException extends Exception{
    public InvalidCaptchaTokenException(String message) {
        super(message);
    }

}
