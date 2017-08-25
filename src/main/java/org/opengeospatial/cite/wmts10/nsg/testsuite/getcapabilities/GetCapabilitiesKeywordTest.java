package org.opengeospatial.cite.wmts10.nsg.testsuite.getcapabilities;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.xpath.XPathException;

import org.opengeospatial.cite.wmts10.ets.core.domain.ProtocolBinding;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.latlon.ets.core.util.TestSuiteLogger;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesKeywordTest extends AbstractBaseGetCapabilitiesFixture {
    /**
     * --- NSG Requirement 17: An NSG WMTS server shall provide a keyword list based on the NSG Application Schema
     * (NAS). ---
     */
    private URI getCapabilitiesURI;

    private static final String KEYWORD_FILE = "nas.keywords";

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 17", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsGetCapabilitiesURLExists() {
        getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities,
                                                                            WMTS_Constants.GET_CAPABILITIES,
                                                                            ProtocolBinding.GET );
        assertTrue( getCapabilitiesURI != null,
                    "GetCapabilities (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document." );
    }

    @Test(description = "NSG Web Map Tile Service (WMTS) 1.0.0, Requirement 17", dependsOnMethods = "wmtsGetCapabilitiesURLExists")
    public void wmtsCapabilitiesKeywordTest() {
        verifyNASkeywords( true, "WMTS ServiceMetadata Capabilities docuemnt", wmtsCapabilities,
                           "//ows:ServiceIdentification/ows:Keywords" );
    }

    public static void verifyNASkeywords( boolean mandatory, String keywordLocationDescription, Node xmlNode,
                                          String keywordLocation ) {
        try {
            Node keywordsElement = (Node) ServiceMetadataUtils.getNode( xmlNode, keywordLocation );
            if ( mandatory ) {
                assertFalse( ( keywordsElement == null ), keywordLocationDescription
                                                          + " contains no mandatory <Keywords> Element." );
                assertFalse( ( ( keywordsElement.getFirstChild() == null ) || ( keywordsElement.getChildNodes().getLength() <= 0 ) ),
                             keywordLocationDescription + " contains no <Keyword> Elements under the <Keywords>." );
            } else {
                if ( keywordsElement == null ) {
                    throw new SkipException( "There is no <Keywords> Element to compare under "
                                             + keywordLocationDescription );
                } else if ( ( keywordsElement.getFirstChild() == null )
                            || ( keywordsElement.getChildNodes().getLength() <= 0 ) ) {
                    throw new SkipException( "There are no <Keyword> Elements under the <Keywords> to compare under "
                                             + keywordLocationDescription );
                }
            }

            NodeList keywords = (NodeList) keywordsElement.getChildNodes();

            List<String> keywordsToCheck = new ArrayList<String>();
            for ( int keywordNodeIndex = 0; keywordNodeIndex < keywords.getLength(); keywordNodeIndex++ ) {
                Node keywordNode = keywords.item( keywordNodeIndex );
                String keyword = keywordNode.getTextContent();
                if ( keyword != null )
                    keywordsToCheck.add( keyword.toLowerCase().trim() );
            }
            assertFalse( ( keywordsToCheck == null ) || ( keywordsToCheck.isEmpty() ) || ( keywordsToCheck.size() < 1 ),
                         "Error creating or corrupt Keyword list" );

            boolean anyFound = false;

            try (BufferedReader br = new BufferedReader(
                                                         new InputStreamReader(
                                                                                GetCapabilitiesKeywordTest.class.getResourceAsStream( KEYWORD_FILE ),
                                                                                "UTF-8" ) )) {
                String nasKeyword;
                while ( ( nasKeyword = br.readLine() ) != null ) {
                    nasKeyword = nasKeyword.toLowerCase().trim();
                    if ( !nasKeyword.isEmpty() ) {
                        if ( keywordsToCheck.contains( nasKeyword ) ) {
                            anyFound = true;
                            break;
                        }
                    }
                }
                br.close();
            } catch ( IOException e ) {
                TestSuiteLogger.log( Level.WARNING, "Keywords file " + KEYWORD_FILE + " could not be parsed.", e );
                assertTrue( false, "Keywords file " + KEYWORD_FILE + " could not be parsed." );
            }

            assertTrue( anyFound, "No valid NAS keywords found in: " + keywordLocationDescription );
        } catch ( XPathException xpe ) {
            xpe.printStackTrace();
        }
    }

}