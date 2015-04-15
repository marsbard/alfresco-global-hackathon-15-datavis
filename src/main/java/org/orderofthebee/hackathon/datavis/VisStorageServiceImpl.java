package org.orderofthebee.hackathon.datavis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VisStorageServiceImpl implements VisStorageService {

	Map<String, Object> storage = new ConcurrentHashMap<String, Object>();
	
	@Override
	public void put(String tag, Object value) {
		storage.put(tag, value);
	}

	@Override
	public Object get(String tag) {
		Object ret=storage.get(tag);
		return ret;
	}

	@Override
	public void remove(String tag) {
		storage.remove(tag);
		
	}

}
