package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;

import org.testng.ITestContext;


import static org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion.assertContentType;
import static org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion.assertStatusCode;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import com.sun.jersey.api.client.ClientResponse;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;

/*
*
* @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
* @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
*
*/
public class GetCapabilitiesKvpFormatTest extends AbstractBaseGetCapabilitiesFixture 
{
/*---
	NSG Requirement 15: 
			An NSG WMTS server shall support negotiation of the standard version used for client-server 
			interactions (KVP encoding).  
---*/
	private URI getCapabilitiesURI;
	
	
	
	
	
	public GetCapabilitiesKvpFormatTest() 
	{
	}
	   
	// ---

	public void init(ITestContext testContext)
	{
		this.initBaseFixture(testContext);
		this.initParser();

		getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, WMTS_Constants.GET_CAPABILITIES, ProtocolBinding.GET );
		assertTrue(getCapabilitiesURI != null, "GetCapabilities (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");

		this.buildGetCapabilitiesRequest();
		
	}
	
	// ---
	
	public void TestXML()
	{
		verifyFormatResponse(WMTS_Constants.TEXT_XML);
	}
	// ---

	public void TestHTML()
	{
		verifyFormatResponse(WMTS_Constants.TEXT_HTML);
	}

	// ---
	   
	private void verifyFormatResponse(String requestFormat)
	{
		if ( getCapabilitiesURI == null )
		{
			getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, WMTS_Constants.GET_CAPABILITIES, ProtocolBinding.GET );
		}
		
		this.reqEntity.removeKvp(WMTS_Constants.FORMAT_PARAM);
		this.reqEntity.addKvp(WMTS_Constants.FORMAT_PARAM, requestFormat);
			
		ClientResponse rsp = wmtsClient.submitRequest( this.reqEntity, getCapabilitiesURI );
		assertTrue( rsp.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );
		assertStatusCode(  rsp.getStatus(),  200 );
		assertContentType( rsp.getHeaders(), requestFormat );
		
	}	 

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