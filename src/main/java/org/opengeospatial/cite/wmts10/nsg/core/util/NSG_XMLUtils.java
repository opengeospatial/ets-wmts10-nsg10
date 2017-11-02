package org.opengeospatial.cite.wmts10.nsg.core.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Jim Beatty
 */
public class NSG_XMLUtils {

    /**
     * Parse the text value of the tag with the specified name.
     *
     * @param element
     *            , the element to parse from, never <code>null</code>
     * @param tagName
     *            , the name of the tag, never <code>null</code>
     * @return the text value of the tag, <code>null</code> if not exists
     */
    public static String getXMLElementTextValue( Element element, String tagName ) {
        NodeList nl = element.getElementsByTagName( tagName );
        if ( nl != null && nl.getLength() > 0 ) {
            Element el = (Element) nl.item( 0 );
            return el.getFirstChild().getNodeValue();
        }
        return null;
    }

    /**
     * Opens the XML document with the element with the specified rootName.
     * 
     * @param xmlDocument
     *            to open, never <code>null</code>
     * @param rootName
     *            the name of the element to return, never <code>null</code>
     * @return the element as {@link NodeList}, mey be <code>null</code> if the document could not be opened.
     */
    public static NodeList openXMLDocument( InputStream xmlDocument, String rootName ) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder ioe = dbf.newDocumentBuilder();
            Document dom = ioe.parse( xmlDocument );
            if ( dom != null ) {
                Element docElems = dom.getDocumentElement();
                if ( docElems != null ) {
                    return docElems.getElementsByTagName( rootName );
                }
            }
        } catch ( ParserConfigurationException pcEx ) {
            pcEx.printStackTrace();
        } catch ( SAXException saxEx ) {
            saxEx.printStackTrace();
        } catch ( IOException ioEx ) {
            ioEx.printStackTrace();
        }

        return null;
    }

}