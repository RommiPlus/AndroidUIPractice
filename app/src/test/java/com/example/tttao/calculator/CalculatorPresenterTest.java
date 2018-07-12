package com.example.tttao.calculator;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorPresenterTest {

    public static final String EXPRESSION = "125x5-234+67x78";
    public static final String[] ARRAY = new String[]{"125", "x", "5", "-", "234", "+", "67", "x", "78"};
    public static final String[] ARRAY_POSTFIX = new String[]{"125", "5", "x", "234", "-", "67", "78", "x", "+"};
    public static final String[] ERROR_ARRAY_POSTFIX = new String[]{"125", "5", "*", "234", "-", "67", "78", "x", "+"};
    public static final String FORMAT_ERROR = "格式错误";
    public static final String[] ARRAY_FORMAT_ERROR = new String[]{"格式错误"};
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
    public void splitExpression2Elements() {
        Assert.assertEquals(Arrays.toString(ARRAY), Arrays.toString(presenter.splitExpression2Elements(EXPRESSION)));
    }

    @Test
    public void convertToPostfixExpression_normalCaseReturnRight() {
        Assert.assertEquals(Arrays.toString(ARRAY_POSTFIX), Arrays.toString(presenter.convertToPostfixExpression(ARRAY)));
    }

    @Test
    public void calculatePostfixExpression_normalCaseReturnRight() {
        Assert.assertEquals(RESULT, presenter.calculatePostfixExpression(ARRAY_POSTFIX));
    }

    @Test
    public void calculatePostfixExpression_divideZeroReturnError() {
        Assert.assertEquals(DIVIDE_NOT_ZERO, presenter.calculatePostfixExpression(DIVIDE_ZERO));
    }

    @Test
    public void isLegalExpressionTest_EmptyStringReturnFalse() {
        Assert.assertFalse(presenter.isLegalExpression(""));
    }

    @Test
    public void isLegalExpressionTest_startWithOperatorReturnFalse() {
        Assert.assertFalse(presenter.isLegalExpression("/5"));
    }

    @Test
    public void isLegalExpressionTest_MultiPleOperatorReturnFalse() {
        Assert.assertFalse(presenter.isLegalExpression("5////3"));
    }

    @Test
    public void isLegalExpressionTest_endWithOperatorReturnFalse() {
        Assert.assertFalse(presenter.isLegalExpression("5/-"));
        Assert.assertFalse(presenter.isLegalExpression("5/"));
    }

    @Test
    public void isLegalExpressionTest_divideZeroReturnTrue() {
        Assert.assertTrue(presenter.isLegalExpression("5/0"));
    }

    @Test
    public void isLegalExpressionTest_normalExpressionReturnTrue() {
        Assert.assertTrue(presenter.isLegalExpression("5/1"));
        Assert.assertTrue(presenter.isLegalExpression("523+235x6-123x1"));
        Assert.assertTrue(presenter.isLegalExpression("523/2-259+8x78"));
    }

}
