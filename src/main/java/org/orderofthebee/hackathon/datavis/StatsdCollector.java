package org.orderofthebee.hackathon.datavis;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

public class StatsdCollector implements
		NodeServicePolicies.BeforeCreateStorePolicy,
		NodeServicePolicies.OnCreateStorePolicy,
		NodeServicePolicies.BeforeCreateNodePolicy,
		NodeServicePolicies.OnCreateNodePolicy,
		NodeServicePolicies.BeforeMoveNodePolicy,
		NodeServicePolicies.OnMoveNodePolicy,
		NodeServicePolicies.BeforeUpdateNodePolicy,
		NodeServicePolicies.OnUpdateNodePolicy,
		NodeServicePolicies.OnUpdatePropertiesPolicy,
		NodeServicePolicies.BeforeDeleteNodePolicy,
		NodeServicePolicies.BeforeArchiveNodePolicy,
		NodeServicePolicies.OnDeleteNodePolicy,
		NodeServicePolicies.BeforeAddAspectPolicy,
		NodeServicePolicies.OnAddAspectPolicy,
		NodeServicePolicies.BeforeRemoveAspectPolicy,
		NodeServicePolicies.OnRemoveAspectPolicy,
		NodeServicePolicies.OnRestoreNodePolicy,
		NodeServicePolicies.OnCreateChildAssociationPolicy,
		NodeServicePolicies.BeforeDeleteChildAssociationPolicy,
		NodeServicePolicies.OnDeleteChildAssociationPolicy,
		NodeServicePolicies.OnCreateAssociationPolicy,
		NodeServicePolicies.BeforeDeleteAssociationPolicy,
		NodeServicePolicies.OnDeleteAssociationPolicy,
		NodeServicePolicies.BeforeSetNodeTypePolicy,
		NodeServicePolicies.OnSetNodeTypePolicy {

	private static final String STATSD_HOST = "localhost";

	private static final StatsDClient statsd = new NonBlockingStatsDClient(
			"ootb.datavis", STATSD_HOST, 8125);

	private static final Logger log = Logger.getLogger(StatsdCollector.class);

	private PolicyComponent policyComponent;

	private Behaviour onCreateNode;
	private Behaviour onDeleteNode;
	private Behaviour onUpdateNode;
	private Behaviour beforeCreateStore;
	private Behaviour onCreateStore;
	private Behaviour beforeCreateNode;
	private Behaviour beforeMoveNode;
	private Behaviour onMoveNode;
	private Behaviour beforeUpdateNode;
	private Behaviour onUpdateProperties;
	private Behaviour beforeDeleteNode;
	private Behaviour beforeArchiveNode;
	private Behaviour beforeAddAspect;
	private Behaviour onAddAspect;
	private Behaviour beforeRemoveAspect;
	private Behaviour onRemoveAspect;
	private Behaviour onRestoreNode;
	private Behaviour onCreateChildAssociation;
	private Behaviour beforeDeleteChildAssociation;
	private Behaviour onDeleteChildAssociation;
	private Behaviour onCreateAssociation;
	private Behaviour beforeDeleteAssociation;
	private Behaviour onDeleteAssociation;
	private Behaviour beforeSetNodeType;
	private Behaviour onSetNodeType;

	public void init() {

		this.beforeCreateStore = doBind("beforeCreateStore");
		this.onCreateStore = doBind("onCreateStore");
		this.beforeCreateNode = doBind("beforeCreateNode");
		this.onCreateNode = doBind("onCreateNode");
		this.beforeMoveNode = doBind("beforeMoveNode");
		this.onMoveNode = doBind("onMoveNode");
		this.beforeUpdateNode = doBind("beforeUpdateNode");
		this.onUpdateNode = doBind("onUpdateNode");
		this.onUpdateProperties = doBind("onUpdateProperties");
		this.beforeDeleteNode = doBind("beforeDeleteNode");
		this.beforeArchiveNode = doBind("beforeArchiveNode");
		this.onDeleteNode = doBind("onDeleteNode");
		this.beforeAddAspect = doBind("beforeAddAspect");
		this.onAddAspect = doBind("onAddAspect");
		this.beforeRemoveAspect = doBind("beforeRemoveAspect");
		this.onRemoveAspect = doBind("onRemoveAspect");
		this.onRestoreNode = doBind("onRestoreNode");
		this.onCreateChildAssociation = doBind("onCreateChildAssociation");
		this.beforeDeleteChildAssociation = doBind("beforeDeleteChildAssociation");
		this.onDeleteChildAssociation = doBind("onDeleteChildAssociation");
		this.onCreateAssociation = doBind("onCreateAssociation");
		this.beforeDeleteAssociation = doBind("beforeDeleteAssociation");
		this.onDeleteAssociation = doBind("onDeleteAssociation");
		this.beforeSetNodeType = doBind("beforeSetNodeType");
		this.onSetNodeType = doBind("onSetNodeType");
	}

	private JavaBehaviour doBind(String behaviourName) {
		JavaBehaviour behaviour = new JavaBehaviour(this, behaviourName,
				NotificationFrequency.TRANSACTION_COMMIT);
		this.getPolicyComponent()
				.bindClassBehaviour(
						QName.createQName(NamespaceService.ALFRESCO_URI,
								behaviourName), ContentModel.TYPE_CONTENT,
						behaviour);

		return behaviour;
	}

	public StatsdCollector() {
	}

	public PolicyComponent getPolicyComponent() {
		return policyComponent;
	}

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}

	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		statsd.incrementCounter("onCreateNode");
	}

	@Override
	public void onDeleteNode(ChildAssociationRef childAssocRef,
			boolean isNodeArchived) {
		statsd.incrementCounter("onDeleteNode");
	}

	@Override
	public void onUpdateNode(NodeRef nodeRef) {
		statsd.incrementCounter("onUpdateNode");
	}

	@Override
	public void onSetNodeType(NodeRef nodeRef, QName oldType, QName newType) {
		statsd.incrementCounter("onSetNodeType");
	}

	@Override
	public void beforeSetNodeType(NodeRef nodeRef, QName oldType, QName newType) {
		statsd.incrementCounter("beforeSetNodeType");

	}

	@Override
	public void onDeleteAssociation(AssociationRef nodeAssocRef) {
		statsd.incrementCounter("onDeleteAssociation");

	}

	@Override
	public void beforeDeleteAssociation(AssociationRef nodeAssocRef) {
		statsd.incrementCounter("beforeDeleteAssociation");

	}

	@Override
	public void onCreateAssociation(AssociationRef nodeAssocRef) {
		statsd.incrementCounter("onCreateAssociation");

	}

	@Override
	public void onDeleteChildAssociation(ChildAssociationRef childAssocRef) {
		statsd.incrementCounter("onDeleteChildAssociation");

	}

	@Override
	public void beforeDeleteChildAssociation(ChildAssociationRef childAssocRef) {
		statsd.incrementCounter("beforeDeleteChildAssociation");

	}

	@Override
	public void onCreateChildAssociation(ChildAssociationRef childAssocRef,
			boolean isNewNode) {
		statsd.incrementCounter("onCreateChildAssociation"
				+ (isNewNode ? "_new" : ""));

	}

	@Override
	public void onRestoreNode(ChildAssociationRef childAssocRef) {
		statsd.incrementCounter("onRestoreNode");

	}

	@Override
	public void onRemoveAspect(NodeRef nodeRef, QName aspectTypeQName) {
		statsd.incrementCounter("onRemoveAspect");

	}

	@Override
	public void beforeRemoveAspect(NodeRef nodeRef, QName aspectTypeQName) {
		statsd.incrementCounter("beforeRemoveAspect");

	}

	@Override
	public void onAddAspect(NodeRef nodeRef, QName aspectTypeQName) {
		statsd.incrementCounter("onAddAspect");

	}

	@Override
	public void beforeAddAspect(NodeRef nodeRef, QName aspectTypeQName) {
		statsd.incrementCounter("beforeAddAspect");

	}

	@Override
	public void beforeArchiveNode(NodeRef nodeRef) {
		statsd.incrementCounter("beforeArchiveNode");

	}

	@Override
	public void beforeDeleteNode(NodeRef nodeRef) {
		statsd.incrementCounter("beforeDeleteNode");

	}

	@Override
	public void onUpdateProperties(NodeRef nodeRef,
			Map<QName, Serializable> before, Map<QName, Serializable> after) {
		statsd.incrementCounter("onUpdateProperties");

	}

	@Override
	public void beforeUpdateNode(NodeRef nodeRef) {
		statsd.incrementCounter("beforeUpdateNode");

	}

	@Override
	public void onMoveNode(ChildAssociationRef oldChildAssocRef,
			ChildAssociationRef newChildAssocRef) {
		statsd.incrementCounter("onMoveNode");

	}

	@Override
	public void beforeMoveNode(ChildAssociationRef oldChildAssocRef,
			NodeRef newParentRef) {
		statsd.incrementCounter("beforeMoveNode");

	}

	@Override
	public void beforeCreateNode(NodeRef parentRef, QName assocTypeQName,
			QName assocQName, QName nodeTypeQName) {
		statsd.incrementCounter("beforeCreateNode");

	}

	@Override
	public void onCreateStore(NodeRef rootNodeRef) {
		statsd.incrementCounter("onCreateStore");

	}

	@Override
	public void beforeCreateStore(QName nodeTypeQName, StoreRef storeRef) {
		statsd.incrementCounter("beforeCreateStore");

	}
}
