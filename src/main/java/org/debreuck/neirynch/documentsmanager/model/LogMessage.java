package org.debreuck.neirynch.documentsmanager.model;

public enum LogMessage {
	START_RENDERING("Executing request startRendering with arguments"),
	START_RENDERING_RETURN("Service startRendering returned"),
	GET_RENDERING("Executing request getRendering with arguments");
	
	private String logMessageContent;
	
	LogMessage(String logMessageContent) {
		this.logMessageContent = logMessageContent;
	}
	
	public String getLogMessageContent() {
		return this.logMessageContent;
	}
	
}
