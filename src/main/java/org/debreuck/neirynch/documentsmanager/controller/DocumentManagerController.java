package org.debreuck.neirynch.documentsmanager.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.debreuck.neirynch.documentsmanager.model.Report;
import org.debreuck.neirynch.documentsmanager.service.DocumentManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/document")
public class DocumentManagerController {
	
	private static final Logger LOG = Logger.getLogger(DocumentManagerController.class.getName());
	
	@Autowired
	DocumentManagerService documentManagerService;
	@ApiOperation(value = "Retorna a análise do log da aplicação de renderização de documentos")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Retorna a análise do arquivo de log", response = Report.class),
		    @ApiResponse(code = 400, message = "Parâmetro do arquivo de log vazio"),
		    @ApiResponse(code = 500, message = "Exceção de conversão de arquivo gerada")
		})
	@PostMapping(path = "/analyze", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Report> analyzeDocument(@RequestPart("logFile") MultipartFile logFile) throws IOException {
		
		Report reportFile = null;
		try {
			InputStream logFileInputStream = new ByteArrayInputStream(logFile.getBytes());
			reportFile = documentManagerService.analyzeDocument(logFileInputStream);
			reportFile = documentManagerService.summarize(reportFile); 
			return new ResponseEntity<>(reportFile, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
}
