package org.opengeospatial.cite.wmts10.nsg.testsuite.gettile;

import static org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion.assertContentType;
import static org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion.assertStatusCode;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.List;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.gettile.AbstractBaseGetTileFixture;
import org.testng.annotations.Test;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetTileCachingInfo extends AbstractBaseGetTileFixture {
    /**
     * --- NSG Requirement 19: : An NSG WMTS server shall provide caching information (expiration date) for the data.
     * ---
     */

    private URI getTileURI = null;

    private Response response = null;

    private List<Object> cacheControls = null;

    private List<Object> expires = null;

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 19", dependsOnMethods = "verifyGetTileSupported")
    public void wmtsGetTileKVPRequestsExists() {
        getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, WMTS_Constants.GET_TILE,
                                                                    ProtocolBinding.GET );
        assertTrue( getTileURI != null,
                    "GetTile (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document." );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 19", dependsOnMethods = "wmtsGetTileKVPRequestsExists")
    public void wmtsGetTileCachingInformationExists() {
        if ( getTileURI == null ) {
            getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, WMTS_Constants.GET_TILE,
                                                                        ProtocolBinding.GET );
        }

        this.reqEntity.removeKvp( WMTS_Constants.FORMAT_PARAM );
        String requestFormat = WMTS_Constants.IMAGE_PNG;
        this.reqEntity.addKvp( WMTS_Constants.FORMAT_PARAM, requestFormat );

        response = wmtsClient.submitRequest( this.reqEntity, getTileURI );

        assertTrue( response.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );

        storeResponseImage( response, "Requirement19", "simple", requestFormat );
        assertStatusCode( response.getStatus(), 200 );
        assertContentType( response.getHeaders(), requestFormat );

        cacheControls = response.getHeaders().get( "Cache-control" );
        expires = response.getHeaders().get( "Expires" );

        boolean anyCacheControls = ( ( cacheControls != null ) && ( cacheControls.size() > 0 ) );
        boolean anyExpires = ( ( expires != null ) && ( expires.size() > 0 ) );
        assertTrue( anyCacheControls || anyExpires, "WMTS does not provide appropriate caching information" );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 19", dependsOnMethods = "wmtsGetTileCachingInformationExists")
    public void wmtsGetTileExpirationExists() {
        boolean hasExpiration = false;
        if ( ( expires != null ) && ( expires.size() > 0 ) ) {
            hasExpiration = true;
        }

        if ( ( cacheControls != null ) && ( cacheControls.size() > 0 ) ) {
            String cacheControl = (String)cacheControls.get( 0 );
            hasExpiration |= ( cacheControl.contains( "max-age" ) || cacheControl.contains( "maxage" ) );
        }

        assertTrue( hasExpiration,
                    "WMTS has cache-control or expiration headers, but no expiration time or date is found." );
    }

}