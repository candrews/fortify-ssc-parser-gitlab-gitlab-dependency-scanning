package com.example.ssc.parser.sample.alternative;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ssc.parser.sample.alternative.parser.ScanParser;
import com.example.ssc.parser.sample.alternative.parser.VulnerabilitiesParser;
import com.fortify.plugin.api.ScanBuilder;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanParsingException;
import com.fortify.plugin.api.VulnerabilityHandler;
import com.fortify.plugin.spi.ParserPlugin;

public class AlternativeSampleParserPlugin implements ParserPlugin<CustomVulnAttribute> {
    private static final Logger LOG = LoggerFactory.getLogger(AlternativeSampleParserPlugin.class);

    @Override
    public void start() throws Exception {
        LOG.info("{} is starting", this.getClass().getSimpleName());
    }

    @Override
    public void stop() throws Exception {
        LOG.info("{} is starting", this.getClass().getSimpleName());
    }

    @Override
    public Class<CustomVulnAttribute> getVulnerabilityAttributesClass() {
        return CustomVulnAttribute.class;
    }

    @Override
    public void parseScan(final ScanData scanData, final ScanBuilder scanBuilder) throws ScanParsingException, IOException {
        new ScanParser(scanData, scanBuilder).parse();
    }

	@Override
	public void parseVulnerabilities(final ScanData scanData, final VulnerabilityHandler vulnerabilityHandler) throws ScanParsingException, IOException {
		new VulnerabilitiesParser(scanData, vulnerabilityHandler).parse();
	}
}
