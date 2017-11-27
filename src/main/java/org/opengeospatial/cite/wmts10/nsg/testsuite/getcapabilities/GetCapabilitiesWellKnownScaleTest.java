package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.opengeospatial.cite.wmts10.nsg.core.util.NSG_XMLUtils.getXMLElementTextValue;
import static org.opengeospatial.cite.wmts10.nsg.core.util.NSG_XMLUtils.openXMLDocument;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.opengeospatial.cite.wmts10.nsg.core.util.NSG_CRSUtils;
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

        wellKnownScaleSet = NSG_CRSUtils.normaliseCrsName( wellKnownScaleSet );

        NodeList tileMatrixSetList = ServiceMetadataUtils.getNodeElements( wmtsCapabilities,
                                                                           "//wmts:Contents/wmts:TileMatrixSet" );

        List<Element> tileMatrixSetsWithCrs = retrieveTileMatrixSetsWithSupportedCrs( wellKnownScaleSet,
                                                                                      tileMatrixSetList );
        assertTrue( tileMatrixSetsWithCrs.size() > 0, "Well-Known Scale Set for " + wellKnownScaleSet
                                                      + " is not advertised in WMTS" );

        boolean isAtLeastOneTileMatrixSetWithCrsWellKnown = isAtLeastOneTileMatrixSetWithCrsWellKnown( listFromAnnexB,
                                                                                                       tileMatrixSetsWithCrs );
        assertTrue( isAtLeastOneTileMatrixSetWithCrsWellKnown, "Scale Set for " + wellKnownScaleSet
                                                               + " is advertised in WMTS but not Well-Known" );
    }

    private List<Element> retrieveTileMatrixSetsWithSupportedCrs( String wellKnownScaleSet, NodeList tileMatrixSetList ) {
        List<Element> tileMatrixSetsWithSupportedCrs = new ArrayList<>();
        for ( int tmsI = 0; tmsI < tileMatrixSetList.getLength(); tmsI++ ) {
            Element tileMatrixSet = (Element) tileMatrixSetList.item( tmsI );
            String crsName = getXMLElementTextValue( tileMatrixSet, "ows:SupportedCRS" );
            crsName = NSG_CRSUtils.normaliseCrsName( crsName );
            if ( crsName.contains( wellKnownScaleSet ) )
                tileMatrixSetsWithSupportedCrs.add( tileMatrixSet );

        }
        return tileMatrixSetsWithSupportedCrs;
    }

    private boolean isAtLeastOneTileMatrixSetWithCrsWellKnown( NodeList listFromAnnexB,
                                                               List<Element> tileMatrixSetsWithCrs ) {
        for ( Element tileMatrixSet : tileMatrixSetsWithCrs ) {
            boolean tileMatrixSetWellKnown = isTileMatrixSetWellKnown( listFromAnnexB, tileMatrixSet );
            if ( tileMatrixSetWellKnown )
                return true;
        }
        return false;
    }

    private boolean isTileMatrixSetWellKnown( NodeList listFromAnnexB, Element tileMatrixSet ) {
        NodeList tileMatrixes = tileMatrixSet.getElementsByTagName( "TileMatrix" );
        if ( !( listFromAnnexB.getLength() >= tileMatrixes.getLength() ) )
            return false;

        // -- check each advertised zoom level, ensuring each matches the prescribed tables
        for ( int tmI = 0; tmI < tileMatrixes.getLength(); tmI++ ) {
            boolean tileMatrixWellKnown = isTileMatrixWellKnown( listFromAnnexB, tileMatrixes, tmI );
            if ( !tileMatrixWellKnown )
                return false;
        }
        return true;
    }

    private boolean isTileMatrixWellKnown( NodeList listFromAnnexB, NodeList tileMatrixes, int currentTileMatrix ) {
        Element annexNode = (Element) listFromAnnexB.item( currentTileMatrix );
        Element node_tms = (Element) tileMatrixes.item( currentTileMatrix );

        boolean scaleDenominatorCorrect = isScaleDenominatorCorrect( annexNode, node_tms );
        boolean tileDimensionsCorrect = isTileDimensionsCorrect( annexNode, node_tms );
        boolean matrixDimensionsCorrect = isMatrixDimensionsCorrect( annexNode, node_tms );
        return scaleDenominatorCorrect && tileDimensionsCorrect && matrixDimensionsCorrect;
    }

    private boolean isScaleDenominatorCorrect( Element annexNode, Element node_tms ) {
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
        return Math.abs( scaleDenominator4326 - scaleDenominator ) <= tolerance;
    }

    private boolean isTileDimensionsCorrect( Element annexNode, Element node_tms ) {
        int annexTileWidth = parseAsInt( annexNode, "TileWidth" );
        int annexTileHeight = parseAsInt( annexNode, "TileHeight" );
        int tileWidth = parseAsInt( node_tms, "TileWidth" );
        int tileHeight = parseAsInt( node_tms, "TileHeight" );
        return tileWidth == annexTileWidth && tileHeight == annexTileHeight && tileWidth == tileHeight;
    }

    private boolean isMatrixDimensionsCorrect( Element annexNode, Element node_tms ) {
        int annexMatrixWidth = parseAsInt( annexNode, "MatrixWidth" );
        int annexMatrixHeight = parseAsInt( annexNode, "MatrixHeight" );
        int matrixWidth = parseAsInt( node_tms, "MatrixWidth" );
        int matrixHeight = parseAsInt( node_tms, "MatrixHeight" );
        return matrixWidth == annexMatrixWidth && matrixHeight == annexMatrixHeight;
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