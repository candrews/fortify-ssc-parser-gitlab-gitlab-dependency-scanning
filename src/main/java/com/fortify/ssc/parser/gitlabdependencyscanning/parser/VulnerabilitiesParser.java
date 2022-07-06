package com.fortify.ssc.parser.gitlabdependencyscanning.parser;

import java.io.IOException;

import com.fortify.ssc.parser.gitlabdependencyscanning.CustomVulnAttribute;
import com.fortify.ssc.parser.gitlabdependencyscanning.domain.Finding;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanParsingException;
import com.fortify.plugin.api.StaticVulnerabilityBuilder;
import com.fortify.plugin.api.VulnerabilityHandler;
import com.fortify.util.ssc.parser.EngineTypeHelper;
import com.fortify.util.ssc.parser.json.ScanDataStreamingJsonParser;

public class VulnerabilitiesParser {
	private static final String ENGINE_TYPE = EngineTypeHelper.getEngineType();
	private final ScanData scanData;
	private final VulnerabilityHandler vulnerabilityHandler;

    public VulnerabilitiesParser(final ScanData scanData, final VulnerabilityHandler vulnerabilityHandler) {
    	this.scanData = scanData;
		this.vulnerabilityHandler = vulnerabilityHandler;
	}
    
    /**
	 * Main method to commence parsing the input provided by the configured {@link ScanData}.
	 * @throws ScanParsingException
	 * @throws IOException
	 */
	public final void parse() throws ScanParsingException, IOException {
		new ScanDataStreamingJsonParser()
			.handler("/findings/*", Finding.class, this::handleFinding)
			.parse(scanData);
	}
	
    private final void handleFinding(Finding finding) {
		StaticVulnerabilityBuilder vb = vulnerabilityHandler.startStaticVulnerability(finding.getUniqueId());
		// Set builtin attributes
		vb.setEngineType(ENGINE_TYPE);
        vb.setCategory(finding.getCategory());                             // REST -> issueName
        vb.setFileName(finding.getFileName());                             // REST -> fullFileName or shortFileName
        vb.setVulnerabilityAbstract(finding.getVulnerabilityAbstract());   // REST -> brief
        vb.setLineNumber(finding.getLineNumber());                         // REST -> N/A, UI issue table -> part of Primary Location
        vb.setConfidence(finding.getConfidence());                         // REST -> confidence
        vb.setImpact(finding.getImpact());                                 // REST -> impact
        vb.setPriority(finding.getPriority());                             // REST -> friority, UI issue table -> Criticality
        
        vb.setStringCustomAttributeValue(CustomVulnAttribute.uniqueId, finding.getUniqueId());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.categoryId, finding.getCategoryId());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.artifact, finding.getArtifact());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.buildNumber, finding.getBuildNumber());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.customStatus, finding.getCustomStatus()==null?null:finding.getCustomStatus().name());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.description, finding.getDescription());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.comment, finding.getComment());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.textBase64, finding.getTextBase64());
        vb.setDateCustomAttributeValue(CustomVulnAttribute.lastChangeDate, finding.getLastChangeDate());
        vb.setDateCustomAttributeValue(CustomVulnAttribute.artifactBuildDate, finding.getArtifactBuildDate());
		vb.completeVulnerability();
    }
}
