import static org.junit.Assert.*;

import org.junit.Test;

public class Method1Test {

	@Test
	public void test() {
		Main main = new Main();
		int result = main.Method1();
		assertEquals(7, result);
	}

}
