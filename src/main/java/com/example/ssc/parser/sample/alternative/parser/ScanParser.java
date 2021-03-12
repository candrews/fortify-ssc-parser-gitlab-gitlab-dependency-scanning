package com.example.ssc.parser.sample.alternative.parser;

import java.io.IOException;

import com.fortify.plugin.api.ScanBuilder;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanParsingException;
import com.fortify.util.jackson.IsoDateTimeConverter;
import com.fortify.util.ssc.parser.json.ScanDataStreamingJsonParser;

public class ScanParser {
	private final ScanData scanData;
    private final ScanBuilder scanBuilder;
    
	public ScanParser(final ScanData scanData, final ScanBuilder scanBuilder) {
		this.scanData = scanData;
		this.scanBuilder = scanBuilder;
	}
	
	public final void parse() throws ScanParsingException, IOException {
		new ScanDataStreamingJsonParser()
			.handler("/engineVersion", jp -> scanBuilder.setEngineVersion(jp.getValueAsString()))
			.handler("/scanDate", jp -> scanBuilder.setScanDate(IsoDateTimeConverter.getInstance().convert(jp.getValueAsString())))
			.handler("/buildServer", jp -> scanBuilder.setHostName(jp.getValueAsString()))
			.handler("/elapsed", jp -> scanBuilder.setElapsedTime(jp.getValueAsInt()))
			.parse(scanData);
		scanBuilder.completeScan();
	}
}
