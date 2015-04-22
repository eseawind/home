package net.casnw.xinanjiang;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XAJModelTest {
	
	private XAJModel model; 

	@Before
	public void setUp() throws Exception {
		model = new XAJModel();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRunModel() {
		model.init("forcing.txt", "runoff_all.txt");
		model.runModle();
	}

}
