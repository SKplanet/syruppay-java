package com.skplanet.jose;

import com.skplanet.jose.jwe.JweSerializer;
import com.skplanet.jose.jws.JwsSerializer;

/**
 * Created by 박병찬 on 2015-07-30.
 */
public class DeserializationBuilder extends JoseCompactBuilder {
	private String serializedSource;

	public DeserializationBuilder(JoseActionType joseActionType) {
		super.compactBuilder(joseActionType);
	}

	public DeserializationBuilder(JoseMethod joseMethod, JoseActionType joseActionType) {
		super.compactBuilder(joseMethod, joseActionType);
	}

	public DeserializationBuilder serializedSource(String serializedSource) {
		this.serializedSource = serializedSource;

		return this;
	}

	public JoseAction create() {
		JoseHeader header = new JoseHeader();
		header.setEncoded(serializedSource);
		joseMethod = header.getJoseMethod();

		switch (joseSerializeType) {
		case COMPACT_SERIALIZATION:
			if (JoseMethod.JWE == joseMethod && JoseActionType.DESERIALIZATION == joseActionType) {
				return new JweSerializer(serializedSource, key);
			} else if (JoseMethod.JWS == joseMethod && JoseActionType.DESERIALIZATION == joseActionType) {
				return new JwsSerializer(serializedSource, key);
			} else {
				throw new IllegalArgumentException("unknown JoseSerializeType and JoseActionType");
			}
		case JSON_SERIALIZATION:
			return null;
		default:
			return null;
		}
	}
}
