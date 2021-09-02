package org.debreuck.neirynch.documentsmanager.exception;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

class RestResponseEntityExceptionHandlerTest {
	
	private static final String LOG_FILE_MISSING_EXCEPTION_MESSAGE = "Required request part 'Test' is not present"; 

	@Test
	void handleMissingServletRequestPart() {
		MissingServletRequestPartException missingServletRequestPartException = new MissingServletRequestPartException("Test");
		HttpHeaders httpHeaders = new HttpHeaders();
		WebRequestImpl webRequest = new WebRequestImpl();
		ResponseEntity<Object> response = new RestResponseEntityExceptionHandler().handleMissingServletRequestPart(missingServletRequestPartException, httpHeaders, HttpStatus.BAD_REQUEST, webRequest);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) response.getBody();
		Assert.assertEquals(LOG_FILE_MISSING_EXCEPTION_MESSAGE, body.get("message"));
	}

}

class WebRequestImpl implements WebRequest {

	@Override
	public Object getAttribute(String name, int scope) {
		return null;
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		
	}

	@Override
	public void removeAttribute(String name, int scope) {
		
	}

	@Override
	public String[] getAttributeNames(int scope) {
		return null;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback, int scope) {
		
	}

	@Override
	public Object resolveReference(String key) {
		return null;
	}

	@Override
	public String getSessionId() {
		return null;
	}

	@Override
	public Object getSessionMutex() {
		return null;
	}

	@Override
	public String getHeader(String headerName) {
		return null;
	}

	@Override
	public String[] getHeaderValues(String headerName) {
		return null;
	}

	@Override
	public Iterator<String> getHeaderNames() {
		return null;
	}

	@Override
	public String getParameter(String paramName) {
		return null;
	}

	@Override
	public String[] getParameterValues(String paramName) {
		return null;
	}

	@Override
	public Iterator<String> getParameterNames() {
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return null;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public boolean checkNotModified(long lastModifiedTimestamp) {
		return false;
	}

	@Override
	public boolean checkNotModified(String etag) {
		return false;
	}

	@Override
	public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
		return false;
	}

	@Override
	public String getDescription(boolean includeClientInfo) {
		return null;
	}
	
}
