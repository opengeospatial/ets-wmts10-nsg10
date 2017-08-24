package org.opengeospatial.cite.wmts10.ets.core.dgiwg.testsuite.gettile;

import static java.nio.file.Files.createTempDirectory;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;
import org.testng.ISuite;
import org.testng.ITestContext;

import com.sun.jersey.api.client.ClientResponse;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class BaseGetMapFixtureTest
{
/*--
    private static final String TEST_CLASS = "testClass";

    private static final String TEST_NAME = "testName";

    @Test
    public void test()
                    throws Exception {
        BaseGetMapFixture baseGetMapFixture = new BaseGetMapFixture();
        Path reportDirectory = createTempDirectory( "BaseGetMapFixtureTest" );
        baseGetMapFixture.setResultDirectory( testContext( reportDirectory ) );

        ClientResponse rsp = createResponse();
        baseGetMapFixture.storeResponseImage( rsp, TEST_CLASS, TEST_NAME, "image/png" );

        Path imageDirectory = reportDirectory.resolve( BaseGetMapFixture.SUBDIRECTORY );
        assertTrue( Files.exists( imageDirectory ) );
        assertTrue( Files.isDirectory( imageDirectory ) );

        Path testClassDirectory = imageDirectory.resolve( TEST_CLASS );
        assertTrue( Files.exists( testClassDirectory ) );
        assertTrue( Files.isDirectory( testClassDirectory ) );

        Path imageFile = testClassDirectory.resolve( TEST_NAME + ".png" );
        assertTrue( Files.exists( imageFile ) );
        assertTrue( Files.isRegularFile( imageFile ) );
    }

    private ClientResponse createResponse() {
        ClientResponse mockedResponse = mock( ClientResponse.class );
        InputStream imageStream = BaseGetMapFixtureTest.class.getResourceAsStream( "latlon_bg4.png" );
        when( mockedResponse.getEntityInputStream() ).thenReturn( imageStream );
        return mockedResponse;
    }

    private ITestContext testContext( Path reportDirectory ) {
        Path testNgReportDir = reportDirectory.resolve( "step1" ).resolve( "step2" ).resolve( "step3" );
        ISuite mockedSuite = mock( ISuite.class );
        when( mockedSuite.getOutputDirectory() ).thenReturn( testNgReportDir.toFile().getAbsolutePath() );
        ITestContext mockedTestContext = mock( ITestContext.class );
        when( mockedTestContext.getSuite() ).thenReturn( mockedSuite );
        return mockedTestContext;
    }
--*/
}