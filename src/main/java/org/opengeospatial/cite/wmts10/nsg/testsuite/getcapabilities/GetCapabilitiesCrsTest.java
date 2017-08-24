package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;

import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.domain.BoundingBox;
import org.opengeospatial.cite.wmts10.ets.core.domain.LayerInfo;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;

/*
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesCrsTest extends AbstractBaseGetCapabilitiesFixture 
{
/*---
	NSG Requirement 11: 
			An NSG WMTS server shall support the following coordinate reference systems:
			•	CRS:84 WGS84 geographic longitude, then latitude, expressed in decimal degrees
			•	EPSG:4326 WGS84 geographic latitude, then longitude, expressed in decimal degrees 
---*/
	
	// ---
	   
	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 11", dependsOnMethods="verifyGetCapabilitiesSupported")
	public void wmtsCapabilitiesExists() 
	{
		assertTrue(this.wmtsCapabilities != null, "No ServerMetadata Capabilities document");
	}
	
	// ---
	   
	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 11", dependsOnMethods="wmtsCapabilitiesExists")
	public void wmtsCapabilitiesCrsTest() 
	{
		try 
		{
			boolean CRS84 = false;
			boolean EPSG4326 = false;
			
			NodeList crsList = (NodeList)ServiceMetadataUtils.getNodeElements(wmtsCapabilities, "//ows:SupportedCRS");
			for (int crsI=0; (crsI<crsList.getLength() && !(CRS84 && EPSG4326)); crsI++)
			{
				Node supportedCRS = crsList.item(crsI);
				String crsName = supportedCRS.getTextContent();
				
				if ( !CRS84 && crsName.contains("CRS:") && crsName.contains(":84"))
				{
					int indx0 = crsName.lastIndexOf("CRS:");
					int indx1 = crsName.lastIndexOf(":84");
					if ( indx0 < indx1 )
					{
						String modCrsName = crsName.substring(indx0, indx0+3) + crsName.substring(indx1, indx1+3);
						CRS84 = (modCrsName.equals("CRS:84"));
					}
				}
				if ( !EPSG4326 && crsName.contains("EPSG:") && crsName.contains(":4326"))
				{
					int indx0 = crsName.lastIndexOf("EPSG:");
					int indx1 = crsName.lastIndexOf(":4326");
					if ( indx0 < indx1 )
					{
						String modCrsName = crsName.substring(indx0, indx0+4) + crsName.substring(indx1, indx1+5);
						EPSG4326 = (modCrsName.equals("EPSG:4326"));
					}
				}
			}
			
			// --- not in TileMatrixSet, check if defined in the Layers
			if ( !(CRS84 && EPSG4326) )
			{
				for (int layerI=0; (layerI<layerInfo.size() && !(CRS84 && EPSG4326)); layerI++)
				{
					LayerInfo layer = layerInfo.get(layerI);
					
					List<BoundingBox> bbox = layer.getBboxes();
					
					for (int bboxI =-1; (bboxI<bbox.size() && !(CRS84 && EPSG4326)); bboxI++)
					{
						String crsName = null;
						if ( bboxI < 0 )
						{
							crsName = layer.getGeographicBbox().getCrs();
						}
						else
						{
							crsName = bbox.get(bboxI).getCrs();
						}
						
						if ( !CRS84 && crsName.contains("CRS:") && crsName.contains(":84"))
						{
							int indx0 = crsName.lastIndexOf("CRS:");
							int indx1 = crsName.lastIndexOf(":84");
							if ( indx0 < indx1 )
							{
								String modCrsName = crsName.substring(indx0, indx0+3) + crsName.substring(indx1, indx1+3);
								CRS84 = (modCrsName.equals("CRS:84"));
							}
						}
						if ( !EPSG4326 && crsName.contains("EPSG:") && crsName.contains(":4326"))
						{
							int indx0 = crsName.lastIndexOf("EPSG:");
							int indx1 = crsName.lastIndexOf(":4326");
							if ( indx0 < indx1 )
							{
								String modCrsName = crsName.substring(indx0, indx0+4) + crsName.substring(indx1, indx1+5);
								EPSG4326 = (modCrsName.equals("EPSG:4326"));
							}
						}
					}
				}
			}
			
			assertTrue(CRS84, "WMTS does not support CRS:84 (WGS84) in none of its <Layer>s or <TileMatrixSet>s.");
			assertTrue(EPSG4326, "WMTS does not support EPSG:4326 (WGS84) in none of its <Layer>s or <TileMatrixSet>s.");
		}
		catch (XPathExpressionException xpe) 
		{
			//xpe.printStackTrace();
		}
	}
	
	// ---
	

	   // --- -------
/*--	   
	private XPath createXPath()
               throws XPathFactoryConfigurationException
	{
		XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext( NS_BINDINGS );
		return xpath;
	}
	--*/
}