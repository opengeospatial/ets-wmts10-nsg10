package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.opengeospatial.cite.wmts10.nsg.core.util.NSG_CRSUtils;
import org.opengeospatial.cite.wmts10.nsg.core.util.NSG_XMLUtils;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesWellKnownScaleTest extends AbstractBaseGetCapabilitiesFixture {
	final private double tolerance = 1.0e-10;
	
    /**
     * --- NSG Requirement 13: An NSG WMTS server shall employ the Well-Known Scale Sets identified in Annex B (based
     * upon World Mercator projection EPSG 3395 and WGS 84 Geodetic EPSG 4326) ---
     */
	private final String Annex_B1_3395_Table = "Annex_B1_EPSG3395.xml";
	private final String Annex_B2_4326_Table = "Annex_B2_EPSG4326.xml";
	private final String NSG_WMTS_EPSG_5041 = "NSG_WMTS_EPSG_5041.xml";
	private final String NSG_WMTS_EPSG_5042 = "NSG_WMTS_EPSG_5042.xml";

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesExists() {
        assertTrue( this.wmtsCapabilities != null, "No ServerMetadata Capabilities document" );
    }


    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.1", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_3395_Test() throws IOException, XPathExpressionException
    {
        // --- check for well-known scale per Appendix B (for EPSG:3395)
    	
    	NodeList listFromAnnexB = NSG_XMLUtils.openXMLDocument( this.getClass().getResourceAsStream( this.Annex_B1_3395_Table ), "TileMatrix");
    	
    	String wellKnownScaleSet = "EPSG:3395";
    	if (( listFromAnnexB != null ) && ( listFromAnnexB.getLength() > 0 ))
    	{
    		this.wmtsCapabilitiesWellKnownScaleTest(wellKnownScaleSet, null, listFromAnnexB);
		}
		else
		{
			throw new IOException ("Required external file of " + wellKnownScaleSet + " values is not accessible");
    	}
    }
    
    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.2", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_4326_Test() 
    										throws IOException, XPathExpressionException
    {
         // --- check for well-known scale per Appendix B (for EPSG:4326)
    	
    	NodeList listFromAnnexB = NSG_XMLUtils.openXMLDocument( this.getClass().getResourceAsStream( this.Annex_B2_4326_Table ), "TileMatrix");
    	
    	String wellKnownScaleSet = "EPSG:4326";
    	if (( listFromAnnexB != null ) && ( listFromAnnexB.getLength() > 0 ))
    	{
    		this.wmtsCapabilitiesWellKnownScaleTest(wellKnownScaleSet, null, listFromAnnexB);
    		// --- this.wmtsCapabilitiesWellKnownScaleTest(wellKnownScaleSet, "CRS:84", listFromAnnexB);
		}
		else
		{
			throw new IOException ("Required external file of " + wellKnownScaleSet + " values is not accessible");
    	}
    }

	// ----------------------------------------------------

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.3", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_5041_Test() 
    										throws IOException, XPathExpressionException
    {
         // --- check for well-known scale per Appendix B (for EPSG:5041)
    	
    	NodeList listFromAnnexB = NSG_XMLUtils.openXMLDocument( this.getClass().getResourceAsStream( this.NSG_WMTS_EPSG_5041 ), "TileMatrix");
    	
    	String wellKnownScaleSet = "EPSG:5041";
    	if (( listFromAnnexB != null ) && ( listFromAnnexB.getLength() > 0 ))
    	{
    		this.wmtsCapabilitiesWellKnownScaleTest(wellKnownScaleSet, null, listFromAnnexB);
    		// --- this.wmtsCapabilitiesWellKnownScaleTest(wellKnownScaleSet, "EPSG:32661", listFromAnnexB);
		}
		else
		{
			throw new IOException ("Required external file of " + wellKnownScaleSet + " values is not accessible");
    	}
    }

	// ----------------------------------------------------

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 13, Annex B.3", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesWellKnownScaleTest_5042_Test()
    										throws IOException, XPathExpressionException
    {
         // --- check for well-known scale per Appendix B (for EPSG:5042)
    	
    	NodeList listFromAnnexB = NSG_XMLUtils.openXMLDocument( this.getClass().getResourceAsStream( this.NSG_WMTS_EPSG_5042 ), "TileMatrix");
    	
    	String wellKnownScaleSet = "EPSG:5042";
    	if (( listFromAnnexB != null ) && ( listFromAnnexB.getLength() > 0 ))
    	{
    		this.wmtsCapabilitiesWellKnownScaleTest(wellKnownScaleSet, null, listFromAnnexB);
    		// --- this.wmtsCapabilitiesWellKnownScaleTest(wellKnownScaleSet, "EPSG:32761", listFromAnnexB);
		}
		else
		{
			throw new IOException ("Required external file of " + wellKnownScaleSet + " values is not accessible");
    	}
    }
    
    
    private void wmtsCapabilitiesWellKnownScaleTest(String wellKnownScaleSet, String wellKnownScaleSet_alt, NodeList listFromAnnexB )
    		throws XPathExpressionException
    {
		boolean isWellKnown = false;
		
		wellKnownScaleSet = NSG_CRSUtils.parseCRS( wellKnownScaleSet );
		if (( wellKnownScaleSet_alt != null ) && !wellKnownScaleSet_alt.isEmpty() )
		{
			wellKnownScaleSet_alt = NSG_CRSUtils.parseCRS( wellKnownScaleSet_alt );
		}
			
		try
		{
			Node tileMatrixSet = null;
			NodeList tileMatrixSetList = (NodeList) ServiceMetadataUtils.getNodeElements( wmtsCapabilities, "//wmts:Contents/wmts:TileMatrixSet" );
			
			for ( int tmsI = 0; ( tmsI < tileMatrixSetList.getLength() && !isWellKnown ); tmsI++ ) 
			{
				tileMatrixSet = tileMatrixSetList.item( tmsI );
				String crsName = NSG_XMLUtils.getXMLElementTextValue((Element)tileMatrixSet, "ows:SupportedCRS");
				crsName = NSG_CRSUtils.parseCRS( crsName );

				isWellKnown = ( crsName.contains( wellKnownScaleSet ));
				if (( !isWellKnown ) && (( wellKnownScaleSet_alt != null ) && !wellKnownScaleSet_alt.isEmpty() )) 
				{
					isWellKnown = ( crsName.contains( wellKnownScaleSet_alt ));
				}
			}


			// --- in case "employed" means each is required, otherwise would fall through as a 'skip test' if not present
			//  assertTrue(isWellKnown, "Well-Known Scale Set for " + wellKnownScaleSet + " currently not advertised in WMTS" );

			if ( isWellKnown && ( tileMatrixSet != null ))
			{
				NodeList tileMatrixes = (NodeList)((Element)tileMatrixSet).getElementsByTagName("TileMatrix");

				if (( listFromAnnexB != null) && ( listFromAnnexB.getLength() > 0 ))
				{
					assertTrue( listFromAnnexB.getLength() >= tileMatrixes.getLength() );
					
					int annexI = 0; 
					int tmsI = 0;
					
					// -- check each advertised zoom level, ensuring each matches the prescribed tables
					while ( tmsI < tileMatrixes.getLength() )
					{
						Node annexNode = listFromAnnexB.item(annexI++);
						Node node_tms = tileMatrixes.item(tmsI++);
						
						String idStr = NSG_XMLUtils.getXMLElementTextValue((Element) node_tms,  "ows:Identifier");
						int idNum = 0;
						try
						{
							idNum = Integer.parseInt(idStr);
						}
						catch (Exception e)
						{
							idNum = 0;
						}
						
						// --- check ScaleDenominator

						double _tolerance = this.tolerance;

						String scaleDemon = NSG_XMLUtils.getXMLElementTextValue((Element)annexNode, "ScaleDenominator");
						BigDecimal bigNum = new BigDecimal(scaleDemon);
						
						if ( bigNum.scale() <= 0 )
						{
							_tolerance = 0.0;
						}
						else //if ( bigNum.scale() <= 10)
						{
							_tolerance = Math.pow(10.0, -(bigNum.scale()-2));
						}
						
						double ScaleDenominator4326 = Double.parseDouble(scaleDemon);//NSG_XMLUtils.getXMLElementTextValue((Element)node4326, "ScaleDenominator"));
						double ScaleDenominator     = Double.parseDouble(NSG_XMLUtils.getXMLElementTextValue((Element)node_tms, "ScaleDenominator"));

						assertTrue(( Math.abs(ScaleDenominator4326-ScaleDenominator) <= _tolerance),
								"TileMatrix #" + idStr + " contains an incorrect ScaleDenominator: " + ScaleDenominator + " (should be:  " + ScaleDenominator4326 + " )");
						
						// --- check tile dimensions ( TileWidth / TileHeight )
						
						int annexTileWidth  = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)annexNode, "TileWidth"));
						int annexTileHeight = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)annexNode, "TileHeight"));
						int tileWidth      = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)node_tms, "TileWidth"));
						int tileHeight     = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)node_tms, "TileHeight"));
						assertEquals( tileWidth, annexTileWidth,
								"TileMatrix #" + idStr + " contains an incorrect TileWidth: " + tileWidth + " (should be:  " + annexTileWidth + " )");						
						assertEquals( tileHeight, annexTileHeight,
								"TileMatrix #" + idStr + " contains an incorrect TileHeight: " + tileHeight + " (should be:  " + annexTileHeight + " )");						
						assertEquals( tileWidth, tileHeight,
								"TileMatrix #" + idStr + " has values for TileWidth (" + tileWidth + ") and TileHeight (" + tileHeight + ") that do not match");						
						
						// --- check matrix dimensions ( MatrixWidth / MatrixHeight )

						int annexMatrixWidth  = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)annexNode, "MatrixWidth"));
						int annexMatrixHeight = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)annexNode, "MatrixHeight"));
						int matrixWidth      = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)node_tms, "MatrixWidth"));
						int matrixHeight     = Integer.parseInt(NSG_XMLUtils.getXMLElementTextValue((Element)node_tms, "MatrixHeight"));
						assertEquals( matrixWidth, annexMatrixWidth,
								"TileMatrix #" + idStr + " contains an incorrect MatrixWidth: " + matrixWidth + " (should be:  " + annexMatrixWidth + " )");						
						assertEquals( matrixHeight, annexMatrixHeight,
								"TileMatrix #" + idStr + " contains an incorrect MatrixHeight: " + matrixHeight + " (should be:  " + annexMatrixHeight + " )");													
						
					}				
				}  	
			}
			else
			{
				throw new SkipException( "Well-Known Scale Set for " + wellKnownScaleSet + " currently not advertised in WMTS" );
			}
		}
		catch (XPathExpressionException e)
		{
			//e.printStackTrace();
			throw new XPathExpressionException(e);
		}

    }
}