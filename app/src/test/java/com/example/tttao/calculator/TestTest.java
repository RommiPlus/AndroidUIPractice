package com.example.tttao.calculator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestTest {

    @Test
    public void testTest() {
        Assert.assertEquals("[123,+,675,*,7,-,88,*,3,-,1]", Modify.test());
    }
}
