package org.orderofthebee.hackathon.datavis;

public interface VisStorageService {

	public Object get(String tag);
	
	public void remove(String tag);

	void put(String tag, Object value);

}
