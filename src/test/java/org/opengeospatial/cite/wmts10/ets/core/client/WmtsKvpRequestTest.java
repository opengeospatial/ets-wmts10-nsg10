package org.opengeospatial.cite.wmts10.ets.core.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsKvpRequestTest {

    @Test
    public void testAsQueryString() {
        WmtsKvpRequest wmtsKvpRequest = new WmtsKvpRequest();
        wmtsKvpRequest.addKvp( "key1", "value1" );
        wmtsKvpRequest.addKvp( "key2", "value2" );

        String queryString = wmtsKvpRequest.asQueryString();

        assertThat( queryString, CoreMatchers.anyOf( is( "key1=value1&key2=value2" ), is( "key2=value2&key1=value1" ) ) );
    }

    @Test
    public void testAsQueryStringOverwriteKey() {
        WmtsKvpRequest wmtsKvpRequest = new WmtsKvpRequest();
        wmtsKvpRequest.addKvp( "key2", "value2" );
        wmtsKvpRequest.addKvp( "key2", "value3" );

        String queryString = wmtsKvpRequest.asQueryString();

        assertThat( queryString, is( "key2=value3" ) );
    }

    @Test
    public void testAsQueryStringNullKey() {
        WmtsKvpRequest wmtsKvpRequest = new WmtsKvpRequest();
        wmtsKvpRequest.addKvp( "key1", "value1" );
        wmtsKvpRequest.addKvp( null, "value2" );

        String queryString = wmtsKvpRequest.asQueryString();

        assertThat( queryString, is( "key1=value1" ) );
    }

}