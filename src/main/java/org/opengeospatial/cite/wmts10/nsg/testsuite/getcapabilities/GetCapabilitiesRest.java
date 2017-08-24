package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.annotations.Test;
import org.testng.util.Strings;
import org.w3c.dom.Document;

import de.latlon.ets.core.util.URIUtils;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesRest extends AbstractBaseGetCapabilitiesFixture {
    /**
     * --- NSG Requirement 4: An NSG WMTS server shall generate a ServiceMetadata document in response to a
     * GetResourceRepresentation request in REST architecture. ---
     */

    private URI restURI;

    private boolean _debug = false;

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 4", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesRESTCapable()
                            throws XPathExpressionException, XPathFactoryConfigurationException, URISyntaxException {
        restURI = ServiceMetadataUtils.getOperationEndpoint_REST( wmtsCapabilities, WMTS_Constants.GET_CAPABILITIES,
                                                                  ProtocolBinding.GET );
        if ( restURI == null ) {
            String restURIstr = (String) createXPath().evaluate( "//wmts:ServiceMetadataURL/@xlink:href",
                                                                 wmtsCapabilities, XPathConstants.STRING );
            if ( !Strings.isNullOrEmpty( restURIstr ) ) {
                restURI = new URI( restURIstr );
            }
        }
        assertTrue( this.restURI != null, "This WMTS does not support REST" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 4", dependsOnMethods = "wmtsCapabilitiesRESTCapable")
    public void wmtsCapabilitiesRESTReponseTest() {
        assertTrue( restURI != null, "There is no REST URL to test against" );
        String restURIstr = restURI.toString();
        assertUrl( restURIstr );

        try {
            Document responseDoc = URIUtils.resolveURIAsDocument( restURI );
            assertTrue( WMTS_Constants.WMTS_CAPABILITIES.equals( responseDoc.getDocumentElement().getLocalName() ),
                        "Invalid REST request for WMTS ServeiceMetadata capabilities document: "
                                                + responseDoc.getDocumentElement().getNodeName() );
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
            if ( this._debug ) {
                e.printStackTrace();
            }
            assertTrue( false,
                        "Error found when retrieving REST  WMTS ServeiceMetadata capabilities document: "
                                                + e.getMessage() );
        }
    }

    private XPath createXPath()
                            throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }

}