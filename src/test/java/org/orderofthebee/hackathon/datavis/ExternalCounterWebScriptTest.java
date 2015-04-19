package org.orderofthebee.hackathon.datavis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExternalCounterWebScriptTest {
	
	private static final Logger log = Logger
			.getLogger(ExternalCounterWebScriptTest.class);

	private static final String COUNTER_SCRIPT = "ootb/datavis/counters.json?guest=true";
	private static final String CREATE_FILE_SCRIPT = "ootb/datavis/create-test-file.json?guest=true";
	
	static WebScriptHelper helper;

	String baseUrl = TestMeta.BASE_URL;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		helper = new WebScriptHelper(TestMeta.AUTH_USER, TestMeta.AUTH_PASSWORD);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException, JSONException {
		JSONObject json = helper.readJsonFromUrl(baseUrl + COUNTER_SCRIPT);
		Long baseCount =  json.getLong("createCount");
		
		String url= baseUrl + CREATE_FILE_SCRIPT;

		json = helper.readJsonFromInputStream(helper.post(url));
		assertEquals(1, json.get("numCreated"));

		log.debug("Look I really made a node: " + json.get("nodeRefStr"));
		
		json = helper.readJsonFromUrl(baseUrl + COUNTER_SCRIPT);
		log.debug("count=" + json.get("createCount"));
		assertEquals(baseCount + 1, json.getLong("createCount"));
		
	}

}
