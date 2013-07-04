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

package es.upm.dit.gsi.episteme.matching;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;

import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import de.fuberlin.wiwiss.semmf.engine.MatchingEngine;
import de.fuberlin.wiwiss.semmf.result.GraphMatchingResult;
import de.fuberlin.wiwiss.semmf.result.MatchingResult;
import de.fuberlin.wiwiss.semmf.vocabulary.MD;
import es.upm.dit.gsi.episteme.json.JSONTreatment;

/**
 * 
 * @author Adriano Martin
 * @version 1.0
 */
public class SemanticSemMF {

	/**
	 * @param baseURL
	 * @param pathFileEnt2 
	 * @param localURL
	 * @return
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONArray calMatching(String baseURL, String pathFileEnt,String pathFileOffer, String oferta, JSONTreatment jt) 
			throws MalformedURLException, IOException, JSONException {
		String filePathServiceMD = baseURL + "doc/serviceMD.n3";
		Model serviceMD = createServiceMD(baseURL, pathFileEnt, pathFileOffer,oferta);
		writeServiceMDtoFile (serviceMD, filePathServiceMD, "N3");	     
			
		MatchingEngine me = new MatchingEngine("file:" + baseURL + "config/assemblerMappings.rdf", 
				"file:" + baseURL + "doc/serviceMD.n3", "N3");
		MatchingResult mr = me.exec();
		
		File outputFile = new File(filePathServiceMD);
		if (outputFile.exists()) {
        	outputFile.delete();        	 
		}
		
//		printMatchingResult(mr);  // Para imprimir los resultados por consola
		return getSemanticResult(mr, jt);
	}
	

	/**
	 * see tutorial: how to create a matching description (included in SemMF distribution)
	 */ 
	private Model createServiceMD (String baseURL, String pathFileEnt, String pathFileOffer, String oferta) {
		
		Model m = ModelFactory.createDefaultModel();
		
		Resource gmd = m.createResource();
		gmd.addProperty(RDF.type, MD.GraphMatchingDescription);
		gmd.addProperty(MD.queryModelURL, "file:" + pathFileOffer);
		gmd.addProperty(MD.resModelURL, "file:" + pathFileEnt);
		gmd.addProperty(MD.queryGraphURI, "http://example.org/CategoriesRequired.rdfs#"+oferta);
		gmd.addProperty(MD.resGraphURIpath, "(?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kmm.lboro.ac.uk/ecos/1.0#Enterprise>)");
						
		Bag cmds = m.createBag();
		gmd.addProperty(MD.hasClusterMatchingDescriptions, cmds);
		
		Resource cmd_skills = m.createResource();
		cmd_skills.addProperty(RDF.type, MD.ClusterMatchingDescription);		
		cmd_skills.addProperty(MD.label, "skills");
		cmd_skills.addProperty(MD.weight, "1");						
		cmds.add(cmd_skills);
			
		Bag nmds_cskills = m.createBag();
		cmd_skills.addProperty(MD.hasNodeMatchingDescriptions, nmds_cskills);
		
		Resource cskills_nmd = m.createResource();
		nmds_cskills.add(cskills_nmd);
		cskills_nmd.addProperty(RDF.type, MD.NodeMatchingDescription);
		cskills_nmd.addProperty(MD.label, "skill node");
		cskills_nmd.addProperty(MD.weight, "1");
		cskills_nmd.addProperty(MD.queryNodePath, "(<http://example.org/CategoriesRequired.rdfs#"+oferta+"> <http://example.org/CategoriesRequired.rdfs#hasCategorieDetails> ?categorieDetails) (?categorieDetails <http://example.org/CategoriesRequired.rdfs#requiredCompetence> ?x)");
		cskills_nmd.addProperty(MD.resNodePath, "(<#graphEntryURI#> <http://kmm.lboro.ac.uk/ecos/1.0#Skill> ?x)");
		cskills_nmd.addProperty(MD.reverseMatching, "false");
		
		Bag pmds = m.createBag();
		cskills_nmd.addProperty(MD.hasPropertyMatchingDescriptions, pmds);
							
		Resource pmd_skill = m.createResource();
		pmds.add(pmd_skill);
		pmd_skill.addProperty(RDF.type, MD.PropertyMatchingDescription);
		pmd_skill.addProperty(MD.label, "skill");
		pmd_skill.addProperty(MD.weight, "0.6");
		pmd_skill.addProperty(MD.queryPropURI, RDF.type);
		pmd_skill.addProperty(MD.resPropURI, RDF.type);
		pmd_skill.addProperty(MD.reverseMatching, "false");
		
		Resource tm_skill = m.createResource();
		tm_skill.addProperty(RDF.type, MD.TaxonomicMatcher);
		pmd_skill.addProperty(MD.useMatcher, tm_skill);
		tm_skill.addProperty(MD.simInheritance, "true");
					
		Resource taxon_skills = m.createResource();
		taxon_skills.addProperty(RDF.type, MD.Taxonomy);
		taxon_skills.addProperty(MD.taxonomyURL, "file:" + baseURL + "doc/it-cat.rdfs");
		taxon_skills.addProperty(MD.rootConceptURI, "http://kmm.lboro.ac.uk/ecos/1.0#IT_Cats");
		tm_skill.addProperty(MD.taxonomy, taxon_skills);
					
		Resource emc = m.createResource();
		emc.addProperty(RDF.type, MD.ExpMilestCalc);
		emc.addProperty(MD.k_factor, "2");
		tm_skill.addProperty(MD.useMilestoneCalc, emc);
		
		Resource pmd_skilllevel = m.createResource();
		pmds.add(pmd_skilllevel);
		pmd_skilllevel.addProperty(RDF.type, MD.PropertyMatchingDescription);
		pmd_skilllevel.addProperty(MD.label, "skill level");
		pmd_skilllevel.addProperty(MD.weight, "0.4");
		pmd_skilllevel.addProperty(MD.queryPropURI, "http://kmm.lboro.ac.uk/ecos/1.0#competenceLevel");
		pmd_skilllevel.addProperty(MD.resPropURI, "http://kmm.lboro.ac.uk/ecos/1.0#competenceLevel");
		pmd_skilllevel.addProperty(MD.reverseMatching, "false");
		
		Resource tm_skilllevel = m.createResource();
		tm_skilllevel.addProperty(RDF.type, MD.TaxonomicMatcher);
		pmd_skilllevel.addProperty(MD.useMatcher, tm_skilllevel);
		tm_skilllevel.addProperty(MD.simInheritance, "true");
		
		Resource taxon_skillLevel = m.createResource();
		taxon_skillLevel.addProperty(RDF.type, MD.Taxonomy);
		taxon_skillLevel.addProperty(MD.taxonomyURL, "file:" + baseURL + "doc/it-cat.rdfs");
		taxon_skillLevel.addProperty(MD.rootConceptURI, "http://kmm.lboro.ac.uk/ecos/1.0#Skill_level");
		tm_skilllevel.addProperty(MD.taxonomy, taxon_skillLevel);
		
		Resource lmc = m.createResource();
		lmc.addProperty(RDF.type, MD.LinMilestCalc);
		tm_skilllevel.addProperty(MD.useMilestoneCalc, lmc);
		
		return m;		
	}
	
	
	
	/**
	 * @param mr
	 * @param oferta
	 * @param jt
	 * @param rc
	 * @return
	 * @throws JSONException 
	 */
	private JSONArray getSemanticResult (MatchingResult mr, JSONTreatment jt) throws JSONException {
		JSONArray response = jt.treatment();
		for (int i = 0; i < response.length(); i++) {
			response.getJSONObject(i).put("semantic", 0);
		}
		
		while (mr.hasNext()) {
			GraphMatchingResult gmr = mr.next();
			String id = gmr.getResGraphEntryNode().getURI().toString().substring(28);
			float semanticResult = gmr.getSimilarity();
			for (int i = 0; i < response.length(); i++) {
				if (id.equals(response.getJSONObject(i).get("id").toString())) 
					response.getJSONObject(i).put("semantic", semanticResult);
			}
		}		
		mr.setToFirst();
		
		return response;
	}
	
	/**
	 * @param m
	 * @param filePath
	 * @param lang
	 */
	private void writeServiceMDtoFile(Model m, String filePath, String lang) {
		
		m.setNsPrefix("semmf", MD.NS);
		m.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		m.setNsPrefix("ja", "http://jena.hpl.hp.com/2005/11/Assembler#");
		
		try {
			File outputFile = new File(filePath);
			if (!outputFile.exists()) {
	        	outputFile.createNewFile();        	 
	        }
			FileOutputStream out = new FileOutputStream(outputFile);
			m.write(out, lang);
			out.close();
		}
		catch (IOException e) { System.out.println(e.toString()); }
	}
	
}