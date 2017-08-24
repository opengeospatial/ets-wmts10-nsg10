package org.opengeospatial.cite.wmts10.ets.core.domain.dimension;

import java.util.List;
import java.util.Random;

/**
 * Encapsulates a {@link List} of {@link RequestableDimension}s.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class RequestableDimensionList implements RequestableDimension {

    private static final Random RANDOM = new Random();

    private final List<RequestableDimension> requestableDimensions;

    /**
     * @param requestableDimensions
     *            list of {@link RequestableDimension}s, never <code>null</code> or empty
     */
    public RequestableDimensionList( List<RequestableDimension> requestableDimensions ) {
        this.requestableDimensions = requestableDimensions;
    }

    @Override
    public String retrieveRequestableValue() {
        RequestableDimension requestableDimension = getRequestableDimensions().get( RANDOM.nextInt( getRequestableDimensions().size() ) );
        return requestableDimension.retrieveRequestableValue();
    }

    /**
     * @return the list of {@link RequestableDimension}s, never <code>null</code> or empty
     */
    public List<RequestableDimension> getRequestableDimensions() {
        return requestableDimensions;
    }

}