package org.orderofthebee.hackathon.datavis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class CreateTestFilePostWebScript extends DeclarativeWebScript {

	private NodeService nodeService;
	private ContentService contentService;
	private SearchService searchService;
	private RandomString randomString;

	
	public CreateTestFilePostWebScript(){
		randomString = new RandomString(8);
	}
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	// http://docs.alfresco.com/4.0/tasks/api-java-content-create.html
	private NodeRef createContentNode(NodeRef parent, String name, String text) {

		// Create a map to contain the values of the properties of the node

		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
		props.put(ContentModel.PROP_NAME, name);

		// use the node service to create a new node
		NodeRef node = this.nodeService
				.createNode(
						parent,
						ContentModel.ASSOC_CONTAINS,
						QName.createQName(
								NamespaceService.CONTENT_MODEL_1_0_URI, name),
						ContentModel.TYPE_CONTENT, props).getChildRef();

		// Use the content service to set the content onto the newly created
		// node
		ContentWriter writer = this.contentService.getWriter(node,
				ContentModel.PROP_CONTENT, true);
		writer.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
		writer.setEncoding("UTF-8");
		writer.putContent(text);

		// Return a node reference to the newly created node
		return node;
	}

	protected Map<String, Object> executeImpl(WebScriptRequest req,
			Status status, Cache cache) {

		StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
		ResultSet rs = searchService.query(storeRef, SearchService.LANGUAGE_XPATH, "/app:company_home");
		NodeRef companyHomeNodeRef = null;
		try
		{
		    if (rs.length() == 0)
		    {
		        throw new AlfrescoRuntimeException("Didn't find Company Home");
		    }
		    companyHomeNodeRef = rs.getNodeRef(0);
		}
		finally
		{
		    rs.close();
		}

		String filename = randomString.nextString() + ".txt";
		
		NodeRef newNode = createContentNode(companyHomeNodeRef, filename, "blah blah");
		
		HashMap<String, Object> model = new HashMap<String, Object>();
		
		if (newNode==null) {
			model.put("numCreated", 0);
			model.put("nodeRefStr", "NUFFINK");
		} else {
			model.put("numCreated", 1);
			model.put("nodeRefStr", newNode.getId());
		}
		
		return model;

	}
}
