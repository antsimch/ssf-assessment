package vttp2023.batch3.ssf.frontcontroller.model;

import java.util.Random;

import jakarta.json.JsonObject;

public class Captcha {
    
    private static final int NUM_MIN = 1;

    private static final int NUM_MAX = 50;

    private static final String[] OPERATORS = {
        "+",
        "-",
        "*",
        "/"
    };

    private int num1;

    private int num2;

    private String operator;

    private int result;

    private int answer;

    private String captchaString;

    public Captcha() {
        this.num1 = generateRandomInteger();
        this.num2 = generateRandomInteger();
        this.operator = generateRandomOperator();
        
        if (this.operator.equals("+")) {
            this.result = this.num1 + this.num2;
        } else if (this.operator.equals("-")) {
            this.result = this.num1 - this.num2;
        } else if (this.operator.equals("*")) {
            this.result = this.num1 * this.num2;
        } else if (this.operator.equals("/")) {
            this.result = this.num1 / this.num2;
        }

        this.captchaString = String.format("%d %s %d?",
                this.num1, this.operator, this.num2);
    }

    public static Captcha createCaptchaObject(JsonObject obj) {
        
        Captcha captcha = new Captcha();

        captcha.setNum1(obj.getJsonNumber("num1").intValue());
        captcha.setNum2(obj.getJsonNumber("num2").intValue());
        captcha.setOperator(obj.getString("operator"));
        captcha.setResult(obj.getJsonNumber("result").intValue());

        return captcha;
    }

    public int generateRandomInteger() {
        return new Random().nextInt(NUM_MIN, NUM_MAX + 1);
    }

    public String generateRandomOperator() {
        return OPERATORS[new Random().nextInt(OPERATORS.length)];
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getCaptchaString() {
        return captchaString;
    }

    public void setCaptchaString(String captchaString) {
        this.captchaString = captchaString;
    }   
}
