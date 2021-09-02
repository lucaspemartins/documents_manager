package org.debreuck.neirynch.documentsmanager.service;

import java.io.File;

import org.debreuck.neirynch.documentsmanager.model.Report;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class FileServiceTests {

	@Test
	void createFile() {
		Report report = new Report();
		Assert.assertTrue(new FileService().createFile(report) instanceof byte[]);
	}
	
	@Test
	void convertByteArrayToFile() {
		byte[] byteArray = new byte[10];
		Assert.assertTrue(new FileService().convertByteArrayToFile(byteArray) instanceof File);
	}
	
	@Test
	void zipFile() {
		byte[] byteArray = "1st position".getBytes();
		FileService fileService = new FileService();
		File file = fileService.convertByteArrayToFile(byteArray);
		Assert.assertTrue(new FileService().zipFile(file) instanceof byte[]);
	}
}
