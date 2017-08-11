package ets.wmts10.core.domain.dimension.number;

import java.text.NumberFormat;

import ets.wmts10.core.domain.dimension.RequestableDimension;

/**
 * {@link RequestableDimension} encapsulating a single {@link Number} value.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class NumberRequestableDimension implements RequestableDimension {

    private final Number value;

    /**
     * @param value
     *            never <code>null</code>
     */
    public NumberRequestableDimension( Number value ) {
        this.value = value;
    }

    @Override
    public String retrieveRequestableValue() {
        return NumberFormat.getInstance().format( getValue().floatValue() );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( getValue() == null ) ? 0 : getValue().hashCode() );
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
        NumberRequestableDimension other = (NumberRequestableDimension) obj;
        if ( getValue() == null ) {
            if ( other.getValue() != null )
                return false;
        } else if ( !getValue().equals( other.getValue() ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NumberRequestableDimension [value=" + getValue() + "]";
    }

    /**
     * @return the value ,never <code>null</code>
     */
    public Number getValue() {
        return value;
    }

}