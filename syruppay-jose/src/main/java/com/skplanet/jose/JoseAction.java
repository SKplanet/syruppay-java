package com.skplanet.jose;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public interface JoseAction {
	String compactSerialization();
	String compactDeserialization();
	JoseHeader getJoseHeader();
}
