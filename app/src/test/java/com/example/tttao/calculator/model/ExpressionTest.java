package com.example.tttao.calculator.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static com.example.tttao.calculator.presenter.CalculatorPresenterTest.ARRAY_POSTFIX;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

    public static final String EXPRESSION = "125x5-234+67x78";
    public static final String[] ARRAY = new String[]{"125", "x", "5", "-", "234", "+", "67", "x", "78"};

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression();
    }

    @Test
    public void splitExpression2Elements() {
        Assert.assertEquals(Arrays.toString(ARRAY), Arrays.toString(expression.splitExpression2Elements(EXPRESSION)));
    }

    @Test
    public void convertToPostfixExpression_normalCaseReturnRight() {
        Assert.assertEquals(Arrays.toString(ARRAY_POSTFIX), Arrays.toString(expression.convertToPostfixExpression(ARRAY)));
    }

    @Test
    public void isLegalExpressionTest_EmptyStringReturnFalse() {
        Assert.assertFalse(expression.isLegalExpression(""));
    }

    @Test
    public void isLegalExpressionTest_startWithOperatorReturnFalse() {
        Assert.assertFalse(expression.isLegalExpression("/5"));
    }

    @Test
    public void isLegalExpressionTest_MultiPleOperatorReturnFalse() {
        Assert.assertFalse(expression.isLegalExpression("5////3"));
    }

    @Test
    public void isLegalExpressionTest_endWithOperatorReturnFalse() {
        Assert.assertFalse(expression.isLegalExpression("5/-"));
        Assert.assertFalse(expression.isLegalExpression("5/"));
    }

    @Test
    public void isLegalExpressionTest_divideZeroReturnTrue() {
        Assert.assertTrue(expression.isLegalExpression("5/0"));
    }

    @Test
    public void isLegalExpressionTest_normalExpressionReturnTrue() {
        Assert.assertTrue(expression.isLegalExpression("5/1"));
        Assert.assertTrue(expression.isLegalExpression("523+235x6-123x1"));
        Assert.assertTrue(expression.isLegalExpression("523/2-259+8x78"));
    }
}
