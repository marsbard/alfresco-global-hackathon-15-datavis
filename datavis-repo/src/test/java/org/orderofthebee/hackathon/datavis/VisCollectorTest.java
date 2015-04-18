package org.orderofthebee.hackathon.datavis;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicLong;

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
		storageService.put(VisCollector.CREATE_COUNT_TAG, new AtomicLong());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		storageService=null;
		collector=null;
	}

	@Test
	public void testCreateAndDelete() {
		ChildAssociationRef childAssocRef = mock(ChildAssociationRef.class);
		collector.onCreateNode(childAssocRef);
		assertEquals(1, (((AtomicLong) storageService.get(VisCollector.CREATE_COUNT_TAG)).intValue()));
		collector.onCreateNode(childAssocRef);
		assertEquals(2, (((AtomicLong) storageService.get(VisCollector.CREATE_COUNT_TAG)).intValue()));
		collector.onDeleteNode(childAssocRef, true);
		assertEquals(2, (((AtomicLong) storageService.get(VisCollector.CREATE_COUNT_TAG)).intValue()));
		assertEquals(1, (((AtomicLong) storageService.get(VisCollector.DELETE_COUNT_TAG)).intValue()));

	}
	

	@Test
	public void testUpdate() {
		NodeRef nodeRef = new NodeRef(":// :/");
		collector.onUpdateNode(nodeRef);
		assertEquals(1, (((AtomicLong) storageService.get(VisCollector.UPDATE_COUNT_TAG)).intValue()));
		collector.onUpdateNode(nodeRef);
		assertEquals(2, (((AtomicLong) storageService.get(VisCollector.UPDATE_COUNT_TAG)).intValue()));
		collector.onUpdateNode(nodeRef);
		assertEquals(3, (((AtomicLong) storageService.get(VisCollector.UPDATE_COUNT_TAG)).intValue()));
	}
	
}
