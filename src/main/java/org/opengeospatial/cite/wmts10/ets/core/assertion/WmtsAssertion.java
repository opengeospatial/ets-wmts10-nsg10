package org.opengeospatial.cite.wmts10.ets.core.assertion;

import static de.latlon.ets.core.assertion.ETSAssert.assertQualifiedName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.glassfish.jersey.client.ClientConfig;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengeospatial.cite.wmts10.ets.core.domain.WmtsNamespaces;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;
import de.latlon.ets.core.util.NamespaceBindings;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

/**
 * Provides WMTS 1.0.0 specific test assertion methods
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS/WMTS)
 */
public final class WmtsAssertion {

	private WmtsAssertion() {
	}

	/**
	 * Asserts that the given DOM document has the expected root element
	 * 'WMTS_Capabilities' in namespace {http://www.opengis.net/wmts}.
	 * @param doc A Document node having {http://www.opengis.net/wmts}
	 * {@value WMTS_Constants#WMTS_CAPABILITIES} as the root element.
	 */
	public static void assertSimpleWMTSCapabilities(Document doc) {
		assertQualifiedName(doc.getDocumentElement(), new QName(WmtsNamespaces.WMTS, WMTS_Constants.WMTS_CAPABILITIES));
	}

	/**
	 * Asserts that the actual content type matches the expected content type.
	 * @param response A Document node having {http://www.opengis.net/wmts}
	 * {@value WMTS_Constants#WMTS_CAPABILITIES} as the root element.
	 */
	public static void assertVersion100(Document response) {
		assertXPath("//wmts:Capabilities/@version = '1.0.0'", response, WmtsNamespaces.withStandardBindings());
	}

	/**
	 * Asserts that the actual HTTP status code matches one of the expected status codes.
	 * @param sa an optional assertion class to delay terminating test and handle in the
	 * calling routine
	 * @param actualCode The actual status code.
	 * @param expectedCodes An int[] array containing the expected status codes.
	 */
	public static void assertStatusCode(SoftAssert sa, int actualCode, int... expectedCodes) {
		Arrays.sort(expectedCodes); // precondition for binary search
		boolean containsExpectedCodes = (Arrays.binarySearch(expectedCodes, actualCode) >= 0);
		String msg = String.format("Expected status code(s) %s but received %d.", Arrays.toString(expectedCodes),
				actualCode);
		if (sa == null) {
			Assert.assertTrue(containsExpectedCodes, msg);
		}
		else {
			sa.assertTrue(containsExpectedCodes, msg);
		}
	}

	/**
	 * Asserts that the actual HTTP status code matches one of the expected status codes.
	 * @param actualCode The actual status code.
	 * @param expectedCodes An int[] array containing the expected status codes.
	 */
	public static void assertStatusCode(int actualCode, int... expectedCodes) {
		assertStatusCode(null, actualCode, expectedCodes);
	}

	/**
	 * Asserts that the actual content type matches the expected content type.
	 * @param sa an optional assertion class to delay terminating test and handle in the
	 * calling routine
	 * @param headers The header of the response.
	 * @param expectedContentType The expected content type, never <code>null</code>.
	 */
	public static void assertContentType(SoftAssert sa, MultivaluedMap<String, Object> headers,
			String expectedContentType) {
		List<Object> contentTypes = headers.get("Content-Type");
		boolean containsContentType = containsContentType(contentTypes, expectedContentType);
		String msg = String.format("Expected content type %s, but received %s", expectedContentType,
				asString(contentTypes));
		if (sa == null) {
			Assert.assertTrue(containsContentType, msg);
		}
		else {
			sa.assertTrue(containsContentType, msg);
		}
	}

	/**
	 * Asserts that the actual content type matches the expected content type.
	 * @param headers The header of the response.
	 * @param expectedContentType The expected content type, never <code>null</code>.
	 */
	public static void assertContentType(MultivaluedMap<String, Object> headers, String expectedContentType) {
		assertContentType(null, headers, expectedContentType);
	}

	// ---

	/**
	 * Asserts that the string is a valid url.
	 * @param sa an optional assertion class to delay terminating test and handle in the
	 * calling routine
	 * @param url The url to check.
	 */
	public static void assertUrl(SoftAssert sa, String url) {
		boolean urlWellFormed = false;
		String msg = null;

		try {
			new URL(url);
			urlWellFormed = true;
		}
		catch (MalformedURLException e) {
			msg = String.format("Invalid URL: %s", url);
		}

		if (sa == null) {
			Assert.assertTrue(urlWellFormed, msg);
		}
		else {
			sa.assertTrue(urlWellFormed, msg);
		}
	}

	// ---
	/**
	 * Asserts that the string is a valid url.
	 * @param url The url to check.
	 */
	public static void assertUrl(String url) {
		assertUrl(null, url);
	}

	// ---

	/**
	 * Asserts that the url is resolvable (status code is 200).
	 * @param sa an optional assertion class to delay terminating test and handle in the
	 * calling routine
	 * @param url The url to check.
	 */
	public static void assertUriIsResolvable(SoftAssert sa, String url) {
		try {
			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);
			WebTarget target = client.target(url);
			Builder reqBuilder = target.request();
			Response response = reqBuilder.buildGet().invoke();
			assertStatusCode(sa, response.getStatus(), 200);
		}
		catch (NullPointerException e) {
			String errorMsg = String.format("Invalid URI %s: %s", url, e.getMessage());
			throw new AssertionError(errorMsg);
		}
	}

	/**
	 * Asserts that an XPath 1.0 expression holds true for the given evaluation context.
	 *
	 * The method arguments will be logged at level FINE or lower.
	 * @param sa an optional assertion class to delay terminating test and handle in the
	 * calling routine
	 * @param expr A valid XPath 1.0 expression.
	 * @param context The context node.
	 * @param nsBindings A collection of namespace bindings for the XPath expression,
	 * where each entry maps a namespace URI (key) to a prefix (value). Never
	 * {@code null}.
	 */
	public static void assertXPath(SoftAssert sa, String expr, Node context, NamespaceBindings nsBindings) {
		Boolean result = de.latlon.ets.core.assertion.ETSAssert.checkXPath(expr, context, nsBindings);
		if (sa == null) {
			Assert.assertTrue(result, ErrorMessage.format(ErrorMessageKey.XPATH_RESULT, context.getNodeName(), expr));
		}
		else {
			sa.assertTrue(result, ErrorMessage.format(ErrorMessageKey.XPATH_RESULT, context.getNodeName(), expr));
		}
	}

	/**
	 * Asserts that an XPath 1.0 expression holds true for the given evaluation context.
	 *
	 * The method arguments will be logged at level FINE or lower.
	 * @param expr A valid XPath 1.0 expression.
	 * @param context The context node.
	 * @param nsBindings A collection of namespace bindings for the XPath expression,
	 * where each entry maps a namespace URI (key) to a prefix (value). Never
	 * {@code null}.
	 */
	public static void assertXPath(String expr, Node context, NamespaceBindings nsBindings) {
		assertXPath(null, expr, context, nsBindings);
	}

	private static boolean containsContentType(List<Object> contentTypes, String expectedContentType) {
		if (contentTypes != null)
			for (Object contentType : contentTypes) {
				if (!(contentType instanceof String)) {
					return false;
				}
				if (((String) contentType).contains(expectedContentType))
					return true;
			}
		return false;
	}

	private static String asString(List<Object> values) {
		StringBuilder sb = new StringBuilder();
		for (Object value : values) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(value);
		}
		return sb.toString();
	}

}