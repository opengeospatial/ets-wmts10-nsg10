package org.opengeospatial.cite.wmts10.nsg.testsuite.getfeatureinfo;

import static org.testng.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getfeatureinfo.AbstractBaseGetFeatureInfoFixture;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
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
    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 20", dependsOnMethods = "verifyGetFeatureInfoSupported")
    public void wmtsGetFeatureInfoRespondWith() {
        try {
            boolean xmlFormat = checkFormat( wmtsCapabilities, "//wmts:InfoFormat[text() = 'text/xml']" );
            boolean htmlFormat = checkFormat( wmtsCapabilities, "//wmts:InfoFormat[text() = 'text/html']" );
            assertTrue( xmlFormat && htmlFormat,
                        "This WMTS does not support 'text/xml' and 'text/html' for GetFeatureInfo function." );

        } catch ( XPathExpressionException xpe ) {
        }
    }

    private boolean checkFormat( Document wmtsCapabilities, String xPathAbstract )
                            throws XPathExpressionException {
        NodeList nodeElements = ServiceMetadataUtils.getNodeElements( wmtsCapabilities, xPathAbstract );
        return nodeElements != null && nodeElements.getLength() > 0;
    }

}