package net.casnw.xinanjiang;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SATest {
	
	private SA sa;

	@Before
	public void setUp() throws Exception {
		sa = new SA();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRndu() {
		double num;
		for (int i=0; i<10000; i++) {
			num = this.sa.Rndu();
			Assert.assertTrue(num>=0.0f && num<=1.0f);
		}
	}

}
