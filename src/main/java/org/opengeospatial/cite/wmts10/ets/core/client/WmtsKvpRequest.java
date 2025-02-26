package org.opengeospatial.cite.wmts10.ets.core.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Encapsulates the parameters of a KVP request (GET).
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS/WMTS)
 */
public class WmtsKvpRequest {

	private final Map<String, String> kvps = new HashMap<String, String>();

	/**
	 * Add a new key value pair. If the key already exists the old value will be
	 * overwritten. If the key is <code>null</code> the KVP will be ignored.
	 * @param key never <code>null</code>
	 * @param value may be <code>null</code>
	 */
	public void addKvp(String key, String value) {
		if (key != null)
			kvps.put(key, encode(value));
	}

	/**
	 * @return the KVPs as query string (e.g. key1=value1&amp;key2=value2)
	 */
	public String asQueryString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> kvp : kvps.entrySet()) {
			if (sb.length() > 1)
				sb.append('&');
			sb.append(kvp.getKey()).append('=').append(kvp.getValue());
		}
		return sb.toString();
	}

	/**
	 * Removes the KVP with the passed key, if existing.
	 * @param key of the KVP to remove, may be <code>null</code> (nothing happens)
	 */
	public void removeKvp(String key) {
		kvps.remove(key);
	}

	// ---
	/**
	 * Gets the current value of a the KVP, if existing.
	 * @param keyd of the KVP to remove, may be <code>null</code> (nothing happens)
	 * @return the value of the KVP at key
	 */
	public String getKvpValue(String keyd) {
		String value = null;
		for (Entry<String, String> kvp : kvps.entrySet()) {
			if (kvp.getKey().equals(keyd)) {
				value = kvp.getValue();
				value = decode(value);
				break;
			}
		}
		return value;
	}

	private String encode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// UTF-8 should be available
		}
		return value;
	}

	private String decode(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// UTF-8 should be available
		}
		return value;
	}

}