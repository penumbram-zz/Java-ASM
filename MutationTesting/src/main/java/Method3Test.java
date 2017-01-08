import static org.junit.Assert.*;

import org.junit.Test;

public class Method3Test {

	@Test
	public void test() {
		Main main = new Main();
		
		boolean result = main.Method3();
		assertEquals(false, result);
	}

}
