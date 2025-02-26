package org.opengeospatial.cite.wmts10.ets.core;

/**
 * An enumerated type defining all recognized test run arguments.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public enum TestRunArg {

	/**
	 * An absolute URI referring to metadata about the WMTS implementation under test.
	 * This is expected to be a WMTS 1.0.0 capabilities document where the document
	 * element is {@code http://www.opengis.net/wmts} WMTS_Capabilities}.
	 */
	WMTS;

	@Override
	public String toString() {
		return name().toLowerCase();
	}

}