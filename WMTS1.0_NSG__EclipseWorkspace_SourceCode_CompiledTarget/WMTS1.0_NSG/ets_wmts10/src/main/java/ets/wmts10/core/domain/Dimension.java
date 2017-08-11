package ets.wmts10.core.domain;

import ets.wmts10.core.domain.dimension.DimensionUnitValue;

/**
 * Represents a dimension element from a layer.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class Dimension {

    private final String name;

    private final DimensionUnitValue dimensionValue;

    /**
     * @param name
     *            of the dimension, never <code>null</code>
     *  
     * @param dimensionValue
     * 			the value of the dimension, never <code>null</code>
     */
    public Dimension( String name, DimensionUnitValue dimensionValue ) {
        if ( name == null )
            throw new IllegalArgumentException( "name must not be null!" );
        if ( dimensionValue == null )
            throw new IllegalArgumentException( "dimensionValue must not be null!" );
        this.name = name;
        this.dimensionValue = dimensionValue;
    }

    /**
     * @return the name of the dimension, never <code>null</code>
     */
    public String getName() {
        return name;
    }

    /**
     * @return the values of the dimension, never <code>null</code>
     */
    public DimensionUnitValue getDimensionValue() {
        return dimensionValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( dimensionValue == null ) ? 0 : dimensionValue.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Dimension other = (Dimension) obj;
        if ( dimensionValue == null ) {
            if ( other.dimensionValue != null )
                return false;
        } else if ( !dimensionValue.equals( other.dimensionValue ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Dimension [name=" + name + ", dimensionValue=" + dimensionValue + "]";
    }

}