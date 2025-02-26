package org.opengeospatial.cite.wmts10.ets.core;

import static org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils.parseLayerInfo;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;

import org.opengeospatial.cite.wmts10.ets.core.domain.SuiteAttribute;
import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.Reporter;
import org.w3c.dom.Document;

import de.latlon.ets.core.util.TestSuiteLogger;
import de.latlon.ets.core.util.URIUtils;

//import org.opengeospatial.cite.wmts10.ets.core.domain.InteractiveTestResult;

/**
 * A listener that performs various tasks before and after a test suite is run, usually
 * concerned with maintaining a shared test suite fixture. Since this listener is loaded
 * using the ServiceLoader mechanism, its methods will be called before those of other
 * suite listeners listed in the test suite definition and before any annotated
 * configuration methods.
 *
 * Attributes set on an ISuite instance are not inherited by constituent test group
 * contexts (ITestContext). However, suite attributes are still accessible from lower
 * contexts.
 *
 * @see org.testng.ISuite ISuite interface
 */
public class SuiteFixtureListener implements ISuiteListener {

	@Override
	public void onStart(ISuite suite) {
		processWmtsParameter(suite);
		Reporter.clear(); // clear output from previous test runs
		StringBuilder str = new StringBuilder("Initial test run parameters:\n");
		str.append(suite.getXmlSuite().getAllParameters().toString());
		Reporter.log(str.toString());
		TestSuiteLogger.log(Level.CONFIG, str.toString());
	}

	@Override
	public void onFinish(ISuite suite) {
		Reporter.log("Success? " + !suite.getSuiteState().isFailed());
		String reportDir = suite.getOutputDirectory();
		String msg = String.format("Test run directory: %s",
				reportDir.substring(0, reportDir.lastIndexOf(File.separatorChar)));
		Reporter.log(msg);
	}

	/**
	 * Processes the "wmts" test suite parameter that specifies a URI reference for the
	 * service description (capabilities document). The URI is dereferenced and the entity
	 * is parsed; the resulting Document object is set as the value of the
	 * {@link SuiteAttribute#TEST_SUBJECT testSubject} suite attribute.
	 * @param suite An ISuite object representing a TestNG test suite.
	 */
	void processWmtsParameter(ISuite suite) {
		Map<String, String> params = suite.getXmlSuite().getParameters();
		String wmtsRef = params.get(TestRunArg.WMTS.toString());
		if ((null == wmtsRef) || wmtsRef.isEmpty()) {
			throw new IllegalArgumentException("Required parameter not found");
		}
		URI wmtsURI = URI.create(wmtsRef);
		Document doc = null;
		try {
			doc = URIUtils.resolveURIAsDocument(wmtsURI);
			if (!WMTS_Constants.WMTS_CAPABILITIES.equals(doc.getDocumentElement().getLocalName())) {
				throw new RuntimeException("Did not receive WMTS ServeiceMetadata capabilities document: "
						+ doc.getDocumentElement().getNodeName());
			}
		}
		catch (Exception ex) {
			// push exception up through TestNG ISuiteListener interface
			throw new RuntimeException("Failed to parse resource located at " + wmtsURI, ex);
		}
		if (null != doc) {
			suite.setAttribute(SuiteAttribute.TEST_SUBJECT.getName(), doc);
			suite.setAttribute(SuiteAttribute.LAYER_INFO.getName(), parseLayerInfo(doc));
		}
	}

}