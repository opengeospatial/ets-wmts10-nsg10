package org.opengeospatial.cite.wmts10.ets.core.domain;

/**
 * An enumerated type that indicates how a request message is bound to an application
 * protocol. In effect, a binding prescribes how the message content is mapped into a
 * concrete exchange format.
 *
 * <ul>
 * <li>HTTP GET (element name: 'Get')</li>
 * <li>HTTP POST (element name : 'Post')</li>
 * </ul>
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public enum ProtocolBinding {

	/** HTTP GET method */
	GET(WMTS_Constants.KVP_ENC),
	/** HTTP POST method */
	POST(WMTS_Constants.XML_ENC),
	/** SOAP */
	SOAP(WMTS_Constants.SOAP_ENC),
	/** Any supported binding */
	ANY("");

	private final String elementName;

	ProtocolBinding(String elementName) {
		this.elementName = elementName;
	}

	public String getElementName() {
		return elementName;
	}

}