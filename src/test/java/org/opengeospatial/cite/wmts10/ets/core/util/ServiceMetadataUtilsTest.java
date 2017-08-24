package org.opengeospatial.cite.wmts10.ets.core.util;

import static org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils.parseLayers;
import static org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils.parseRequestableDimension;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.opengeospatial.cite.wmts10.ets.core.domain.BoundingBox;
import org.opengeospatial.cite.wmts10.ets.core.domain.Dimension;
import org.opengeospatial.cite.wmts10.ets.core.domain.LayerInfo;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;
import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.DimensionUnitValue;
import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.RequestableDimension;
import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.RequestableDimensionList;
import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.date.DateTimeDimensionInterval;
import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.date.DateTimeRequestableDimension;
import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.number.NumberDimensionInterval;
import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.number.NumberRequestableDimension;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class ServiceMetadataUtilsTest {

    @Test
    public void testGetOperationBindings()
                    throws Exception {
        Set<ProtocolBinding> globalBindings = ServiceMetadataUtils.getOperationBindings( wmtsCapabilities(), "GetMap" );

        assertThat( globalBindings.size(), is( 1 ) );
        assertThat( globalBindings, hasItems( ProtocolBinding.GET ) );
    }

    @Test
    public void testGetOperationEndpoint()
                    throws Exception {
        URI endpointUri = ServiceMetadataUtils.getOperationEndpoint_REST( wmtsCapabilities(), "GetTile", ProtocolBinding.GET );

        assertThat( endpointUri, is( new URI( "http://demo.deegree.org/utah-workspace/services/wms" ) ) );
    }

    @Test
    public void testGetOperationEndpointUnsupportedProtocol()
                    throws Exception {
        URI endpointUri = ServiceMetadataUtils.getOperationEndpoint_REST( wmtsCapabilities(), "GetTile", ProtocolBinding.POST );

        assertThat( endpointUri, is( nullValue() ) );
    }

    @Test
    public void testP()
                    throws Exception {
    	/*--
        List<String> supportedFormats = ServiceMetadataUtils.parseSupportedFormats( wmtsCapabilities(), "GetTile" );

        assertThat( supportedFormats.size(), is( 7 ) );
        assertThat( supportedFormats, hasItems( "image/png", "image/x-ms-bmp", "image/png; mode=8bit" ) );
        --*/
    }

    @Test
    public void testParseLayerInfo()
                    throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );

        assertThat( layerInfos.size(), is( 24 ) );
    }

    @Test
    public void testParseLayerInfo_BoundingBoxes()
                    throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );
        LayerInfo layerInfo = getLayerInfoWithName( layerInfos, "weatherstations" );

        List<BoundingBox> bboxes = layerInfo.getBboxes();
        assertThat( bboxes.size(), is( 4 ) );
        assertThat( bboxes, hasItems( new BoundingBox( "EPSG:26912", 330187.5966164868, 4256460.6503901705,
                        450589.437745681, 4541709.090664327 ) ) );
        assertThat( bboxes, hasItems( new BoundingBox( "EPSG:4326", -113.01934846637089, 38.440287470710686,
                        -111.56629505010868, 41.025094027971484 ) ) );
        assertThat( bboxes, hasItems( new BoundingBox( "EPSG:4258", -113.01934846637089, 38.440287470710686,
                        -111.56629505010868, 41.025094027971484 ) ) );
        assertThat( bboxes, hasItems( new BoundingBox( "CRS:84", -113.01934846637089, 38.440287470710686,
                        -111.56629505010868, 41.025094027971484 ) ) );
    }
/*--
    @Test
    public void testParseLayerInfo_queryable1()
                    throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );
        LayerInfo layerInfo = getLayerInfoWithName( layerInfos, "weatherstations" );

        assertThat( layerInfo.isQueryable(), is( true ) );
    }
--*/
/*--
    @Test
    public void testParseLayerInfo_queryable0()
                    throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );
        LayerInfo layerInfo = getLayerInfoWithName( layerInfos, "counties" );

        assertThat( layerInfo.isQueryable(), is( false ) );
    }
--*/
/*--    
    @Test
    public void testParseLayerInfo_queryableMissing()
                    throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );
        LayerInfo layerInfo = getLayerInfoWithName( layerInfos, "countynames" );

        assertThat( layerInfo.isQueryable(), is( false ) );
    }
--*/
    /*
    @Test
    public void testParseUpdateSequence()
                    throws Exception {
        String updateSequence = ServiceMetadataUtils.parseUpdateSequence( wmtsCapabilities() );

        assertThat( updateSequence, is( "0" ) );
    }

    @Test
    public void testParseUpdateSequence_missingUpdateSequence()
                    throws Exception {
        String updateSequence = ServiceMetadataUtils.parseUpdateSequence( wmtsCapabilitiesMissingUpdateSequence() );

        assertThat( updateSequence, is( nullValue() ) );
    }

    @Test
    public void testParseUpdateSequence_emptyUpdateSequence()
                    throws Exception {
        String updateSequence = ServiceMetadataUtils.parseUpdateSequence( wmtsCapabilitiesEmptyUpdateSequence() );

        assertThat( updateSequence, is( nullValue() ) );
    }
--*/
    @Test
    public void testParseGeographicBoundingBox()
                    throws Exception
    {
        Node layerNode = layerNode( "municipalities" );
        BoundingBox geographicBoundingBox = ServiceMetadataUtils.parseBoundingBox( layerNode, true );

        assertThat( geographicBoundingBox.getCrs(), is( "CRS:84" ) );
        assertThat( geographicBoundingBox.getMinX(), is( -115.40027851288826 ) );
        assertThat( geographicBoundingBox.getMinY(), is( 35.43540497508244 ) );
        assertThat( geographicBoundingBox.getMaxX(), is( -107.69462459394548 ) );
        assertThat( geographicBoundingBox.getMaxY(), is( 43.06062467392451 ) );
    }

    @Test
    public void testParseLayerInfo_TimeDimension()
                    throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );
        LayerInfo layerInfo = getLayerInfoWithName( layerInfos, "weatherstations" );

        List<Dimension> dimensions = layerInfo.getDimensions();
        assertThat( dimensions.size(), is( 1 ) );

        Dimension dimension = dimensions.get( 0 );
        assertThat( dimension.getName(), is( "time" ) );

        DimensionUnitValue dimensionValue = dimension.getDimensionValue();
        assertThat( dimensionValue.getUnits(), is( "ISO8601" ) );

        DateTimeDimensionInterval dimensionInterval = (DateTimeDimensionInterval) dimensionValue.getRequestableDimension();
        assertTrue( dimensionInterval.getMin().isEqual( new DateTime( 2009, 1, 1, 0, 0, DateTimeZone.UTC ) ) );
        assertTrue( dimensionInterval.getMax().isEqual( new DateTime( 2009, 1, 7, 0, 0, DateTimeZone.UTC ) ) );
        assertThat( dimensionInterval.getResolution(), is( new Period( 0, 0, 0, 1, 0, 0, 0, 0 ) ) );
    }

    @Test
    public void testParseLayerInfo_ElevationDimension()
                    throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );
        LayerInfo layerInfo = getLayerInfoWithName( layerInfos, "DEM30" );

        List<Dimension> dimensions = layerInfo.getDimensions();
        assertThat( dimensions.size(), is( 1 ) );

        Dimension dimension = dimensions.get( 0 );
        assertThat( dimension.getName(), is( "elevation" ) );

        DimensionUnitValue dimensionValue = dimension.getDimensionValue();
        assertThat( dimensionValue.getUnits(), is( "CRS:88" ) );

        NumberDimensionInterval dimensionInterval = (NumberDimensionInterval) dimensionValue.getRequestableDimension();
        assertThat( dimensionInterval.getMin().intValue(), is( 1 ) );
        assertThat( dimensionInterval.getMax().intValue(), is( 3500 ) );
        assertThat( dimensionInterval.getResolution().intValue(), is( 1 ) );
    }

    @Test
    public void testParseRequestableDimension_Meter_SingleValue()
                    throws Exception {
        NumberRequestableDimension requestableDimension = (NumberRequestableDimension) parseRequestableDimension( "m",
                                                                                                                  "1" );
        assertThat( requestableDimension.getValue().intValue(), is( 1 ) );
    }

    @Test
    public void testParseRequestableDimension_Meter_ValueList()
                    throws Exception {
        RequestableDimensionList requestableDimension = (RequestableDimensionList) parseRequestableDimension( "m",
                                                                                                              "1,5,10,15" );

        List<RequestableDimension> requestableDimensions = requestableDimension.getRequestableDimensions();
        assertThat( requestableDimensions.size(), is( 4 ) );

        assertThat( requestableDimensions, hasNumberRequestableDimension( 1 ) );
        assertThat( requestableDimensions, hasNumberRequestableDimension( 5 ) );
        assertThat( requestableDimensions, hasNumberRequestableDimension( 10 ) );
        assertThat( requestableDimensions, hasNumberRequestableDimension( 15 ) );
    }

    @Test
    public void testParseRequestableDimension_Date_SingleValue()
                    throws Exception {
        DateTimeRequestableDimension requestableDimension = (DateTimeRequestableDimension) parseRequestableDimension( "ISO8601",
                                                                                                                      "1990-01-01" );
        assertThat( requestableDimension, is( new DateTimeRequestableDimension( new DateTime( 1990, 1, 1, 0, 0 ) ) ) );
    }

    @Test
    public void testParseRequestableDimension_Date_ValueList()
                    throws Exception {
        RequestableDimensionList requestableDimension = (RequestableDimensionList) parseRequestableDimension( "ISO8601",
                                                                                                              "1990-01-01,1995-01-01,2000-01-01" );

        List<RequestableDimension> requestableDimensions = requestableDimension.getRequestableDimensions();
        assertThat( requestableDimensions.size(), is( 3 ) );

        assertThat( requestableDimensions,
                    hasItem( new DateTimeRequestableDimension( new DateTime( 1990, 1, 1, 0, 0 ) ) ) );
        assertThat( requestableDimensions,
                    hasItem( new DateTimeRequestableDimension( new DateTime( 1995, 1, 1, 0, 0 ) ) ) );
        assertThat( requestableDimensions,
                    hasItem( new DateTimeRequestableDimension( new DateTime( 2000, 1, 1, 0, 0 ) ) ) );
    }

    @Test
    public void testParseRequestableDimension_Date_ZeroResolution()
                    throws Exception {
        DateTimeDimensionInterval dimensionInterval = (DateTimeDimensionInterval) parseRequestableDimension( "ISO8601",
                                                                                                             "1990-01-01/1990-01-07/0" );
        assertThat( dimensionInterval.getMin(), is( new DateTime( 1990, 1, 1, 0, 0 ) ) );
        assertThat( dimensionInterval.getMax(), is( new DateTime( 1990, 1, 7, 0, 0 ) ) );
        assertThat( dimensionInterval.getResolution(), is( nullValue() ) );
    }

    @Test
    public void testParseRequestableDimension_Meter_Interval()
                    throws Exception {
        NumberDimensionInterval dimensionInterval = (NumberDimensionInterval) parseRequestableDimension( "m",
                                                                                                         "1.1/2.2/0.1" );

        assertThat( dimensionInterval.getMin().doubleValue(), is( 1.1 ) );
        assertThat( dimensionInterval.getMax().doubleValue(), is( 2.2 ) );
        assertThat( dimensionInterval.getResolution().doubleValue(), is( 0.1 ) );
    }

    @Test
    public void testParseRequestableDimension_Meter_ZeroResolutionInterval()
                    throws Exception {
        NumberDimensionInterval dimensionInterval = (NumberDimensionInterval) parseRequestableDimension( "m",
                                                                                                         "1.1/2.2/0" );

        assertThat( dimensionInterval.getMin().doubleValue(), is( 1.1 ) );
        assertThat( dimensionInterval.getMax().doubleValue(), is( 2.2 ) );
        assertThat( dimensionInterval.getResolution().doubleValue(), is( 0.0 ) );
    }

    @Test
    public void testParseAllLayerNodes()
                    throws Exception {
        NodeList allLayerNodes = parseLayers( wmtsCapabilities() );

        assertThat( allLayerNodes.getLength(), is( 29 ) );
    }

    @Test(expected = Exception.class)
    public void testParseAllLayerNodesWithNullShouldThrowException()
                    throws Exception {
        parseLayers( null );
    }

    @Test
    public void testParseRequestableLayerNodes()
                    throws Exception {
        NodeList requestableLayerNodes = parseLayers( wmtsCapabilities() );

        assertThat( requestableLayerNodes.getLength(), is( 24 ) );
    }

    @Test(expected = Exception.class)
    public void testParseRequestableLayerNodesWithNullShouldThrowException()
                    throws Exception {
    	parseLayers( null );
    }

    private LayerInfo getLayerInfoWithName( List<LayerInfo> layerInfos, String name ) {
        for ( LayerInfo layerInfo : layerInfos ) {
            if ( name.equals( layerInfo.getLayerName() ) )
                return layerInfo;
        }
        return null;
    }

    private Document wmtsCapabilities()
                    throws SAXException, IOException, ParserConfigurationException {
        return capabilities( "../capabilities_wmts10.xml" );
    }

    private Document wmtsCapabilitiesEmptyUpdateSequence()
                    throws ParserConfigurationException, SAXException, IOException {
        return capabilities( "../capabilities_wmts10_emptyUpdateSequence.xml" );
    }

    private Document wmtsCapabilitiesMissingUpdateSequence()
                    throws ParserConfigurationException, SAXException, IOException {
        return capabilities( "../capabilities_wmts10_missingUpdateSequence.xml" );
    }

    private Node layerNode( String layerName )
                    throws Exception {
        Document wmtsCapabilities = wmtsCapabilities();
        String expression = String.format( "//wms:Layer[wms:Name/text() = '%s']", layerName );

        return (Node) createXPath().evaluate( expression, wmtsCapabilities, XPathConstants.NODE );
    }

    private Document capabilities( String resource )
                    throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class.getResourceAsStream( resource );
        return builder.parse( new InputSource( wmtsCapabilities ) );
    }

    private XPath createXPath()
                    throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( WmtsNamespaces.withStandardBindings() );
        return xpath;
    }

    private Matcher<Iterable<? super NumberRequestableDimension>> hasNumberRequestableDimension( final int value ) {
        return new BaseMatcher<Iterable<? super NumberRequestableDimension>>() {

            @SuppressWarnings("unchecked")
            @Override
            public boolean matches( Object item ) {
                for ( Object iterable_element : (Iterable<? super NumberRequestableDimension>) item ) {
                    NumberRequestableDimension dim = (NumberRequestableDimension) iterable_element;
                    if ( dim.getValue().intValue() == value )
                        return true;
                }
                return false;
            }

            @Override
            public void describeTo( Description description ) {
                description.appendText( "Expected item: " + value );
            }
        };
    }

}