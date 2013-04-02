package es.upm.dit.gsi.episteme.rdfs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.upm.dit.gsi.episteme.json.JSONTreatment;

public class RdfConstructor {
	
	/**
	 * @param enterprises
	 * @return
	 */
	public void rdfEnterprises(File enterprises, JSONTreatment jt){
		JSONArray skills = new JSONArray();
		String id ="", idComp = "";
        try {
        	skills = jt.getJSONSkills();
    		enterprises.createNewFile();
    		FileWriter out = new FileWriter(enterprises);
    		out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            out.write("<rdf:RDF\n");
            out.write("xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
            out.write("xmlns:ecos=\"http://kmm.lboro.ac.uk/ecos/1.0#\"\n");
            out.write("xmlns:v=\"http://www.w3.org/2006/vcard/ns#\">\n");
            for (int i = 0; i < skills.length(); i++) {
            	try {
					id = skills.getJSONObject(i).getJSONObject("id").getString("value").replace(" ", "_").replace(",", "");
					if (i==0) out.write("<ecos:Enterprise rdf:about=\""+id+"\">\n");
					else if (id.compareTo(idComp)!=0) {
			            out.write("</ecos:Enterprise>\n");
						out.write("<ecos:Enterprise rdf:about=\""+id+"\">\n");
					}
	                out.write("<ecos:Skill>\n");
	                out.write("<ecos:"+transform(skills.getJSONObject(i).getJSONObject("skill").getString("value"))+">\n");
	                out.write("<ecos:competenceLevel rdf:resource=\"http://kmm.lboro.ac.uk/ecos/1.0#"+skills.getJSONObject(i).getJSONObject("skilllevel").getString("value").replace(" ", "_").replace(",", "")+"\"/>\n");
	                out.write("</ecos:"+transform(skills.getJSONObject(i).getJSONObject("skill").getString("value"))+">\n");
	                out.write("</ecos:Skill>\n");
	                idComp = id;      
				} catch (JSONException e) {
					e.printStackTrace();
			 	}
			}
            out.write("</ecos:Enterprise>\n");
            out.write("</rdf:RDF>");
            out.close();       
		} catch (JSONException e1) {
			e1.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	/**
	 * @param offer
	 * @return
	 */
	public void rdfOffer(File offer, JSONTreatment jt, String search, int entity){
		JSONObject oportunitie = new JSONObject();
        try {
        	oportunitie = jt.getOportunitieWithJSON(search);
    		offer.createNewFile();
    		FileWriter out = new FileWriter(offer);
    		out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            out.write("<rdf:RDF\n");
            out.write("xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
            out.write("xmlns:cr=\"http://example.org/CategoriesRequired.rdfs#\"\n");
            out.write("xmlns:skill=\"http://kmm.lboro.ac.uk/ecos/1.0#\"\n");
            out.write("xml:base=\"http://example.org/CategoriesRequired.rdfs#\">\n\r");
           	String nameOffer = oportunitie.getString("name");
        	out.write("<cr:CategoriesRequired rdf:ID=\""+nameOffer+"\">\n\r");
            out.write("<cr:hasCategorieDetails>\n");
            out.write("<cr:CategorieDetails>\n\r");
            JSONArray skills = oportunitie.getJSONArray("result").getJSONObject(entity).getJSONArray("semantic");
            for (int i = 0; i < skills.length(); i++) {
                out.write("<cr:requiredCompetence>\n");
                String aux = skills.getJSONObject(i).getString("skill");
        		byte[] bytes = aux.getBytes("ISO-8859-1");
        		String skill = new String(bytes, "UTF-8");
                out.write("<skill:"+transform(skill)+">\n");
                String level = skills.getJSONObject(i).getString("level");
                if (level.equals("basic"))	level = "Intermediate";
                else if (level.equals("expert"))	level = "Expert";
                else if (level.equals("advanced"))	level = "Advanced";
                out.write("<skill:competenceLevel rdf:resource=\"http://kmm.lboro.ac.uk/ecos/1.0#"+level+"\"/>\n");
                out.write("</skill:"+transform(skill)+">\n");
                out.write("</cr:requiredCompetence>\n\r"); 
            }
            out.write("</cr:CategorieDetails>\n\r");
            out.write("</cr:hasCategorieDetails>\n\r");
            out.write("</cr:CategoriesRequired>\n");   
            out.write("</rdf:RDF>");
            out.close();        
		} catch (JSONException e1) {
			e1.printStackTrace();            
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	

	/**
	 * @param s
	 * @return
	 */
	public String transform (String s){
		return s.replace(" ", "_").replace(",", "").replace("(", "").replace(")","").replace("/", "_").replace("&", "_").replace("®", "").replace(":", "").replace("-*", "").replace("…", "");
	}
}
