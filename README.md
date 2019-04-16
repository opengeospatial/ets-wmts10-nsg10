## Conformance Test Suite: NSG WMTS Profile

### Scope

This test suite verifies that a WMTS implementation conforms to the NSG Web Map Tile Service 
1.0 Implementation Interoperability Profile, v1.0.0 ([NGA.STND.0063_1.0_WMTS](https://nsgreg.nga.mil/doc/view?i=4258), 
2016-09-15). The aim is to cover the MANDATORY and RECOMMENDED capabilities stipulated in 
all relevant specifications.

Note that this profile extends and restricts the specifications upon which it is based. The 
main dependencies are:

* OGC WMTS 1.0.0 (REST, KVP bindings) [OGC 07-057r7]
* OGC WSC 1.1 [OGC 06-121r3]

Visit the [project documentation website](https://opengeospatial.github.io/ets-wmts10-nsg/) 
for more information, including the API documentation.

### Dependencies and Building
Before building it you need to build ets-dgiwg-core. Do the follwoing:
* git clone https://github.com/opengeospatial/ets-dgiwg-core
* cd ets-dgiwg-core
* mvn clean install

Build the test:
* git clone https://github.com/opengeospatial/ets-wmts10-nsg
* cd ets-wmts10-nsg
* mvn clean install

It will creat the jars to be installed in TEAM Engine.


### How to run the tests
The test suite is built using [Apache Maven v3](https://maven.apache.org/). The options 
for running the suite are summarized below.

#### 1. Integrated development environment (IDE)

Use a Java IDE such as Eclipse, NetBeans, or IntelliJ. Clone the repository and build the project.

Set the main class to run: `org.opengis.cite.wfs20-nsg.TestNGController`

Arguments: The first argument must refer to an XML properties file containing the 
required test run arguments. If not specified, the default location at `$
{user.home}/test-run-props.xml` will be used.
   
You can modify the sample file in `src/main/config/test-run-props.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties version="1.0">
  <comment>Test run arguments</comment>
  <entry key="iut">http://schemas.opengis.net/gml/3.2.1/gml.xsd</entry>
</properties>
```

The TestNG results file (`testng-results.xml`) will be written to a subdirectory
in `${user.home}/testng/` having a UUID value as its name.

#### 2. Command shell (console)

One of the build artifacts is an "all-in-one" JAR file that includes the test 
suite and all of its dependencies; this makes it very easy to execute the test 
suite in a command shell:

`java -jar ets-wmts10-nsg-0.1-SNAPSHOT-aio.jar [-o|--outputDir $TMPDIR] [test-run-props.xml]`

#### 3. OGC test harness

Use [TEAM Engine](https://github.com/opengeospatial/teamengine), the official OGC test harness.
The latest test suite release are usually available at the [beta testing facility](http://cite.opengeospatial.org/te2/). 
You can also [build and deploy](https://github.com/opengeospatial/teamengine) the test 
harness yourself and use a local installation.


### How to contribute

If you would like to get involved, you can:

* [Report an issue](https://github.com/opengeospatial/ets-wmts10-nsg/issues) such as a defect or 
an enhancement request
* Help to resolve an [open issue](https://github.com/opengeospatial/ets-wmts10-nsg/issues?q=is%3Aopen)
* Fix a bug: Fork the repository, apply the fix, and create a pull request
* Add new tests: Fork the repository, implement and verify the tests on a new topic branch, 
and create a pull request (don't forget to periodically rebase long-lived branches so 
there are no extraneous conflicts)
