package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
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
	List<LayerInfo> layerInfos;
	GetCapabilitiesWellKnownScaleTest iut;

	private static ITestContext testContext;
	private static ISuite suite;
	private static final String SUBJ = "testSubject";
	private static final String LAYER = "layerInfo";
	private static final String file = "../NSGWMTSImplementation.xml";
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		testContext = mock(ITestContext.class);
		suite = mock(ISuite.class);
		when(testContext.getSuite()).thenReturn(suite);
		
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}


	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_3395_Test() {
		try {
			prepareTest(file);
			this.iut.wmtsCapabilitiesWellKnownScaleTest_3395_Test();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_4326_Test() {
		try {
			prepareTest(file);
			this.iut.wmtsCapabilitiesWellKnownScaleTest_4326_Test();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_5041_Test() {
		try {
			prepareTest(file);
			this.iut.wmtsCapabilitiesWellKnownScaleTest_5041_Test();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void wmtsCapabilitiesWellKnownScaleTest_5042_Test() {
		try {
			prepareTest(file);
			this.iut.wmtsCapabilitiesWellKnownScaleTest_5042_Test();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void prepareTest(String resource) throws ParserConfigurationException, SAXException, IOException {

		try {
			layerInfos = ServiceMetadataUtils.parseLayerInfo(capabilities(resource));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		when(suite.getAttribute(SUBJ)).thenReturn(capabilities(resource));		
		when(suite.getAttribute(LAYER)).thenReturn(layerInfos);

		this.iut = new GetCapabilitiesWellKnownScaleTest();	
		this.iut.initBaseFixture(testContext);
		this.iut.buildGetCapabilitiesRequest();
	}

	private Document capabilities(String resource) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class.getResourceAsStream(resource);
		return builder.parse(new InputSource(wmtsCapabilities));
	}

}
