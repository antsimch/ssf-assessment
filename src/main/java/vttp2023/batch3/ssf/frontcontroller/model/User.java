package vttp2023.batch3.ssf.frontcontroller.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class User implements Serializable {

    @NotNull(message = "Please enter your username")
    @NotBlank(message = "Please enter your username")
    @Size(min = 2, message = "Username should be 2 or more characters long")
    private String username;

    @NotNull(message = "Please enter your username")
    @NotBlank(message = "Please enter your username")
    @Size(min = 2, message = "Username should be 2 or more characters long")
    private String password;

    private int loginAttempts = 0;
    
    private boolean authenticated;

    private Captcha captcha;

    public User() {
    }

    public JsonObject toJSONForAuth() {

        return Json.createObjectBuilder()
                .add("username", this.getUsername())
                .add("password", this.getPassword())
                .build();
    }

    public static User createUserObject(String jsonString) throws IOException {

        User user = new User();

        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            
            JsonReader reader = Json.createReader(is);
            JsonObject obj = reader.readObject();

            user.setUsername(obj.getString("username"));
            user.setPassword(obj.getString("password"));
            user.setLoginAttempts(obj.getJsonNumber("loginAttempts").intValue());
            user.setAuthenticated(obj.getBoolean("authenticated"));
            user.setCaptcha(Captcha.createCaptchaObject(obj.getJsonObject("captcha")));
        }

        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    } 
}
