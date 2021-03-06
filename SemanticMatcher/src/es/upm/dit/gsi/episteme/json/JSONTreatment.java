/*
* 
* This file is part of Episteme UI.
* 
* Episteme UI has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the  Technical University of Madrid [UPM] 
* (Universidad Politécnica de Madrid) 
* 
* Contact: 
* http://www.gsi.dit.upm.es/;
* 
* 
* Copyright 2012 UPM-GSI: Group of Intelligent Systems - Universidad Politécnica de Madrid (UPM)
* Licensed under the Apache License, Version 2.0 (the "License"); 
* You may not use this file except in compliance with the License. 
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
* applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific 
* language governing permissions and limitations under the License.
* 
*/

package es.upm.dit.gsi.episteme.json;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONTreatment {

	/*
	 * Example to fill in the properties file
	 * http://domain/episteme/LMF/sparql/select?query=
	 */
	private String lmfSparqlUrl;

	/**
	 * This constructor loads the needed properties from the jsonTreatment.properties file
	 * it must be correctly edited.
	 * These properties are the URLs of the endpoint
	 */
	public JSONTreatment(String base){
		Properties prop =  new Properties();
		try {
			prop.load(new FileInputStream(base + "/config/jsonTreatment.properties"));    		
			this.lmfSparqlUrl = prop.getProperty("lmfSparqlUrl");
			prop.getProperty("lmfEpistemeSearchUrl"); 
		} catch (IOException ex) {			
			ex.printStackTrace();
		}
	}	

	private static String queryPrefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
			+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			+ "PREFIX gsi: <http://www.gsi.dit.upm.es/>"
			+ "PREFIX ecos: <http://kmm.lboro.ac.uk/ecos/1.0#>";

	/**
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray getJSONSkills() throws JSONException{
		String querySkills = queryPrefix + "SELECT ?id ?skill ?skilllevel WHERE {" +
				"?id <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kmm.lboro.ac.uk/ecos/1.0#Enterprise> ." +
				"?id <http://www.w3.org/2006/vcard/ns#VCard> ?vcard ." +
				"?id <http://kmm.lboro.ac.uk/ecos/1.0#Specific> ?specific ." +
				"?specific <http://kmm.lboro.ac.uk/ecos/1.0#Skill> ?nodoskill ." +
				"?nodoskill rdf:Bag ?nodobag ." +
				"?nodobag ?p ?skillcontent ." +
				"?skillcontent <http://kmm.lboro.ac.uk/ecos/1.0#name> ?skill ." +
				"?skillcontent <http://kmm.lboro.ac.uk/ecos/1.0#level> ?skilllevel" +
				"}";

		try {
			querySkills = URLEncoder.encode(querySkills, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		querySkills = lmfSparqlUrl + querySkills + "&output=json";
		JSONObject skills = getJson(querySkills);

		return skills.getJSONObject("results").getJSONArray("bindings");
	}

	

	/**
	 * @param offer
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getOportunitieWithJSON(String offer) throws JSONException {

		JSONObject jo = new JSONObject(offer);
		JSONArray oportunitie = jo.getJSONArray("episteme.search." + getNameOffer(offer));		

		return oportunitie.getJSONObject(0);
	}

	/**
	 * @param offer
	 * @return
	 */
	public String getNameOffer(String offer) {
		return offer.subSequence(2, offer.indexOf("\":")).toString().substring(16);
	}

	/**
	 * @return 
	 * @throws JSONException 
	 * 
	 */
	public JSONArray treatment() throws JSONException{		
		String queryEnterprise = queryPrefix + "SELECT ?id ?name ?logo ?postalcode ?province ?address ?type ?description WHERE {" +
				"?id <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kmm.lboro.ac.uk/ecos/1.0#Enterprise> ;" +
				"   <http://www.w3.org/2006/vcard/ns#VCard> ?vcard ." +
				"?vcard <http://www.w3.org/2006/vcard/ns#fn> ?name ;" +
				"}";


		try {
			queryEnterprise = URLEncoder.encode(queryEnterprise, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		queryEnterprise = lmfSparqlUrl + queryEnterprise + "&output=json";
		JSONObject result = getJson(queryEnterprise);

		JSONArray enterprises = new JSONArray();
		JSONArray aux = result.getJSONObject("results").getJSONArray("bindings");
		for (int i = 0; i < aux.length(); i++) {
			JSONObject enterprise = new JSONObject();
			JSONObject auxObj = aux.getJSONObject(i);
			enterprise.put("id", auxObj.getJSONObject("id").get("value").toString().substring(28));
			enterprise.put("name", auxObj.getJSONObject("name").get("value"));
			enterprises.put(enterprise);
			
		}

		return enterprises;		
	}

	/**
	 * @param query
	 * @param categorieRequired
	 * @return
	 */
	private JSONObject getJson(String query){
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		JSONObject json = new JSONObject();
		String line;
		String result = "";
		try {
			url = new URL(query);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
			json = new JSONObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * @param object
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray filterSemantic (JSONArray object) throws JSONException {
		JSONArray filter = new JSONArray();
		for (int j = 0; j < object.length(); j++) {
			JSONObject enterprise = object.getJSONObject(j);
			if (enterprise.getDouble("weight") >= 0.5)
				filter.put(enterprise);
		}
		return filter;
	}
}
