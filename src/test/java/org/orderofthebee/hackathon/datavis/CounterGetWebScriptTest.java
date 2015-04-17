package org.orderofthebee.hackathon.datavis;

import static org.junit.Assert.assertEquals;


import java.util.Map;

import javax.transaction.Status;

import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class CounterGetWebScriptTest {

	private static VisStorageService storageService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		storageService = new VisStorageServiceImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private CounterGetWebScript counterGet;
	private VisCollector collector;

	@Before
	public void setUp() throws Exception {
		counterGet = new CounterGetWebScript();
		counterGet.setStorageService(storageService);
		
		collector = new VisCollector();
		collector.setStorageService(storageService);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteImpl() {
		WebScriptRequest req = null;
		Status status = null;
		Cache cache = null;
		
		ChildAssociationRef childAssocRef = null;
		NodeRef nodeRef = new NodeRef(":// :/");
		
		Map<String, Object> map = counterGet.executeImpl(req, status, cache);
		assertEquals("zero", 0L,  map.get("createCount"));
		assertEquals("zero", 0L,  map.get("updateCount"));
		assertEquals("zero", 0L,  map.get("deleteCount"));
		
		collector.onCreateNode(childAssocRef);
		map = counterGet.executeImpl(req, status, cache);
		assertEquals("one", 1L,  map.get("createCount"));
		assertEquals("zero", 0L,  map.get("updateCount"));
		assertEquals("zero", 0L,  map.get("deleteCount"));
		
		collector.onUpdateNode(nodeRef);
		map = counterGet.executeImpl(req, status, cache);
		assertEquals("one", 1L,  map.get("createCount"));
		assertEquals("one", 1L,  map.get("updateCount"));
		assertEquals("zero", 0L,  map.get("deleteCount"));
		
		collector.onDeleteNode(childAssocRef, false);
		map = counterGet.executeImpl(req, status, cache);
		assertEquals("one", 1L,  map.get("createCount"));
		assertEquals("one", 1L,  map.get("updateCount"));
		assertEquals("one", 1L,  map.get("deleteCount"));
		
		
		collector.onUpdateNode(nodeRef);
		map = counterGet.executeImpl(req, status, cache);
		assertEquals("one", 1L,  map.get("createCount"));
		assertEquals("two", 2L,  map.get("updateCount"));
		assertEquals("one", 1L,  map.get("deleteCount"));
		
	}

}
