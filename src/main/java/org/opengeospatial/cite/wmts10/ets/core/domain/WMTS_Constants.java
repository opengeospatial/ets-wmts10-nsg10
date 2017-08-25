package org.opengeospatial.cite.wmts10.ets.core.domain;

/**
 * Contains various constants pertaining to WMTS service interfaces and related standards.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS and/or WMTS)
 */
public final class WMTS_Constants {

    private WMTS_Constants() {
    }

    /** Capabilities element indicating support for HTTP GET method bindings. */
    public static final String KVP_ENC = "Get";

    /** Capabilities element indicating support for HTTP POST method bindings. */
    public static final String XML_ENC = "Post";

    /** Capabilities element indicating support for SOAP HTTP method bindings. */
    public static final String SOAP_ENC = "Post";

    /** Local name of document element in WMTS capabilities document. */
    public static final String WMTS_CAPABILITIES = "Capabilities";

    /** common request values **/
    public static final String SERVICE_TYPE_CODE = "WMTS";

    public static final String VERSION = "1.0.0";

    /** request types **/
    public static final String GET_CAPABILITIES = "GetCapabilities";

    public static final String GET_TILE = "GetTile";

    public static final String GET_FEATURE_INFO = "GetFeatureInfo";

    /** common request parameters **/
    public static final String REQUEST_PARAM = "Request";

    public static final String SERVICE_PARAM = "Service";

    public static final String VERSION_PARAM = "Version";

    /** GetCapabilities request parameters **/
    public static final String UPDATE_SEQUENCE_PARAM = "UpdateSequence";

    public static final String ACCEPT_FORMAT_PARAM = "AcceptFormats";

    public static final String ACCEPT_VERSIONS_PARAM = "AcceptVersions";

    public static final String OUTPUT_PARAM = "OutputFormat";

    /** GetTile request parameters **/
    public static final String LAYER_PARAM = "Layer";

    public static final String STYLE_PARAM = "Style";

    public static final String FORMAT_PARAM = "Format";

    public static final String TILE_MATRIX_SET_PARAM = "TileMatrixSet";

    public static final String TILE_MATRIX_PARAM = "TileMatrix";

    public static final String TILE_ROW_PARAM = "TileRow";

    public static final String TILE_COL_PARAM = "TileCol";

    /** GetFeatureInfo request parameters **/
    public static final String INFO_FORMAT_PARAM = "InfoFormat";

    public static final String I_PARAM = "I";

    public static final String J_PARAM = "J";

    /** FORMATS **/
    public static final String TEXT_XML = "text/xml";

    public static final String SOAP_XML = "application/soap+xml";

    public static final String TEXT_HTML = "text/html";

    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_GIF = "image/gif";

    public static final String IMAGE_JPEG = "image/jpeg";

}