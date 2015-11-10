package com.skplanet.jose;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public class JoseBuilders {
	public static SerializationBuilder JsonSignatureCompactSerializationBuilder() {
		return new SerializationBuilder(JoseMethod.JWS, JoseActionType.SERIALIZATION);
	}

	@Deprecated
	public static DeserializationBuilder JsonSignatureCompactDeserializationBuilder() {
		return new DeserializationBuilder(JoseMethod.JWS, JoseActionType.DESERIALIZATION);
	}

	public static SerializationBuilder JsonEncryptionCompactSerializationBuilder() {
		return new SerializationBuilder(JoseMethod.JWE, JoseActionType.SERIALIZATION);
	}

	@Deprecated
	public static DeserializationBuilder JsonEncryptionCompactDeserializationBuilder() {
		return new DeserializationBuilder(JoseMethod.JWE, JoseActionType.DESERIALIZATION);
	}

	public static DeserializationBuilder compactDeserializationBuilder() {
		return new DeserializationBuilder(JoseActionType.DESERIALIZATION);
	}
}
