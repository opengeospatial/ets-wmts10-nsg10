package org.opengeospatial.cite.wmts10.ets.core.util;

import static org.testng.Assert.assertTrue;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class WMTS_SOAPcontainer {
    private SOAPMessage soapMessage = null;

    private SOAPEnvelope soapMessageEnvelope = null;

    private SOAPBody soapMessageBody = null;

    private SOAPPart soapMessagePart = null;

    private SOAPElement soapMessageElement = null;

    private SOAPElement soapMessageElementParent = null;

    private SOAPMessage soapResponse = null;

    private Document responseDocument = null;

    private String soapURL;

    private String callFunction;

    private boolean _debug = false;

    protected WMTS_SOAPcontainer() {
        // create();
    }

    public WMTS_SOAPcontainer( String function, String soap_URL ) {
        create( function, soap_URL );
    }

    public void create( String function, String soap_URL ) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance( SOAPConstants.SOAP_1_2_PROTOCOL );
            soapMessage = messageFactory.createMessage();

            MimeHeaders headers = soapMessage.getMimeHeaders();
            this.soapURL = soap_URL;
            this.callFunction = function;
            headers.addHeader( "SOAPAction", soapURL + "/" + callFunction );

            soapMessagePart = soapMessage.getSOAPPart();

            // --- SOAP Envelope
            soapMessageEnvelope = soapMessagePart.getEnvelope();

            // --- SOAP Body
            soapMessageBody = soapMessageEnvelope.getBody();

            QName qnElem = new QName( WmtsNamespaces.WMTS, callFunction );
            soapMessageElement = soapMessageBody.addChildElement( qnElem );

            this.AddWMTSAttribute( WMTS_Constants.SERVICE_PARAM, WMTS_Constants.SERVICE_TYPE_CODE );
            if ( !callFunction.equals( WMTS_Constants.GET_CAPABILITIES ) ) {
                this.AddWMTSAttribute( WMTS_Constants.VERSION_PARAM, WMTS_Constants.VERSION );
            }
            this.AddNamespace( WmtsNamespaces.serviceOWS, WmtsNamespaces.OWS );

            if ( callFunction.equals( WMTS_Constants.GET_FEATURE_INFO ) ) {
                soapMessageElementParent = soapMessageElement;

                QName qnTileElem = new QName( WmtsNamespaces.WMTS, WMTS_Constants.GET_TILE );
                soapMessageElement = soapMessageElementParent.addChildElement( qnTileElem );

                this.AddWMTSAttribute( WMTS_Constants.SERVICE_PARAM, WMTS_Constants.SERVICE_TYPE_CODE );
                this.AddWMTSAttribute( WMTS_Constants.VERSION_PARAM, WMTS_Constants.VERSION );

                this.AddNamespace( WmtsNamespaces.serviceOWS, WmtsNamespaces.OWS );
            }
        } catch ( SOAPException se ) {
            System.out.println( "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
            if ( this._debug ) {
                se.printStackTrace();
            }
            assertTrue( false, "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
        }
    }

    public void AddWMTSAttribute( String attribute, String value ) {
        if ( soapMessageElement != null ) {
            try {
                QName qAttr = new QName( WmtsNamespaces.WMTS, attribute.toLowerCase() );
                soapMessageElement.addAttribute( qAttr, value );
            } catch ( SOAPException se ) {
                System.out.println( "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
                if ( this._debug ) {
                    se.printStackTrace();
                }
                assertTrue( false, "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
            }
        }
    }

    public void AddNamespace( String namespace, String namespaceURL ) {
        if ( soapMessageElement != null ) {
            try {
                soapMessageElement.addNamespaceDeclaration( namespace, namespaceURL );
                // soapMessageElementOperation.addNamespaceDeclaration(WmtsNamespaces.serviceOWS, WmtsNamespaces.OWS);
            } catch ( SOAPException se ) {
                System.out.println( "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
                if ( this._debug ) {
                    se.printStackTrace();
                }
                assertTrue( false, "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
            }
        }
    }

    public void AddParameter( String namespace, String parameterName, String value ) {
        if ( soapMessageElement != null ) {
            try {
                SOAPElement element = soapMessageElement;
                if ( callFunction.equals( WMTS_Constants.GET_FEATURE_INFO ) && ( soapMessageElementParent != null ) ) {
                    if ( parameterName.equals( WMTS_Constants.I_PARAM )
                         || parameterName.equals( WMTS_Constants.J_PARAM )
                         || parameterName.equals( WMTS_Constants.INFO_FORMAT_PARAM ) // ||
                    ) {
                        element = soapMessageElementParent;
                    }
                }
                SOAPElement childElement = element.addChildElement( parameterName, namespace );
                childElement.addTextNode( value );
            } catch ( SOAPException se ) {
                System.out.println( "Error adding SOAP Parameter:  " + se.getMessage() );
                if ( this._debug ) {
                    se.printStackTrace();
                }
                assertTrue( false, "Error adding SOAP Parameter:  " + se.getMessage() );
            }
        }
    }

    public void AddParameterWithChild( String namespace, String parameterName, String childParameterName, String value ) {
        if ( soapMessageElement != null ) {
            try {
                SOAPElement childElement = soapMessageElement.addChildElement( parameterName, namespace );
                SOAPElement childChildElement = childElement.addChildElement( childParameterName, namespace );
                childChildElement.addTextNode( value );
                /*--
                SOAPElement elemOperationAcceptVersions = elemOperation.addChildElement(WMTS_Constants.ACCEPT_VERSIONS_PARAM, WmtsNamespaces.serviceOWS);
                SOAPElement elemOperationAcceptVersionsVersion = elemOperationAcceptVersions.addChildElement(WMTS_Constants.VERSION_PARAM, WmtsNamespaces.serviceOWS);
                elemOperationAcceptVersionsVersion.addTextNode(WMTS_Constants.VERSION);
                --*/
            } catch ( SOAPException se ) {
                System.out.println( "Error adding SOAP Parameter:  " + se.getMessage() );
                if ( this._debug ) {
                    se.printStackTrace();
                }
                assertTrue( false, "Error adding SOAP Parameter:  " + se.getMessage() );
            }
        }
    }

    public SOAPMessage getSOAPresponse( boolean trapSoapErrors ) {
        try {
            // --- Send SOAP Message to SOAP Server

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            this.finalizeSoapMessage();

            // --- Print the request message
            if ( this._debug ) {
                System.out.print( "Request SOAP Message = " );
                soapMessage.writeTo( System.out );
                System.out.println();
            }
            soapResponse = soapConnection.call( soapMessage, soapURL );
            soapConnection.close();

            responseDocument = makeResponseDocument( soapResponse );

            NodeList anyResponseExceptions = (NodeList) ServiceMetadataUtils.getNodeElements( responseDocument,
                                                                                              "//ows:ExceptionText" );
            if ( ( anyResponseExceptions != null ) && ( anyResponseExceptions.getLength() > 0 ) ) {
                String exceptionText = "";
                for ( int i = 0; i < anyResponseExceptions.getLength(); i++ ) {
                    exceptionText += anyResponseExceptions.item( i ).getTextContent().trim() + ";  ";
                }
                throw new SOAPException( exceptionText );
            }

            // --- Process the SOAP Response
            if ( this._debug ) {
                this.printSOAPResponse();
            }
        } catch ( SOAPException se ) {
            if ( trapSoapErrors ) {
                System.out.println( "Error with SOAP Response:  " + se.getMessage() );
                if ( this._debug ) {
                    se.printStackTrace();
                }
                assertTrue( false, "Error with SOAP Response:  " + se.getMessage() );
            }
        } catch ( IOException ioe ) {
            System.out.println( "Error writing SOAP message:  " + ioe.getMessage() );
            if ( this._debug ) {
                ioe.printStackTrace();
            }
            assertTrue( false, "Error writing SOAP message:  " + ioe.getMessage() );
        } catch ( XPathExpressionException xe ) {
            System.out.println( "Error processing SOAP exception message:  " + xe.getMessage() );
            if ( this._debug ) {
                xe.printStackTrace();
            }
            assertTrue( false, "Error processing SOAP exception message:  " + xe.getMessage() );
        }
        return soapResponse;
    }

    public Document getResponseDocument() {
        return this.responseDocument;
    }

    public static Document makeResponseDocument( SOAPMessage soapResponse ) {
        Document soapDocument = null;
        if ( soapResponse == null ) {
            return null;
        }

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            Source sourceContent = soapResponse.getSOAPPart().getContent();

            DOMResult result = new DOMResult();
            transformer.transform( sourceContent, result );

            soapDocument = (Document) result.getNode();
        } catch ( SOAPException se ) {
            System.out.println( "Converting SOAP message to document Error = " + se.getMessage() );
            // if ( this._debug )
            {
                se.printStackTrace();
            }
            assertTrue( false, "Converting SOAP message to document Error = " + se.getMessage() );
        } catch ( TransformerException te ) {
            System.out.println( "Transforming SOAP message to document error = " + te.getMessage() );
            // if ( this._debug )
            {
                te.printStackTrace();
            }
            assertTrue( false, "Transforming SOAP message to document error = " + te.getMessage() );
        }
        return soapDocument;
    }

    private void finalizeSoapMessage() {
        // -- clean up default settings (not sure if meaningful or not)
        try {
            SOAPHeader header = soapMessage.getSOAPHeader();
            SOAPFault fault = soapMessageBody.getFault();
            soapMessageEnvelope.removeNamespaceDeclaration( soapMessageEnvelope.getPrefix() );
            soapMessageEnvelope.addNamespaceDeclaration( WmtsNamespaces.serviceSOAP, WmtsNamespaces.SOAP );
            soapMessageEnvelope.setPrefix( WmtsNamespaces.serviceSOAP );
            header.setPrefix( WmtsNamespaces.serviceSOAP );
            soapMessageBody.setPrefix( WmtsNamespaces.serviceSOAP );
            if ( fault != null ) {
                fault.setPrefix( WmtsNamespaces.serviceSOAP );
            }
            soapMessage.saveChanges();
        } catch ( SOAPException se ) {
            System.out.println( "Completing SOAP message construct Error = " + se.getMessage() );
            if ( this._debug ) {
                se.printStackTrace();
            }
            assertTrue( false, "Completing SOAP message construct Error = " + se.getMessage() );
        }
    }

    /**
     * Method used to print the SOAP Response
     */
    private void printSOAPResponse() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            Source sourceContent = soapResponse.getSOAPPart().getContent();
            System.out.print( "\nResponse SOAP Message = " );
            StreamResult result = new StreamResult( System.out );
            transformer.transform( sourceContent, result );
        } catch ( TransformerException te ) {
            System.out.println( "Transforming Error when printing SOAPresponse: " + te.getMessage() );
            if ( this._debug ) {
                te.printStackTrace();
            }
            assertTrue( false, "Transforming Error when printing SOAPresponse: " + te.getMessage() );
        } catch ( SOAPException se ) {
            System.out.println( "Error when printing SOAPresponse: " + se.getMessage() );
            if ( this._debug ) {
                se.printStackTrace();
            }
            assertTrue( false, "Error when printing SOAPresponse: " + se.getMessage() );
        }
    }

}
