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
	private static final int DATE_INITIAL_INDEX = 0;
	private static final int DATE_FINAL_INDEX = 23;
	private static final String RETURNED = "returned";
	private static final String LEFT_BRACKET = "[";
	private static final String RIGHT_BRACKET = "]";
	private static final int START_RENDERING_LEFT_BRACKET_LINE_OCCURRENCE_NUMBER = 3;
	private static final int START_RENDERING_RIGHT_BRACKET_LINE_OCCURRENCE_NUMBER = 3;
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

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
					start = line.substring(DATE_INITIAL_INDEX, DATE_FINAL_INDEX);
					start = start.replaceFirst(",", ".");
					startTime = LocalDateTime.from(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).parse(start));
					String startRenderingArguments = line.substring(StringUtils.ordinalIndexOf(line, LEFT_BRACKET, START_RENDERING_LEFT_BRACKET_LINE_OCCURRENCE_NUMBER) + 1,
							StringUtils.ordinalIndexOf(line, RIGHT_BRACKET, START_RENDERING_RIGHT_BRACKET_LINE_OCCURRENCE_NUMBER));
					String[] startRenderingArgumentsArray = startRenderingArguments.split(",");
					documentId = startRenderingArgumentsArray[0].trim();
					page = startRenderingArgumentsArray[1].trim();
				} else if (line.contains(LogMessage.START_RENDERING_RETURN.getLogMessageContent())) {
					int uidFirstIndex = line.indexOf(RETURNED) + RETURNED.length();
					String uid = line.substring(uidFirstIndex).trim();
					Long document = Long.parseLong(documentId);
					Long pageLong = Long.parseLong(page);
					Rendering rendering = new Rendering(document, pageLong, uid);
					rederingSet.add(rendering);
					LocalDateTime newStartTime = startTime;
					rederingSet.stream().filter(r -> r.getUid().equals(uid)).findFirst().ifPresent(r -> r.getStart().add(newStartTime));
				} else if (line.contains(LogMessage.GET_RENDERING.getLogMessageContent())) {
					String getTime = line.substring(DATE_INITIAL_INDEX, DATE_FINAL_INDEX);
					getTime = getTime.replaceFirst(",", ".");
					String getRenderingArguments = line.substring(StringUtils.ordinalIndexOf(line, LEFT_BRACKET, START_RENDERING_LEFT_BRACKET_LINE_OCCURRENCE_NUMBER) + 1,
							StringUtils.ordinalIndexOf(line, RIGHT_BRACKET, START_RENDERING_RIGHT_BRACKET_LINE_OCCURRENCE_NUMBER));
					LocalDateTime dateTime = LocalDateTime
							.from(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).parse(getTime));
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
