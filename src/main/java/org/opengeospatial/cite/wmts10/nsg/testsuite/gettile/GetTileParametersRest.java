package org.opengeospatial.cite.wmts10.nsg.testsuite.gettile;

import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.glassfish.jersey.client.ClientConfig;
import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.gettile.AbstractBaseGetTileFixture;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.util.Strings;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.latlon.ets.core.assertion.ETSAssert;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetTileParametersRest extends AbstractBaseGetTileFixture {

	/**
	 * --- NSG Requirement 7: An NSG WMTS server shall provide standard endpoints from
	 * which a representation of each Tile resource can b e obtained. ---
	 */

	private URI getTileURI = null;

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 7",
			dependsOnMethods = "verifyGetTileSupported")
	public void wmtsGetTileRESTCapable() throws XPathExpressionException, XPathFactoryConfigurationException {
		getTileURI = ServiceMetadataUtils.getOperationEndpoint_REST(wmtsCapabilities, WMTS_Constants.GET_TILE,
				ProtocolBinding.GET);
		URI sampleURI = getTileURI;

		if (getTileURI == null) {
			XPath xPath = createXPath();
			NodeList resourceURLs = (NodeList) ServiceMetadataUtils.getNodeElements(xPath, wmtsCapabilities,
					"//wmts:Contents/wmts:Layer/wmts:ResourceURL");

			for (int i = 0; ((sampleURI == null) && (i < resourceURLs.getLength())); i++) {
				String templateURL = (String) ServiceMetadataUtils.getNodeText(xPath, resourceURLs.item(i),
						"@template");
				String tileMatrixSet = (String) ServiceMetadataUtils.getNodeText(xPath, wmtsCapabilities,
						"//wmts:Contents/wmts:TileMatrixSet/ows:Identifier");
				if (templateURL != null) {
					try {
						templateURL = templateURL.replaceAll("\\{(?i)Style\\}", "default");
						templateURL = templateURL.replaceAll("\\{(?i)TileMatrixSet\\}", tileMatrixSet);
						templateURL = templateURL.replaceAll("\\{(?i)TileMatrix\\}", "0");
						templateURL = templateURL.replaceAll("\\{(?i)TileRow\\}", "0");
						templateURL = templateURL.replaceAll("\\{(?i)TileCol\\}", "0");
						// --- just in case
						templateURL = templateURL.replaceAll("\\{(?i)I\\}", "0");
						templateURL = templateURL.replaceAll("\\{(?i)J\\}", "0");
						sampleURI = new URI(templateURL);
					}
					catch (URISyntaxException ue) {
						sampleURI = null;
					}
				}
			}
		}
		assertTrue(sampleURI != null,
				"GetTile (GET) endpoint not found or REST is not supported in ServiceMetadata capabilities document.");
	}

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 7",
			dependsOnMethods = "wmtsGetTileRESTCapable")
	public void wmtsGetTileRequestParametersSupported(ITestContext testContext) {
		String requestFormat = null;

		try {
			String layerName = this.reqEntity.getKvpValue(WMTS_Constants.LAYER_PARAM);
			if (layerName == null) {
				NodeList layers = ServiceMetadataUtils.getNodeElements(wmtsCapabilities,
						"//wmts:Contents/wmts:Layer/ows:Identifier");
				if (layers.getLength() > 0) {
					layerName = ((Node) layers.item(0)).getTextContent().trim();
				}
			}

			XPath xPath = createXPath();

			// --- get the prepopulated KVP parameters, for the SOAP parameters

			String style = this.reqEntity.getKvpValue(WMTS_Constants.STYLE_PARAM);
			String tileMatrixSet = this.reqEntity.getKvpValue(WMTS_Constants.TILE_MATRIX_SET_PARAM);
			String tileMatrix = this.reqEntity.getKvpValue(WMTS_Constants.TILE_MATRIX_PARAM);
			String tileRow = this.reqEntity.getKvpValue(WMTS_Constants.TILE_ROW_PARAM);
			String tileCol = this.reqEntity.getKvpValue(WMTS_Constants.TILE_COL_PARAM);

			requestFormat = this.reqEntity.getKvpValue(WMTS_Constants.FORMAT_PARAM);

			{
				NodeList resourceURLs = ServiceMetadataUtils.getNodeElements(wmtsCapabilities,
						"//wmts:Contents/wmts:Layer[ows:Identifier = '" + layerName + "']/wmts:ResourceURL");

				Assert.assertTrue(((resourceURLs != null) && (resourceURLs.getLength() > 0)),
						"WMTS apparently does not support REST or contains no REST endpoints for layer: " + layerName);

				Random random = new Random();
				int randomIndx = random.nextInt(resourceURLs.getLength());

				Node resourceNode = resourceURLs.item(randomIndx);

				String templateURL = (String) ServiceMetadataUtils.getNodeText(xPath, resourceNode, "@template");
				requestFormat = (String) ServiceMetadataUtils.getNodeText(xPath, resourceNode, "@format");
				if (Strings.isNullOrEmpty(templateURL) || Strings.isNullOrEmpty(requestFormat)) {
					throw new XPathExpressionException("Invalid or corrupt Resource URL image format");
				}

				try {
					templateURL = templateURL.replaceAll("\\{(?i)Style\\}", style);
					templateURL = templateURL.replaceAll("\\{(?i)TileMatrixSet\\}", tileMatrixSet);
					templateURL = templateURL.replaceAll("\\{(?i)TileMatrix\\}", tileMatrix);
					templateURL = templateURL.replaceAll("\\{(?i)TileRow\\}", tileRow);
					templateURL = templateURL.replaceAll("\\{(?i)TileCol\\}", tileCol);
					// --- just in case
					templateURL = templateURL.replaceAll("\\{(?i)I\\}", "0");
					templateURL = templateURL.replaceAll("\\{(?i)J\\}", "0");
					getTileURI = new URI(templateURL);
				}
				catch (URISyntaxException ue) {
					getTileURI = null;
				}
			}
			assertUrl(getTileURI.toString());

			// --- Example of valid URL
			{

				ClientConfig config = new ClientConfig();
				Client client = ClientBuilder.newClient(config);
				WebTarget target = client.target(getTileURI);
				Builder reqBuilder = target.request();
				Response rsp = reqBuilder.buildGet().invoke();
				this.rspEntity = rsp.readEntity(Document.class);

				Assert.assertTrue(rsp != null, "Error processing REST GetTile request");

				storeResponseImage(rsp, "Requirement7", "simple", requestFormat);

				ETSAssert.assertContentType(rsp.getHeaders(), requestFormat);
				ETSAssert.assertStatusCode(rsp.getStatus(), 200);
			}
			// --- Example of invalid URL
			{
				String erroneousURL = getTileURI.toString();
				int indx = erroneousURL.lastIndexOf("/");
				erroneousURL = erroneousURL.substring(0, indx + 1) + "X" + erroneousURL.substring(indx + 1);

				URI invalidURI = null;
				try {
					invalidURI = new URI(erroneousURL);
				}
				catch (URISyntaxException ue) {
					System.out.println(ue.getMessage());
					invalidURI = null;
				}

				ClientConfig config = new ClientConfig();
				Client client = ClientBuilder.newClient(config);
				WebTarget target = client.target(invalidURI);
				Builder reqBuilder = target.request();
				Response rsp = reqBuilder.buildGet().invoke();
				this.rspEntity = rsp.readEntity(Document.class);

				Assert.assertTrue(rsp != null, "Error processing invalid REST GetTile request");
				Assert.assertFalse(rsp.getStatus() == 200,
						"Expected status code from Invalid REST GetTile request is not expected to be 200. ");
				ETSAssert.assertContentType(rsp.getHeaders(), WMTS_Constants.TEXT_XML);

			}
		}
		catch (XPathExpressionException | XPathFactoryConfigurationException xpe) {
			assertTrue(false, "Error found when retrieving REST Get Tile request: " + xpe.getMessage());
		}
	}

	private XPath createXPath() throws XPathFactoryConfigurationException {
		XPathFactory factory = XPathFactory.newInstance(XPathConstants.DOM_OBJECT_MODEL);
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(NS_BINDINGS);
		return xpath;
	}

}