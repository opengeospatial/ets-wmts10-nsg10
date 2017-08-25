# NSG – Web Map Tile Service 1.0 Profile Conformance Test Suite

## Scope

This test suite verifies that a WMTS implementation conforms to the NSG Wem Map Tile Service 
1.0 Implementation Interoperability Profile, v1.0.0 ([NGA.STND.0063_1.0_WMTS](https://nsgreg.nga.mil/doc/view?i=4258), 
2016-09-15). The aim is to cover the MANDATORY and RECOMMENDED capabilities stipulated in 
all relevant specifications.

Note that this profile extends and restricts the specifications upon which it is based. The 
main dependencies are:

* OGC WMTS 1.0.0 (REST, KVP bindings) [OGC 07-057r7]

Visit the [project documentation website](http://opengeospatial.github.io/ets-wmts10-nsg/) 
for more information, including the API documentation.

Conformance testing is a kind of "black box" testing that examines the externally 
visible characteristics or behaviors of the IUT while disregarding any implementation details.

## What is tested

  - ...


## What is not tested

  - ...


## Test requirements

The documents listed below stipulate requirements that must be satisfied by a 
conforming implementation.

1. [NGA.STND.0063_1.0_WMTS](https://nsgreg.nga.mil/doc/view?i=4258)
2. [Web Map Tile Service Implementation Specification, Version 1.0.0 ( 07-057r7)](http://portal.opengeospatial.org/files/?artifact_id=35326)

If any of the following preconditions are not satisfied then all tests in the 
suite will be marked as skipped.

1. WMS capabilities document must be available.


## Test suite structure

The test suite definition file (testng.xml) is located in the root package, 
`org.opengeospatial.cite.wmts10.nsg`. A group corresponds to a &lt;test&gt; element, each 
of which includes a set of test classes that contain the actual test methods. 
The general structure of the test suite is shown in Table 1.

<table>
  <caption>Table 1 - Test suite structure</caption>
  <thead>
    <tr style="text-align: left; background-color: LightCyan">
      <th>Group</th>
      <th>Test classes</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Preconditions</td>
      <td>org.opengeospatial.cite.wmts10.nsg.ets.testsuite.Prerequisites</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 1</td>
      <td>Not yet tested (Reqirement is passed when Requirements 2, 4, 5, 7, 8, and 10 pass)</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 2</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesOperations</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 3</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesSoap</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Service Metadata Content Test (Requirements 4,7</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.ServiceMetadataContent</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 4</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesRest</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 5</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.gettile.GetTileParametersKvp</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 6</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.gettile.GetTileParametersSoap</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 7</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.gettile.GetTileParametersRest</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 8</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo.GetFeatureInfoKvp</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 9</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo.GetFeatureInfoSoap</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 10</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo.GetFeatureInfoRest</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 11</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesCrsTest</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 12</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesProjectionTest</td>
    </tr>
    <tr>
      <td>NSG Test A.3 - Server Test Module;  Requirement 13</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesWellKnownScaleTest</td>
    </tr>
    <tr>
      <td>G Test A.3 - Server Test Module;  Requirement 14</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.gettile.GetTileOfferings</td>
    </tr>
    <tr>
      <td>G Test A.3 - Server Test Module;  Requirement 15</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesKvpVersionTest</td>
    </tr>
    <tr>
      <td>G Test A.3 - Server Test Module;  Requirement 16</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesSoapVersionTest</td>
    </tr>
    <tr>
      <td>G Test A.3 - Server Test Module;  Requirement 17</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesKeywordTest</td>
    </tr>
    <tr>
      <td>G Test A.3 - Server Test Module;  Requirement 18</td>
      <td>Test is missing as pending changes expected to NMF.</td>
    </tr>
    <tr>
      <td>G Test A.3 - Server Test Module;  Requirement 19</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.gettile.GetTileCachingInfo</td>
    </tr>
    <tr>
      <td>G Test A.3 - Server Test Module;  Requirement 20</td>
      <td>org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo.GetFeatureInfoResponse</td>
    </tr>
  </tbody>
</table>

The Javadoc documentation provides more detailed information about the test 
methods that constitute the suite.


## Test run arguments

The test run arguments are summarized in Table 2. The _Obligation_ descriptor can 
have the following values: M (mandatory), O (optional), or C (conditional).

<table>
  <caption>Table 2 -Test run arguments</caption>
  <thead>
    <tr style="text-align: left; background-color: LightCyan">
      <th>Name</th>
      <th>Value domain</th>
      <th>Obligation</th>
  	  <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>wmts</td>
      <td>URI</td>
      <td>M</td>
	  <td>A URI that refers to the implementation under test or metadata about it.
      Ampersand ('&amp;') characters must be percent-encoded as '%26'.</td>
    </tr>
  </tbody>
</table>


## Test Lead

  - ...


##  Contributors

  - ...


##  License

[Apache License, Version 2.0](http://opensource.org/licenses/Apache-2.0 "Apache License")
