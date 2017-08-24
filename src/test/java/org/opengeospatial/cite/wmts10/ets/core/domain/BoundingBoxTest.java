package org.opengeospatial.cite.wmts10.ets.core.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.opengeospatial.cite.wmts10.ets.core.domain.BoundingBox;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class BoundingBoxTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidX() {
        new BoundingBox( "EPSG:4326", 10, 53, 9, 55 );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidY() {
        new BoundingBox( "EPSG:4326", 9, 56, 10, 55 );
    }

    @Test
    public void testGetBboxAsString() {
        BoundingBox boundingBox = new BoundingBox( "EPSG:4326", 9.0, 53.0, 10.0, 55.0 );
        String bboxAsString = boundingBox.getBboxAsString();

        assertThat( bboxAsString, is( "9.0,53.0,10.0,55.0" ) );
    }

}