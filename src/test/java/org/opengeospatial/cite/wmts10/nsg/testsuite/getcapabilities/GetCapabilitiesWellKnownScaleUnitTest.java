package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opengeospatial.cite.wmts10.ets.core.domain.LayerInfo;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtilsTest;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GetCapabilitiesWellKnownScaleUnitTest {

	private static ITestContext testContext;

	private static ISuite suite;

	private static final String SUBJ = "testSubject";

	private static final String LAYER = "layerInfo";

	private static final String TEST_RESOURCE = "../NSGWMTSImplementation.xml";

	private static final String TEST_RESOURCE_INVALID = "../InvalidNSGWMTSImplementation.xml";

	@BeforeClass
	public static void setUpClass() throws Exception {
		testContext = mock(ITestContext.class);
		suite = mock(ISuite.class);
		when(testContext.getSuite()).thenReturn(suite);
	}

	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_3395_Test() throws Exception {
		GetCapabilitiesWellKnownScaleTest iut = prepareTest(TEST_RESOURCE);
		iut.wmtsCapabilitiesWellKnownScaleTest_3395_Test();
	}

	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_4326_Test() throws Exception {
		GetCapabilitiesWellKnownScaleTest iut = prepareTest(TEST_RESOURCE);
		iut.wmtsCapabilitiesWellKnownScaleTest_4326_Test();
	}

	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_5041_Test() throws Exception {
		GetCapabilitiesWellKnownScaleTest iut = prepareTest(TEST_RESOURCE);
		iut.wmtsCapabilitiesWellKnownScaleTest_5041_Test();
	}

	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_5042_Test() throws Exception {
		GetCapabilitiesWellKnownScaleTest iut = prepareTest(TEST_RESOURCE);
		iut.wmtsCapabilitiesWellKnownScaleTest_5042_Test();
	}

	@Test(expected = AssertionError.class)
	public void wmtsCapabilitiesWellKnownScaleTest_4326_Test_Invalid() throws Exception {
		GetCapabilitiesWellKnownScaleTest iut = prepareTest(TEST_RESOURCE_INVALID);
		iut.wmtsCapabilitiesWellKnownScaleTest_4326_Test();
	}

	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_3395_Test_TwoTimeOneValid() throws Exception {
		GetCapabilitiesWellKnownScaleTest iut = prepareTest(TEST_RESOURCE_INVALID);
		iut.wmtsCapabilitiesWellKnownScaleTest_3395_Test();
	}

	private GetCapabilitiesWellKnownScaleTest prepareTest(String testResource) throws Exception {

		List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo(capabilities(testResource));

		when(suite.getAttribute(SUBJ)).thenReturn(capabilities(testResource));
		when(suite.getAttribute(LAYER)).thenReturn(layerInfos);

		GetCapabilitiesWellKnownScaleTest iut = new GetCapabilitiesWellKnownScaleTest();
		iut.initBaseFixture(testContext);
		iut.buildGetCapabilitiesRequest();
		return iut;
	}

	private Document capabilities(String resource) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class.getResourceAsStream(resource);
		return builder.parse(new InputSource(wmtsCapabilities));
	}

}
