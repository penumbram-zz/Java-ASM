import static org.junit.Assert.*;

import org.junit.Test;

public class Method2Test {

	@Test
	public void test() {
		Main main = new Main();
		
		int result = main.Method2();
		assertEquals(8, result);
	}

}
