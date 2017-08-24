package org.opengeospatial.cite.wmts10.ets.core.domain;

import java.util.List;

import org.w3c.dom.Document;

/**
 * An enumerated type defining ISuite attributes that may be set to constitute a shared test fixture.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public enum SuiteAttribute
{
    /**
     * A DOM Document describing the WMTS under test. This is typically a WMTS capabilities document.
     */
    TEST_SUBJECT( "testSubject", Document.class ),

    LAYER_INFO( "layerInfo", List.class ),

    IS_VECTOR( "vector", Boolean.class ),
/*--
    INTERACTIVE_TEST_RESULT( "interactiveTestResult", InteractiveTestResult.class ),
--*/
    ;
	
    private final Class<?> attrType;

    private final String attrName;

    private SuiteAttribute( String attrName, Class<?> attrType ) {
        this.attrName = attrName;
        this.attrType = attrType;
    }

    public Class<?> getType() {
        return attrType;
    }

    public String getName() {
        return attrName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( attrName );
        sb.append( '(' ).append( attrType.getName() ).append( ')' );
        return sb.toString();
    }

}