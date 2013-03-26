package es.dit.upm.gsi.lmf.rdf_import;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

// args[] = [URL, CONTEXT]
public class RDF_import {

	public static void main(String[] args) throws ClientProtocolException, IOException {

		HttpClient httpclient = new DefaultHttpClient();
		String baseURL = args[0];
		String context = args[1];

		if(args.length != 2){
			System.out.println("Se requieren los siguientes argumentos: URL a LMF (por ejemplo http://localhost:8080/LMF/) y el contexto o grafo (por ejemplo http://default)");
		}else{
			try {
				// Sacamos todos los ficheros que hay en el directorio
				String path = "./"; 

				File folder = new File(path);
				File[] listOfFiles = folder.listFiles(); 

				for (int i = 0; i < listOfFiles.length; i++) {
					File file = listOfFiles[i];
					System.out.println("Subiendo archivo " + file.getName());
					uploadFile(httpclient, file, baseURL, context);
				}

			} finally {
				httpclient.getConnectionManager().shutdown();
			}

		}
	}

	private static void uploadFile(HttpClient httpclient, File file, String baseURL, String context) throws ClientProtocolException, IOException{

		HttpPost httppost = new HttpPost(baseURL + "import/upload?context=" + context);

		InputStreamEntity reqEntity = new InputStreamEntity(
				new FileInputStream(file), -1);

		reqEntity.setContentType("application/rdf+xml");
		reqEntity.setChunked(true);

		httppost.setEntity(reqEntity);

		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());

		if (resEntity != null) {
			System.out.println("Response content length: " + resEntity.getContentLength());
			System.out.println("Chunked?: " + resEntity.isChunked());
		}
		EntityUtils.consume(resEntity);
	}
}
