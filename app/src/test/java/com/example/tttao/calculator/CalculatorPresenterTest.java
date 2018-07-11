package com.example.tttao.calculator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorPresenterTest {

    public static final String EXPRESSION = "125*5-234+67*78";
    public static final String[] ARRAY = new String[]{"125", "*", "5", "-", "234", "+", "67", "*", "78"};
    public static final String[] ARRAY_POSTFIX = new String[]{"125", "*", "5", "-", "234", "+", "67", "*", "78"};

    CalculatorPresenter presenter;

    @Before
    public void setUp() {
        presenter = new CalculatorPresenter(null, null);
    }

    @Test
    public void splitExpression2Elements() {
        Assert.assertEquals(Arrays.toString(ARRAY), Arrays.toString(presenter.splitExpression2Elements(EXPRESSION)));
    }

    @Test
    public void convertToPostfixExpression_normalCaseReturnRight() {
        presenter.convertToPostfixExpression(ARRAY_POSTFIX);
    }
}
