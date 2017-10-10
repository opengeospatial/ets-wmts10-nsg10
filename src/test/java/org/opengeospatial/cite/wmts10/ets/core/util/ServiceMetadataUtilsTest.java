package org.opengeospatial.cite.wmts10.ets.core.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils.parseLayers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.opengeospatial.cite.wmts10.ets.core.domain.LayerInfo;
import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class ServiceMetadataUtilsTest {

    @Test
    public void testGetOperationBindings()
                            throws Exception {
        Set<ProtocolBinding> globalBindings = ServiceMetadataUtils.getOperationBindings( wmtsCapabilities(), "GetTile" );

        assertThat( globalBindings.size(), is( 1 ) );
        assertThat( globalBindings, hasItems( ProtocolBinding.GET ) );
    }

    @Test
    public void testGetOperationEndpoint()
                            throws Exception {
        URI endpointUri = ServiceMetadataUtils.getOperationEndpoint_KVP( wmtsCapabilities(), "GetTile",
                                                                         ProtocolBinding.GET );

        assertThat( endpointUri, is( new URI( "http://cite.deegree.org/deegree-webservices-3.4-RC3/services/wmts100" ) ) );
    }

    @Test
    public void testGetOperationEndpointUnsupportedProtocol()
                            throws Exception {
        URI endpointUri = ServiceMetadataUtils.getOperationEndpoint_REST( wmtsCapabilities(), "GetTile",
                                                                          ProtocolBinding.POST );

        assertThat( endpointUri, is( nullValue() ) );
    }

    @Test
    public void testGetOperationEndpoint_Soap()
                            throws Exception {
        URI soapEndpoint = ServiceMetadataUtils.getOperationEndpoint_SOAP( wmtsCapabilities(),
                                                                             "GetFeatureInfo",
                                                                             ProtocolBinding.POST );

        assertThat( soapEndpoint, is( new URI( "http://ips.terrapixel.com/terrapixel/cubeserv.cgi" ) ) );
    }

    @Test
    public void testParseLayerInfo()
                            throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );

        assertThat( layerInfos.size(), is( 1 ) );
    }

    @Test
    public void testParseAllLayerNodes()
                            throws Exception {
        NodeList allLayerNodes = parseLayers( wmtsCapabilities() );

        assertThat( allLayerNodes.getLength(), is( 1 ) );
    }

    @Test(expected = Exception.class)
    public void testParseAllLayerNodesWithNullShouldThrowException()
                            throws Exception {
        parseLayers( null );
    }

    @Test(expected = Exception.class)
    public void testParseRequestableLayerNodesWithNullShouldThrowException()
                            throws Exception {
        parseLayers( null );
    }

    private Document wmtsCapabilities()
                            throws SAXException, IOException, ParserConfigurationException {
        return capabilities( "../capabilities_wmts10.xml" );
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