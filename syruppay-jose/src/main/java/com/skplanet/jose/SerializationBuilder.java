package com.skplanet.jose;

import com.skplanet.jose.jwe.JweSerializer;
import com.skplanet.jose.jws.JwsSerializer;

/**
 * Created by 박병찬 on 2015-07-30.
 */
public class SerializationBuilder extends JoseCompactBuilder {
	private JoseHeader header;
	private String payload;

	public SerializationBuilder(JoseMethod joseMethod, JoseActionType joseActionType) {
		super.compactBuilder(joseMethod, joseActionType);

		header = new JoseHeader();
	}

	public SerializationBuilder header(JoseHeader header) {
		this.header = header;

		return this;
	}

	public SerializationBuilder payload(String payload) {
		this.payload = payload;

		return this;
	}

	public JoseAction create() {
		switch (joseSerializeType) {
		case COMPACT_SERIALIZATION:
			if (JoseMethod.JWE == joseMethod && JoseActionType.SERIALIZATION == joseActionType) {
				return new JweSerializer(header, payload, key);
			} else if (JoseMethod.JWS == joseMethod && JoseActionType.SERIALIZATION == joseActionType) {
				return new JwsSerializer(header, payload, key);
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
