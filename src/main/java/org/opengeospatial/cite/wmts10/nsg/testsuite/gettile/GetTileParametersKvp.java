package org.opengeospatial.cite.wmts10.nsg.testsuite.gettile;

import static org.testng.Assert.assertTrue;

import java.net.URI;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion;
import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.gettile.AbstractBaseGetTileFixture;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;
import jakarta.ws.rs.core.Response;

/**
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetTileParametersKvp extends AbstractBaseGetTileFixture {

	/**
	 * --- NSG Requirement 5: An NSG WMTS server shall respond to a GetTile operation
	 * request with a tile map that complies with the requested parameters. ---
	 */

	private URI getTileURI = null;

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 5",
			dependsOnMethods = "verifyGetTileSupported")
	public void wmtsGetTileKVPRequestsExists() {
		getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities, WMTS_Constants.GET_TILE,
				ProtocolBinding.GET);
		assertTrue(getTileURI != null,
				"GetTile (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");
	}

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 5",
			dependsOnMethods = "wmtsGetTileKVPRequestsExists")
	public void wmtsGetTileRequestFormatParameters(ITestContext testContext) {
		if (getTileURI == null) {
			getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities, WMTS_Constants.GET_TILE,
					ProtocolBinding.GET);
		}
		String requestFormat = null;

		try {
			XPath xPath = createXPath();

			String layerName = this.reqEntity.getKvpValue(WMTS_Constants.LAYER_PARAM);
			if (layerName == null) {
				NodeList layers = ServiceMetadataUtils.getNodeElements(xPath, wmtsCapabilities,
						"//wmts:Contents/wmts:Layer/ows:Identifier");
				if (layers.getLength() > 0) {
					layerName = ((Node) layers.item(0)).getTextContent().trim();
				}
			}
			NodeList imageFormats = ServiceMetadataUtils.getNodeElements(xPath, wmtsCapabilities,
					"//wmts:Contents/wmts:Layer[ows:Identifier = '" + layerName + "']/wmts:Format");

			SoftAssert sa = new SoftAssert();

			for (int i = 0; i < imageFormats.getLength(); i++) {
				this.reqEntity.removeKvp(WMTS_Constants.FORMAT_PARAM);

				requestFormat = imageFormats.item(i).getTextContent().trim();
				this.reqEntity.addKvp(WMTS_Constants.FORMAT_PARAM, requestFormat);

				Response rsp = wmtsClient.submitRequest(this.reqEntity, getTileURI);

				sa.assertTrue(rsp.hasEntity(), ErrorMessage.get(ErrorMessageKey.MISSING_XML_ENTITY));

				storeResponseImage(rsp, "Requirement5", "simple", requestFormat);
				WmtsAssertion.assertStatusCode(sa, rsp.getStatus(), 200);
				WmtsAssertion.assertContentType(sa, rsp.getHeaders(), requestFormat);
			}
			sa.assertAll();
		}
		catch (XPathExpressionException | XPathFactoryConfigurationException xpe) {
			assertTrue(false, "Invalid or corrupt XML or KVP structure:  " + xpe.getMessage());
		}
	}

	private XPath createXPath() throws XPathFactoryConfigurationException {
		XPathFactory factory = XPathFactory.newInstance(XPathConstants.DOM_OBJECT_MODEL);
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(NS_BINDINGS);
		return xpath;
	}

}