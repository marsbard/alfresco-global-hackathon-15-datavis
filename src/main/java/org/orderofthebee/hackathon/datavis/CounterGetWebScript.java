package org.orderofthebee.hackathon.datavis;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class CounterGetWebScript extends DeclarativeWebScript {

	private static final Logger log = Logger
			.getLogger(CounterGetWebScript.class);

	private VisStorageService storageService;

	public void setStorageService(VisStorageService storageService) {
		this.storageService = storageService;
	}

	protected Map<String, Object> executeImpl(WebScriptRequest req,
			Status status, Cache cache) {

		Map<String, Object> model = new HashMap<String, Object>();
		Long createCount = storageService.getAtomicLong(
				VisCollector.CREATE_COUNT_TAG).get();
		Long updateCount = storageService.getAtomicLong(
				VisCollector.UPDATE_COUNT_TAG).get();
		Long deleteCount = storageService.getAtomicLong(
				VisCollector.DELETE_COUNT_TAG).get();

		model.put("createCount", createCount+"");
		model.put("updateCount", updateCount+"");
		model.put("deleteCount", deleteCount+"");

		return model;

	}
}
