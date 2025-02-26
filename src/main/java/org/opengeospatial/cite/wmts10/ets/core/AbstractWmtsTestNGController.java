package org.opengeospatial.cite.wmts10.ets.core;

import java.util.Map;

import de.latlon.ets.core.AbstractTestNGController;

/**
 * Main test run controller for WMTS oversees execution of TestNG test suites.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS and/or WMTS)
 */
public abstract class AbstractWmtsTestNGController extends AbstractTestNGController {

	/**
	 * Default constructor uses the location given by the "user.home" system property as
	 * the root output directory.
	 */
	public AbstractWmtsTestNGController() {
		super();
	}

	/**
	 * Construct a controller that writes results to the given output directory.
	 * @param outputDirUri A file URI that specifies the location of the directory in
	 * which test results will be written. It will be created if it does not exist.
	 */
	public AbstractWmtsTestNGController(String outputDirUri) {
		super(outputDirUri);
	}

	@Override
	protected void validateTestRunArgs(Map<String, String> args) {
		if (!args.containsKey(TestRunArg.WMTS.toString())) {
			throw new IllegalArgumentException(
					String.format("Missing argument: '%s' must be present.", TestRunArg.WMTS));
		}
	}

}
