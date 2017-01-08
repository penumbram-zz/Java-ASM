import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by tolgacaner on 23/11/16.
 */
public class CalculatorTests {

    @Test
    public void testSub() {
        Calculator calc = new Calculator();
        assertEquals(1, calc.sub(3, 2));
    }

    @Test
    public void testDivide() {
        Calculator calc = new Calculator();

        assertEquals(4, calc.div(8, 2));
    }

    @Test
    public void testMult() {
        Calculator calc = new Calculator();

        assertEquals(10, calc.mult(5, 2));
    }

    @Test
    public void complex() {
        Calculator calc = new Calculator();
        calc.other();
    }

    @Test
    public void testAdd() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(3, 2));
    }


    @Test
    public void uncoveredtest() {
        Calculator calc = new Calculator();
        calc.uncovered();
    }

}
