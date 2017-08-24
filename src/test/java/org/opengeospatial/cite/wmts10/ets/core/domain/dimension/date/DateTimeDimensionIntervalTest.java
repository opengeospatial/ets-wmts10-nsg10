package org.opengeospatial.cite.wmts10.ets.core.domain.dimension.date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class DateTimeDimensionIntervalTest {

    @Test
    public void testRetrieveRequestableValue() {
        DateTime min = asDateTime( "2009-01-01T00:00:00Z" );
        DateTime max = asDateTime( "2009-01-07T00:00:00Z" );
        Period resolution = asPeriod( "P1D" );
        DateTimeDimensionInterval interval = new DateTimeDimensionInterval( min, max, resolution );

        String requestableValue = interval.retrieveRequestableValue();
        DateTime requestValue = asDateTime( requestableValue );

        for ( int i = 0; i < 25; i++ ) {
            assertTrue( requestValue.isAfter( min ) || requestValue.isEqual( min ) );
            assertTrue( requestValue.isBefore( max ) || requestValue.isEqual( max ) );
        }
    }

    @Test
    public void testRetrieveRequestableValue_EmptyDayInterval() {
        DateTime min = asDateTime( "2009-01-01T00:00:00Z" );
        DateTime max = asDateTime( "2009-01-01T00:00:00Z" );
        Period resolution = asPeriod( "P1D" );
        DateTimeDimensionInterval interval = new DateTimeDimensionInterval( min, max, resolution );

        String requestableValue = interval.retrieveRequestableValue();
        DateTime requestValue = asDateTime( requestableValue );

        assertThat( requestValue.isEqual( min ), is( true ) );
        assertThat( requestValue.isEqual( max ), is( true ) );
    }

    @Test
    public void testRetrieveRequestableValue_NullInterval() {
        DateTime min = asDateTime( "2009-01-01T00:00:00Z" );
        DateTime max = asDateTime( "2009-01-02T00:00:00Z" );
        Period resolution = null;
        DateTimeDimensionInterval interval = new DateTimeDimensionInterval( min, max, resolution );

        String requestableValue = interval.retrieveRequestableValue();
        DateTime requestValue = asDateTime( requestableValue );

        assertTrue( requestValue.isAfter( min ) || requestValue.isEqual( min ) );
        assertTrue( requestValue.isBefore( max ) || requestValue.isEqual( max ) );
    }

    private DateTime asDateTime( String dateTime ) {
        return new DateTime( DatatypeConverter.parseDateTime( dateTime ).getTimeInMillis() );
    }

    private Period asPeriod( String period ) {
        return ISOPeriodFormat.standard().parsePeriod( period );
    }

}