package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;
import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import javax.xml.soap.SOAPMessage;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.core.util.WmtsSoapContainer;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesSoap extends AbstractBaseGetCapabilitiesFixture {
    /**
     * --- NSG Requirement 3: An NSG WMTS server shall generate a ServiceMetadata document in response to a SOAP encoded
     * GetCapabilities request. ---
     */
    private URI soapURI;

    GetCapabilitiesSoap() {
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 3", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesSoapSupported() {
        soapURI = ServiceMetadataUtils.getOperationEndpoint_SOAP( wmtsCapabilities, WMTS_Constants.GET_CAPABILITIES,
                                                                  ProtocolBinding.POST );
        if ( this.soapURI == null )
            throw new SkipException(
                                     "GetCapabilities (POST) endpoint not found in ServiceMetadata capabilities document or WMTS does not support SOAP." );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 3", dependsOnMethods = "wmtsCapabilitiesSoapSupported")
    public void wmtsCapabilitiesSoapReponseTest() {
        assertTrue( soapURI != null, "There is no SOAP URL to test against" );

        String soapURIstr = soapURI.toString();
        assertUrl( soapURIstr );

        WmtsSoapContainer soap = new WmtsSoapContainer( WMTS_Constants.GET_CAPABILITIES, soapURIstr );

        soap.addParameterWithChild( WmtsNamespaces.serviceOWS, WMTS_Constants.ACCEPT_VERSIONS_PARAM,
                                    WMTS_Constants.VERSION_PARAM, WMTS_Constants.VERSION );
        soap.addParameterWithChild( WmtsNamespaces.serviceOWS, WMTS_Constants.ACCEPT_FORMAT_PARAM,
                                    WMTS_Constants.OUTPUT_PARAM, WMTS_Constants.SOAP_XML );

        SOAPMessage soapResponse = soap.getSoapResponse( true );
        assertTrue( soapResponse != null, "SOAP reposnse came back null" );

        Document soapDocument = soap.getResponseDocument();
        assertXPath( "//wmts:Capabilities/@version = '1.0.0'", soapDocument, NS_BINDINGS );

    }

}