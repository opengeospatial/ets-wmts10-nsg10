package org.opengeospatial.cite.wmts10.ets.core.util;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.GET_CAPABILITIES;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.GET_FEATURE_INFO;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.GET_TILE;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.INFO_FORMAT_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.I_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.J_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.SERVICE_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.SERVICE_TYPE_CODE;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.VERSION;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants.VERSION_PARAM;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces.OWS;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces.SOAP;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces.WMTS;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces.serviceOWS;
import static org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces.serviceSOAP;
import static org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils.getNodeElements;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class WmtsSoapContainer {

    private static final Logger LOGR = Logger.getLogger( WmtsSoapContainer.class.getName() );

    private final String soapURL;

    private final String callFunction;

    private SOAPMessage soapMessage;

    private SOAPEnvelope soapMessageEnvelope;

    private SOAPBody soapMessageBody;

    private SOAPPart soapMessagePart;

    private SOAPElement soapMessageElement;

    private SOAPElement soapMessageElementParent;

    private SOAPMessage soapResponse;

    private Document responseDocument;

    public WmtsSoapContainer( String function, String soap_URL ) {
        this.callFunction = function;
        this.soapURL = soap_URL;
        try {
            MessageFactory messageFactory = MessageFactory.newInstance( SOAPConstants.SOAP_1_2_PROTOCOL );
            soapMessage = messageFactory.createMessage();

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader( "SOAPAction", soapURL + "/" + callFunction );

            soapMessagePart = soapMessage.getSOAPPart();
            soapMessageEnvelope = soapMessagePart.getEnvelope();
            soapMessageBody = soapMessageEnvelope.getBody();

            QName qnElem = new QName( WMTS, callFunction );
            soapMessageElement = soapMessageBody.addChildElement( qnElem );

            this.addWmtsAttribute( SERVICE_PARAM, SERVICE_TYPE_CODE );
            if ( !callFunction.equals( GET_CAPABILITIES ) ) {
                this.addWmtsAttribute( VERSION_PARAM, VERSION );
            }
            this.addNamespace( serviceOWS, OWS );

            if ( callFunction.equals( GET_FEATURE_INFO ) ) {
                soapMessageElementParent = soapMessageElement;

                QName qnTileElem = new QName( WMTS, GET_TILE );
                soapMessageElement = soapMessageElementParent.addChildElement( qnTileElem );

                this.addWmtsAttribute( SERVICE_PARAM, SERVICE_TYPE_CODE );
                this.addWmtsAttribute( VERSION_PARAM, VERSION );

                this.addNamespace( serviceOWS, OWS );
            }
        } catch ( SOAPException se ) {
            LOGR.log( SEVERE, "Error adding SOAP Namespace identifier", se );
            assertTrue( false, "Error adding SOAP Namespace identifier:  " + se.getMessage() );
        }
    }

    public void addWmtsAttribute( String attribute, String value ) {
        if ( soapMessageElement != null ) {
            try {
                QName qAttr = new QName( WMTS, attribute.toLowerCase() );
                soapMessageElement.addAttribute( qAttr, value );
            } catch ( SOAPException se ) {
                LOGR.log( SEVERE, "Error adding SOAP Namespace identifier", se );
                assertTrue( false, "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
            }
        }
    }

    public void addNamespace( String namespace, String namespaceURL ) {
        if ( soapMessageElement != null ) {
            try {
                soapMessageElement.addNamespaceDeclaration( namespace, namespaceURL );
                // soapMessageElementOperation.addNamespaceDeclaration(WmtsNamespaces.serviceOWS, WmtsNamespaces.OWS);
            } catch ( SOAPException se ) {
                LOGR.log( SEVERE, "Error adding SOAP Namespace identifier", se );
                assertTrue( false, "Error adding SOAP Namesapce identifier:  " + se.getMessage() );
            }
        }
    }

    public void addParameter( String namespace, String parameterName, String value ) {
        if ( soapMessageElement != null ) {
            try {
                SOAPElement element = soapMessageElement;
                if ( callFunction.equals( GET_FEATURE_INFO ) && ( soapMessageElementParent != null ) ) {
                    if ( parameterName.equals( I_PARAM ) || parameterName.equals( J_PARAM )
                         || parameterName.equals( INFO_FORMAT_PARAM ) ) {
                        element = soapMessageElementParent;
                    }
                }
                SOAPElement childElement = element.addChildElement( parameterName, namespace );
                childElement.addTextNode( value );
            } catch ( SOAPException se ) {
                LOGR.log( SEVERE, "Error adding SOAP Parameter", se );
                assertTrue( false, "Error adding SOAP Parameter:  " + se.getMessage() );
            }
        }
    }

    public void addParameterWithChild( String namespace, String parameterName, String childParameterName, String value ) {
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
                LOGR.log( SEVERE, "Error adding SOAP Parameter", se );
                assertTrue( false, "Error adding SOAP Parameter:  " + se.getMessage() );
            }
        }
    }

    public SOAPMessage getSoapResponse( boolean trapSoapErrors ) {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            this.finalizeSoapMessage();

            logSoapMessage();

            soapResponse = soapConnection.call( soapMessage, soapURL );
            soapConnection.close();

            responseDocument = makeResponseDocument( soapResponse );

            NodeList anyResponseExceptions = getNodeElements( responseDocument, "//ows:ExceptionText" );
            if ( ( anyResponseExceptions != null ) && ( anyResponseExceptions.getLength() > 0 ) ) {
                String exceptionText = "";
                for ( int i = 0; i < anyResponseExceptions.getLength(); i++ ) {
                    exceptionText += anyResponseExceptions.item( i ).getTextContent().trim() + ";  ";
                }
                throw new SOAPException( exceptionText );
            }

            logSoapResponse();
        } catch ( SOAPException se ) {
            if ( trapSoapErrors ) {
                LOGR.log( SEVERE, "Error with SOAP Response:  ", se );
                assertTrue( false, "Error with SOAP Response" + se.getMessage() );
            }
        } catch ( XPathExpressionException xe ) {
            LOGR.log( SEVERE, "Error processing SOAP exception message", xe );
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
            LOGR.log( SEVERE, "Converting SOAP message to document", se );
            assertTrue( false, "Converting SOAP message to document Error = " + se.getMessage() );
        } catch ( TransformerException te ) {
            LOGR.log( SEVERE, "Transforming SOAP message to document", te );
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
            soapMessageEnvelope.addNamespaceDeclaration( serviceSOAP, SOAP );
            soapMessageEnvelope.setPrefix( serviceSOAP );
            header.setPrefix( serviceSOAP );
            soapMessageBody.setPrefix( serviceSOAP );
            if ( fault != null ) {
                fault.setPrefix( serviceSOAP );
            }
            soapMessage.saveChanges();
        } catch ( SOAPException se ) {
            LOGR.log( SEVERE, "Completing SOAP message construct", se );
            assertTrue( false, "Completing SOAP message construct Error = " + se.getMessage() );
        }
    }

    private void logSoapResponse() {
        try {
            if ( SEVERE.equals( LOGR.getLevel() ) ) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                Source sourceContent = soapResponse.getSOAPPart().getContent();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                StreamResult result = new StreamResult( bos );
                transformer.transform( sourceContent, result );
                bos.close();
                LOGR.log( SEVERE, "Response SOAP message ", bos.toString() );
            }
        } catch ( TransformerException te ) {
            LOGR.log( WARNING, "Transforming Error when printing SOAP response for logging", te );
        } catch ( SOAPException se ) {
            LOGR.log( WARNING, "Error when printing SOAP response for logging", se );
        } catch ( IOException ioe ) {
            LOGR.log( WARNING, "Error when printing SOAP response for logging", ioe );
        }
    }

    private void logSoapMessage() {
        try {
            if ( SEVERE.equals( LOGR.getLevel() ) ) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                soapMessage.writeTo( bos );
                bos.close();
                LOGR.log( SEVERE, "Request SOAP message ", bos.toString() );
            }
        } catch ( SOAPException se ) {
            LOGR.log( WARNING, "Error when printing SOAP message for logging", se );
        } catch ( IOException ioe ) {
            LOGR.log( WARNING, "Error when printing SOAP message for logging", ioe );
        }
    }

}
