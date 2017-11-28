package controllers.webrequest.spotify;

import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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
import model.spotify.Artist;
import model.spotify.createplaylistrequest.CreatePlaylistRequest;
import model.spotify.createplaylistresponse.CreatePlaylistResponse;

public class AddTracksRequestClient {
	  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  static final JsonFactory JSON_FACTORY = new JacksonFactory();
	  
	  static final String TYPE = "artist";
	  static final String USER = "niggity_nige";
	  private static final Integer [] SUCCESS = new Integer [] {0, 200, 202};	
	  private static final Set<Integer> SUCCESSCODES = new HashSet<Integer>(Arrays.asList(SUCCESS));

	  public static class SpotifyUrl extends GenericUrl {
	    public SpotifyUrl(String encodedUrl) {
	      super(encodedUrl);
	    }
	  }

	  public static void run(Playlist playlist) throws Exception {
	    HttpRequestFactory requestFactory =
	        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
	            @Override
	          public void initialize(HttpRequest request) {
	            request.setParser(new JsonObjectParser(JSON_FACTORY));
	          }
	        });
	    String baseUrl = "https://api.spotify.com/v1/users/";
	    baseUrl = 	baseUrl + USER + 
	    			"/playlists" + "/" + 
	    			playlist.getId() + "/" +
	    			"tracks?uris=";
	    ArrayList<Artist> artists = playlist.getArtists();
	    for (int i = 0; i < artists.size(); i++) {
	    	String trackUri = artists.get(i).getTrackUris();
	    	if (trackUri != null) {
	    		baseUrl += URLEncoder.encode(trackUri, "UTF-8");;
		    	if((artists.size() - 1) != i)
		    		baseUrl += ",";
	    	}
	    }
	    SpotifyUrl url = new SpotifyUrl(baseUrl);
	    
	  	//Create request
	    HttpRequest request = requestFactory.buildPostRequest(url, null);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept("application/json");
	    headers.setAuthorization(Oauth2Controller.getOAUTH2AUTHTOKEN());	    
	    request.setHeaders(headers);

	    HttpResponse response = request.execute();
	    
	    System.out.println(response.parseAsString());
	    
	  }
}
