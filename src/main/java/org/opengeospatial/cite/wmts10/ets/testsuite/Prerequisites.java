package org.opengeospatial.cite.wmts10.ets.testsuite;

import static org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion.assertSimpleWMTSCapabilities;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.opengeospatial.cite.wmts10.ets.core.client.WmtsClient;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.domain.SuiteAttribute;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;

/**
 * Confirms the readiness of the SUT to undergo testing. If any of these configuration
 * methods fail then all remaining tests in the suite are skipped.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS and/or WMTS)
 */
public class Prerequisites {

	/**
	 * Verifies that the service capabilities description is a WMTS Capabilities document.
	 * @param testContext the test run context, never <code>null</code>
	 */
	@Test
	public void verifyServiceDescription(ITestContext testContext) {
		Document wmtsMetadata = (Document) testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
		assertSimpleWMTSCapabilities(wmtsMetadata);
	}

	/**
	 * Confirms that the SUT is available and produces a service description in response
	 * to a basic GetCapabilities request. The document element is expected to have the
	 * following infoset properties:
	 * <ul>
	 * <li>[local name] = "WMTS_Capabilities"</li>
	 * <li>[namespace name] = "http://www.opengis.net/wmts"</li>
	 * </ul>
	 * @param testContext the test context
	 */
	@Test(dependsOnMethods = { "verifyServiceDescription" })
	public void serviceIsAvailable(ITestContext testContext) {
		Document wmtsMetadata = (Document) testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
		WmtsClient wmtsClient = new WmtsClient(wmtsMetadata);
		Document capabilities = wmtsClient.getCapabilities();
		assertNotNull(capabilities, "No GetCapabilities response from SUT.");
		Element docElement = capabilities.getDocumentElement();
		assertEquals(docElement.getLocalName(), WMTS_Constants.WMTS_CAPABILITIES,
				"Capabilities document element has unexpected [local name].");
		assertEquals(docElement.getNamespaceURI(), WmtsNamespaces.WMTS,
				"Capabilities document element has unexpected [namespace name].");
	}

}