package ets.wmts10.core.domain;

import java.util.List;

/**
 * Represents a named layer from a capabilities document.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class LayerInfo
{

    private final String layerName;

    //private final boolean isQueryable;

    private final List<BoundingBox> bboxes;

    private final List<Dimension> dimensions;

    private final BoundingBox geographicBbox;

    /**
     * @param layerName
     *            name of the layer (ows:Layer/ows:Name), never <code>null</code> or empty
     * param isQueryable
     *            <code>true</code> if the layer is queryable (ows:Layer/@queryable=1), <code>false</code> if the layer
     *            is not queryable (ows:Layer/@queryable=1)
     * @param bboxes
     *            bounding boxes of the layer (ows:Layer/ows:ows:BoundingBox), never <code>null</code>
     * @param dimensions
     *            dimensions of the layer,never <code>null</code>
     * @param geographicBbox
     *            geographic bounding box of the layer, never <code>null</code>
     * @throws IllegalArgumentException
     *             if layerName or bboxes is <code>null</code>
     */
    public LayerInfo( String layerName, /*--boolean isQueryable,--*/ List<BoundingBox> bboxes, List<Dimension> dimensions, BoundingBox geographicBbox )
    {
        if ( layerName == null || layerName.isEmpty() )
            throw new IllegalArgumentException( "layerName must not be null!" );
        if ( bboxes == null )
            throw new IllegalArgumentException( "bboxes must not be null!" );
        if ( dimensions == null )
            throw new IllegalArgumentException( "dimensions must not be null!" );
        if ( geographicBbox == null )
            throw new IllegalArgumentException( "geographicBbox must not be null!" );
        //this.isQueryable = isQueryable;
        this.layerName = layerName;
        this.bboxes = bboxes;
        this.dimensions = dimensions;
        this.geographicBbox = geographicBbox;
    }

    /**
     * @return the name of the layer (ows:Layer/ows:Name), never <code>null</code>
     */
    public String getLayerName() {
        return layerName;
    }

    /**
     * @return <code>true</code> if the layer is queryable, <code>false</code> otherwise
     */
/*--    
    public boolean isQueryable() {
        return isQueryable;
    }
--*/
    /**
     * @return the bounding boxes of the layer (ows:Layer/ows:ows:BoundingBox), never <code>null</code>
     */
    public List<BoundingBox> getBboxes() {
        return bboxes;
    }

    /**
     * @return the dimensions of the layer (ows:Layer/ows:ows:Dimension), never <code>null</code>
     */
    public List<Dimension> getDimensions() {
        return dimensions;
    }
    
    /**
     * @return the geographic bounding box of the layer, never <code>null</code>
     */
    public BoundingBox getGeographicBbox() {
        return geographicBbox;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( bboxes == null ) ? 0 : bboxes.hashCode() );
        result = prime * result + ( ( layerName == null ) ? 0 : layerName.hashCode() );
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
        LayerInfo other = (LayerInfo) obj;
        if ( bboxes == null ) {
            if ( other.bboxes != null )
                return false;
        } else if ( !bboxes.equals( other.bboxes ) )
            return false;
        if ( layerName == null ) {
            if ( other.layerName != null )
                return false;
        } else if ( !layerName.equals( other.layerName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LayerInfo [layerName=" + layerName + ", bboxes=" + bboxes + "]";
    }

}