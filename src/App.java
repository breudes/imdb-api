import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

import src.help.JsonParser;

public class App {

	public static void main(String[] args) {
				// get auth key from your IMDb Api Account 
				String authKey = null;
				// get first line of txt file 
				FileReader txtFile;
				try {
					txtFile = new FileReader("src/auth-key.txt");
					try (BufferedReader buffer = new BufferedReader(txtFile)) {
						String line = buffer. readLine();
						
						if(line != null) {
							authKey = line;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// use auth key to access data via HTTP request
				String endpoint = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
				String url = endpoint + authKey;
				
				// create uri 
				URI address = URI.create(url);
				// set http client
				HttpClient client = HttpClient.newHttpClient();
				// set http request
				HttpRequest request = HttpRequest.newBuilder(address).GET().build();
				
				String body = null;
				try {
					HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
					body = response.body();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				
				// extract data
				JsonParser parser = new JsonParser();
				List<Map<String, String>> moviesList = parser.parse(body);
				
				// poster's endpoint to get image of each movie
				String posterEndpoint = "https://image.tmdb.org/t/p/w500/";
				
				// display especific data: title, poster image and vote average
				for (Map<String,String> movie : moviesList) {
		            System.out.println(movie.get("title"));
		            System.out.println(posterEndpoint + movie.get("backdrop_path"));
		            System.out.println(movie.get("vote_average"));
		            System.out.println();
		        }
				
				System.out.println(moviesList.size() + " movies.");
	}

}
