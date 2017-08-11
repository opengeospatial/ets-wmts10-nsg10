package ets.wmts10.core;

/**
 * An enumerated type defining all recognized test run arguments.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public enum TestRunArg {

    /**
     * An absolute URI referring to metadata about the WMTS implementation under
     * test. This is expected to be a WMTS 1.0.0 capabilities document where the
     * document element is {@code http://www.opengis.net/wmts} WMTS_Capabilities}.
     */
    WMTS,

    /**
     * An absolute URI that specifies the location of the test execution service
     * at which the base WMTS test suite is available. If not specified, the OGC
     * beta installation will be used.
     */
//    TES,

    /**
     * <code>true</code> if the WMTS contains vector data layers,
     * <code>false</code> otherwise
     */
//    VECTOR,

    /**
     * <code>true</code> if the interactive test for metadata content in english
     * language is passed, <code>false</code> otherwise
     */
//    CAPABILITIES_IN_ENGLISH,

    /**
     * <code>true</code> if the interactive test for GetFeatureInfo response in
     * english language is passed, <code>false</code> otherwise
     */
//    GETFEATUREINFO_IN_ENGLISH,

    /**
     * <code>true</code> if the interactive test for GetFeatureInfo exceptions
     * in english language is passed, <code>false</code> otherwise
     */
//    GETFEATUREINFO_EXCEPTION_IN_ENGLISH,

    /**
     * <code>true</code> if the interactive test for GetTile response in english
     * language is passed, <code>false</code> otherwise
     */
//    GETTILE_EXCEPTION_IN_ENGLISH,
    ;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}