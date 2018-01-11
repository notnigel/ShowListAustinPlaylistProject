package controllers.webrequest.spotify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import model.spotify.toptracksresponse.TopTracksResponse;

public class TopTrackRequestClient {

  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  static final JsonFactory JSON_FACTORY = new JacksonFactory();
  
  static final String OAUTH2ACCESSTOKEN = "BQAzXLdIgNh9TzMeoooBIVzzjG3DjwNOpY4Xr0Xsl63zwyDyGeX-mZYpCaDmuXoWSfq_MpFFQr1EyOjefQXQHw";
  static final String TYPE = "artist";
  static final int TOPTRACKCOUNT = 1;

  public static class SpotifyUrl extends GenericUrl {
    public SpotifyUrl(String encodedUrl) {
      super(encodedUrl);
    }
  }

  public static String run(String artist) throws Exception {
	  
    HttpRequestFactory requestFactory =
        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
          public void initialize(HttpRequest request) {
            request.setParser(new JsonObjectParser(JSON_FACTORY));
          }
        });
    
    String oauth2AccessToken = Oauth2Controller.getOAUTH2AUTHTOKEN();  
    String baseUrl = "https://api.spotify.com/v1/artists/";
    baseUrl = 	baseUrl + artist + 
    			"/top-tracks?country=US"; 
    SpotifyUrl url = new SpotifyUrl(baseUrl);
    HttpRequest request = requestFactory.buildGetRequest(url);
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept("application/json");
    headers.setAuthorization(oauth2AccessToken);
    request.setHeaders(headers);
    HttpResponse response = request.execute();
  	ObjectMapper objectMapper = new ObjectMapper();
  	TopTracksResponse topTracksResponse = objectMapper.readValue(response.parseAsString(), TopTracksResponse.class);
  		
  	if (!topTracksResponse.getTracks().isEmpty()) {
  		return topTracksResponse.getTracks().get(0).getUri();
  	}
  	
  	return null;
    
  }
  
}
