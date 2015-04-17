package org.orderofthebee.hackathon.datavis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

public class VisStorageServiceImpl implements VisStorageService {

	
	private static final Logger log = Logger
			.getLogger(VisStorageServiceImpl.class);
	
	public VisStorageServiceImpl() {
		log.debug("%%%%%%%%%%%%%%%%%%%%%% debug showing");
		log.error("%%%%%%%%%%%%%%%%%%%%%% error showing");
	}
	
	
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
			ret.set(0);
			put(tag,ret);
		}
		return ret;
	}

}
