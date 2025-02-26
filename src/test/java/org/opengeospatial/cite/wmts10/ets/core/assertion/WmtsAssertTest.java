package org.opengeospatial.cite.wmts10.ets.core.assertion;

import static org.opengeospatial.cite.wmts10.ets.core.assertion.WmtsAssertion.assertVersion100;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtilsTest;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsAssertTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testAssertVersion130() throws Exception {
		assertVersion100(wmtsCapabilities());
	}

	private Document wmtsCapabilities() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class
			.getResourceAsStream("../NSGWMTSImplementation.xml");
		return builder.parse(new InputSource(wmtsCapabilities));
	}

}