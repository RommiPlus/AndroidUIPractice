package com.example.tttao.calculator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
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

    private EditText editText = null;//输入框

    private String doneString = "";//已经完成的表达式
    private String toDoString = "";//待计算的表达式
    private boolean isExecuteNow = false;//判断是否执行完，如果执行完需要加换行符号

    //按钮中的字符
    private final String[] buttons = new String[]{
            "9", "8", "7", ADD,
            "6", "5", "4", MINUS,
            "3", "2", "1", MULTIPLY,
            "0", CLEAR, EQUAL, DIVIDE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        GridView gridView = findViewById(R.id.grid_buttons);
        editText = findViewById(R.id.edit_input);

        //禁止editText从键盘输入
        editText.setKeyListener(null);

        //设置适配器
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, buttons);
        gridView.setAdapter(adapter);

        //触发事件
        gridView.setOnItemClickListener(new OnButtonItemClickListener());
    }

    @NonNull
    private String formatResult() {
        return FONT_COLOR_858585 + doneString + FONT + FONT_COLOR_CD2626 + toDoString + FONT;
    }

    /**
     * 输出表达式的值
     */
    private void ExecuteExpression() {
        String result = null;
        try {
            result = calculatePost(convertToPostfixExpression(toDoString));
        } catch (Exception e) {
            isExecuteNow = false;
        }
        toDoString += result;
        //显示内容
        editText.setText(Html.fromHtml(formatResult()));
        //设置光标在最后的位置
        editText.setSelection(editText.getText().length());
        isExecuteNow = true;
    }

    /**
     * 中缀表达式转换为后缀表达式
     *
     * @param expression
     */
    private String[] convertToPostfixExpression(String expression) {
        int infixIndex = 0;
        int postfixIndex = 0;
        String postfix[] = new String[expression.length()];
        postfix[0] = "";
        Stack<String> stack = new Stack<>();
        String top;//栈顶元素
        String current;//当前元素
        String next = "";//当前元素的下一个元素
        int isMul = 0;//判断是否是复数
        while (!next.equals(EQUAL)) {
            current = String.valueOf(expression.charAt(infixIndex));
            next = String.valueOf(expression.charAt(infixIndex + 1));
            if (isMatchesNumberOrDot(current) && isMatchesNumberOrDot(next)) {
                // 如果是数字，直接添加进字符中
                // 如果前下一个字符也是数字，把他们并到一起
                if (isMul == 0) {
                    postfix[postfixIndex] = postfix[postfixIndex] + current.concat(next);
                } else {
                    postfix[postfixIndex] = postfix[postfixIndex].concat(next);
                }
                isMul = 1;
            } else if (isMatchesNumberOrDot(current) && isNotNumberAndDot(next) && isMul == 0) {
                postfix[postfixIndex] = current;
                postfixIndex++;
                postfix[postfixIndex] = "";
            } else if (isMatchesNumberOrDot(current) && isNotNumberAndDot(next) && isMul == 1) {
                isMul = 0;
                postfixIndex++;
                postfix[postfixIndex] = "";
            } else if (current.equals(ADD) || current.equals(MINUS)) {
                //如果是加减符号
                if (stack.isEmpty()) {
                    //空栈就直接入栈
                    stack.push(current);
                } else {
                    //弹出优先级高于加减的运算符(因为加减运算符优先级最低)
                    while (!stack.isEmpty()) {
                        top = stack.peek();
                        stack.pop();
                        postfix[postfixIndex] = top;
                        postfixIndex++;
                        postfix[postfixIndex] = "";
                    }
                    stack.push(current);
                }
            } else if (current.equals(MULTIPLY) || current.equals(DIVIDE)) {
                //如果是乘除、左括号，优先级高，直接入栈
                stack.push(current);
            } else {
                return new String[]{"格式错误"};
            }
            infixIndex++;
        }

        //字符遍历完后将栈中剩余的字符出栈
        while (!stack.isEmpty()) {
            postfix[postfixIndex] = stack.peek();
            postfixIndex++;
            stack.pop();
        }

        return postfix;
    }

    private boolean isNotNumberAndDot(String next) {
        return next.matches("[^\\d.]");
    }

    private boolean isMatchesNumberOrDot(String current) {
        return current.matches("[\\d.]");
    }


    /**
     * 根据后缀表达式计算结果
     *
     * @param post
     */
    private String calculatePost(String post[]) {
        LinkedList<String> list = new LinkedList<>();
        for (String s : post) {
            if (TextUtils.isEmpty(s)) continue;

            // 遇到数字就入栈
            if (!isFourOperatorString(s) || list.isEmpty()) {
                list.push(s);
            } else {
                // 遇到运算符就对栈顶的两个数字运算
                double num1 = Double.valueOf(list.pop());
                if (isDivideZero(s, num1)) {
                    return "除数不能为空";
                }
                list.push(String.valueOf(calculate(Double.valueOf(list.pop()), num1, s)));
            }
        }

        if (!list.isEmpty()) {
            return list.pop();
        }

        return null;
    }

    private boolean isDivideZero(String s, double divisor) {
        return s.equals(DIVIDE) && divisor == 0;
    }

    private static double calculate(double num2, double num1, String op) {
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

    private static double divide(double num1, double num2) {
        return num2 / num1;
    }

    private static double multiply(double num2, double num1) {
        return num2 * num1;
    }

    private static double minus(double num2, double num1) {
        return num2 - num1;
    }

    private static double add(double num2, double num1) {
        return num2 + num1;
    }

    //触发事件
    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (getItem(adapterView, position).equals(EQUAL)) {
                toDoString += EQUAL;
                ExecuteExpression();
            } else if (getItem(adapterView, position).equals(CLEAR)) {
                doneString = "";
                toDoString = "";
                isExecuteNow = false;
                editText.setText("");
            } else {
                if (isExecuteNow) {
                    /*把待计算的表达式加到已计算表达式中，后面再加个换行符
                     * */
                    doneString = doneString + toDoString + BR;
                    isExecuteNow = false;
                    toDoString = getItem(adapterView, position);
                } else {
                    toDoString += getItem(adapterView, position);
                }

                //显示内容
                editText.setText(Html.fromHtml(formatResult()));
                //设置光标在最后的位置
                editText.setSelection(editText.getText().length());
            }

        }

        private String getItem(AdapterView<?> adapterView, int position) {
            return (String) adapterView.getAdapter().getItem(position);
        }
    }

    /**
     * @param text
     * @return
     */
    private boolean isFourOperatorString(String text) {
        return text.equals(ADD) || text.equals(MINUS) || text.equals(MULTIPLY) || text.equals(DIVIDE);
    }


}
