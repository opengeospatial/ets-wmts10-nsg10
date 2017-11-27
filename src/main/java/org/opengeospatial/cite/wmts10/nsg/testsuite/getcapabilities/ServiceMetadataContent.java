package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;
import static org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities.GetCapabilitiesKeywordTest.verifyNASkeywords;
import static org.testng.Assert.assertTrue;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion;
import org.opengeospatial.cite.wmts10.ets.core.domain.LayerInfo;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.util.Strings;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class ServiceMetadataContent extends AbstractBaseGetCapabilitiesFixture {
    /**
     * --- Service Metadata Content ---
     */
    private final String NSG_ABSTRACTSTATEMENT = "This service implements the NSG WMTS 1.0.0 profile version 1.0";

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesExists() {
        // --- base test
        assertXPath( ".", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesServiceIdentificationExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//ows:ServiceIdentification", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesServiceProviderExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//ows:ServiceProvider", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesOperationsMetadataExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//ows:OperationsMetadata", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesContentsExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//wmts:Contents", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesAccessConstraints() {
        // --- Test Method: 2 (The response uses <AccessContraints> to identify classification levels for the service)
        assertXPath( "//ows:ServiceIdentification//ows:AccessConstraints", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesAbstract()
                            throws XPathExpressionException, XPathFactoryConfigurationException {
        // --- Test Method: 3 (The response in the Abstract element contains the following information:
        // "This service implements the NSG WMTS 1.0.0 profile version 1.0.")
        XPath xPath = createXPath();
        Node abstractElement = (Node) xPath.evaluate( "//ows:ServiceIdentification/ows:Abstract", wmtsCapabilities,
                                                      XPathConstants.NODE );
        if ( abstractElement == null ) {
            throw new SkipException( "There is no <Abstract> Element to compare." );
        }

        String abstractStatement = (String) xPath.evaluate( ".", abstractElement, XPathConstants.STRING );
        assertTrue( ( ( abstractStatement != null ) && !abstractStatement.isEmpty() ),
                    "The <Abstract> statement is blank." );
        assertTrue( abstractStatement.contains( NSG_ABSTRACTSTATEMENT ), "The <Abstract> statement ("
                                                                         + abstractStatement + ") should be:  "
                                                                         + NSG_ABSTRACTSTATEMENT + "." );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesNASkeywords() {
        // --- Test Method: 4.0 - (The response provides keywords based upon the NAS for the following: Layer data
        // structure, Style data structure, Dimension data structure, TileMatrixSet data structure, TileMatrix data
        // structure, and Themes data structure.)
        verifyNASkeywords( false, "WMTS ServiceMetadata Capabilities docuemnt", wmtsCapabilities,
                           "//ows:ServiceIdentification/ows:Keywords" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesNASkeywords_Layer() {
        // --- Test Method: 4.1 - (The response provides keywords based upon the NAS for the following: Layer data
        // structure ...)
        verifyNASkeywords( false, "Layer Data Structure", wmtsCapabilities, "//wmts:Layer/ows:Keywords" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesNASkeywords_LayerStyle() {
        // --- Test Method: 4.2 - (The response provides keywords based upon the NAS for the following: ..., Style data
        // structure, ...)
        verifyNASkeywords( false, "Style Data Structure", wmtsCapabilities, "//wmts:Layer/wmts:Style/ows:Keywords" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesNASkeywords_LayerDimension() {
        // --- Test Method: 4.3 - (The response provides keywords based upon the NAS for the following: ..., Dimension
        // data structure, ...)
        verifyNASkeywords( false, "Dimension Data Structure", wmtsCapabilities,
                           "//wmts:Layer/wmts:Dimension/ows:Keywords" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesNASkeywords_TileMatrixSet() {
        // --- Test Method: 4.4 - (The response provides keywords based upon the NAS for the following: ...
        // TileMatrixSet data structure, ...)
        verifyNASkeywords( false, "TileMatrixSet Data Structure", wmtsCapabilities, "//wmts:TileMatrixSet/ows:Keywords" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesNASkeywords_TileMatrix() {
        // --- Test Method: 4.5 - (The response provides keywords based upon the NAS for the following: ... TileMatrix
        // data structure ...)
        verifyNASkeywords( false, "TileMatrix Data Structure", wmtsCapabilities, "//wmts:TileMatrix/ows:Keywords" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesServiceIdentificationExists")
    public void wmtsCapabilitiesNASkeywords_Theme() {
        // --- Test Method: 4.6 - (The response provides keywords based upon the NAS for the following: ... Themes data
        // structure.)
        verifyNASkeywords( false, "Theme Data Structure", wmtsCapabilities, "//wmts:Theme/ows:Keywords" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesContentsExists")
    public void wmtsCapabilitiesLayerStyles()
                            throws XPathExpressionException, XPathFactoryConfigurationException {
        // --- Test Method: 5 (The response provides information on the supported styles)
        // --- Test Method: 6 (The response provides a defined style for the default style)
        XPath xPath = createXPath();
        if ( ( layerInfo == null ) || ( layerInfo.size() <= 0 ) ) {
            throw new SkipException( "There are no Layers identified" );
        }

        SoftAssert sa = new SoftAssert();

        for ( int i = 0; i < layerInfo.size(); i++ ) {
            LayerInfo layer = layerInfo.get( i );

            String exprPath = "//wmts:Contents/wmts:Layer[ows:Identifier = '" + layer.getLayerName() + "']/wmts:Style";
            // --- will soft assess in order to go thru all layers // assertXPath(exprPath, wmtsCapabilities,
            // NS_BINDINGS);

            NodeList layerStyles = (NodeList) xPath.evaluate( exprPath, wmtsCapabilities, XPathConstants.NODESET );
            sa.assertTrue( ( layerStyles != null ) && ( layerStyles.getLength() > 0 ),
                           "There are no <Style> elements for <Layer>:  " + layer.getLayerName() );

            if ( ( layerStyles != null ) && ( layerStyles.getLength() > 0 ) ) {
                boolean foundDefault = false;

                for ( int si = 0; ( ( si < layerStyles.getLength() ) && !foundDefault ); si++ ) {
                    Node style = layerStyles.item( si );

                    String attribute = (String) xPath.evaluate( "@isDefault", style, XPathConstants.STRING );
                    String styleIdentifier = ServiceMetadataUtils.parseNodeElementName( xPath, style );

                    foundDefault = ( attribute.equals( "true" ) || styleIdentifier.equals( "default" ) );
                }
                sa.assertTrue( foundDefault, "There is no default <Style> for <Layer>:  " + layer.getLayerName() );
            }
        }
        sa.assertAll();
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesContentsExists")
    public void wmtsCapabilitiesLayerStyleLegends()
                            throws XPathExpressionException, XPathFactoryConfigurationException {
        // --- Test Method: 7 (The response provides an associated legend in at least one of the following formats: PNG,
        // GIF, JPEG)
        // --- Test Method: 8 (The provided LegendURL is accessible online)
        // --- Test Method: 11 (The response provides scale denominators for all layers)
        // --- Test Method: 12 (The provided <MinScaleDenominator> value is less than or equal to the
        // <MaxScaleDenominator>)
        XPath xPath = createXPath();
        if ( ( layerInfo == null ) || ( layerInfo.size() <= 0 ) ) {
            throw new SkipException( "There are no Layers identified" );
        }

        SoftAssert sa = new SoftAssert();

        for ( int i = 0; i < layerInfo.size(); i++ ) {
            LayerInfo layer = layerInfo.get( i );

            String exprPath = "//wmts:Contents/wmts:Layer[ows:Identifier = '" + layer.getLayerName() + "']/wmts:Style";
            // --- will soft assess in order to go thru all layers // assertXPath(exprPath, wmtsCapabilities,
            // NS_BINDINGS);

            NodeList layerStyles = (NodeList) xPath.evaluate( exprPath, wmtsCapabilities, XPathConstants.NODESET );
            sa.assertTrue( ( layerStyles != null ) && ( layerStyles.getLength() > 0 ),
                           "There are no <Style> elements for <Layer>:  " + layer.getLayerName() );

            if ( ( layerStyles != null ) && ( layerStyles.getLength() > 0 ) ) {
                for ( int si = 0; si < layerStyles.getLength(); si++ ) {
                    Node style = layerStyles.item( si );
                    String styleIdentifier = ServiceMetadataUtils.parseNodeElementName( xPath, style );

                    NodeList legendList = (NodeList) xPath.evaluate( "./wmts:LegendURL", style, XPathConstants.NODESET );
                    sa.assertTrue( ( legendList != null ) && ( legendList.getLength() > 0 ),
                                   "There is no Legend for <Style>: " + styleIdentifier + " under <Layer>: "
                                                           + layer.getLayerName() );

                    if ( ( legendList != null ) && ( legendList.getLength() > 0 ) ) {
                        boolean foundPreferredFormat = false;
                        for ( int li = 0; li < legendList.getLength(); li++ ) {
                            Node legend = legendList.item( li );

                            String format = (String) xPath.evaluate( "@format", legend, XPathConstants.STRING );
                            String url = (String) xPath.evaluate( "@xlink:href", legend, XPathConstants.STRING );

                            sa.assertTrue( ( !Strings.isNullOrEmpty( format ) ) && ( !Strings.isNullOrEmpty( url ) ),
                                           "Legend for Style: " + styleIdentifier + " under Layer: "
                                                                   + layer.getLayerName() + " is not properly defined." );

                            // -- Test for formats (Test Method 7)
                            foundPreferredFormat |= ( format.equals( WMTS_Constants.IMAGE_PNG )
                                                      || format.equals( WMTS_Constants.IMAGE_GIF ) || format.equals( WMTS_Constants.IMAGE_JPEG ) );

                            // -- Test for valid url (Test Method 8)
                            WmtsAssertion.assertUrl( sa, url );
                            WmtsAssertion.assertUriIsResolvable( sa, url );

                            // -- Test for scale denominator (Test Method 11)
                            checkScaleDenominator( xPath, sa, layer, styleIdentifier, legend );
                        }
                        sa.assertTrue( foundPreferredFormat,
                                       "<Style>: " + styleIdentifier + " under <Layer>: " + layer.getLayerName()
                                                               + " does not use a preferred Legend image format." );
                    }
                }
            }
        }
        sa.assertAll();
    }

    private void checkScaleDenominator( XPath xPath, SoftAssert sa, LayerInfo layer, String styleIdentifier, Node legend )
                            throws XPathExpressionException {
        String minScaleDenominator = (String) xPath.evaluate( "@minScaleDenominator", legend, XPathConstants.STRING );
        String maxScaleDenominator = (String) xPath.evaluate( "@maxScaleDenominator", legend, XPathConstants.STRING );

        boolean minScaleDenominatorIsSet = !Strings.isNullOrEmpty( minScaleDenominator );
        boolean maxScaleDenominatorIsSet = !Strings.isNullOrEmpty( maxScaleDenominator );
        if ( minScaleDenominatorIsSet && maxScaleDenominatorIsSet ) {
            Double minScale = Double.parseDouble( minScaleDenominator );
            Double maxScale = Double.parseDouble( maxScaleDenominator );
            sa.assertTrue( minScale <= maxScale,
                           "The minScaleDenominator > maxScaleDenominator for <Layer>: " + layer.getLayerName()
                                                   + " and <Style>: " + styleIdentifier + " for a Legend" );
        }
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesContentsExists")
    public void wmtsCapabilitiesLayerDimension()
                            throws XPathExpressionException, XPathFactoryConfigurationException {
        // --- Test Method: 9 (The response provides Dimension information, if applicable to a layer)
        XPath xPath = createXPath();
        if ( ( layerInfo == null ) || ( layerInfo.size() <= 0 ) ) {
            throw new SkipException( "There are no Layers identified" );
        }

        boolean anyDimensions = false;

        SoftAssert sa = new SoftAssert();

        for ( int i = 0; i < layerInfo.size(); i++ ) {
            LayerInfo layer = layerInfo.get( i );

            String exprPath = "//wmts:Contents/wmts:Layer[ows:Identifier = '" + layer.getLayerName()
                              + "']/wmts:Dimension";
            // --- will soft assess in order to go thru all layers // assertXPath(exprPath, wmtsCapabilities,
            // NS_BINDINGS);

            NodeList layerDimension = (NodeList) xPath.evaluate( exprPath, wmtsCapabilities, XPathConstants.NODESET );
            // sa.assertTrue((layerDimension != null) && ( layerDimension.getLength()>0),
            // "There are no <Dimension> elements for <Layer>:  " + layer.getLayerName());

            if ( ( layerDimension != null ) && ( layerDimension.getLength() > 0 ) ) {
                anyDimensions |= true;
                for ( int si = 0; si < layerDimension.getLength(); si++ ) {
                    Node dimension = layerDimension.item( si );
                    String dimensionIdentifier = ServiceMetadataUtils.parseNodeElementName( xPath, dimension );

                    String defaultValue = (String) xPath.evaluate( "./wmts:Default", dimension, XPathConstants.STRING );
                    NodeList values = (NodeList) xPath.evaluate( "./wmts:Value", dimension, XPathConstants.STRING );

                    // --- TO-DO
                    // --- assume the following meets test criteria

                    sa.assertTrue( !Strings.isNullOrEmpty( dimensionIdentifier ),
                                   "There is no <Identifier> element for the <Dimension>, under <Layer>: "
                                                           + layer.getLayerName() );
                    if ( !Strings.isNullOrEmpty( dimensionIdentifier ) ) {
                        sa.assertTrue( !Strings.isNullOrEmpty( defaultValue ),
                                       "There is no <Default> value element for the <Dimension>, under <Layer>: "
                                                               + layer.getLayerName() );
                        if ( !Strings.isNullOrEmpty( defaultValue ) ) {
                            sa.assertTrue( ( values != null ) && ( values.getLength() > 0 ),
                                           "There are no <Value> elements for the <Dimension>, under <Layer>: "
                                                                   + layer.getLayerName() );
                        }
                    }
                }
            }
        }
        if ( anyDimensions ) {
            sa.assertAll();
        } else {
            throw new SkipException( "There are no <Dimension> elements within any of the defined <Layer> elements." );
        }
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesContentsExists")
    public void wmtsCapabilitiesLayerInfoFormat()
                            throws XPathExpressionException, XPathFactoryConfigurationException {
        // --- Test Method: 10 (The response provides a valid output format for Layer data structure infoFormat
        // parameter thus enabling GetFeatureInfo)
        XPath xPath = createXPath();
        if ( ( layerInfo == null ) || ( layerInfo.size() <= 0 ) ) {
            throw new SkipException( "There are no Layers identified" );
        }

        SoftAssert sa = new SoftAssert();

        for ( int i = 0; i < layerInfo.size(); i++ ) {
            LayerInfo layer = layerInfo.get( i );

            String exprPath = "//wmts:Contents/wmts:Layer[ows:Identifier = '" + layer.getLayerName()
                              + "']/wmts:InfoFormat";
            // --- will soft assess in order to go thru all layers // assertXPath(exprPath, wmtsCapabilities,
            // NS_BINDINGS);

            NodeList layerInfoFormats = (NodeList) xPath.evaluate( exprPath, wmtsCapabilities, XPathConstants.NODESET );
            sa.assertTrue( ( layerInfoFormats != null ) && ( layerInfoFormats.getLength() > 0 ),
                           "There are no <InfoFormat> elements for <Layer>:  " + layer.getLayerName() );

            if ( ( layerInfoFormats != null ) && ( layerInfoFormats.getLength() > 0 ) ) {
                for ( int fi = 0; fi < layerInfoFormats.getLength(); fi++ ) {
                    Node infoFormatNode = layerInfoFormats.item( fi );

                    String infoFormat = (String) xPath.evaluate( ".", infoFormatNode, XPathConstants.STRING );

                    // --- TO-DO
                    // --- assume the following meets test criteria

                    sa.assertTrue( !Strings.isNullOrEmpty( infoFormat ),
                                   "There is no or invalid <InfoFormat> element under <Layer>: " + layer.getLayerName() );
                }
            }
        }
        sa.assertAll();
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesContentsExists")
    public void wmtsCapabilitiesFeatureListURL()
                            throws XPathExpressionException, XPathFactoryConfigurationException {
        // --- Test Method: 13 (The list of features is resolvable through the provided FeatureListURLs)
        throw new SkipException( "WMTS ServiceMetadata Capabilities does not currently include FeatureListURLs" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirements 4,7", dependsOnMethods = "wmtsCapabilitiesContentsExists")
    public void wmtsCapabilitiesDataURL()
                            throws XPathExpressionException, XPathFactoryConfigurationException {
        // --- Test Method: 14 (The data is resolvable through the provided URL for all provided DatURLs)
        throw new SkipException( "WMTS ServiceMetadata Capabilities does not currently include DataURLs" );
    }

    private XPath createXPath()
                            throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }

}