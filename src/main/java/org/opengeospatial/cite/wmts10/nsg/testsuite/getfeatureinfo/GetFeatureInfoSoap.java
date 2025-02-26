package org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo;

import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;
import static org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding.POST;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.FORMAT_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.GET_FEATURE_INFO;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.INFO_FORMAT_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.I_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.J_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.LAYER_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.STYLE_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.TILE_MATRIX_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.TILE_MATRIX_SET_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.TILE_ROW_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces.serviceOWS;
import static org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils.getNodeElements;
import static org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils.getOperationEndpoint_SOAP;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.logging.Level;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.util.WmtsSoapContainer;
import org.opengeospatial.cite.wmts10.ets.testsuite.getfeatureinfo.AbstractBaseGetFeatureInfoFixture;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

import de.latlon.ets.core.util.TestSuiteLogger;
import jakarta.xml.soap.SOAPMessage;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetFeatureInfoSoap extends AbstractBaseGetFeatureInfoFixture {
    /**
     * --- NSG Requirement 9: An NSG WMTS server shall implement SOAP encoding using HTTP POST transfer of the
     * GetFeatureInfo operation request, using SOAP version 1.2 encoding. ---
     */

    private URI getFeatureInfoURI = null;

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 9", dependsOnMethods = "verifyGetFeatureInfoSupported")
    public void wmtsGetFeatureInfoSoapSupported() {
        getFeatureInfoURI = getOperationEndpoint_SOAP( wmtsCapabilities, GET_FEATURE_INFO, POST );
        if ( this.getFeatureInfoURI == null )
            throw new SkipException(
                                     "GetFeatureInfo (POST) endpoint not found in ServiceMetadata capabilities document or WMTS does not support SOAP." );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 9", dependsOnMethods = "wmtsGetFeatureInfoSoapSupported")
    public void wmtsGetFeatureInfoSoapRequestFormatParameters( ITestContext testContext ) {
        if ( getFeatureInfoURI == null ) {
            getFeatureInfoURI = getOperationEndpoint_SOAP( this.wmtsCapabilities, GET_FEATURE_INFO, POST );
        }
        String soapURIstr = getFeatureInfoURI.toString();
        assertUrl( soapURIstr );

        try {
            WmtsSoapContainer soap = createSoapContainer( soapURIstr );
            SOAPMessage soapResponse = soap.getSoapResponse( true );
            assertTrue( soapResponse != null, "SOAP response came back null" );
        } catch ( XPathExpressionException xpe ) {
            TestSuiteLogger.log( Level.WARNING, "Invalid or corrupt SOAP content or XML format", xpe );
        }
    }

    private WmtsSoapContainer createSoapContainer( String soapURIstr )
                            throws XPathExpressionException {
        WmtsSoapContainer soap = new WmtsSoapContainer( GET_FEATURE_INFO, soapURIstr );

        soap.addParameter( serviceOWS, LAYER_PARAM, findLayerName() );
        addParamFromRequest( soap, STYLE_PARAM );
        addParamFromRequest( soap, FORMAT_PARAM );
        addParamFromRequest( soap, TILE_MATRIX_SET_PARAM );
        addParamFromRequest( soap, TILE_MATRIX_PARAM );
        addParamFromRequest( soap, TILE_ROW_PARAM );
        addParamFromRequest( soap, I_PARAM );
        addParamFromRequest( soap, J_PARAM );
        addParamFromRequest( soap, INFO_FORMAT_PARAM );
        return soap;
    }

    private String findLayerName()
                            throws XPathExpressionException {
        String layerName = this.reqEntity.getKvpValue( LAYER_PARAM );
        if ( layerName == null ) {
            NodeList layers = getNodeElements( wmtsCapabilities, "//wmts:Contents/wmts:Layer/ows:Identifier" );
            if ( layers.getLength() > 0 ) {
                layerName = ( layers.item( 0 ) ).getTextContent().trim();
            }
        }
        return layerName;
    }

    private void addParamFromRequest( WmtsSoapContainer soap, String paramName ) {
        String style = this.reqEntity.getKvpValue( paramName );
        soap.addParameter( serviceOWS, paramName, style );
    }

}