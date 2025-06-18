package org.opengeospatial.cite.wmts10.ets.core.domain;

/**
 * Represents a bounding box of a layer from a capabilities document.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class BoundingBox {

	private final String crs;

	private final double minX;

	private final double minY;

	private final double maxX;

	private final double maxY;

	/**
	 * @param crs of the bounding box, never <code>null</code>
	 * @param minX the min x value, must be a value less than maxX
	 * @param minY the min y value, must be a value less than maxY
	 * @param maxX the max x value, must be a value greater than minX
	 * @param maxY the max y value, must be a value greater than minY
	 * @throws IllegalArgumentException if crs is <code>null</code> or minX greater/equal
	 * maxX or minY greater/equal maxY
	 */
	public BoundingBox(String crs, double minX, double minY, double maxX, double maxY) {
		checkParameters(crs, minX, minY, maxX, maxY);
		this.crs = crs;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	/**
	 * @return the of the bounding box, never <code>null</code>
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * @return the min x value
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * @return the min y value
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * @return the max x value
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * @return the max y value
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * @return bounding box as a string
	 */
	public String getBboxAsString() {
		return minX + "," + minY + "," + maxX + "," + maxY;
	}

	private void checkParameters(String crs, double minX, double minY, double maxX, double maxY) {
		if (crs == null)
			throw new IllegalArgumentException("CRS must not be null!");
		if (minX >= maxX)
			throw new IllegalArgumentException("minX must be less than maxX!");
		if (minY >= maxY)
			throw new IllegalArgumentException("minY must be less than maxY!");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crs == null) ? 0 : crs.hashCode());
		long temp;
		temp = Double.doubleToLongBits(maxX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoundingBox other = (BoundingBox) obj;
		if (crs == null) {
			if (other.crs != null)
				return false;
		}
		else if (!crs.equals(other.crs))
			return false;
		if (Double.doubleToLongBits(maxX) != Double.doubleToLongBits(other.maxX))
			return false;
		if (Double.doubleToLongBits(maxY) != Double.doubleToLongBits(other.maxY))
			return false;
		if (Double.doubleToLongBits(minX) != Double.doubleToLongBits(other.minX))
			return false;
		if (Double.doubleToLongBits(minY) != Double.doubleToLongBits(other.minY))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoundingBox [crs=" + crs + ", minX=" + minX + ", minY=" + minY + ", maxX=" + maxX + ", maxY=" + maxY
				+ "]";
	}

}