package ets.wmts10.core.util.request;

import static ets.wmts10.core.domain.WMTS_Constants.GET_TILE;
import static ets.wmts10.core.domain.WMTS_Constants.IMAGE_PNG;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsRequestBuilderTest 
{
	/*--
    @Test
    public void testGetSupportedTransparentFormat()
                    throws Exception
    {
        String format = WmtsKvpRequestBuilder.getSupportedTransparentFormat( wmtsCapabilities(), GET_TILE );

        assertThat( format, is( IMAGE_PNG ) );
    }
--*/
    private Document wmtsCapabilities()
                    throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream wmtsCapabilities = WmtsRequestBuilderTest.class.getResourceAsStream( "../../capabilities_wmts10.xml" );
        return builder.parse( new InputSource( wmtsCapabilities ) );
    }

}