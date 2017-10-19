package org.opengeospatial.cite.wmts10.nsg.core.util;

public class NSG_CRSUtils 
{

	public static String parseCRS(String crsNameIN)
	{
		// --- convert possible strings of:  "urn:ogc:def:crs:EPSG::3857" into "EPSG:3857"
		
		String crsName = crsNameIN.toUpperCase().replace("::",  ":");
		
		int lastIndx = crsName.lastIndexOf(":");
		int indx = crsName.lastIndexOf("EPSG:");
		int len = 5;
		if ( indx < 0 )
		{
			indx = crsName.lastIndexOf("CRS:");
			len = 4;
		}
		
		if ( indx >= 0 )
		{
			crsName = crsName.substring( indx, indx + len ) + crsName.substring(lastIndx + 1);
		}
		return crsName;
	}
}
