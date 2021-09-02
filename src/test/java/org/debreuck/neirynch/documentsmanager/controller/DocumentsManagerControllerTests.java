package org.debreuck.neirynch.documentsmanager.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.debreuck.neirynch.documentsmanager.model.Rendering;
import org.debreuck.neirynch.documentsmanager.model.Report;
import org.debreuck.neirynch.documentsmanager.model.Summary;
import org.debreuck.neirynch.documentsmanager.service.DocumentManagerService;
import org.debreuck.neirynch.documentsmanager.service.FileService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

class DocumentsManagerControllerTests {

	private static Logger LOG = Logger.getLogger(DocumentsManagerControllerTests.class.getName());
	private static final String EXCEPTION_MESSAGE = "Json invalid!";
	
	private MultipartFile logFileMock; 
	private Report reportMock;
	
	@InjectMocks
	private DocumentManagerController documentManagerController;

	@Mock
	private DocumentManagerService documentManagerService;
	
	@Mock
	private FileService fileService;
	
	@BeforeEach
	public void init() {
		List<String> lines = Arrays.asList(
				"2010-10-06 09:02:11,550 [WorkerThread-4] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373729338-5317] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:02:13,631 [WorkerThread-2] INFO  [ServiceProvider]: Executing request startRendering with arguments [114466, 0] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:02:13,634 [WorkerThread-2] INFO  [ServiceProvider]: Service startRendering returned 1286373733634-5423",
				"2010-10-06 09:02:14,825 [WorkerThread-0] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373733634-5423] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:03:05,869 [WorkerThread-17] INFO  [ServiceProvider]: Executing request startRendering with arguments [114466, 0] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:03:05,873 [WorkerThread-17] INFO  [ServiceProvider]: Service startRendering returned 1286373785873-3536",
				"2010-10-06 09:03:06,547 [WorkerThread-15] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373785873-3536] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:03:26,774 [WorkerThread-12] INFO  [ServiceProvider]: Executing request startRendering with arguments [114466, 0] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:03:26,777 [WorkerThread-12] INFO  [ServiceProvider]: Service startRendering returned 1286373806777-5552",
				"2010-10-06 09:03:27,985 [WorkerThread-13] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373806777-5552] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:03:57,879 [WorkerThread-14] INFO  [ServiceProvider]: Executing request startRendering with arguments [114273, 0] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:03:57,895 [WorkerThread-14] INFO  [ServiceProvider]: Service startRendering returned 1286373837895-7889",
				"2010-10-06 09:03:58,214 [WorkerThread-9] INFO  [ServiceProvider]: Executing request startRendering with arguments [114273, 0] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:03:58,218 [WorkerThread-9] INFO  [ServiceProvider]: Service startRendering returned 1286373837895-7889",
				"2010-10-06 09:03:59,790 [WorkerThread-14] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373837895-7889] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:04:01,287 [WorkerThread-3] INFO  [ServiceProvider]: Executing request startRendering with arguments [114273, 1] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }",
				"2010-10-06 09:04:01,290 [WorkerThread-3] INFO  [ServiceProvider]: Service startRendering returned 1286373841290-1204");
		Path file = Paths.get("server.txt");
		try {
			Files.write(file, lines, StandardCharsets.UTF_8);
			InputStream stream = new FileInputStream(file.toFile());
			MultipartFile multipartFile = new MockMultipartFile("logFile", file.toFile().getName(), MediaType.MULTIPART_FORM_DATA_VALUE, stream);
			logFileMock = multipartFile;
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		Set<Rendering> renderingSet = new HashSet<>();
		Rendering rendering1 = new Rendering();
		rendering1.setDocument(114466L);
		rendering1.setPage(0L);
		rendering1.setUid("1286373785873-3536");
		renderingSet.add(rendering1);
		Rendering rendering2 = new Rendering();
		rendering2.setDocument(114273L);
		rendering2.setPage(0L);
		rendering2.setUid("1286373837895-7889");
		renderingSet.add(rendering2);
		Rendering rendering3 = new Rendering();
		rendering3.setDocument(114466L);
		rendering3.setPage(0L);
		rendering3.setUid("1286373806777-5552");
		renderingSet.add(rendering3);
		Rendering rendering4 = new Rendering();
		rendering4.setDocument(114273L);
		rendering4.setPage(1L);
		rendering4.setUid("1286373841290-1204");
		renderingSet.add(rendering4);
		Rendering rendering5 = new Rendering();
		rendering5.setDocument(114466L);
		rendering5.setPage(0L);
		rendering5.setUid("1286373733634-5423");
		renderingSet.add(rendering5);
		Report report = new Report();
		report.setRendering(renderingSet);
		this.reportMock = report;
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void analyzeSampleLog() throws IOException {
		Mockito.when(documentManagerService.analyzeDocument(Mockito.any(InputStream.class))).thenReturn(reportMock);
		Summary summary = new Summary();
		summary.setCount(5L);
		summary.setDuplicates(1L);
		summary.setUnnecessary(1L);
		reportMock.setSummary(summary);
		Mockito.when(documentManagerService.summarize(Mockito.any(Report.class))).thenReturn(reportMock);
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(byteArray);
			outputStream.writeObject(reportMock);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		byte[] byteArrayReport = byteArray.toByteArray();
		Mockito.when(fileService.createFile(Mockito.any(Object.class))).thenReturn(byteArrayReport);
		File responseFile = new File("fileConverted");
		FileUtils.writeByteArrayToFile(responseFile, byteArrayReport);
		Mockito.when(fileService.convertByteArrayToFile(Mockito.any(byte[].class))).thenReturn(responseFile);
		Mockito.when(fileService.zipFile(Mockito.any(File.class))).thenReturn(byteArrayReport);
		ResponseEntity<Object> response = documentManagerController.analyzeDocument(logFileMock);
		Assert.assertTrue(response.getBody() instanceof byte[]);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void analyzeSampleLogException() {
		Mockito.when(documentManagerService.analyzeDocument(Mockito.any(InputStream.class))).thenThrow(new NullPointerException(EXCEPTION_MESSAGE));
		assertThrows(ResponseStatusException.class, () -> documentManagerController.analyzeDocument(logFileMock));
	}
	
}
