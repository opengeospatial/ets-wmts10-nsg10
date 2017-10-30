package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

public class GetCapabilitiesProjectionUnitTest {

    private static ITestContext testContext;

    private static ISuite suite;

    private static final String SUBJ = "testSubject";

    private static final String LAYER = "layerInfo";

    private static final String TEST_RESOURCE = "../NSGWMTSImplementation.xml";

    @BeforeClass
    public static void setUpClass()
                            throws Exception {
        testContext = mock( ITestContext.class );
        suite = mock( ISuite.class );
        when( testContext.getSuite() ).thenReturn( suite );
    }

    @AfterClass
    public static void tearDownClass()
                            throws Exception {
    }

    @Test
    public void testwmtsCapabilitiesExists()
                            throws Exception {
        GetCapabilitiesProjectionTest iut = prepareTest();
        iut.wmtsCapabilitiesExists();
    }

    @Test
    public void wmtsCapabilitiesEPSG3395Test()
                            throws Exception {
        GetCapabilitiesProjectionTest iut = prepareTest();
        iut.wmtsCapabilitiesEPSG3395Test();
    }

    @Test
    public void wmtsCapabilitiesUPS_NorthTest()
                            throws Exception {
        GetCapabilitiesProjectionTest iut = prepareTest();
        iut.wmtsCapabilitiesUPS_NorthTest();
    }

    @Test
    public void wmtsCapabilitiesUPS_SouthTest()
                            throws Exception {
        GetCapabilitiesProjectionTest iut = prepareTest();
        iut.wmtsCapabilitiesUPS_SouthTest();
    }

    private GetCapabilitiesProjectionTest prepareTest()
                            throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( capabilities( TEST_RESOURCE ) );

        when( suite.getAttribute( SUBJ ) ).thenReturn( capabilities( TEST_RESOURCE ) );
        when( suite.getAttribute( LAYER ) ).thenReturn( layerInfos );

        GetCapabilitiesProjectionTest iut = new GetCapabilitiesProjectionTest();
        iut.initBaseFixture( testContext );
        return iut;
    }

    private Document capabilities( String resource )
                            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class.getResourceAsStream( resource );
        return builder.parse( new InputSource( wmtsCapabilities ) );
    }

}
