/*******************************************************************************
 * (c) Copyright 2020 Micro Focus or one of its affiliates
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to 
 * whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.example.ssc.parser.sample.alternative.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fortify.plugin.api.BasicVulnerabilityBuilder.Priority;

import lombok.Getter;

@Getter
public class Finding {
	public static enum CustomStatus {
        NEW, OPEN, REMEDIATED;
    };
    
	// mandatory attributes
    @JsonProperty private String uniqueId;

    // builtin attributes
    @JsonProperty private String category;
    @JsonProperty private String fileName;
    @JsonProperty private String vulnerabilityAbstract;
    @JsonProperty private Integer lineNumber;
    @JsonProperty private Float confidence;
    @JsonProperty private Float impact;
    @JsonProperty private Priority priority;

    // custom attributes
    @JsonProperty private String categoryId;
    @JsonProperty private String artifact;
    @JsonProperty private String description;
    @JsonProperty private String comment;
    @JsonProperty private String buildNumber;
    @JsonProperty private CustomStatus customStatus;
    @JsonProperty private Date lastChangeDate;
    @JsonProperty private Date artifactBuildDate;
    @JsonProperty private String textBase64;
}
