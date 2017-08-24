package org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities;


import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.core.util.request.WmtsKvpRequestBuilder;
import org.opengeospatial.cite.wmts10.ets.testsuite.AbstractBaseGetFixture;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS and/or WMTS)
 */
public abstract class AbstractBaseGetCapabilitiesFixture extends AbstractBaseGetFixture 
{
    /**
     * Builds a (WmtsKvpRequest} representing a GetCapabilities request for a complete service metadata document.
     */
    @BeforeClass
    public void buildGetCapabilitiesRequest()
    {
    	this.reqEntity = WmtsKvpRequestBuilder.buildGetCapabilitiesRequest(wmtsCapabilities, layerInfo);
    }

    @Test
    public void verifyGetCapabilitiesSupported() 
    {
    	Node getCapabilitiesEntry = null;
    	try
    	{
    		getCapabilitiesEntry = (Node)ServiceMetadataUtils.getNode(wmtsCapabilities, "//ows:OperationsMetadata/ows:Operation[@name = 'GetCapabilities']");
		} 
    	catch (XPathExpressionException e) 
    	{
		}
    	assertNotNull( getCapabilitiesEntry, "GetCapabilities is not supported by this WMTS" );
    }
    
    // ---
    
    // ---
    
  
    // --- -------
  
}