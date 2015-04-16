package org.orderofthebee.hackathon.datavis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.extensions.webscripts.WebScriptResponse;

public class VisWebScriptTest {

	private static final String JSON_ZCOUNT_RES = "{\"count\":0}";
	private static final Object JSON_1COUNT_RES = "{\"count\":1}";
	private static final Object JSON_2COUNT_RES = "{\"count\":2}";
	
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

	@Test
	public void testCounterIncr() throws IOException {
		WebScriptResponse res = mock(WebScriptResponse.class);
		
		StringWriter sw = new StringWriter();

		when(res.getWriter()).thenReturn(sw);
		
		webscript.execute(null, res);
		assertEquals("json result zero count", JSON_ZCOUNT_RES, sw.toString());
		
		// normally we wouldn't need to reset the stringwriter but we are mocking
		sw.getBuffer().setLength(0);
		
//		
		
		AtomicLong al = storageService.getAtomicLong(VisCollector.CREATE_COUNT_TAG);
		al.getAndIncrement();
		
		webscript.execute(null, res);
		assertEquals("json result one count", JSON_1COUNT_RES, sw.toString());

		sw.getBuffer().setLength(0);
		
		
		storageService.getAtomicLong(VisCollector.CREATE_COUNT_TAG).getAndIncrement();
		webscript.execute(null, res);
		assertEquals("json result two count", JSON_2COUNT_RES, sw.toString());
	}

}
