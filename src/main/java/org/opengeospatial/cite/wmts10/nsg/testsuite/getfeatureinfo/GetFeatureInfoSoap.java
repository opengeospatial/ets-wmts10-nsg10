package org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo;

import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.logging.Level;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.core.util.WmtsSoapContainer;
import org.opengeospatial.cite.wmts10.ets.testsuite.getfeatureinfo.AbstractBaseGetFeatureInfoFixture;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

import de.latlon.ets.core.util.TestSuiteLogger;

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
        getFeatureInfoURI = ServiceMetadataUtils.getOperationEndpoint_SOAP( wmtsCapabilities,
                                                                            WMTS_Constants.GET_FEATURE_INFO,
                                                                            ProtocolBinding.POST );
        if ( this.getFeatureInfoURI == null )
            throw new SkipException(
                                     "GetFeatureInfo (POST) endpoint not found in ServiceMetadata capabilities document or WMTS does not support SOAP." );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 9", dependsOnMethods = "wmtsGetFeatureInfoSoapSupported")
    public void wmtsGetFeatureInfoSoapRequestFormatParameters( ITestContext testContext ) {
        if ( getFeatureInfoURI == null ) {
            getFeatureInfoURI = ServiceMetadataUtils.getOperationEndpoint_SOAP( this.wmtsCapabilities,
                                                                                WMTS_Constants.GET_FEATURE_INFO,
                                                                                ProtocolBinding.POST );
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
        String layerName = this.reqEntity.getKvpValue( WMTS_Constants.LAYER_PARAM );
        if ( layerName == null ) {
            NodeList layers = ServiceMetadataUtils.getNodeElements( wmtsCapabilities,
                                                                    "//wmts:Contents/wmts:Layer/ows:Identifier" );
            if ( layers.getLength() > 0 ) {
                layerName = ( layers.item( 0 ) ).getTextContent().trim();
            }
        }

        // --- get the prepopulated KVP parameters, for the SOAP parameters
        String style = this.reqEntity.getKvpValue( WMTS_Constants.STYLE_PARAM );
        String tileMatrixSet = this.reqEntity.getKvpValue( WMTS_Constants.TILE_MATRIX_SET_PARAM );
        String tileMatrix = this.reqEntity.getKvpValue( WMTS_Constants.TILE_MATRIX_PARAM );
        String tileRow = this.reqEntity.getKvpValue( WMTS_Constants.TILE_ROW_PARAM );
        String tileCol = this.reqEntity.getKvpValue( WMTS_Constants.TILE_COL_PARAM );

        String pixelI = this.reqEntity.getKvpValue( WMTS_Constants.I_PARAM );
        String pixelJ = this.reqEntity.getKvpValue( WMTS_Constants.J_PARAM );

        String infoFormat = this.reqEntity.getKvpValue( WMTS_Constants.INFO_FORMAT_PARAM );
        String requestFormat = this.reqEntity.getKvpValue( WMTS_Constants.FORMAT_PARAM );

        WmtsSoapContainer soap = new WmtsSoapContainer( WMTS_Constants.GET_FEATURE_INFO, soapURIstr );

        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.LAYER_PARAM, layerName );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.STYLE_PARAM, style );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.FORMAT_PARAM, requestFormat );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.TILE_MATRIX_SET_PARAM, tileMatrixSet );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.TILE_MATRIX_PARAM, tileMatrix );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.TILE_ROW_PARAM, tileRow );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.TILE_COL_PARAM, tileCol );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.I_PARAM, pixelI );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.J_PARAM, pixelJ );
        soap.addParameter( WmtsNamespaces.serviceOWS, WMTS_Constants.INFO_FORMAT_PARAM, infoFormat );
        return soap;
    }

}