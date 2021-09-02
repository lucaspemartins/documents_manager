package org.debreuck.neirynch.documentsmanager.model;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;


class LogMessageTests {

	private static final String START_RENDERING_MESSAGE = "Executing request startRendering with arguments";
	private static final String START_RENDERING_RETURN_MESSAGE = "Service startRendering returned";
	private static final String GET_RENDERING_MESSAGE = "Executing request getRendering with arguments";
	private static final int NUMBER_OF_LOG_MESSAGE_KEYS = 3;
	
	@Test
	void getNumberOfLogMessageKeys() throws IOException {
		Assert.assertEquals(NUMBER_OF_LOG_MESSAGE_KEYS, LogMessage.values().length);
	}
	
	@Test
	void getStartRenderingMessage() throws IOException {
		Assert.assertEquals(START_RENDERING_MESSAGE, LogMessage.START_RENDERING.getLogMessageContent());
	}
	
	@Test
	void getStartRenderingReturnMessage() throws IOException {
		Assert.assertEquals(START_RENDERING_RETURN_MESSAGE, LogMessage.START_RENDERING_RETURN.getLogMessageContent());
	}
	
	@Test
	void getGetRenderingMessage() throws IOException {
		Assert.assertEquals(GET_RENDERING_MESSAGE, LogMessage.GET_RENDERING.getLogMessageContent());
	}
	
}
