package controllers.webrequest.spotify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import model.Playlist;
import model.spotify.createplaylistrequest.CreatePlaylistRequest;
import model.spotify.createplaylistresponse.CreatePlaylistResponse;

public class CreatePlaylistRequestClient {
	  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  static final JsonFactory JSON_FACTORY = new JacksonFactory();
	  
	  static final String TYPE = "artist";
	  static final String USER = "hi63nr6jeyewgbcscpvcvom8y";
	  private static final Integer [] SUCCESS = new Integer [] {0, 200, 201};	
	  private static final Set<Integer> SUCCESSCODES = new HashSet<Integer>(Arrays.asList(SUCCESS));

	  public static class SpotifyUrl extends GenericUrl {
	    public SpotifyUrl(String encodedUrl) {
	      super(encodedUrl);
	    }
	  }

	  public static String run(Playlist playlist) throws Exception {
	    HttpRequestFactory requestFactory =
	        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
	            @Override
	          public void initialize(HttpRequest request) {
	            request.setParser(new JsonObjectParser(JSON_FACTORY));
	          }
	        });
	    String baseUrl = "https://api.spotify.com/v1/users/";
	    baseUrl = 	baseUrl + USER + "/playlists";
	    SpotifyUrl url = new SpotifyUrl(baseUrl);
	    
	    //Create response body
	    CreatePlaylistRequest cplr = new CreatePlaylistRequest();
	    cplr.setName(playlist.getName());
	    cplr.setPublic(true);
	    cplr.setDescription(playlist.getDescription());
	  	ObjectMapper objectMapper = new ObjectMapper();
	  	String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cplr);
	  	
	  	//Create request
	    HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString(null, requestBody));
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept("application/json");
	    headers.setAuthorization(Oauth2Controller.getOAUTH2AUTHTOKEN());	    
	    request.setHeaders(headers);

	    HttpResponse response = request.execute();
	    
	    if(SUCCESSCODES.contains(response.getStatusCode())) {
	    	CreatePlaylistResponse createPlaylistResponse = objectMapper.readValue(response.parseAsString(), CreatePlaylistResponse.class);
	    	if (createPlaylistResponse.getId() != null) {
	    		return createPlaylistResponse.getId();
	    	}
	    }
	  	
	  	return null;
	    
	  }
}
