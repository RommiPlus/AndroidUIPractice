package omron.omron.com.caculatorbyttt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import com.example.tttao.calculator.R;

import java.util.LinkedList;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    private EditText editText = null;//输入框

    private String doneString = "";//已经完成的表达式
    private String toDoString = "";//待计算的表达式
    private boolean isExecuteNow = false;//判断是否执行完，如果执行完需要加换行符号

    //按钮中的字符
    private final String[] buttons = new String[]{
            "9", "8", "7", "+",
            "6", "5", "4", "-",
            "3", "2", "1", "x",
            "0", "clear", "=", "/",
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
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buttons);
        gridView.setAdapter(adapter);
        //触发事件
        gridView.setOnItemClickListener(new OnButtonItemClickListener());
    }

    /**
     * 设置EditText的显示内容，计算完成后换行等
     */
    private void setText() {
        final String[] tags = new String[]{
                "<font color='#858585'>",
                "<font color='#CD2626'>",
                "</font>"
        };
        StringBuilder builder = new StringBuilder();
        builder.append(tags[0]).append(doneString).append(tags[2]);
        builder.append(tags[1]).append(toDoString).append(tags[2]);
        //显示内容
        editText.setText(Html.fromHtml(builder.toString()));
        //设置光标在最后的位置
        editText.setSelection(editText.getText().length());
    }

    //    /**
    //     * 输出表达式的值
    //     */
    //    private void ExcuteExpression() {
    //        double result = 0;
    //        try {
    //            result = execute(toDoString);
    //        } catch (Exception e) {
    //            isExecuteNow = false;
    //            return;
    //        }
    //        toDoString += result;
    //        setText();
    //        isExecuteNow = true;
    //    }

    /**
     * 输出表达式的值
     */
    private void ExcuteExpression() {
        String result = null;
        try {
            result = converExpr(toDoString);
            Log.i("tag", result);
        } catch (Exception e) {
            isExecuteNow = false;
        }
        toDoString += result;
        setText();
        isExecuteNow = true;
    }

    /**
     * 中缀表达式转换为后缀表达式
     *
     * @param expression
     */
    private String converExpr(String expression) {
        int InfixIndex = 0;
        int PostfixIndex = 0;
        String PostStr[] = new String[expression.length()];
        PostStr[0] = "";
        Stack<String> stack = new Stack<String>();
        //char post[] = new char[100];
        String topString;//栈顶元素
        String curString = "";//当前元素
        String NextString = "";//当前元素的下一个元素
        int isMul = 0;//判断是否是复数
        while (!NextString.equals("=")) {
            curString = String.valueOf(expression.charAt(InfixIndex));
            NextString = String.valueOf(expression.charAt(InfixIndex + 1));
            //  如果是数字，直接添加进字符中
            // 如果前下一个字符也是数字，把他们并到一起
            if (curString.matches("[\\d\\.]") && NextString.matches("[\\d\\.]")) {
                if (isMul == 0) {
                    PostStr[PostfixIndex] = PostStr[PostfixIndex] + curString.concat(NextString);
                    Log.i("a", "PostStr[i]" + PostStr[PostfixIndex]);
                } else {
                    PostStr[PostfixIndex] = PostStr[PostfixIndex].concat(NextString);
                    Log.i("a", "PostStr[i]" + PostStr[PostfixIndex]);
                }
                isMul = 1;
                //InfixIndex++;
                //PostfixIndex++;
            } else if (curString.matches("[\\d\\.]") && NextString.matches("[^\\d\\.]") && isMul == 0) {
                PostStr[PostfixIndex] = curString;
                Log.i("a", "PostStr[i]" + PostStr[PostfixIndex]);
                PostfixIndex++;
                PostStr[PostfixIndex] = "";
            } else if (curString.matches("[\\d\\.]") && NextString.matches("[^\\d\\.]") && isMul == 1) {
                isMul = 0;
                PostfixIndex++;
                PostStr[PostfixIndex] = "";
            } else if (curString.equals(")")) {
                //如果是右括号，将栈中其他运算符出栈，添加进字符数组，知道匹配到左括号
                while (!stack.peek().equals("(")) {
                    PostStr[PostfixIndex] = stack.peek();
                    PostfixIndex++;
                    PostStr[PostfixIndex] = "";
                    stack.pop();
                }
                stack.pop();
            } else if (curString.equals("+") || curString.equals("-")) {
                //如果是加减符号
                if (stack.isEmpty())//空栈就直接入栈
                    stack.push(curString);
                else {//弹出优先级高于加减的运算符(因为加减运算符优先级最低)
                    while (!stack.isEmpty()) {
                        topString = stack.peek();
                        stack.pop();
                        if (topString.equals("(")) {
                            stack.push(topString);
                            break;
                        } else {
                            PostStr[PostfixIndex] = topString;
                            PostfixIndex++;
                            PostStr[PostfixIndex] = "";
                        }
                    }
                    stack.push(curString);
                }
            } else if (curString.equals("x") || curString.equals("/") || curString.equals("(")) {
                //如果是乘除、左括号，优先级高，直接入栈
                stack.push(curString);
            } else {
                return "格式错误";
            }
            InfixIndex++;

            Log.i("ss", "PostStr[i]" + PostStr[PostfixIndex]);
        }
        //字符遍历完后将栈中剩余的字符出栈
        while (!stack.isEmpty()) {
            PostStr[PostfixIndex] = stack.peek();
            PostfixIndex++;
            stack.pop();
        }
        return calcuPost(PostStr);

    }


    /**
     * 根据后缀表达式计算结果
     *
     * @param post
     */
    private String calcuPost(String post[]) {

        LinkedList<String> mList = new LinkedList<>();
        for (String s : post) {
            if (!TextUtils.isEmpty(s)) {
                //遇到运算符就对栈顶的两个数字运算
                if (isEqualString(s)) {
                    if (!mList.isEmpty()) {
                        double num1 = Double.valueOf(mList.pop());
                        double num2 = Double.valueOf(mList.pop());
                        if (s.equals("/") && num1 == 0) {
                            return "除数不能为空";
                        }
                        double newNum = cal(num2, num1, s);
                        mList.push(String.valueOf(newNum));
                    }
                } else {
                    //遇到数字就入栈
                    mList.push(s);
                }
            }
        }
        if (!mList.isEmpty()) {
            return mList.pop();
        }
        return null;
    }

    private static double cal(double num2, double num1, String op) {
        switch (op) {
            case "+":
                return num2 + num1;
            case "-":
                return num2 - num1;
            case "x":
                return num2 * num1;
            case "/":
                return num2 * 1.0 / num1;
            default:
                return 0;
        }
    }


    //计算结果(对于两个数字的运算)
    private double execute(String toDoString) {
        if (TextUtils.isEmpty(toDoString)) {
            return 0;
        }
        double res = 0;
        String s1 = toDoString.substring(0, toDoString.indexOf(" "));
        String op = toDoString.substring(toDoString.indexOf(" ") + 1, toDoString.indexOf(" ") + 2);
        String s2 = toDoString.substring(toDoString.indexOf(" ") + 3);
        //两个操作数都不为空时
        if (!s1.equals(" ") && !s2.equals(" ")) {
            double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                res = d1 + d2;
            } else if (op.equals("-")) {
                res = d1 - d2;
            } else if (op.equals("x")) {
                res = d1 * d2;
            } else if (op.equals("/")) {
                if (d2 == 0) res = 0;
                res = d1 / d2;
            }
        } else if (s1.equals("") && !s2.equals("")) {
            //左操作数空，右操作数不为空
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                res = 0 + d2;
            } else if (op.equals("-")) {
                res = 0 - d2;
            } else if (op.equals("x")) {
                res = 0;
            } else if (op.equals("/")) {
                res = 0;
            }
        }
        return res;
    }

    //触发事件
    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String text = (String) adapterView.getAdapter().getItem(position);
            int last = 0;
            if (text.equals("=")) {
                toDoString += "=";
                ExcuteExpression();
            } else if (text.equals("clear")) {
                doneString = "";
                toDoString = "";
                isExecuteNow = false;
                editText.setText("");
            } else {
                if (isExecuteNow) {
                    /*把待计算的表达式加到已计算表达式中，后面再加个换行符
                     * */
                    doneString = doneString + toDoString + "<br>";
                    isExecuteNow = false;
                    toDoString = text;
                } else {
                    toDoString += text;
                }
                setText();
            }

        }
    }

    /**
     * @param text
     * @return
     */
    private boolean isEqualString(String text) {
        return text.equals("+") || text.equals("-") || text.equals("x") || text.equals("/");
    }
}
