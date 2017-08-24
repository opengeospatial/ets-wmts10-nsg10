package org.opengeospatial.cite.wmts10.ets.core.crs;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

//import org.opengeospatial.cite.wmts10.ets.core.crs.CrsMatcher;
import org.opengeospatial.cite.wmts10.ets.core.domain.BoundingBox;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class CrsMatcherTest
{
/*--
    private CrsMatcher crsUtils = initCrsMatcher();

    @Test
    public void testParseBoundingBoxes() {
        List<BoundingBox> bboxes = crsUtils.parseBoundingBoxes();

        assertThat( bboxes.size(), is( 4 ) );
        assertThat( bboxes, hasItem( new BoundingBox( "EPSG:32661", -180, 60, 180, 90 ) ) );
        assertThat( bboxes, hasItem( new BoundingBox( "EPSG:32761", -180, -90, 180, -60 ) ) );
        assertThat( bboxes, hasItem( new BoundingBox( "EPSG:32601", -180, 0, -174, 84 ) ) );
        assertThat( bboxes, hasItem( new BoundingBox( "EPSG:32701", -180, -80, -174, 0 ) ) );
    }

    @Test
    public void testRetrieveOverlappingCrs_Outside() {
        BoundingBox geographicBoundingBox = new BoundingBox( "CRS:84", 0, 20, 10, 30 );
        List<String> overlappingCrs = crsUtils.retrieveOverlappingCrs( geographicBoundingBox );

        assertThat( overlappingCrs.size(), is( 0 ) );
    }

    @Test
    public void testRetrieveOverlappingCrs_OverlappingOne() {
        BoundingBox geographicBoundingBox = new BoundingBox( "CRS:84", 0, 50, 10, 65 );
        List<String> overlappingCrs = crsUtils.retrieveOverlappingCrs( geographicBoundingBox );

        assertThat( overlappingCrs.size(), is( 1 ) );
        assertThat( overlappingCrs, hasItem( "EPSG:32661" ) );
    }

    @Test
    public void testRetrieveOverlappingCrs_InsideOne() {
        BoundingBox geographicBoundingBox = new BoundingBox( "CRS:84", 0, 61, 10, 65 );
        List<String> overlappingCrs = crsUtils.retrieveOverlappingCrs( geographicBoundingBox );

        assertThat( overlappingCrs.size(), is( 1 ) );
        assertThat( overlappingCrs, hasItem( "EPSG:32661" ) );
    }

    @Test
    public void testRetrieveOverlappingCrs_OverlappingTwo() {
        BoundingBox geographicBoundingBox = new BoundingBox( "CRS:84", -175, 50, -170, 65 );
        List<String> overlappingCrs = crsUtils.retrieveOverlappingCrs( geographicBoundingBox );

        assertThat( overlappingCrs.size(), is( 2 ) );
        assertThat( overlappingCrs, hasItems( "EPSG:32661", "EPSG:32601" ) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveOverlappingCrs_NullCrs() {
        crsUtils.retrieveOverlappingCrs( null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveOverlappingCrs_UnexpectedCrs() {
        BoundingBox geographicBoundingBox = new BoundingBox( "EPSG:4326", -175, 50, -170, 65 );
        crsUtils.retrieveOverlappingCrs( geographicBoundingBox );
    }

    private CrsMatcher initCrsMatcher() {
        CrsMatcher spy = spy( CrsMatcher.class );
        InputStream resource = CrsMatcherTest.class.getResourceAsStream( "crs_test.properties" );
        when( spy.openStream() ).thenReturn( resource );
        return spy;
    }
--*/
}