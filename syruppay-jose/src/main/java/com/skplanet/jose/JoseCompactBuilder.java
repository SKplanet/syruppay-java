package com.skplanet.jose;

import java.security.Key;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public abstract class JoseCompactBuilder {
	protected JoseMethod joseMethod;
	protected JoseSerializeType joseSerializeType;
	protected JoseActionType joseActionType;

	protected byte[] key;

	protected void compactBuilder(JoseActionType joseActionType) {
		compactBuilder(null, JoseSerializeType.COMPACT_SERIALIZATION, joseActionType);
	}

	protected void compactBuilder(JoseMethod joseMethod, JoseActionType joseActionType) {
		compactBuilder(joseMethod, JoseSerializeType.COMPACT_SERIALIZATION, joseActionType);
	}

	protected void compactBuilder(JoseMethod joseMethod, JoseSerializeType joseSerializeType, JoseActionType joseActionType) {
		this.joseMethod = joseMethod;
		this.joseSerializeType = joseSerializeType;
		this.joseActionType = joseActionType;
	}

	public JoseActionType getJoseActionType() {
		return joseActionType;
	}

	public JoseCompactBuilder key(byte[] key) {
		this.key = key;

		return this;
	}

	public JoseCompactBuilder key(Key key) {
		this.key = key.getEncoded();

		return this;
	}

	public JoseCompactBuilder key(String key) {
		this.key = key.getBytes();

		return this;
	}

	abstract public JoseAction create();
}
