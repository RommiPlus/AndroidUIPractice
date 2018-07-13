package com.example.tttao.calculator.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.tttao.calculator.CalculatorTask;
import com.example.tttao.calculator.R;
import com.example.tttao.calculator.model.Calculator;
import com.example.tttao.calculator.model.Expression;

import java.util.LinkedList;

public class CalculatorPresenter {
    public static final String FONT_COLOR_858585 = "<font color='#858585'>";
    public static final String FONT = "</font>";
    public static final String FONT_COLOR_CD2626 = "<font color='#CD2626'>";
    public static final String BR = "<br>";
    public static final String CLEAR = "clear";
    public static final String EQUAL = "=";

    private String doneExpression = "";//已经完成的表达式
    private String toDoExpression = "";//待计算的表达式

    private Context context;
    private CalculatorTask mTask;
    private Expression mExpression;
    private Calculator mCalculator;

    public CalculatorPresenter(Context context, CalculatorTask task) {
        this.context = context;
        this.mTask = task;
        mExpression = new Expression();
        mCalculator = new Calculator();
    }

    public void clearData() {
        doneExpression = "";
        clearToDoExpression();
        mTask.clearEditText();
    }

    public void doCalculate(String item) {
        switch (item) {
            case EQUAL:
                String result = calculate();
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
     * 输出表达式的值
     */
    public String calculate() {
        if (mExpression.isLegalExpression(toDoExpression)) {
            String[] splitString = mExpression.splitExpression2Elements(toDoExpression);
            String[] postfixExpression = mExpression.convertToPostfixExpression(splitString);
            return calculatePostfixExpression(postfixExpression);
        }
        return context.getString(R.string.format_error);
    }

    /**
     * 根据后缀表达式计算结果
     *
     * @param postfixExpressionArray
     */
    public String calculatePostfixExpression(String[] postfixExpressionArray) {
        LinkedList<String> list = new LinkedList<>();
        for (String op : postfixExpressionArray) {
            if (!mExpression.isFourOperatorString(op) || list.isEmpty()) {
                list.push(op);
            } else {
                // 遇到运算符就对栈顶的两个数字运算
                double num1 = getNumber(list);
                if (mCalculator.isDivideZero(Calculator.DIVIDE, num1)) return context.getString(R.string.deivice_not_zero);
                list.push(String.valueOf(mCalculator.calculate(getNumber(list), num1, op)));
            }
        }

        return list.pop();
    }

    public Double getNumber(LinkedList<String> list) {
        return Double.valueOf(list.pop());
    }

    @NonNull
    public String formatResult() {
        return FONT_COLOR_858585 + doneExpression + FONT + FONT_COLOR_CD2626 + toDoExpression + FONT;
    }

}
