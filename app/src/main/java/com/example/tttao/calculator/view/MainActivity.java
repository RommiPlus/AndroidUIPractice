package com.example.tttao.calculator.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import com.example.tttao.calculator.R;
import com.example.tttao.calculator.model.Calculator;
import com.example.tttao.calculator.presenter.CalculatorPresenter;
import com.example.tttao.calculator.task.CalculatorTask;

public class MainActivity extends AppCompatActivity implements CalculatorTask {

    private EditText editText = null;//输入框

    //按钮中的字符
    private final String[] CALCULATOR_PRESENT_ARRAY = new String[]{
            "9", "8", "7", Calculator.ADD,
            "6", "5", "4", Calculator.MINUS,
            "3", "2", "1", Calculator.MULTIPLY,
            "0", CalculatorPresenter.CLEAR, CalculatorPresenter.EQUAL, Calculator.DIVIDE,
    };

    public CalculatorPresenter presenter;

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

        presenter = new CalculatorPresenter(this, this);
    }


    //触发事件
    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            presenter.doCalculate(getItem(adapterView, position));
        }

        private String getItem(AdapterView<?> adapterView, int position) {
            return (String) adapterView.getAdapter().getItem(position);
        }
    }

    @Override
    public void updateOutputResult(String formatResult) {
        //显示内容
        editText.setText(Html.fromHtml(formatResult));
        //设置光标在最后的位置
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void clearEditText() {
        editText.setText("");
    }


}
