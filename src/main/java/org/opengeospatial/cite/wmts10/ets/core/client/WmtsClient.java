package org.opengeospatial.cite.wmts10.ets.core.client;

import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Source;

import org.apache.tika.io.FilenameUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.logging.LoggingFeature;
import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.SOAPMessageConsumer;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.w3c.dom.Document;

import de.latlon.ets.core.util.XMLUtils;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

/**
 * A WMTS 1.0.0 client component supporting HTTP GET and POST.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS/WMTS)
 */
public class WmtsClient {

    private static final Logger LOGR = Logger.getLogger( WmtsClient.class.getPackage().getName() );

    private Client client;

    /** A Document that describes the service under test. */
    private Document wmtsCapabilities;

    /**
     * Constructs a client that is aware of the capabilities of some WMTS. The request and response may be logged to a
     * default JDK logger (in the namespace "com.sun.jersey.api.client").
     * 
     * @param wmtsCapabilities
     *            the WMTS capabilities document, never <code>null</code>
     */
    public WmtsClient( Document wmtsCapabilities ) {
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.FOLLOW_REDIRECTS, true);
        config.property(ClientProperties.CONNECT_TIMEOUT, 10000);
        config.register(new LoggingFeature(LOGR, Level.ALL, LoggingFeature.Verbosity.PAYLOAD_ANY, 5000));
        config.register(SOAPMessageConsumer.class);
        this.client = ClientBuilder.newClient(config);
        this.wmtsCapabilities = wmtsCapabilities;
    }

    /**
     * Retrieves a complete representation of the capabilities document from the WMTS implementation described by the
     * service metadata.
     * 
     * @return a document containing the response to a GetCapabilities request
     */
    public Document getCapabilities() {
        if ( null == this.wmtsCapabilities ) {
            throw new IllegalStateException( "Service description is unavailable." );
        }
        URI endpoint = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities,
                                                                      WMTS_Constants.GET_CAPABILITIES,
                                                                      ProtocolBinding.GET );
        if ( null == endpoint ) {
            throw new RuntimeException( "GetCapabilities (GET) endpoint not found in capabilities document." );
        }
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add( WMTS_Constants.REQUEST_PARAM, WMTS_Constants.GET_CAPABILITIES );
        queryParams.add( WMTS_Constants.SERVICE_PARAM, WMTS_Constants.SERVICE_TYPE_CODE );
        queryParams.add( WMTS_Constants.VERSION_PARAM, WMTS_Constants.VERSION );
        UriBuilder uriBuilder = UriBuilder.fromUri(endpoint);
        if (null != queryParams) {
                for (Entry<String, List<String>> param : queryParams.entrySet()) {
                        uriBuilder.queryParam(param.getKey(), param.getValue().get(0));
                }
        }
        URI uri = uriBuilder.build();
        WebTarget target = this.client.target(uri);
        Builder reqBuilder = target.request();
        return reqBuilder.buildGet().invoke().readEntity(Document.class);
    }

    /**
     * Submits a HTTP GET request.
     * 
     * @param request
     *            the KVP encoded request, never <code>null</code>
     * @param endpoint
     *            the service endpoint, never <code>null</code>
     * 
     * @return the {@link ClientResponse} object representing the response message
     */
    public Response submitRequest( WmtsKvpRequest request, URI endpoint ) {
        WebTarget target = this.client.target(endpoint);
        return submitGetRequest(target, request);
    }

    private Response submitPostRequest(WebTarget target, Source payload ) {
        LOGR.log( Level.FINE, String.format( "Submitting POST request to URI %s", target.getUri() ) );
        LOGR.log( Level.FINE, String.format( "Request Payload: %s", XMLUtils.transformToString( payload ) ) );
        Response response = null;
        try {
            response = target.request(WMTS_Constants.SOAP_XML).buildPost(Entity.entity(payload, WMTS_Constants.SOAP_XML)).invoke();
            if ( LOGR.isLoggable( Level.FINE ) ) {
                LOGR.log( Level.FINE, String.format( "SOAP Response: %s", FilenameUtils.normalize(response.toString()) ) );
            }
        } catch ( ProcessingException ex ) {
            LOGR.log( Level.SEVERE, "Failed to process SOAP request/response: " + target.getUri(), ex );
        }
        return response;
    }

    private Response submitGetRequest( WebTarget target, WmtsKvpRequest requestParameter ) {
        LOGR.log(Level.FINE, String.format("Submitting GET request to URI %s", target.getUri()));
        String queryString = requestParameter.asQueryString();
        URI requestURI = UriBuilder.fromUri(target.getUri()).replaceQuery(queryString).build();
        LOGR.log(Level.FINE, String.format("Request URI: %s", requestURI));
        target = this.client.target(requestURI);
        Builder reqBuilder = target.request();
        Invocation req = reqBuilder.buildGet();
        return req.invoke();
    }

}