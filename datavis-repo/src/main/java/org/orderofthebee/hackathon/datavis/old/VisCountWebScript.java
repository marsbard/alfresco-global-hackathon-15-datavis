package org.orderofthebee.hackathon.datavis.old;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.orderofthebee.hackathon.datavis.VisCollector;
import org.orderofthebee.hackathon.datavis.VisStorageService;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

@Deprecated
public class VisCountWebScript extends AbstractWebScript {
	
	
	private static final Logger log = Logger.getLogger(VisCountWebScript.class);
	
	private VisStorageService storageService;

	public void setStorageService(VisStorageService storageService) {
		this.storageService = storageService;
	}
	
	

	@Override
	public void execute(WebScriptRequest req, WebScriptResponse res)
			throws IOException {
    	try
    	{
	    	// build a json object
	    	JSONObject obj = new JSONObject();
	    	
	    	Long count = storageService.getAtomicLong(VisCollector.CREATE_COUNT_TAG).get();
	
	    	log.debug("count=" + count);
	    	obj.put("count", count==null?0:count);
	    	
	    	// build a JSON string and send it back
	    	String jsonString = obj.toString();
	    	res.getWriter().write(jsonString);
    	}
    	catch(JSONException e)
    	{
    		throw new WebScriptException("Unable to serialize JSON");
    	}

	}

}
