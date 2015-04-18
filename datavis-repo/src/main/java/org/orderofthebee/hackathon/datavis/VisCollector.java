package org.orderofthebee.hackathon.datavis;

import java.util.concurrent.atomic.AtomicLong;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

public class VisCollector implements NodeServicePolicies.OnDeleteNodePolicy,
		NodeServicePolicies.OnCreateNodePolicy, NodeServicePolicies.OnUpdateNodePolicy {

	
	private static final Logger log = Logger.getLogger(VisCollector.class);

	public static final String CREATE_COUNT_TAG = "createCount";
	public static final String UPDATE_COUNT_TAG = "updateCount";
	public static final String DELETE_COUNT_TAG = "deleteCount";

	private Behaviour onCreateNode;
	private Behaviour onDeleteNode;
	private Behaviour onUpdateNode;

	private PolicyComponent policyComponent;
	
	public void init() {

	    // Create behaviours
	    this.onCreateNode = new JavaBehaviour(this, "onCreateNode", NotificationFrequency.TRANSACTION_COMMIT);	
	    this.onDeleteNode = new JavaBehaviour(this, "onDeleteNode", NotificationFrequency.TRANSACTION_COMMIT);
	    this.onUpdateNode = new JavaBehaviour(this, "onUpdateNode", NotificationFrequency.TRANSACTION_COMMIT);
	    
	    // Bind behaviours to node policies
	    this.getPolicyComponent().bindClassBehaviour(
	        QName.createQName(NamespaceService.ALFRESCO_URI, "onCreateNode"),
	        ContentModel.TYPE_CONTENT,
	        this.onCreateNode
	    );
	    

	    this.getPolicyComponent().bindClassBehaviour(
	        QName.createQName(NamespaceService.ALFRESCO_URI, "onDeleteNode"),
	        ContentModel.TYPE_CONTENT,
	        this.onDeleteNode
	    );
	    
	    this.getPolicyComponent().bindClassBehaviour(
		        QName.createQName(NamespaceService.ALFRESCO_URI, "onUpdateNode"),
		        ContentModel.TYPE_CONTENT,
		        this.onUpdateNode
		    );
	}
	
	VisStorageService storageService;
	
	public VisCollector(){
		log.debug("%%%%%%%%%%%%%%%%%%%% VisCollector");
	}
	
	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		log.debug("onCreateNode");
		long current = storageService.getAtomicLong(CREATE_COUNT_TAG).getAndIncrement();
		log.debug("After onCreateNode, current value for " + CREATE_COUNT_TAG + " is " + current);
	}

	@Override
	public void onDeleteNode(ChildAssociationRef childAssocRef,
			boolean isNodeArchived) {
		log.debug("onDeleteNode");
		long current = storageService.getAtomicLong(DELETE_COUNT_TAG).getAndIncrement();
		log.debug("After onDeleteNode, current value for " + DELETE_COUNT_TAG + " is " + current);
	}

	@Override
	public void onUpdateNode(NodeRef nodeRef) {
		log.debug("onUpdateNode");
		long current = storageService.getAtomicLong(UPDATE_COUNT_TAG).getAndIncrement();
		log.debug("After onUpdateNode, current value for " + UPDATE_COUNT_TAG + " is " + current);
	}

	public void setStorageService(VisStorageService storageService) {
		this.storageService = storageService;
		reset();
	}

	public void reset() {
		log.debug("RESET CALLED");
		storageService.put(CREATE_COUNT_TAG, new AtomicLong());
		storageService.put(UPDATE_COUNT_TAG, new AtomicLong());
	}

	public PolicyComponent getPolicyComponent() {
		return policyComponent;
	}

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}
}
