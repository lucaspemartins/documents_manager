package org.debreuck.neirynch.documentsmanager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.debreuck.neirynch.documentsmanager.model.LogMessage;
import org.debreuck.neirynch.documentsmanager.model.Rendering;
import org.debreuck.neirynch.documentsmanager.model.Report;
import org.springframework.stereotype.Service;

@Service
public class DocumentManagerService {
	
	private static final Logger LOG = Logger.getLogger(DocumentManagerService.class);

	public Report analyzeDocument(InputStream logFile) {
		try (InputStream in = logFile; BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line = null;
			String documentId = null;
			String page = null;
			String start = null;
			LocalDateTime startTime = null;
			Set<Rendering> rederingSet = new HashSet<>();
			while ((line = reader.readLine()) != null) {
				if (line.contains(LogMessage.START_RENDERING.getLogMessageContent())) {
					start = line.substring(0, 23);
					start = start.replaceFirst(",", ".");
					startTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").parse(start));
					String startRenderingArguments = line.substring(StringUtils.ordinalIndexOf(line, "[", 3) + 1,
							StringUtils.ordinalIndexOf(line, "]", 3));
					String[] startRenderingArgumentsArray = startRenderingArguments.split(",");
					documentId = startRenderingArgumentsArray[0].trim();
					page = startRenderingArgumentsArray[1].trim();
				} else if (line.contains(LogMessage.START_RENDERING_RETURN.getLogMessageContent())) {
					int uidFirstIndex = line.indexOf("returned") + 8;
					String uid = line.substring(uidFirstIndex).trim();
					Long document = Long.parseLong(documentId);
					Long pageLong = Long.parseLong(page);
					Rendering rendering = new Rendering(document, pageLong, uid);
					rederingSet.add(rendering);
					LocalDateTime newStartTime = startTime;
					rederingSet.stream().filter(r -> r.getUid().equals(uid)).findFirst().ifPresent(r -> r.getStart().add(newStartTime));
				} else if (line.contains(LogMessage.GET_RENDERING.getLogMessageContent())) {
					String getTime = line.substring(0, 23);
					getTime = getTime.replaceFirst(",", ".");
					String getRenderingArguments = line.substring(StringUtils.ordinalIndexOf(line, "[", 3) + 1,
							StringUtils.ordinalIndexOf(line, "]", 3));
					LocalDateTime dateTime = LocalDateTime
							.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").parse(getTime));
					rederingSet.stream().filter(r -> r.getUid().equals(getRenderingArguments)).findFirst().ifPresent(r -> r.getGet().add(dateTime));
				}
			}
			return new Report(rederingSet);
		} catch (IOException x) {
			LOG.error(x.getMessage());
			return null;
		}
	}
	
	public Report summarize(Report report) {
			report.getSummary().setCount(Long.valueOf(report.getRendering().size()));
			long duplicates = report.getRendering().stream().filter(r -> r.getStart().size() > 1).count();
			report.getSummary().setDuplicates(duplicates);
			long unnecessary = report.getRendering().stream().filter(r -> r.getGet().isEmpty()).count();
			report.getSummary().setUnnecessary(unnecessary);
			return report;
	}
}
