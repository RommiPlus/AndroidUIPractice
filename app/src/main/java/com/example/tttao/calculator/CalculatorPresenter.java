package com.example.tttao.calculator;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Stack;

public class CalculatorPresenter {
    public static final String FONT_COLOR_858585 = "<font color='#858585'>";
    public static final String FONT = "</font>";
    public static final String FONT_COLOR_CD2626 = "<font color='#CD2626'>";
    public static final String BR = "<br>";
    public static final String CLEAR = "clear";
    public static final String EQUAL = "=";
    public static final String ADD = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "x";
    public static final String DIVIDE = "/";

    private String doneExpression = "";//已经完成的表达式
    private String toDoExpression = "";//待计算的表达式

    private Context context;
    private CalculatorTask mTask;

    public CalculatorPresenter(Context context, CalculatorTask task) {
        this.context = context;
        this.mTask = task;
    }

    public void clearData() {
        doneExpression = "";
        clearToDoExpression();
        mTask.clearEditText();
    }

    public void doCalculate(String item) {
        switch (item) {
            case EQUAL:
                String result = calculateExpression();
                updateTodoExpression(EQUAL + result);
                mTask.updateOutputResult(formatResult());
                updateDoneExpression();
                clearToDoExpression();
                break;

            case CLEAR:
                clearData();
                break;

            default:
                updateTodoExpression(item);
                mTask.updateOutputResult(formatResult());
                break;
        }
    }

    private void clearToDoExpression() {
        toDoExpression = "";
    }

    private void updateTodoExpression(String resultExpression) {
        toDoExpression += resultExpression;
    }

    public void updateDoneExpression() {
        doneExpression = doneExpression + toDoExpression + BR;
    }

    /**
     * convert Expression to single element.<br><br>
     * for example:
     * 123+5*2-6 -> {"123", "+", "5", "*", "2", "-", "6"}
     *
     * @param expression
     * @return
     */
    public String[] splitExpression2Elements(String expression) {
        return expression.split("((?<=[^0-9])|(?=[^0-9]))");
    }

    /**
     * 输出表达式的值
     *
     */
    public String calculateExpression() {
        String[] splitString = splitExpression2Elements(toDoExpression);
        String[] postfixExpression = convertToPostfixExpression(splitString);
        return calculatePostfixExpression(postfixExpression);
    }

    /**
     * 中缀表达式转换为后缀表达式
     *
     * @param array
     */
    public String[] convertToPostfixExpression(String[] array) {
        int postfixIndex = 0;
        String postfix[] = new String[array.length];
        Stack<String> stack = new Stack<>();

        for (String element : array) {
            if (isMatchesNumber(element)) {
                postfix[postfixIndex] = element;
                postfixIndex++;
            } else if (element.equals(ADD) || element.equals(MINUS)) {
                if (stack.isEmpty()) {
                    stack.push(element);
                } else {
                    //弹出优先级高于加减的运算符(因为加减运算符优先级最低)
                    while (!stack.isEmpty()) {
                        String top = stack.peek();
                        stack.pop();
                        postfix[postfixIndex] = top;
                        postfixIndex++;
                    }
                    stack.push(element);
                }
            } else if (element.equals(MULTIPLY) || element.equals(DIVIDE)) {
                stack.push(element);
            } else {
                return new String[]{context.getString(R.string.format_error)};
            }
        }

        //字符遍历完后将栈中剩余的字符出栈
        while (!stack.isEmpty()) {
            postfix[postfixIndex] = stack.peek();
            postfixIndex++;
            stack.pop();
        }

        return postfix;
    }

    /**
     * 根据后缀表达式计算结果
     *
     * @param postfixExpressionArray
     */
    public String calculatePostfixExpression(String[] postfixExpressionArray) {
        LinkedList<String> list = new LinkedList<>();
        for (String op : postfixExpressionArray) {
            if (!isFourOperatorString(op) || list.isEmpty()) {
                list.push(op);
            } else {
                // 遇到运算符就对栈顶的两个数字运算
                double num1 = getNumber(list);
                if (isDivideZero(op, num1)) return context.getString(R.string.deivice_not_zero);
                list.push(String.valueOf(calculate(getNumber(list), num1, op)));
            }
        }

        if (!list.isEmpty()) {
            return list.pop();
        }

        return null;
    }

    public Double getNumber(LinkedList<String> list) {
        return Double.valueOf(list.pop());
    }

    public boolean isDivideZero(String s, double divisor) {
        return s.equals(DIVIDE) && divisor == 0;
    }

    public boolean isMatchesNumber(String current) {
        return current.matches("\\d+");
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

    @NonNull
    public String formatResult() {
        return FONT_COLOR_858585 + doneExpression + FONT + FONT_COLOR_CD2626 + toDoExpression + FONT;
    }

    /**
     * @param text
     * @return
     */
    public boolean isFourOperatorString(String text) {
        return text.equals(ADD) || text.equals(MINUS) || text.equals(MULTIPLY) || text.equals(DIVIDE);
    }
}
