package com.sse.salesforce;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sse.pages.Pages;
import com.sse.utilities.Browser;
import com.sse.utilities.StringUtilities;
import com.sse.utilities.WaitUtilities;

import io.cucumber.java.Scenario;

/**
 * This class is designed to store data from Salesforce. As there may be
 * multiple occurrences of each object (e.g. multiple Contacts), the main
 * instance variable for each type is a HashMap of key/value = Id/Object where
 * Id is the 15/18-character Salesforce ID of that object.<br>
 *
 * Note that the relationships are NOT maintained within the objects themselves,
 * e.g. an Account does NOT 'have' instances of Contact within it. It does store
 * the id(s).<br>
 * This is because we may not wish to traverse the object graph to find what we
 * need to directly,<br>
 * BUT the storage of ids does mean that we can traverse the graph if
 * required.<br>
 *
 * This can also contain other 'ad-hoc' scenario-based data if it is required
 * across different steps within a scenario.<br>
 *
 * This is instantiated afresh for each scenario.
 *
 * @author mitchella3
 */
public class ScenarioData implements Serializable {
	private static Logger log = LogManager.getLogger(ScenarioData.class);
	private static final long serialVersionUID = 1L;

	/*
	 * Fields derived from the current Cucumber scenario. Used for information when
	 * logged, and for serialisation when deriving the file name.
	 */
	private String scenarioName;
	private String scenarioFeatureTag;
	private String scenarioScenarioTag;
	private Instant serialisationEventInstant;
	private String currentUserName;
	private final Map<String,Object> myGeneralMap = new HashMap<>();


	/**
	 * Constructor which stores the name of the current scenario.
	 *
	 * @param scenario the current Cucumber Scenario.
	 */
	public ScenarioData(Scenario scenario) {
		this.scenarioName = scenario.getName();
		this.scenarioFeatureTag = ((List<String>)scenario.getSourceTagNames()).get(0);
		this.scenarioScenarioTag = ((List<String>)scenario.getSourceTagNames()).get(1);

		/**
		 * As we are unable to check attributes of checkboxes these values are not stored with storeDetails method.
		 * It is stored to myGeneralMap with default values and will be changed and called over myGeneralMap.
		 * If default values changes in the future, we also need to change that from below part.
		 * If values changes automatically during the scenario(with impact of any changes),
		 * we also need to change it from myGeneralMap. Because we will be calling values from myGeneralMap.
		 */
		this.myGeneralMap.put("alternativeBillingSystem", "False");
		this.myGeneralMap.put("reApproval", "False");
		this.myGeneralMap.put("integratedProduct", "False");
		this.myGeneralMap.put("taxable", "False");

	}

	/**
	 * For instances in which we expect only one item in one of the Maps, this
	 * method returns the first SalesforceObject in that map.<br>
	 * Returns null if the map is empty.
	 *
	 * @param map the Map to be inspected.
	 * @return the value of the first entry in the Map, or null if the Map is empty.
	 */
	public SalesforceObject getFirstFromMap(Map<String, ? extends SalesforceObject> map) {
		for (SalesforceObject s : map.values()) {
			return s;
		}
		return null;
	}
	
	/**
	 * For instances in which we expect more than one item in one of the Maps, this
	 * method returns the SalesforceObject on the specified order in that map.<br>
	 *
	 * @param map   the Map to be inspected.
	 * @param order order of the SalesforceObject to be returned from the map
	 * @return the value of the specified entry in the Map
	 */
	public <T extends SalesforceObject> SalesforceObject getObjectFromMapByOrder(Map<String, T> map, int order) {
		Set<Map.Entry<String, T>> entrySet = map.entrySet();
		Iterator<Map.Entry<String, T>> iterator = entrySet.iterator();

		int i = 0;
		SalesforceObject s = null;

		while (iterator.hasNext()) {
			if (order - 1 == i) {
				s = iterator.next().getValue();
				break;
			}
			iterator.next();
			i++;
		}
		return s;
	}

	/**
	 * Uses the Apache commons reflective StringBuilder to generate a default
	 * toString() method.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * Uses the Apache commons reflective StringBuilder to generate a multi-line
	 * version of the toString() method.
	 *
	 * @return a multi-line String of this object's toString() implementation.
	 */
	public String toStringMultiLine() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@SuppressWarnings("javadoc")
	public String getScenarioName() {
		return scenarioName;
	}

	@SuppressWarnings("javadoc")
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	@SuppressWarnings("javadoc")
	public String getScenarioFeatureTag() {
		return scenarioFeatureTag;
	}

	@SuppressWarnings("javadoc")
	public void setScenarioFeatureTag(String scenarioFeatureTag) {
		this.scenarioFeatureTag = scenarioFeatureTag;
	}

	@SuppressWarnings("javadoc")
	public String getScenarioScenarioTag() {
		return scenarioScenarioTag;
	}

	@SuppressWarnings("javadoc")
	public void setScenarioScenarioTag(String scenarioScenarioTag) {
		this.scenarioScenarioTag = scenarioScenarioTag;
	}

	@SuppressWarnings("javadoc")
	public Instant getSerialisationEventInstant() {
		return serialisationEventInstant;
	}

	@SuppressWarnings("javadoc")
	public void setSerialisationEventInstant(Instant serialisationEventInstant) {
		this.serialisationEventInstant = serialisationEventInstant;
	}
	@SuppressWarnings("javadoc")
	public Map<String, Object> getMyGeneralMap() {
		return myGeneralMap;
	}
	
	/**That method stores current url to MyGeneralMap, to use it later on.
	 * @param urlKey keyvalue of the map.
	 */
	public void storeUrlToMyGeneralMap(String urlKey) {
		Pages.scenarioData.getMyGeneralMap().put(urlKey, Browser.driver.getCurrentUrl());
		int count = 0;
		while(count<10) {
			count++;
			if(!StringUtilities.isNullEmptyOrWhiteSpace((String)Pages.scenarioData.getMyGeneralMap().get(urlKey)))
				break;
			WaitUtilities.sleep(1);
			Pages.scenarioData.getMyGeneralMap().put(urlKey, Browser.driver.getCurrentUrl());
		}
		log.info("Url stored as:\n"+urlKey+": "+Pages.scenarioData.getMyGeneralMap().get(urlKey));
	}

}
