package com.skplanet.jose.jwa.crypto;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Test;

public class TestTransformation {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetValue() {
		Transformation transformation = new Transformation(Algorithm.AES);
		String value = transformation.getValue();
		assertThat(value, is("AES"));
		
		transformation = new Transformation(Algorithm.AES, Mode.CBC, Padding.PKCS5Padding);
		value = transformation.getValue();
		assertThat(value, is("AES/CBC/PKCS5Padding"));
	}

	@Test
	public void testGetAlgorithm() {
		Transformation transformation = new Transformation(Algorithm.AES);
		String value = transformation.getAlgorithm();

		assertThat(value, is("AES"));
	}

}
