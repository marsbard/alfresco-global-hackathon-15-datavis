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
	public static final String DELETE_COUNT_TAG = "deleteCount";
	
	VisStorageService storageService;
	
	public VisCollector(){
		
	}
	
	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		storageService.getAtomicLong(CREATE_COUNT_TAG).getAndIncrement();
	}

	@Override
	public void onDeleteNode(ChildAssociationRef childAssocRef,
			boolean isNodeArchived) {
		storageService.getAtomicLong(DELETE_COUNT_TAG).getAndIncrement();
	}

	@Override
	public void onUpdateNode(NodeRef nodeRef) {
		storageService.getAtomicLong(UPDATE_COUNT_TAG).getAndIncrement();
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
