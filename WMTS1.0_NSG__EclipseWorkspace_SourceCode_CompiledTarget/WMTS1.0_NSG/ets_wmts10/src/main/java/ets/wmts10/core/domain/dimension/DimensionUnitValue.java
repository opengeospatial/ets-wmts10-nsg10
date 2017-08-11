package ets.wmts10.core.domain.dimension;

/**
 * Represents the value of Dimension elements.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class DimensionUnitValue {

    private final String units;

    private final RequestableDimension requestableDimension;

    /**
     * @param units
     *            the units of dimensional axis, never <code>null</code>
     * @param requestableDimension
     *            the parsed value of the dimension, may be <code>null</code>
     */
    public DimensionUnitValue( String units, RequestableDimension requestableDimension ) {
        this.units = units;
        this.requestableDimension = requestableDimension;
    }

    /**
     * @return the units of dimensional axis, never <code>null</code>
     */
    public String getUnits() {
        return units;
    }

    /**
     * @return the parsed value of the dimension, may be <code>null</code>
     */
    public RequestableDimension getRequestableDimension() {
        return requestableDimension;
    }

}