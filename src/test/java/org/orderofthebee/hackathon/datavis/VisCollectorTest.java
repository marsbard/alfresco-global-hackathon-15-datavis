package org.orderofthebee.hackathon.datavis;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class VisCollectorTest {

	private static VisStorageService storageService;
	private static VisCollector collector;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		storageService = new VisStorageServiceImpl();
		collector = new VisCollector();
		collector.setStorageService(storageService);
	}

	@Before
	public void setUp() throws Exception {
		collector.reset();
		storageService.put(VisCollector.CREATE_COUNT_TAG, new AtomicInteger());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		storageService=null;
		collector=null;
	}

	@Test
	public void testCreate() {
		ChildAssociationRef childAssocRef = null;
		collector.onCreateNode(childAssocRef);
		assertEquals(1, (((AtomicInteger) storageService.get(VisCollector.CREATE_COUNT_TAG)).intValue()));
		collector.onCreateNode(childAssocRef);
		assertEquals(2, (((AtomicInteger) storageService.get(VisCollector.CREATE_COUNT_TAG)).intValue()));
		collector.onDeleteNode(childAssocRef, true);
		assertEquals(1, (((AtomicInteger) storageService.get(VisCollector.CREATE_COUNT_TAG)).intValue()));

	}
	

	@Test
	public void testUpdate() {
		NodeRef nodeRef = new NodeRef(":// :/");
		collector.onUpdateNode(nodeRef);
		assertEquals(1, (((AtomicInteger) storageService.get(VisCollector.UPDATE_COUNT_TAG)).intValue()));
		collector.onUpdateNode(nodeRef);
		assertEquals(2, (((AtomicInteger) storageService.get(VisCollector.UPDATE_COUNT_TAG)).intValue()));
		collector.onUpdateNode(nodeRef);
		assertEquals(3, (((AtomicInteger) storageService.get(VisCollector.UPDATE_COUNT_TAG)).intValue()));
	}
	
}
