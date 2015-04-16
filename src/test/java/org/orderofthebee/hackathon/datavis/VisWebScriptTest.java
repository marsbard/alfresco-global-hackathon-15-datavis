package org.orderofthebee.hackathon.datavis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.extensions.webscripts.WebScriptResponse;

public class VisWebScriptTest {

	private static final String JSON_ZCOUNT_RES = "{\"count\":0}";
	private static VisStorageService storageService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		storageService = new VisStorageServiceImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private VisCountWebScript webscript;

	@Before
	public void setUp() throws Exception {
		webscript = new VisCountWebScript();
		webscript.setStorageService(storageService);
	}

	@After
	public void tearDown() throws Exception {
		webscript = null;
	}

	@Test
	public void testExecute() throws IOException {
		WebScriptResponse res = mock(WebScriptResponse.class);
		Writer w = new StringWriter();
		when(res.getWriter()).thenReturn(w);
		webscript.execute(null, res);
		assertEquals("json result zero count", JSON_ZCOUNT_RES, w.toString());
	}

}
