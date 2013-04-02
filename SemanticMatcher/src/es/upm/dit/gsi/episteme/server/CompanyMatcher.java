package es.upm.dit.gsi.episteme.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.upm.dit.gsi.episteme.json.JSONTreatment;
import es.upm.dit.gsi.episteme.matching.SemanticSemMF;
import es.upm.dit.gsi.episteme.rdfs.RdfConstructor;

/**
 * Servlet implementation class CompanyMatcher
 */
public class CompanyMatcher extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompanyMatcher() {
        super();
    }
    
    /**
     * @return
     */
    public String getPathFile() throws Exception{
    	String a = getServletContext().getRealPath("/");
    	return a;
    }
    
    /**
     * @return 
     * @throws Exception 
     */
    public void refresh() throws Exception{
    	JSONTreatment jt = new JSONTreatment();
    	RdfConstructor rc = new RdfConstructor();
    	String base = getPathFile();
    	File f = new File (base + "doc/enterprises.rdf");
    	rc.rdfEnterprises(f, jt);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doProcess(request, response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doProcess(request, response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws JSONException 
	 * 
	 */
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {

		// Declaring variables
		String baseUrl = getServletContext().getRealPath("/");
		RdfConstructor rdc = new RdfConstructor();
		SemanticSemMF sSem = new SemanticSemMF();
		JSONTreatment jt = new JSONTreatment();
		
		// Extract the parameters of the query
		String oferta = request.getParameter("offer");
		int entity = Integer.parseInt(request.getParameter("entity"));
		JSONArray req = new JSONArray(request.getParameter("json"));
		
		// write rdf advertising/offer and enterprises
		File fileOff = new File(baseUrl + "/doc/o3.rdf");
	    rdc.rdfOffer(fileOff, jt, oferta, entity); 
	    String pathFileEnt = baseUrl + "/doc/enterprises.rdf";
		
        //execute semantic matching (using semmf)
		JSONArray semanticResult = sSem.calMatching(baseUrl, pathFileEnt, fileOff.getAbsolutePath(), jt.getNameOffer(oferta), jt);
		
		// introduce semantic matching
		for (int i = 0; i < req.length(); i++) {
			JSONObject enterprise = req.getJSONObject(i);
			String id = enterprise.getJSONArray("name").getString(0);
			for (int j = 0; j < semanticResult.length(); j++) {
				JSONObject enterprisesCache = semanticResult.getJSONObject(j);
				if (id.equals(enterprisesCache.get("name"))){
					double semanticValue = semanticResult.getJSONObject(j).getDouble("semantic");
					enterprise.put("weight", semanticValue);
				} 
			}	
		}
		req = jt.filterSemantic(req);
		System.out.println(req);
		
		// return output
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin","*");
		PrintWriter pw = new PrintWriter(response.getOutputStream());
		pw.println(req);
		pw.close();
	}
	
}