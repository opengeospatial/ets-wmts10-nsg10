package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.opengeospatial.cite.wmts10.nsg.core.util.NSG_XMLUtils.getXMLElementTextValue;
import static org.opengeospatial.cite.wmts10.nsg.core.util.NSG_XMLUtils.openXMLDocument;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.opengeospatial.cite.wmts10.nsg.core.util.NSG_CRSUtils;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesWellKnownScaleTest extends AbstractBaseGetCapabilitiesFixture {

    private static final double TOLERANCE = 1.0e-10;

    /**
     * --- NSG Requirement 13: An NSG WMTS server shall employ the Well-Known Scale Sets identified in Annex B (based
     * upon World Mercator projection EPSG 3395 and WGS 84 Geodetic EPSG 4326) ---
     */
    private static final String ANNEX_B1_3395_TABLE = "Annex_B1_EPSG3395.xml";

    private static final String ANNEX_B2_4326_TABLE = "Annex_B2_EPSG4326.xml";

    private static final String NSG_WMTS_EPSG_5041 = "NSG_WMTS_EPSG_5041.xml";

    private static final String NSG_WMTS_EPSG_5042 = "NSG_WMTS_EPSG_5042.xml";

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesExists() {
        assertTrue( this.wmtsCapabilities != null, "No ServerMetadata Capabilities document" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.1", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_3395_Test()
                            throws IOException, XPathExpressionException {
        String wellKnownScaleSet = "EPSG:3395";
        NodeList listFromAnnexB = parseAnnex( ANNEX_B1_3395_TABLE, wellKnownScaleSet );
        testWellKnownScale( wellKnownScaleSet, listFromAnnexB );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.2", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_4326_Test()
                            throws IOException, XPathExpressionException {
        String wellKnownScaleSet = "EPSG:4326";
        NodeList listFromAnnexB = parseAnnex( ANNEX_B2_4326_TABLE, wellKnownScaleSet );
        testWellKnownScale( wellKnownScaleSet, listFromAnnexB );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.3", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_5041_Test()
                            throws IOException, XPathExpressionException {
        String wellKnownScaleSet = "EPSG:5041";
        NodeList listFromAnnexB = parseAnnex( NSG_WMTS_EPSG_5041, wellKnownScaleSet );
        testWellKnownScale( wellKnownScaleSet, listFromAnnexB );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.3", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_5042_Test()
                            throws IOException, XPathExpressionException {
        String wellKnownScaleSet = "EPSG:5042";
        NodeList listFromAnnexB = parseAnnex( NSG_WMTS_EPSG_5042, wellKnownScaleSet );
        testWellKnownScale( wellKnownScaleSet, listFromAnnexB );
    }

    private void testWellKnownScale( String wellKnownScaleSet, NodeList listFromAnnexB )
                            throws XPathExpressionException {
        boolean isWellKnown = false;

        wellKnownScaleSet = NSG_CRSUtils.normaliseCrsName( wellKnownScaleSet );

        Element tileMatrixSet = null;
        NodeList tileMatrixSetList = ServiceMetadataUtils.getNodeElements( wmtsCapabilities,
                                                                           "//wmts:Contents/wmts:TileMatrixSet" );

        for ( int tmsI = 0; ( tmsI < tileMatrixSetList.getLength() && !isWellKnown ); tmsI++ ) {
            tileMatrixSet = (Element) tileMatrixSetList.item( tmsI );
            String crsName = getXMLElementTextValue( tileMatrixSet, "ows:SupportedCRS" );
            crsName = NSG_CRSUtils.normaliseCrsName( crsName );

            isWellKnown = ( crsName.contains( wellKnownScaleSet ) );
        }

        // --- in case "employed" means each is required, otherwise would fall through as a 'skip test' if not
        // present
        // assertTrue(isWellKnown, "Well-Known Scale Set for " + wellKnownScaleSet +
        // " currently not advertised in WMTS" );

        if ( isWellKnown && ( tileMatrixSet != null ) ) {
            NodeList tileMatrixes = tileMatrixSet.getElementsByTagName( "TileMatrix" );

            if ( ( listFromAnnexB != null ) && ( listFromAnnexB.getLength() > 0 ) ) {
                assertTrue( listFromAnnexB.getLength() >= tileMatrixes.getLength() );

                int annexI = 0;
                int tmsI = 0;

                // -- check each advertised zoom level, ensuring each matches the prescribed tables
                while ( tmsI < tileMatrixes.getLength() ) {
                    Element annexNode = (Element) listFromAnnexB.item( annexI++ );
                    Element node_tms = (Element) tileMatrixes.item( tmsI++ );

                    String idStr = getXMLElementTextValue( node_tms, "ows:Identifier" );

                    checkScaleDenominator( annexNode, node_tms, idStr );
                    checkTileDimensions( annexNode, node_tms, idStr );
                    checkMatrixDimensions( annexNode, node_tms, idStr );
                }
            }
        } else {
            throw new SkipException( "Well-Known Scale Set for " + wellKnownScaleSet
                                     + " currently not advertised in WMTS" );
        }

    }

    private void checkScaleDenominator( Element annexNode, Element node_tms, String idStr ) {
        double tolerance = this.TOLERANCE;

        String scaleDemon = getXMLElementTextValue( annexNode, "ScaleDenominator" );
        BigDecimal bigNum = new BigDecimal( scaleDemon );

        if ( bigNum.scale() <= 0 ) {
            tolerance = 0.0;
        } else // if ( bigNum.scale() <= 10)
        {
            tolerance = Math.pow( 10.0, -( bigNum.scale() - 2 ) );
        }

        double scaleDenominator4326 = Double.parseDouble( scaleDemon );
        double scaleDenominator = Double.parseDouble( getXMLElementTextValue( node_tms, "ScaleDenominator" ) );

        assertTrue( ( Math.abs( scaleDenominator4326 - scaleDenominator ) <= tolerance ),
                    "TileMatrix #" + idStr + " contains an incorrect ScaleDenominator: " + scaleDenominator
                                            + " (should be:  " + scaleDenominator4326 + " )" );
    }

    private void checkTileDimensions( Element annexNode, Element node_tms, String idStr ) {
        int annexTileWidth = parseAsInt( annexNode, "TileWidth" );
        int annexTileHeight = parseAsInt( annexNode, "TileHeight" );
        int tileWidth = parseAsInt( node_tms, "TileWidth" );
        int tileHeight = parseAsInt( node_tms, "TileHeight" );
        assertEquals( tileWidth, annexTileWidth, "TileMatrix #" + idStr + " contains an incorrect TileWidth: "
                                                 + tileWidth + " (should be:  " + annexTileWidth + " )" );
        assertEquals( tileHeight, annexTileHeight, "TileMatrix #" + idStr + " contains an incorrect TileHeight: "
                                                   + tileHeight + " (should be:  " + annexTileHeight + " )" );
        assertEquals( tileWidth, tileHeight, "TileMatrix #" + idStr + " has values for TileWidth (" + tileWidth
                                             + ") and TileHeight (" + tileHeight + ") that do not match" );
    }

    private void checkMatrixDimensions( Element annexNode, Element node_tms, String idStr ) {
        int annexMatrixWidth = parseAsInt( annexNode, "MatrixWidth" );
        int annexMatrixHeight = parseAsInt( annexNode, "MatrixHeight" );
        int matrixWidth = parseAsInt( node_tms, "MatrixWidth" );
        int matrixHeight = parseAsInt( node_tms, "MatrixHeight" );
        assertEquals( matrixWidth, annexMatrixWidth, "TileMatrix #" + idStr + " contains an incorrect MatrixWidth: "
                                                     + matrixWidth + " (should be:  " + annexMatrixWidth + " )" );
        assertEquals( matrixHeight, annexMatrixHeight, "TileMatrix #" + idStr + " contains an incorrect MatrixHeight: "
                                                       + matrixHeight + " (should be:  " + annexMatrixHeight + " )" );
    }

    private int parseAsInt( Element elementNode, String tagName ) {
        return Integer.parseInt( getXMLElementTextValue( elementNode, tagName ) );
    }

    private NodeList parseAnnex( String resource, String wellKnownScaleSet )
                            throws IOException {
        NodeList listFromAnnexB = openXMLDocument( this.getClass().getResourceAsStream( resource ), "TileMatrix" );
        if ( listFromAnnexB == null || listFromAnnexB.getLength() == 0 )
            throw new IOException( "Required external file of " + wellKnownScaleSet + " values is not accessible" );
        return listFromAnnexB;
    }

}