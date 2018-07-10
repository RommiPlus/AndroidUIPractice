package com.example.tttao.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private EditText editText = null;//输入框

    private String doneExpression = "";//已经完成的表达式
    private String toDoExpression = "";//待计算的表达式
    private boolean isUpdateDoneExpression = false;//判断是否执行完，如果执行完需要加换行符号

    //按钮中的字符
    private final String[] CALCULATOR_PRESENT_ARRAY = new String[]{
            "9", "8", "7", CalculatorModel.ADD,
            "6", "5", "4", CalculatorModel.MINUS,
            "3", "2", "1", CalculatorModel.MULTIPLY,
            "0", CalculatorModel.CLEAR, CalculatorModel.EQUAL, CalculatorModel.DIVIDE,
    };

    private CalculatorModel model;
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
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CALCULATOR_PRESENT_ARRAY);
        gridView.setAdapter(adapter);

        //触发事件
        gridView.setOnItemClickListener(new OnButtonItemClickListener());

        model = new CalculatorModel(this);
    }


    //触发事件
    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (getItem(adapterView, position)) {
                case CalculatorModel.EQUAL:
                    toDoExpression += CalculatorModel.EQUAL;

                    String result = model.calculateExpression(toDoExpression);
                    if (result != null) {
                        toDoExpression += result;
                        isUpdateDoneExpression = true;
                    } else {
                        isUpdateDoneExpression = false;
                    }

                    updateCalculateResult();
                    break;

                case CalculatorModel.CLEAR:
                    clearData();
                    break;

                default:
                    if (isUpdateDoneExpression) {
                        doneExpression = doneExpression + toDoExpression + CalculatorModel.BR;
                        isUpdateDoneExpression = false;
                    }

                    toDoExpression += getItem(adapterView, position);

                    updateCalculateResult();
                    break;
            }

        }

        private String getItem(AdapterView<?> adapterView, int position) {
            return (String) adapterView.getAdapter().getItem(position);
        }
    }

    private void updateCalculateResult() {
        //显示内容
        editText.setText(Html.fromHtml(CalculatorModel.formatResult(doneExpression, toDoExpression)));
        //设置光标在最后的位置
        editText.setSelection(editText.getText().length());
    }

    private void clearData() {
        doneExpression = "";
        toDoExpression = "";
        editText.setText("");
        isUpdateDoneExpression = false;
    }


}
