package org.orderofthebee.hackathon.datavis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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

	@Override
	public AtomicLong getAtomicLong(String tag) {
		AtomicLong ret = (AtomicLong) get(tag);
		if(ret==null) {
			ret=new AtomicLong();
			put(tag,ret);
		}
		return ret;
	}

}
