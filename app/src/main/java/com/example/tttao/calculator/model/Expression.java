package com.example.tttao.calculator.model;

import java.util.Stack;

public class Expression {

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

    public boolean isCurrentAndNextElementFourOperator(String[] splitString, int i) {
        return isFourOperatorString(splitString[i]) && (i + 1) < splitString.length && isFourOperatorString(splitString[i + 1]);
    }

    public boolean isCurrentAndNextElementNumber(String[] splitString, int i) {
        return isMatchesNumber(splitString[i]) && (i + 1) < splitString.length && isMatchesNumber(splitString[i + 1]);
    }

    public boolean isStartAndEndStringIllegal(String[] splitString) {
        return splitString[0].isEmpty() || !isMatchesNumber(splitString[0]) || isFourOperatorString(splitString[splitString.length - 1]);
    }

    public boolean isLegalExpression(String toDoExpression) {
        String[] splitString = splitExpression2Elements(toDoExpression);
        if (isStartAndEndStringIllegal(splitString)) {
            return false;
        }

        for (int i = 0; i < splitString.length; i++) {
            if (isCurrentAndNextElementNumber(splitString, i))
                return false;

            if (isCurrentAndNextElementFourOperator(splitString, i))
                return false;

            if (!(isMatchesNumber(splitString[i]) || isFourOperatorString(splitString[i])))
                return false;
        }

        return true;
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
            } else if (element.equals(Calculator.ADD) || element.equals(Calculator.MINUS)) {
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
            } else {
                stack.push(element);
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
     * @param text
     * @return
     */
    public boolean isFourOperatorString(String text) {
        return text.equals(Calculator.ADD) || text.equals(Calculator.MINUS) || text.equals(Calculator.MULTIPLY) || text.equals(Calculator.DIVIDE);
    }

    public boolean isMatchesNumber(String current) {
        return current.matches("\\d+");
    }
}
