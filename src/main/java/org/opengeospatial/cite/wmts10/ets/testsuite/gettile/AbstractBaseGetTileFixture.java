package org.opengeospatial.cite.wmts10.ets.testsuite.gettile;

import static org.testng.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.xml.xpath.XPathExpressionException;

import org.apache.tika.io.FilenameUtils;
import org.joda.time.DateTime;
import org.opengeospatial.cite.wmts10.ets.core.util.ServiceMetadataUtils;
import org.opengeospatial.cite.wmts10.ets.core.util.WmtsSoapContainer;
import org.opengeospatial.cite.wmts10.ets.core.util.request.WmtsKvpRequestBuilder;
import org.opengeospatial.cite.wmts10.ets.testsuite.AbstractBaseGetFixture;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.latlon.ets.core.util.TestSuiteLogger;
import jakarta.ws.rs.core.Response;
import jakarta.xml.soap.SOAPMessage;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS and/or WMTS)
 */
@SuppressWarnings("restriction")
public abstract class AbstractBaseGetTileFixture extends AbstractBaseGetFixture {

	private final String MIME_FILENAME = "mime.types";

	private final String SUBDIRECTORY = "GetTileTests";

	private final String DISCRIMINATOR = DateTime.now().toString("yyyyMMddHHmm");

	private Path imageDirectory;

	/**
	 * Builds a {WmtsKvpRequest} representing a GetTile request.
	 * @throws XPathExpressionException in case bad XPath
	 */
	@BeforeClass
	public void buildGetTileRequest() throws XPathExpressionException {
		this.reqEntity = WmtsKvpRequestBuilder.buildGetTileRequest(wmtsCapabilities, layerInfo);
	}

	@BeforeClass
	public void setResultDirectory(ITestContext testContext) {
		String outputDirectory = retrieveSessionDir(testContext);
		TestSuiteLogger.log(Level.INFO, "Directory to store GetTile responses: " + outputDirectory);
		try {
			Path resultDir = Paths.get(outputDirectory);
			imageDirectory = createDirectory(resultDir, SUBDIRECTORY + "_" + DISCRIMINATOR); // ---
																								// create
																								// a
																								// unique
																								// directory
																								// name
		}
		catch (IOException e) {
			TestSuiteLogger.log(Level.WARNING, "Could not create directory for GetTile response.", e);
		}
	}

	@Test
	public void verifyGetTileSupported() {
		Node getTileEntry = null;
		try {
			getTileEntry = (Node) ServiceMetadataUtils.getNode(wmtsCapabilities,
					"//ows:OperationsMetadata/ows:Operation[@name = 'GetTile']");
		}
		catch (XPathExpressionException e) {
		}
		assertNotNull(getTileEntry, "GetTile is not supported by this WMTS");
	}

	/**
	 * Stores the image in a the output directory of the testsuite:
	 * testSUiteOutputDirectory/testGroup/testName.extension
	 * @param rsp containing the image, rsp.getEntityInputStream() is used to retrieve the
	 * content as stream, never <code>null</code>
	 * @param testGroup name of the test group (will be the name of the directory to
	 * create), never <code>null</code>
	 * @param testName name of the test (will be the name of the file to create), never
	 * <code>null</code>
	 * @param requestFormat the mime type of the image, never <code>null</code>
	 */
	protected void storeResponseImage(Response rsp, String testGroup, String testName, String requestFormat) {
		if (imageDirectory == null) {
			TestSuiteLogger.log(Level.WARNING,
					"Directory to store GetTile responses is not set. GetTile response is not written!");
			return;
		}
		writeIntoFile(rsp, testGroup, testName, requestFormat);
	}

	protected void storeSoapResponseImage(SOAPMessage soapResponse, String testGroup, String testName,
			String requestFormat) {
		if (imageDirectory == null) {
			TestSuiteLogger.log(Level.WARNING,
					"Directory to store GetTile responses is not set. GetTile response is not written!");
			return;
		}
		writeIntoFile(soapResponse, testGroup, testName, requestFormat);
	}

	private void writeIntoFile(Response rsp, String testGroup, String testName, String requestFormat) {
		try {
			Path testClassDirectory = createDirectory(imageDirectory, testGroup);
			InputStream imageStream = rsp.readEntity(InputStream.class);

			String fileExtension = detectFileExtension(requestFormat);
			if ((fileExtension != null) && (!fileExtension.startsWith("."))) {
				fileExtension = "." + fileExtension;
			}
			String fileName = testName + fileExtension;
			Path imageFile = testClassDirectory.resolve(FilenameUtils.normalize(fileName));
			Integer indx = -1;
			while (Files.exists(imageFile, java.nio.file.LinkOption.NOFOLLOW_LINKS)) {
				fileName = testName + (++indx).toString() + "." + fileExtension;
				imageFile = testClassDirectory.resolve(FilenameUtils.normalize(fileName));
			}

			Files.copy(imageStream, imageFile);
		}
		catch (IOException ioe) {
			TestSuiteLogger.log(Level.WARNING, "IO:  Writing the GetTile response into file failed.", ioe);
		}
	}

	private void writeIntoFile(SOAPMessage soapResponse, String testGroup, String testName, String requestFormat) {
		try {
			Path testClassDirectory = createDirectory(imageDirectory, testGroup);

			String fileExtension = detectFileExtension(requestFormat);
			if ((fileExtension != null) && (fileExtension.startsWith("."))) {
				fileExtension = fileExtension.substring(1);
			}

			String fileName = testName + "." + fileExtension;
			Path imageFile = testClassDirectory.resolve(FilenameUtils.normalize(fileName));
			Integer indx = -1;
			while (Files.exists(imageFile, java.nio.file.LinkOption.NOFOLLOW_LINKS)) {
				fileName = testName + (++indx).toString() + "." + fileExtension;
				imageFile = testClassDirectory.resolve(FilenameUtils.normalize(fileName));
			}

			Document soapDocument = WmtsSoapContainer.makeResponseDocument(soapResponse);

			// String formatStr =
			// (String)createXPath().evaluate("//wmts:BinaryPayload/wmts:Format",
			// soapDocument,XPathConstants.STRING);
			// String imageString =
			// (String)createXPath().evaluate("//wmts:BinaryPayload/wmts:BinaryContent",
			// soapDocument,XPathConstants.STRING);
			String imageString = (String) ServiceMetadataUtils.getNodeText(soapDocument,
					"//wmts:BinaryPayload/wmts:BinaryContent");

			BufferedImage bufferedImage = null;
			byte[] imageByte;

			Decoder decoder = Base64.getDecoder();
			imageByte = decoder.decode(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			bufferedImage = ImageIO.read(bis);
			bis.close();

			OutputStream imageOutputFile = Files.newOutputStream(imageFile);
			ImageIO.write(bufferedImage, fileExtension, imageOutputFile);
			imageOutputFile.close();
		}
		catch (IOException ioe) {
			TestSuiteLogger.log(Level.WARNING, "IO:  Writing the GetTile response into file failed.", ioe);
		}
		catch (XPathExpressionException xpe)// | XPathFactoryConfigurationException xpe)
		{
			TestSuiteLogger.log(Level.WARNING,
					"SOAP converted document contains an error (no or corrupt BinaryContent)", xpe);
		}
	}

	private String detectFileExtension(String requestFormat) // throws MimeTypeException
	{
		String extension = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(this.getClass().getResourceAsStream(MIME_FILENAME), "UTF-8"));
			String mimeLine = null;

			do {
				mimeLine = br.readLine();

				if ((mimeLine != null) && (mimeLine.indexOf(':') > 0)) {
					int indx = mimeLine.indexOf(':');
					String mime = mimeLine.substring(0, indx);
					String m_ext = mimeLine.substring(indx + 1);

					if (mime.equalsIgnoreCase(requestFormat)) {
						extension = m_ext;
					}
				}
			}
			while ((mimeLine != null) && (extension == null));
			br.close();
		}
		catch (IOException e) {
			TestSuiteLogger.log(Level.WARNING, "Cannot find MIME Types.", e);
		}

		return extension;
	}

	private Path createDirectory(Path parent, String child) throws IOException {
		Path testClassDirectory = parent.resolve(child);
		Files.createDirectories(testClassDirectory);
		return testClassDirectory;
	}

	/**
	 * Gets the location of the output directory from the test run context.
	 * @param testContext Information about a test run.
	 * @return A String that identifies the directory containing test run results.
	 */

	private String retrieveSessionDir(ITestContext testContext) {
		File outputDir = new File(testContext.getOutputDirectory());
		return outputDir.getPath();
	}

	private void parseNodes(Node n, int level) {
		if (n != null) {
			String nam = n.getNodeName();
			String val = n.getNodeValue();
			String lnm = n.getLocalName();
			// String txt = n.getTextContent().trim();
			if (!nam.contains(":") && !nam.startsWith("#")) {
				String namespaceURI = n.getNamespaceURI();
				if (namespaceURI.contains("soap"))
					nam = "soap:" + nam;
				else if (namespaceURI.contains("ows"))
					nam = "ows:" + nam;
				else if (namespaceURI.contains("wmts"))
					nam = "wmts:" + nam;

			}

			for (int i = 0; i < level; i++)
				System.out.print("\t");
			System.out.println("Node: " + nam + " = " + val);// + "( or: " + txt + " )");
			parseNodes(n.getFirstChild(), level + 1);

			parseNodes(n.getNextSibling(), level);
		}
	}

}