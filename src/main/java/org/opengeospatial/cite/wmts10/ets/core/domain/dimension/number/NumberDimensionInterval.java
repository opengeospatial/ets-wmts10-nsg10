package org.opengeospatial.cite.wmts10.ets.core.domain.dimension.number;

import static java.lang.Math.round;

import java.text.NumberFormat;
import java.util.Random;

import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.RequestableDimension;

/**
 * {@link RequestableDimension} encapsulating a number interval.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class NumberDimensionInterval implements RequestableDimension {

    private static final Random RANDOM = new Random();

    private final Number min;

    private final Number max;

    private final Number resolution;

    /**
     * @param min
     *            the minimum value (must be less than or equal to max), never <code>null</code>
     * @param max
     *            the maximum value (must be greater than or equal to min), never <code>null</code>
     * @param resolution
     *            the resolution between min and max (must be greater than or equal to 0), never <code>null</code>
     * @throws IllegalArgumentException
     *             if one of the parameter is null or invalid
     */
    public NumberDimensionInterval( Number min, Number max, Number resolution ) {
        checkParameters( min, max, resolution );
        this.min = min;
        this.max = max;
        this.resolution = resolution;
    }

    @Override
    public String retrieveRequestableValue() {
        int steps = calculateStepsBetween();
        if ( steps <= 0 )
            return asString( min.floatValue() );
        int stepsToGo = RANDOM.nextInt( steps );
        float newFloatValue = min.floatValue() + ( stepsToGo * resolution.floatValue() );
        return asString( newFloatValue );
    }

    @Override
    public String toString() {
        return "NumberDimensionInterval [min=" + min + ", max=" + max + ", resolution=" + resolution + "]";
    }

    /**
     * @return the minimum value (less than or equal to max), never <code>null</code>
     */
    public Number getMin() {
        return min;
    }

    /**
     * @return the maximum value (greater than or equal to min), never <code>null</code>
     */
    public Number getMax() {
        return max;
    }

    /**
     * @return the resolution between min and max (greater than or equal to 0), never <code>null</code>
     */
    public Number getResolution() {
        return resolution;
    }

    private String asString( float newFloatValue ) {
        return NumberFormat.getInstance().format( newFloatValue );
    }

    private int calculateStepsBetween() {
        float diff = max.floatValue() - min.floatValue();
        return round( diff / resolution.floatValue() );
    }

    private void checkParameters( Number min, Number max, Number resolution ) {
        if ( min == null )
            throw new IllegalArgumentException( "min must not be null!" );
        if ( max == null )
            throw new IllegalArgumentException( "max must not be null!" );
        if ( resolution == null )
            throw new IllegalArgumentException( "resolution must not be null!" );
        if ( min.floatValue() > max.floatValue() )
            throw new IllegalArgumentException( "min must be less than or equal to max!" );
        if ( resolution.floatValue() < 0 )
            throw new IllegalArgumentException( "resolution must be greater than or equal to 0!" );
    }

}