package com.example.tttao.calculator.model;

public class Calculator {

    public static final String ADD = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "x";
    public static final String DIVIDE = "/";

    public double divide(double num1, double num2) {
        return num2 / num1;
    }

    public double multiply(double num2, double num1) {
        return num2 * num1;
    }

    public double minus(double num2, double num1) {
        return num2 - num1;
    }

    public double add(double num2, double num1) {
        return num2 + num1;
    }

    public double calculate(double num2, double num1, String op) {
        switch (op) {
            case ADD:
                return add(num2, num1);
            case MINUS:
                return minus(num2, num1);
            case MULTIPLY:
                return multiply(num2, num1);
            case DIVIDE:
                return divide(num1, multiply(num2, 1.0));
            default:
                return 0;
        }
    }

    public boolean isDivideZero(String s, double divisor) {
        return s.equals(DIVIDE) && divisor == 0;
    }
}
