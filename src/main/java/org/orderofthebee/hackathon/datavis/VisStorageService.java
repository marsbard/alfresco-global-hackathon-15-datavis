package org.orderofthebee.hackathon.datavis;

import java.util.concurrent.atomic.AtomicLong;

public interface VisStorageService {

	public Object get(String tag);
	
	public void remove(String tag);

	void put(String tag, Object value);

	public AtomicLong getAtomicLong(String tag);

}
