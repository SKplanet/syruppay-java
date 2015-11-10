package com.skplanet.jose;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public interface SerializeAction {
	String serialization();
	String deserialization();
	JoseHeader getJoseHeader();
}
