package com.skplanet.jose;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public class Jose {
	private JoseAction joseAction;

	public SerializeAction configuration(JoseCompactBuilder joseCompactBuilder) {
		Security.addProvider(new BouncyCastleProvider());

		final JoseActionType joseActionType = joseCompactBuilder.getJoseActionType();
		joseAction = joseCompactBuilder.create();

		return new SerializeAction() {
			public String serialization() {
				if (joseActionType == JoseActionType.DESERIALIZATION) {
					throw new UnsupportedOperationException("mismatch serialization operation");
				}
				return joseAction.compactSerialization();
			}

			public String deserialization() {
				if (joseActionType == JoseActionType.SERIALIZATION) {
					throw new UnsupportedOperationException("mismatch serialization operation");
				}
				return joseAction.compactDeserialization();
			}

			public JoseHeader getJoseHeader() {
				if (joseActionType == JoseActionType.SERIALIZATION) {
					throw new UnsupportedOperationException("mismatch serialization operation");
				}
				return joseAction.getJoseHeader();
			}
		};
	}

	public JoseAction getInstance() {
		return joseAction;
	}
}
