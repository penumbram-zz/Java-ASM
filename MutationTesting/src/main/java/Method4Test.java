import static org.junit.Assert.*;

import org.junit.Test;

public class Method4Test {

	@Test
	public void test() {
		Main main = new Main();
		
		boolean result = main.Method4(true,false);
		assertEquals(false, result);
	}

}
