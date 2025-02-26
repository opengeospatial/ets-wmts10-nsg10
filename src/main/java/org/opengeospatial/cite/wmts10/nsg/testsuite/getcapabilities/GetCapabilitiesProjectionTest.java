package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.domain.BoundingBox;
import org.opengeospatial.cite.wmts10.ets.core.domain.LayerInfo;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.opengeospatial.cite.wmts10.nsg.core.util.NSG_CRSUtils;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesProjectionTest extends AbstractBaseGetCapabilitiesFixture {

	/**
	 * --- NSG Requirement 12: An NSG WMTS server shall support the following projections
	 * whose validity zones overlap data published by the service: • World Mercator
	 * Projection…EPSG:3395 • UPS projection over WGS84 (north zone)…… EPSG:5041 • UPS
	 * projection over WGS84 (south zone)…… EPSG:5042
	 *
	 * ---
	 */

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 12",
			dependsOnMethods = "verifyGetCapabilitiesSupported")
	public void wmtsCapabilitiesExists() {
		assertTrue(this.wmtsCapabilities != null, "No ServerMetadata Capabilities document");
	}

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 12",
			dependsOnMethods = "wmtsCapabilitiesExists")
	public void wmtsCapabilitiesEPSG3395Test() {
		assessAdvertisedProjections("EPSG:3395", "World Mercator Projection", -15496570.7397, 18764656.2314, -84.0,
				80.0);
	}

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 12",
			dependsOnMethods = "wmtsCapabilitiesExists")
	public void wmtsCapabilitiesUPS_NorthTest() {
		assessAdvertisedProjections("EPSG:5041", "WGS 84 / UPS North", -14440759.350252, 18440759.350252, 60.0, 90.0);
	}

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 12",
			dependsOnMethods = "wmtsCapabilitiesExists")
	public void wmtsCapabilitiesUPS_SouthTest() {
		assessAdvertisedProjections("EPSG:5042", "WGS 84 / UPS South", -14440759.350252, 18440759.350252, -90.0, -60.0);
	}

	private void assessAdvertisedProjections(String crsName2Look4, String crsFullName, double crsMin, double crsMax,
			double latMin, double latMax) {
		try {
			String parsedCrs = NSG_CRSUtils.normaliseCrsName(crsName2Look4);

			boolean crsFound = isCrsFound(parsedCrs);

			boolean hasLayerInRange = crsFound;
			boolean isAdvertised = crsFound;

			// --- not in TileMatrixSet, check if defined in the Layers
			if (!crsFound) {
				for (int layerI = 0; (layerI < layerInfo.size() && !crsFound); layerI++) {
					LayerInfo layer = layerInfo.get(layerI);

					List<BoundingBox> bbox = layer.getBboxes();

					for (int bboxI = -1; (bboxI < bbox.size() && !crsFound); bboxI++) {
						double minLat, maxLat;
						boolean insideLimits = false;
						String crsName = null;

						if (bboxI < 0) {
							crsName = layer.getGeographicBbox().getCrs();
							minLat = layer.getGeographicBbox().getMinY();
							maxLat = layer.getGeographicBbox().getMaxY();
							insideLimits = (((minLat >= latMin) && (minLat <= latMax))
									|| ((maxLat >= latMin) && (maxLat <= latMax)));
						}
						else {
							crsName = bbox.get(bboxI).getCrs();
							minLat = bbox.get(bboxI).getMinY();
							maxLat = bbox.get(bboxI).getMaxY();
							insideLimits = (((minLat >= crsMin) && (minLat <= crsMax))
									|| ((maxLat >= crsMin) && (maxLat <= crsMax)));
						}

						if (insideLimits) {
							hasLayerInRange = true;
							crsFound = (crsName.contains(parsedCrs));
						}
					}
				}
			}
			if (hasLayerInRange || !isAdvertised) {
				assertTrue(crsFound && isAdvertised, "WMTS does not support " + parsedCrs + " (" + crsFullName
						+ ") in any of its <Layer>s or <TileMatrixSet>s.");
			}
			else {
				throw new SkipException(
						"WMTS does not have a Layer within range of " + parsedCrs + " (" + crsFullName + ")");
			}
		}
		catch (XPathExpressionException xpe) {
		}
	}

	private boolean isCrsFound(String crsName2Look4) throws XPathExpressionException {
		boolean crsFound = false;
		NodeList crsList = ServiceMetadataUtils.getNodeElements(wmtsCapabilities, "//ows:SupportedCRS");
		for (int crsI = 0; (crsI < crsList.getLength() && !crsFound); crsI++) {
			Node supportedCRS = crsList.item(crsI);
			String crsName = NSG_CRSUtils.normaliseCrsName(supportedCRS.getTextContent());

			crsFound = (crsName.contains(crsName2Look4));
		}
		return crsFound;
	}

}