package com.skplanet.jose.jwa.crypto;

public class Transformation {
	private Algorithm algorithm = null;
	private Mode mode = null;
	private Padding padding = null;

	public Transformation(Algorithm algorithm, Mode mode, Padding padding) {
		this.algorithm = algorithm;
		this.mode = mode;
		this.padding = padding;
	}

	public Transformation(Algorithm algorithm) {
		this(algorithm, null, null);
	}

	public String getValue() {
		return new StringBuilder().append(algorithm.getValue())
				.append(isNotNull(mode) ? isNotEmpty(mode.getValue()) ? "/" + mode.getValue() : "" : "")
				.append(isNotNull(padding) ? isNotEmpty(padding.getValue()) ? "/" + padding.getValue() : "" : "")
				.toString();
	}

	public String getAlgorithm() {
		return algorithm.getValue();
	}

	private boolean isNotEmpty(String src) {
		return src != null && src.length() > 0;
	}

	private boolean isNotNull(Object obj) {
		return obj != null;
	}
}
