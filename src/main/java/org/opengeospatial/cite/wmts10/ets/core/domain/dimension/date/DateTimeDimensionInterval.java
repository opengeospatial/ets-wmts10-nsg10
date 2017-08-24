package org.opengeospatial.cite.wmts10.ets.core.domain.dimension.date;

import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.joda.time.Period;

import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.RequestableDimension;

/**
 * {@link RequestableDimension} encapsulating a date interval.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class DateTimeDimensionInterval implements RequestableDimension {

    private static final Random RANDOM = new Random();

    private final DateTime min;

    private final DateTime max;

    private final Period resolution;

    /**
     * @param min
     *            the start date of this interval (must be less than or equal to max), never <code>null</code>
     * @param max
     *            the end date of this interval (must be greater than or equal to min), never <code>null</code>
     * @param resolution
     *            the resolution between min and max, may be <code>null</code> (infinite-fine resolution)
     */
    public DateTimeDimensionInterval( DateTime min, DateTime max, Period resolution ) {
        this.min = min;
        this.max = max;
        this.resolution = resolution;
    }

    @Override
    public String retrieveRequestableValue() {
        if ( resolution == null )
            return randomDateInInterval();
        int steps = calculateStepsBetween();
        if ( steps <= 0 )
            return asString( min );
        int stepsToGo = RANDOM.nextInt( steps );
        DateTime withRandomPeriodAdded = min.withPeriodAdded( resolution, stepsToGo );
        return asString( withRandomPeriodAdded );
    }

    /**
     * @return the start date of this interval (must be less than or equal to max), never <code>null</code>
     */
    public DateTime getMin() {
        return min;
    }

    /**
     * @return the end date of this interval (must be greater than or equal to min), never <code>null</code>
     */
    public DateTime getMax() {
        return max;
    }

    /**
     * @return the resolution between min and max, may be <code>null</code> (infinite-fine resolution)
     */
    public Period getResolution() {
        return resolution;
    }

    private int calculateStepsBetween() {
        int steps = 0;
        DateTime current = min;
        while ( current.isBefore( max ) ) {
            current = current.plus( resolution );
            steps++;
        }
        return steps;
    }

    private String asString( DateTime withRandomPeriodAdded ) {
        return DatatypeConverter.printDateTime( withRandomPeriodAdded.toGregorianCalendar() );
    }

    private String randomDateInInterval() {
        long minMillis = min.getMillis();
        long maxMillis = max.getMillis();
        long diffMillis = maxMillis - minMillis;
        long nextTime = minMillis + nextLong( diffMillis );
        return asString( new DateTime( nextTime ) );
    }

    private long nextLong( long n ) {
        long bits;
        long val;
        do {
            bits = ( RANDOM.nextLong() << 1 ) >>> 1;
            val = bits % n;
        } while ( bits - val + ( n - 1 ) < 0L );
        return val;
    }

}