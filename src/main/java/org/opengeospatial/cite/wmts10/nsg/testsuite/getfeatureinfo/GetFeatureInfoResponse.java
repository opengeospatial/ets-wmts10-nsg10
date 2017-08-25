package org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo;

import static org.testng.Assert.assertFalse;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getfeatureinfo.AbstractBaseGetFeatureInfoFixture;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetFeatureInfoResponse extends AbstractBaseGetFeatureInfoFixture {
    /**
     * --- NSG Requirement 20: An NSG WMTS server shall provide the GetFeatureInfo output format in text/XML and
     * text/HTML. ---
     */

    // private URI getFeatureInfoURI;

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 20", dependsOnMethods = "verifyGetFeatureInfoSupported")
    public void wmtsGetFeatureInfoRespondWith() {
        NodeList infoFormats = null;
        try {
            infoFormats = (NodeList) ServiceMetadataUtils.getNodeElements( wmtsCapabilities,
                                                                           "//wmts:InfoFormat[text() = 'text/xml']" );
            if ( ( infoFormats == null ) || ( infoFormats.getLength() <= 0 ) ) {
                infoFormats = (NodeList) ServiceMetadataUtils.getNodeElements( wmtsCapabilities,
                                                                               "//wmts:InfoFormat[text() = 'text/html']" );
            }
        } catch ( XPathExpressionException xpe ) {
            // xpe.printStackTrace();
        }
        assertFalse( ( ( infoFormats == null ) || ( infoFormats.getLength() <= 0 ) ),
                     "This WMTS does not support 'text/xml' or 'text/html' for GetFeatureInfo function." );

        // getFeatureInfoURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities,
        // WMTS_Constants.GET_FEATURE_INFO, ProtocolBinding.GET );
        // assertTrue(getFeatureInfoURI != null,
        // "GetFeatureInfo (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");
    }

}