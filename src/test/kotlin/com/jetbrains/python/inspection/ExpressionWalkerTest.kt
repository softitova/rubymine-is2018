package com.jetbrains.python.inspection

/**
 * Created by Sophia Titova on 22.04.18.
 */
import org.junit.Assert.*
import org.junit.Test

class ExpressionWalkerTest {

    @Test
    fun baseTest() {
        assertTrue(ExpressionWalker.walk("True")!!)
        assertFalse(ExpressionWalker.walk("False")!!)
    }

    @Test
    fun eqOperatorTest() {
        assertTrue(ExpressionWalker.walk("1 == 1")!!)
        assertTrue(ExpressionWalker.walk("1 + 1 == 1 + 2 - 1")!!)
        assertFalse(ExpressionWalker.walk("1 + 1 + 1 * 2 == 1 + 2 - 1")!!)
    }

    @Test
    fun notEqOperatorTest() {
        assertTrue(ExpressionWalker.walk("1 != 2")!!)
        assertFalse(ExpressionWalker.walk("1 != 1")!!)
        assertFalse(ExpressionWalker.walk("1 + 1 != 1 + 2 - 1")!!)
        assertTrue(ExpressionWalker.walk("1 + 1 + 1 * 2 != 1 + 2 - 1")!!)
    }

    @Test
    fun gOperatorTest() {
        assertTrue(ExpressionWalker.walk("1 > -1")!!)
        assertFalse(ExpressionWalker.walk("(1 + 1) / 2 > 1 + 2 - 1")!!)
        assertFalse(ExpressionWalker.walk("1 + 4 - 10 * 2 > 1 + 2 - 3 - 14 + (-1)")!!)
    }

    @Test
    fun gEOperatorTest() {
        assertTrue(ExpressionWalker.walk("1 >= -1")!!)
        assertTrue(ExpressionWalker.walk("1 + 4 - 10 * 2 >= 1 + 2 - 3 - 14 + (-1)")!!)
        assertFalse(ExpressionWalker.walk("(1 + 1) / 2 >= 1 + 2 - 1")!!)
    }

    @Test
    fun sOperatorTest() {
        assertTrue(ExpressionWalker.walk("(1 + 1) / 2 < 1 + 2 - 1")!!)
        assertFalse(ExpressionWalker.walk("1 < -1")!!)
        assertFalse(ExpressionWalker.walk("1 + 4 - 10 * 2 < 1 + 2 - 3 - 14 + (-1)")!!)
    }

    @Test
    fun sEOperatorTest() {
        assertTrue(ExpressionWalker.walk("(1 + 1) / 2 <= 1 + 2 - 1")!!)
        assertTrue(ExpressionWalker.walk("1 + 4 - 10 * 2 <= 1 + 2 - 3 - 14 + (-1)")!!)
        assertFalse(ExpressionWalker.walk("1 <= -1")!!)
    }

    @Test
    fun undefinedConditionValueTest() {
        assertNull(ExpressionWalker.walk("(1 + 1) + a / 2 < 1 + 2 - 1"))
    }

    @Test
    fun binaryOperationsTest() {
        assertTrue(ExpressionWalker.walk("True and True")!!)
        assertTrue(ExpressionWalker.walk("True or False")!!)
        assertTrue(ExpressionWalker.walk("True and (True or False)")!!)
        assertTrue(ExpressionWalker.walk("True and (1 - 3 == -2)")!!)
        assertTrue(ExpressionWalker.walk("True and not (1 < -1)")!!)
        assertFalse(ExpressionWalker.walk("True and False")!!)
        assertFalse(ExpressionWalker.walk("False or False")!!)
        assertFalse(ExpressionWalker.walk("True and (1 != 1)")!!)
        assertFalse(ExpressionWalker.walk("not False and False")!!)
        assertFalse(ExpressionWalker.walk("not (1 <= -1 or 5 == 7 or 2 * 3 == 6)")!!)
    }

    @Test
    fun advancedExpressionsTest() {
        assertTrue(ExpressionWalker.walk("1 - 1 ==  1 - 1 + 2 * 4 - 3 * 3 + 1")!!)
    }
}

