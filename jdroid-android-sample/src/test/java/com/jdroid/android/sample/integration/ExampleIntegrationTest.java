package com.jdroid.android.sample.integration;

import com.jdroid.android.sample.AbstractIntegrationTest;
import com.jdroid.android.sample.usecase.SampleUseCase;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ExampleIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void example() {
		SampleUseCase sampleUseCase = new SampleUseCase();
		sampleUseCase.run();
		assertNotNull(sampleUseCase.getItems());
		assertEquals(16, sampleUseCase.getItems().size());
	}
}