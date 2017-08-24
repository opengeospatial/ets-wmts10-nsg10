package org.opengeospatial.cite.wmts10.ets.core.domain.dimension.number;

import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Test;

import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.number.NumberDimensionInterval;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class NumberDimensionIntervalTest {

    @Test
    public void testRetrieveRequestableValue_Int()
                    throws Exception {
        Number min = 1;
        Number max = 10;
        Number resolution = 1;
        NumberDimensionInterval interval = new NumberDimensionInterval( min, max, resolution );

        String requestableValue = interval.retrieveRequestableValue();
        Number requestValue = asNumber( requestableValue );

        for ( int i = 0; i < 25; i++ ) {
            assertTrue( requestValue.floatValue() >= min.floatValue() );
            assertTrue( requestValue.floatValue() <= max.floatValue() );
        }
    }

    @Test
    public void testRetrieveRequestableValue_Decimal()
                    throws Exception {
        Number min = 1.9;
        Number max = 2.6;
        Number resolution = 0.1;
        NumberDimensionInterval interval = new NumberDimensionInterval( min, max, resolution );

        String requestableValue = interval.retrieveRequestableValue();
        Number requestValue = asNumber( requestableValue );

        for ( int i = 0; i < 25; i++ ) {
            assertTrue( requestValue.floatValue() >= min.floatValue() );
            assertTrue( requestValue.floatValue() <= max.floatValue() );
        }
    }

    @Test
    public void testRetrieveRequestableValue_EmptyDayInterval()
                    throws Exception {
        Number min = 1;
        Number max = 1;
        Number resolution = 1;
        NumberDimensionInterval interval = new NumberDimensionInterval( min, max, resolution );

        String requestableValue = interval.retrieveRequestableValue();
        Number requestValue = asNumber( requestableValue );

        assertTrue( requestValue.floatValue() == min.floatValue() );
        assertTrue( requestValue.floatValue() == max.floatValue() );
    }

    @Test
    public void testRetrieveRequestableValue_ZeroResolution()
                    throws Exception {
        Number min = 1;
        Number max = 1;
        Number resolution = 0;
        NumberDimensionInterval interval = new NumberDimensionInterval( min, max, resolution );

        String requestableValue = interval.retrieveRequestableValue();
        Number requestValue = asNumber( requestableValue );

        for ( int i = 0; i < 25; i++ ) {
            assertTrue( requestValue.floatValue() >= min.floatValue() );
            assertTrue( requestValue.floatValue() <= max.floatValue() );
        }
    }

    private Number asNumber( String number )
                    throws ParseException {
        return NumberFormat.getInstance().parse( number );
    }

}
