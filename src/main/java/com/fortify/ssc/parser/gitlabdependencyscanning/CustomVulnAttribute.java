package com.fortify.ssc.parser.gitlabdependencyscanning;

/**
 * (c) Copyright [2017] Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public enum CustomVulnAttribute implements com.fortify.plugin.spi.VulnerabilityAttribute {
	DESCRIPTION(AttrType.LONG_STRING),
	MESSAGE(AttrType.STRING),
	NAME(AttrType.STRING),
	PACKAGE_NAME(AttrType.STRING),
	PACKAGE_VERSION(AttrType.STRING),
	DIRECT(AttrType.STRING),
	FILE(AttrType.STRING),
	ID(AttrType.STRING),
	SOLUTION(AttrType.STRING),
	CONFIDENCE(AttrType.STRING),
	SCANNER_NAME(AttrType.STRING),
	SCANNER_ID(AttrType.STRING),
	IDENTIFIER_NAME(AttrType.STRING),
	IDENTIFIER_TYPE(AttrType.STRING),
	IDENTIFIER_URL(AttrType.STRING),
	IDENTIFIER_URLS(AttrType.LONG_STRING),
    ;

    private final AttrType attributeType;

    CustomVulnAttribute(final AttrType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String attributeName() {
        return name();
    }

    @Override
    public AttrType attributeType() {
        return attributeType;
    }
}
