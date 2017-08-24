package org.opengeospatial.cite.wmts10.ets.core.domain.dimension.date;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;

import org.opengeospatial.cite.wmts10.ets.core.domain.dimension.RequestableDimension;

/**
 * {@link RequestableDimension} encapsulating a single {@link DateTime} value.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class DateTimeRequestableDimension implements RequestableDimension {

    private final DateTime value;

    /**
     * @param value
     *            never <code>null</code>
     */
    public DateTimeRequestableDimension( DateTime value ) {
        this.value = value;
    }

    @Override
    public String retrieveRequestableValue() {
        return DatatypeConverter.printDateTime( value.toGregorianCalendar() );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
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
        DateTimeRequestableDimension other = (DateTimeRequestableDimension) obj;
        if ( value == null ) {
            if ( other.value != null )
                return false;
        } else if ( !value.equals( other.value ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DateTimeRequestableDimension [value=" + value + "]";
    }

    /**
     * @return the value, never <code>null</code>
     */
    public DateTime getValue() {
        return value;
    }

}