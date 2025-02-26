package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;
import jakarta.ws.rs.core.Response;

/**
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesKvpVersionTest extends AbstractBaseGetCapabilitiesFixture {

	/**
	 * --- NSG Requirement 15: An NSG WMTS server shall support negotiation of the
	 * standard version used for client-server interactions (KVP encoding). ---
	 */
	private URI getCapabilitiesURI;

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 15",
			dependsOnMethods = "verifyGetCapabilitiesSupported")
	public void wmtsGetCapabilitiesURLExists() {
		getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities,
				WMTS_Constants.GET_CAPABILITIES, ProtocolBinding.GET);
		assertTrue(getCapabilitiesURI != null,
				"GetCapabilities (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");
	}

	@Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 15",
			dependsOnMethods = "wmtsGetCapabilitiesURLExists")
	public void wmtsCapabilitiesVersionTest() {
		String versionManipulation = WMTS_Constants.VERSION;

		// --- try a version number higher
		versionManipulation = (1 + Integer.parseInt(versionManipulation.substring(0, 1)))
				+ versionManipulation.substring(1);
		assertFalse(verifyVersionResponse(versionManipulation),
				"WMTS did not handle an invalid version ('" + versionManipulation + "') correctly");

		// --- try a version number lower
		// versionManipulation = WMTS_Constants.VERSION;
		versionManipulation = "0.0.1";
		assertFalse(verifyVersionResponse(versionManipulation),
				"WMTS did not handle an invalid version ('" + versionManipulation + "') correctly");

		// --- try the correct version number
		assertTrue(verifyVersionResponse(WMTS_Constants.VERSION),
				"This WMTS does not accept the current version ('" + WMTS_Constants.VERSION + "')");
	}

	private boolean verifyVersionResponse(String version) {
		boolean result = false;

		if (getCapabilitiesURI == null) {
			getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities,
					WMTS_Constants.GET_CAPABILITIES, ProtocolBinding.GET);
		}

		this.reqEntity.removeKvp(WMTS_Constants.ACCEPT_VERSIONS_PARAM);
		this.reqEntity.addKvp(WMTS_Constants.ACCEPT_VERSIONS_PARAM, version);

		Response rsp = wmtsClient.submitRequest(this.reqEntity, getCapabilitiesURI);
		this.rspEntity = rsp.readEntity(Document.class);
		assertTrue(rsp.hasEntity(), ErrorMessage.get(ErrorMessageKey.MISSING_XML_ENTITY));

		result = (rsp.getStatus() == 200);

		return result;
	}

}