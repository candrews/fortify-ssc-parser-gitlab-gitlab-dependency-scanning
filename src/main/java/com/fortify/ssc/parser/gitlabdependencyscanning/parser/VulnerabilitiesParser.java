package com.fortify.ssc.parser.gitlabdependencyscanning.parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fortify.ssc.parser.gitlabdependencyscanning.CustomVulnAttribute;
import com.fortify.ssc.parser.gitlabdependencyscanning.domain.Vulnerability;
import com.fortify.ssc.parser.gitlabdependencyscanning.domain.Vulnerability.Confidence;
import com.fortify.ssc.parser.gitlabdependencyscanning.domain.Vulnerability.Identifier;
import com.fortify.plugin.api.BasicVulnerabilityBuilder.Priority;
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
			.handler("/vulnerabilities/*", Vulnerability.class, this::handleVulnerability)
			.parse(scanData);
	}
	
	/** Convert {@link Confidence} to a score between 1 and 5 (inclusive). 1 means lowest confidence, 5 means highest confidence.
	 * 
	 * @see com.fortify.plugin.api.BasicVulnerabilityBuilder#setConfidence(Float)
	 * @param confidence
	 * @return
	 */
	private static float confidenceToConfidenceScore(Confidence confidence) {
		if(confidence == null) {
			return 2.5f;
		}
		switch(confidence) {
		case IGNORE:
			return 1;
		case UNKNOWN:
			return 2.5f;
		case LOW:
			return 2;
		case MEDIUM:
			return 2.5f;
		case HIGH:
			return 3.5f;
		case CONFIRMED:
			return 5f;
		case EXPERIMENTAL:
			return 1f;
		default:
			throw new IllegalArgumentException("Unexpected value provided: " + confidence);
		}
	}
	
	private static Priority severityToPriority(Vulnerability.Severity severity) {
		if(severity == null) {
			return Priority.Medium;
		}
		switch(severity) {
		case UNKNOWN:
			return Priority.Medium;
		case INFO:
			return Priority.Low;
		case LOW:
			return Priority.Low;
		case MEDIUM:
			return Priority.Medium;
		case HIGH:
			return Priority.High;
		case CRITICAL:
			return Priority.Critical;
		default:
			throw new IllegalArgumentException("Unexpected value provided: " + severity);
		}
	}
	
    private final void handleVulnerability(Vulnerability vulnerability) {
		StaticVulnerabilityBuilder vb = vulnerabilityHandler.startStaticVulnerability(vulnerability.getId());
		vb.setAnalyzer("gemnasium");
		vb.setEngineType(ENGINE_TYPE);
        vb.setFileName(vulnerability.getLocation().getFile());
        vb.setVulnerabilityAbstract(vulnerability.getDescription());
        vb.setConfidence(confidenceToConfidenceScore(vulnerability.getConfidence()));
        vb.setImpact(5f);
        vb.setPriority(severityToPriority(vulnerability.getSeverity()));
        vb.setLikelihood(2.5f);
        vb.setProbability(2.5f);
        vb.setAccuracy(5f);
        vb.setVulnerabilityRecommendation(vulnerability.getSolution());
        vb.setPackageName(Optional.ofNullable(vulnerability.getLocation()).map(Vulnerability.Location::getDependency).map(Vulnerability.Location.Dependency::getPackageInfo).map(Vulnerability.Location.Dependency.Package::getName).orElse(null));
        vb.setCategory(vulnerability.getName());

        vb.setStringCustomAttributeValue(CustomVulnAttribute.DESCRIPTION, vulnerability.getDescription());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.MESSAGE, vulnerability.getMessage());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.NAME, vulnerability.getName());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.ID, vulnerability.getId());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.PACKAGE_NAME, Optional.ofNullable(vulnerability.getLocation()).map(Vulnerability.Location::getDependency).map(Vulnerability.Location.Dependency::getPackageInfo).map(Vulnerability.Location.Dependency.Package::getName).orElse(null));
        vb.setStringCustomAttributeValue(CustomVulnAttribute.PACKAGE_VERSION, Optional.ofNullable(vulnerability.getLocation()).map(Vulnerability.Location::getDependency).map(Vulnerability.Location.Dependency::getVersion).orElse(null));
        vb.setStringCustomAttributeValue(CustomVulnAttribute.DIRECT, Optional.ofNullable(vulnerability.getLocation()).map(Vulnerability.Location::getDependency).map(Vulnerability.Location.Dependency::getDirect).map(s -> (s ? "Yes" : "No")).orElse(null));
        vb.setStringCustomAttributeValue(CustomVulnAttribute.FILE, vulnerability.getLocation().getFile());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.SOLUTION, vulnerability.getSolution());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.CONFIDENCE, Optional.ofNullable(vulnerability.getConfidence()).map(Confidence::toString).orElse(null));
        vb.setStringCustomAttributeValue(CustomVulnAttribute.SCANNER_NAME, vulnerability.getScanner().getName());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.SCANNER_ID, vulnerability.getScanner().getId());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.IDENTIFIER_NAME, vulnerability.getIdentifiers()[0].getName());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.IDENTIFIER_TYPE, vulnerability.getIdentifiers()[0].getType());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.IDENTIFIER_URL, vulnerability.getIdentifiers()[0].getUrl());
        vb.setStringCustomAttributeValue(CustomVulnAttribute.IDENTIFIER_URLS, Arrays.stream(vulnerability.getIdentifiers()).map(Identifier::getUrl).collect(Collectors.joining("\n")));
		vb.completeVulnerability();
    }
}
