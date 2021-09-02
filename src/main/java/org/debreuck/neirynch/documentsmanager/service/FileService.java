package org.debreuck.neirynch.documentsmanager.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	private static final Logger LOG = Logger.getLogger(FileService.class);

	public byte[] createFile(Object object) {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(byteArray);
			outputStream.writeObject(object);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return byteArray.toByteArray();
	}
	
	public File convertByteArrayToFile(byte[] sourceByteArray) {
		File sourceFile = null;
		try {
			sourceFile = new File("source_file");
			FileUtils.writeByteArrayToFile(sourceFile, sourceByteArray);
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return sourceFile;
	}

	public byte[] zipFile(File sourceFile) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
				FileInputStream fileInputStream = FileUtils.openInputStream(sourceFile);) {
			ZipEntry zipEntry = new ZipEntry(sourceFile.getName() + ".zip");
			zipOutputStream.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
	        int length;
	        while((length = fileInputStream.read(bytes)) >= 0) {
	        	zipOutputStream.write(bytes, 0, length);
	        }
			zipOutputStream.closeEntry();
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return new byte[0];
	}
}
