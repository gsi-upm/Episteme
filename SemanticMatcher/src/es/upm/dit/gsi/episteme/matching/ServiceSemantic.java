package es.upm.dit.gsi.episteme.matching;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceSemantic {
	
	/**
	 * @param json
	 * @param weights
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject introduceSemantic(JSONObject json, JSONObject semanticResult, String oferta){
		String[] cajas = {"caja1","caja2","caja3","caja4","caja5","caja6","caja7", "caja8","caja9"};
		JSONArray array;
		try {
			array = new JSONObject(json.getString("results").toString()).getJSONArray("bindings");
			for (int i = 0; i < array.length(); i++) {
				int j = 0;
				String enterprise = array.getJSONObject(i).getJSONObject("id").get("value").toString();
				for (Iterator keys = semanticResult.getJSONObject(enterprise).keys(); keys.hasNext();) {
					String req = keys.next().toString();
					JSONObject aux = new JSONObject();
					double value = semanticResult.getJSONObject(enterprise).getDouble(req);
					aux.put("value", value);
					json.getJSONObject("results").getJSONArray("bindings").getJSONObject(i).put(cajas[j], aux);  // Cambiar caja[j] por req si quieres que salga el número raro de referencia de la caja
					j++;
				}
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	
	/**
	 * @param json
	 * @param weights
	 * @return
	 */
	public JSONObject introduceSemantic2(JSONObject json, JSONObject semanticResult, String oferta){
		JSONArray array;
		try {
			array = new JSONObject(json.getString("results").toString()).getJSONArray("bindings");
			for (int i = 0; i < array.length(); i++) {
				String enterprise = array.getJSONObject(i).getJSONObject("id").get("value").toString();
				json.getJSONObject("results").getJSONArray("bindings").getJSONObject(i).put(oferta, semanticResult.getJSONObject(enterprise));
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
}