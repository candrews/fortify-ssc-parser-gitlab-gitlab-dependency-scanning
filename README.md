<x-tag-head>
<x-tag-meta http-equiv="X-UA-Compatible" content="IE=edge"/>

<x-tag-script language="JavaScript"><!--
<X-INCLUDE url="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.0.0/build/highlight.min.js"/>
--></x-tag-script>

<x-tag-script language="JavaScript"><!--
<X-INCLUDE url="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js" />
--></x-tag-script>

<x-tag-script language="JavaScript"><!--
<X-INCLUDE url="${gradleHelpersLocation}/spa_readme.js" />
--></x-tag-script>

<x-tag-style><!--
<X-INCLUDE url="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.0.0/build/styles/github.min.css" />
--></x-tag-style>

<x-tag-style><!--
<X-INCLUDE url="${gradleHelpersLocation}/spa_readme.css" />
--></x-tag-style>
</x-tag-head>

# Fortify SSC Sample Parser Plugin

## Introduction

This project provides an alternative implementation for the Fortify SSC sample
parser provided at https://github.com/fortify/sample-parser.

### Related Links

* **Downloads**: https://github.com/fortify-ps/fortify-ssc-parser-sample/releases
    * _Development releases may be unstable or non-functional. The `*-thirdparty.zip` file is for informational purposes only and does not need to be downloaded._
* **Sample input files**: [sampleData](sampleData)
* **GitHub**: https://github.com/fortify-ps/fortify-ssc-parser-sample
* **Automated builds**: https://github.com/fortify-ps/fortify-ssc-parser-sample/actions
* **Original sample parser**: https://github.com/fortify/sample-parser

## Comparison with original sample parser

Compared to the original sample parser:

* This project only provides parser functionality; it currently doesn't include
  functionality for generating sample parser input.
* This alternative implementation provides better separation of concerns:
    * The main parser plugin class just provides very simple implementations for 
     the parser SPI methods; actual parsing is done by dedicated parser classes.
    * Functionality for technical JSON parsing (looking for start and end of objects/arrays) is provided by the [fortify-ssc-parser-util](https://github.com/fortify-ps/fortify-ssc-parser-util) library
    * Parser implementations define handlers or use domain objects containing @JsonPropery-annotated fields for handling specific JSON elements.
* This implementation includes some incomplete unit tests. These unit tests will
  try to parse a sample input file, failing if there are any parsing exceptions.
  Information about the parsed data is sent to stderr, but the unit tests don't
  test whether the actual data was parsed correctly.
  
## Developing your own parser plugin

The official documentation for developing SSC parser plugins is available at
https://github.com/fortify/plugin-api, and the official sample plugin implementation is available at https://github.com/fortify/sample-parser.

If you want to use this alternative example as a starting point for developing your own custom SSC parser plugin:

* Generic changes:
    * Update `settings.gradle` according to the name of your project.
    * Rename packages/classes according to the type of data that you
      will be parsing.
    * Update `src/main/resources/images` with your plugin icon and logo.
    * Update `src/main/resources/resources` with relevant message properties.
    * Update `src/main/resources/viewtemplate` with the SSC view template
      used to display issue details.
    * Update `src/main/resources/plugin.xml` with your plugin name, 
      icon/logo, message property files, view template, ...
    * Update CustomVulnAttribute.java to reflect the custom issue
      attributes that can be reported by your plugin.
    * Implement the actual functionality for parsing your data 
      (see below).
    * Update the main parser plugin class to invoke your actual
      parser implementations, and log the appropriate start/stop
      messages.
    * Add one or more sample input files in `sampleData`,
      and create/update corresponding JUnit tests (see 
      AlternativeSampleParserPluginTest.java).
    * Update `src/main/resources/plugin.xml` based on all changes
      above and other plugin configuration.
    * Update `src/main/resources/META-INF/services/com.fortify.plugin.spi.ParserPlugin`
      to point to your renamed parser plugin class.
    * If you need any additional/other dependencies for your plugin,
      update `build.gradle`.
     
* If you need to parse JSON data, use the [fortify-ssc-parser-util-json](https://github.com/fortify-ps/fortify-ssc-parser-util/tree/master/fortify-ssc-parser-util-json) library
* If you need to parse XML data, use the [fortify-ssc-parser-util-xml](https://github.com/fortify-ps/fortify-ssc-parser-util/tree/master/fortify-ssc-parser-util-xml) library
* If you need to parse other types of data, consider developing a similar library and contributing this back to the [fortify-ssc-parser-util](https://github.com/fortify-ps/fortify-ssc-parser-util) project


## Plugin Installation

These sections describe how to install, upgrade and uninstall the plugin.

### Install & Upgrade

* Obtain the plugin binary jar file
	* Either download from Bintray (see [Related Links](#related-links)) 
	* Or by building yourself (see [Developers](#developers))
* If you already have another version of the plugin installed, first uninstall the previously 
 installed version of the plugin by following the steps under [Uninstall](#uninstall) below
* In Fortify Software Security Center:
	* Navigate to Administration->Plugins->Parsers
	* Click the `NEW` button
	* Accept the warning
	* Upload the plugin jar file
	* Enable the plugin by clicking the `ENABLE` button
  
### Uninstall

* In Fortify Software Security Center:
	* Navigate to Administration->Plugins->Parsers
	* Select the parser plugin that you want to uninstall
	* Click the `DISABLE` button
	* Click the `REMOVE` button 


## Obtain results

Sample data is included in the [sampleData](sampleData) directory. Additional sample data can be obtained using the original sample parser code; see https://github.com/fortify/sample-parser#generating-scan-with-fixed-or-random-data for instructions.

## Upload results

* Create a scan.info file containing a single line as follows:  
  `engineType=SAMPLE_ALTERNATIVE`
* Create a zip file containing the following:
	* The scan.info file generated in the previous step
	* The JSON file containing scan results
* Upload the zip file generated in the previous step to SSC
	* Using the SSC web interface or any SSC client like FortifyClient
	* In the SSC web interface, you do not need to enable the `3rd party results` checkbox; SSC will automatically determine the engine type based on the scan.info file included in the zip file
	* Similar to how you would upload an FPR file
	




## Developers

The following sections provide information that may be useful for developers of this utility.

### IDE's

This project uses Lombok. In order to have your IDE compile this project without errors, 
you may need to add Lombok support to your IDE. Please see https://projectlombok.org/setup/overview 
for more information.

### Gradle Wrapper

It is strongly recommended to build this project using the included Gradle Wrapper
scripts; using other Gradle versions may result in build errors and other issues.

The Gradle build uses various helper scripts from https://github.com/fortify-ps/gradle-helpers;
please refer to the documentation and comments in included scripts for more information. 

### Common Commands

All commands listed below use Linux/bash notation; adjust accordingly if you
are running on a different platform. All commands are to be executed from
the main project directory.

* `./gradlew tasks --all`: List all available tasks
* Build: (plugin binary will be stored in `build/libs`)
	* `./gradlew clean build`: Clean and build the project
	* `./gradlew build`: Build the project without cleaning
	* `./gradlew dist distThirdParty`: Build distribution zip and third-party information bundle
* `./fortify-scan.sh`: Run a Fortify scan; requires Fortify SCA to be installed

### Automated Builds

This project uses GitHub Actions workflows to perform automated builds for both development and production releases. All pushes to the main branch qualify for building a production release. Commits on the main branch should use [Conventional Commit Messages](https://www.conventionalcommits.org/en/v1.0.0/); it is recommended to also use conventional commit messages on any other branches.

User-facing commits (features or fixes) on the main branch will trigger the [release-please-action](https://github.com/google-github-actions/release-please-action) to automatically create a pull request for publishing a release version. This pull request contains an automatically generated CHANGELOG.md together with a version.txt based on the conventional commit messages on the main branch. Merging such a pull request will automatically publish the production binaries and Docker images to the locations described in the [Related Links](#related-links) section.

Every push to a branch in the GitHub repository will also automatically trigger a development release to be built. By default, development releases are only published as build job artifacts. However, if a tag named `dev_<branch-name>` exists, then development releases are also published to the locations described in the [Related Links](#related-links) section. The `dev_<branch-name>` tag will be automatically updated to the commit that triggered the build.


## License
<x-insert text="<!--"/>

See [LICENSE.TXT](LICENSE.TXT)

<x-insert text="-->"/>

<x-include url="file:LICENSE.TXT"/>

