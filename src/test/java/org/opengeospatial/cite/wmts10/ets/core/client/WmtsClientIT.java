package org.opengeospatial.cite.wmts10.ets.core.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsClientIT {

    public void testgetCapabilities()
                    throws Exception {
        WmtsClient wmtsClient = new WmtsClient( wmtsCapabilities() );

        Document capabilities = wmtsClient.getCapabilities();
        assertThat( capabilities.getLocalName(), is( WMTS_Constants.WMTS_CAPABILITIES ) );
        assertThat( capabilities.getNamespaceURI(), is( WmtsNamespaces.WMTS ) );
    }

    private Document wmtsCapabilities()
                    throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream wmtsCapabilities = WmtsClientIT.class.getResourceAsStream( "capabilities_wmts10.xml" );
        return builder.parse( new InputSource( wmtsCapabilities ) );
    }

}