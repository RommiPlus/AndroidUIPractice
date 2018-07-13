package com.example.tttao.calculator.presenter;

import android.content.Context;

import com.example.tttao.calculator.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorPresenterTest {
    public static final String[] ARRAY_POSTFIX = new String[]{"125", "5", "x", "234", "-", "67", "78", "x", "+"};
    public static final String DIVIDE_NOT_ZERO = "除数不能为空";
    public static final String[] DIVIDE_ZERO = {"125", "0", "/"};
    public static final String RESULT = "5617.0";

    CalculatorPresenter presenter;

    @Before
    public void setUp() {
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getString(R.string.deivice_not_zero)).thenReturn(DIVIDE_NOT_ZERO);
        presenter = new CalculatorPresenter(context, null);
    }

    @Test
    public void calculatePostfixExpression_normalCaseReturnRight() {
        Assert.assertEquals(RESULT, presenter.calculatePostfixExpression(ARRAY_POSTFIX));
    }

    @Test
    public void calculatePostfixExpression_divideZeroReturnError() {
        Assert.assertEquals(DIVIDE_NOT_ZERO, presenter.calculatePostfixExpression(DIVIDE_ZERO));
    }



}
