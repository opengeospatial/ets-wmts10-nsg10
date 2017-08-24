package org.opengeospatial.cite.wmts10.ets.core.domain;

import javax.xml.XMLConstants;

import de.latlon.ets.core.util.NamespaceBindings;

/**
 * XML namespace names.
 * 
 * @see <a href="http://www.w3.org/TR/xml-names/">Namespaces in XML 1.0</a>
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS and/or WMTS)
 */
public final class WmtsNamespaces {

    private WmtsNamespaces() {
    }

    /** OGC 07-057 (WMTS 1.0) */
    public static final String WMTS = "http://www.opengis.net/wmts/1.0";

    public static final String serviceWMTS = WMTS_Constants.SERVICE_TYPE_CODE.toLowerCase();

    /** W3C XLink */
    public static final String XLINK = "http://www.w3.org/1999/xlink";

    public static final String serviceXLINK = "xlink";

    /** GML */
    public static final String GML = "http://www.opengis.net/gml";

    public static final String serviceGML = "gml";

    /** OWS */
    public static final String OWS = "http://www.opengis.net/ows/1.1";

    public static final String serviceOWS = "ows";

    /** ExtendedCapabilities Namespace used for SOAP binding */
    public static final String SOAPWMTS = "http://schemas.deegree.org/services/wmts/3.4.0/";

    public static final String serviceSOAPWMTS = "soapwmts";

    /** SOAP */
    public static final String SOAP = "http://www.w3.org/2003/05/soap-envelope";

    public static final String serviceSOAP = "soap";

    /**
     * Creates a NamespaceBindings object that declares the following namespace bindings:
     * 
     * <ul>
     * <li>wmts: {@value org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces#WMTS}</li>
     * </ul>
     * 
     * @return A NamespaceBindings object.
     */
    public static NamespaceBindings withStandardBindings() {
        NamespaceBindings nsBindings = new NamespaceBindings();
        nsBindings.addNamespaceBinding( WmtsNamespaces.WMTS, serviceWMTS );
        nsBindings.addNamespaceBinding( WmtsNamespaces.OWS, serviceOWS );
        nsBindings.addNamespaceBinding( WmtsNamespaces.XLINK, serviceXLINK );
        nsBindings.addNamespaceBinding( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "xsi" );
        nsBindings.addNamespaceBinding( WmtsNamespaces.GML, serviceGML );
        nsBindings.addNamespaceBinding( WmtsNamespaces.SOAPWMTS, serviceSOAPWMTS );
        return nsBindings;
    }

}