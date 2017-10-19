package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.core.util.WMTS_SOAPcontainer;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.annotations.Test;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesSoapVersionTest extends AbstractBaseGetCapabilitiesFixture {
    /**
     * --- NSG Requirement 16: An NSG WMTS server shall support negotiation of the standard version used for
     * client-server interactions with SOAP encoding. ---
     */

    private URI getCapabilitiesURI;

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 16", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsGetCapabilitiesURLExists() {
        getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_SOAP( this.wmtsCapabilities,
                                                                             WMTS_Constants.GET_CAPABILITIES,
                                                                             ProtocolBinding.POST );
        assertTrue( getCapabilitiesURI != null,
                    "GetCapabilities (GET) endpoint not found or SOAP is not supported in ServiceMetadata capabilities document." );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 16", dependsOnMethods = "wmtsGetCapabilitiesURLExists")
    public void wmtsCapabilitiesVersionTest()
                            throws XPathExpressionException {
        String versionManipulation = WMTS_Constants.VERSION;

        // --- try a version number higher
        versionManipulation = ( 1 + Integer.parseInt( versionManipulation.substring( 0, 1 ) ) )
                              + versionManipulation.substring( 1 );
        assertFalse( verifyVersionResponse( versionManipulation ), "WMTS did not handle an invalid version ('"
                                                                   + versionManipulation + "') correctly" );

        // --- try a version number lower
        // versionManipulation = WMTS_Constants.VERSION;
        versionManipulation = "0.0.1";
        assertFalse( verifyVersionResponse( versionManipulation ), "WMTS did not handle an invalid version ('"
                                                                   + versionManipulation + "') correctly" );

        // --- try the correct version number
        assertTrue( verifyVersionResponse( WMTS_Constants.VERSION ), "This WMTS does not accept the current version ('"
                                                                     + WMTS_Constants.VERSION + "')" );
    }

    private boolean verifyVersionResponse( String version )
                            throws XPathExpressionException {
        boolean result = false;

        if ( getCapabilitiesURI == null ) {
            getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities,
                                                                                WMTS_Constants.GET_CAPABILITIES,
                                                                                ProtocolBinding.GET );
        }

        String soapURIstr = getCapabilitiesURI.toString();
        assertUrl( soapURIstr );

        WMTS_SOAPcontainer soap = new WMTS_SOAPcontainer( WMTS_Constants.GET_CAPABILITIES, soapURIstr );

        soap.AddParameterWithChild( WmtsNamespaces.serviceOWS, WMTS_Constants.ACCEPT_VERSIONS_PARAM,
                                    WMTS_Constants.VERSION_PARAM, version );
        soap.AddParameterWithChild( WmtsNamespaces.serviceOWS, WMTS_Constants.ACCEPT_FORMAT_PARAM,
                                    WMTS_Constants.OUTPUT_PARAM, WMTS_Constants.SOAP_XML );

        SOAPMessage soapResponse = soap.getSOAPresponse( false );
        result = ( soapResponse != null );
        return result;
    }

}