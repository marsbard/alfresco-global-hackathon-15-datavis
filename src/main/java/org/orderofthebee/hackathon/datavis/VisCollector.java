package org.orderofthebee.hackathon.datavis;

import java.util.concurrent.atomic.AtomicLong;

import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;

public class VisCollector implements NodeServicePolicies.OnDeleteNodePolicy,
		NodeServicePolicies.OnCreateNodePolicy, NodeServicePolicies.OnUpdateNodePolicy {

	
	private static final Logger log = Logger.getLogger(VisCollector.class);

	public static final String CREATE_COUNT_TAG = "createCount";
	public static final String UPDATE_COUNT_TAG = "updateCount";
	
	VisStorageService storageService;
	
	public VisCollector(){
		
	}
	
	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		log.debug(String.format("createCounter count is %d", ((AtomicLong) storageService.get(CREATE_COUNT_TAG)).intValue()));
		((AtomicLong)storageService.get(CREATE_COUNT_TAG)).getAndIncrement();
		log.debug(String.format("createCounter count is %d", ((AtomicLong) storageService.get(CREATE_COUNT_TAG)).intValue()));
	}

	@Override
	public void onDeleteNode(ChildAssociationRef childAssocRef,
			boolean isNodeArchived) {
		((AtomicLong)storageService.get(CREATE_COUNT_TAG)).getAndDecrement();
	}

	@Override
	public void onUpdateNode(NodeRef nodeRef) {
		((AtomicLong)storageService.get(UPDATE_COUNT_TAG)).getAndIncrement();
	}

	public void setStorageService(VisStorageService storageService) {
		this.storageService = storageService;
		reset();
	}

	public void reset() {
		storageService.put(CREATE_COUNT_TAG, new AtomicLong());
		storageService.put(UPDATE_COUNT_TAG, new AtomicLong());
	}
}
