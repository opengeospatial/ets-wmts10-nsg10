package org.opengeospatial.cite.wmts10.nsg.core.util;

/**
 * @author Jim Beatty
 */
public class NSG_CRSUtils {

	/**
	 * Converts the passed crs a form like 'EPSG:4326' e.g "urn:ogc:def:crs:EPSG::3857"
	 * into "EPSG:3857"
	 * @param crsNameToNormalise name of the crs to normalise, never <code>null</code>
	 * @return the normalised crs, never <code>null</code>
	 */
	public static String normaliseCrsName(String crsNameToNormalise) {
		String crsName = crsNameToNormalise.toUpperCase().replace("::", ":");

		int lastIndx = crsName.lastIndexOf(":");
		int indx = crsName.lastIndexOf("EPSG:");
		int len = 5;
		if (indx < 0) {
			indx = crsName.lastIndexOf("CRS:");
			len = 4;
		}

		if (indx >= 0) {
			crsName = crsName.substring(indx, indx + len) + crsName.substring(lastIndx + 1);
		}
		return crsName;
	}

}
