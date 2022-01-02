package net.javaguides.springboot.config;

import com.fasterxml.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReCaptchaResponse {
    private boolean success;
    private String hostname;
    private String action;
    private String score;
    private String challenge_ts;

    @JsonProperty("error-codes")
    private String[] errorCodes;

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getChallenge_ts() {
        return challenge_ts;
    }
    public void setChallenge_ts(String challenge_ts) {
        this.challenge_ts = challenge_ts;
    }
    public String[] getErrorCodes() {
        return errorCodes;
    }
    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }

    
}
