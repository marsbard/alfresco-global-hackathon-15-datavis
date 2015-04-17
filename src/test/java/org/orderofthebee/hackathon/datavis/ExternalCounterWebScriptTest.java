package org.orderofthebee.hackathon.datavis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExternalCounterWebScriptTest {

	private static final String COUNTER_SCRIPT = "/ootb/counters.json?guest=true";
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

		assertEquals(0, json.get("createCount"));

	}

}
