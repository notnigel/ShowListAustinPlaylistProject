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

import model.spotify.artistresponse.ArtistResponse;

public class ArtistRequestClient {

  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  static final JsonFactory JSON_FACTORY = new JacksonFactory();
  
  static final String TYPE = "artist";

  public static class SpotifyUrl extends GenericUrl {

    public SpotifyUrl(String encodedUrl) {
      super(encodedUrl);
    }

    @Key
    public String fields;
  }

  public static String run(String artist) throws Exception {
    HttpRequestFactory requestFactory =
        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
          public void initialize(HttpRequest request) {
            request.setParser(new JsonObjectParser(JSON_FACTORY));
          }
        });
    String baseUrl = "https://api.spotify.com/v1/search?";
    artist = artist.replaceAll(" ", "+");
    artist = artist.toLowerCase();
    
    //Get access token
    String oauth2AccessToken = "Bearer " + Oauth2Controller.retrieveAccessToken();
    baseUrl = 	baseUrl + 
				"q=" + artist + "&" +
				"type=" + TYPE; 
    SpotifyUrl url = new SpotifyUrl(baseUrl);
    HttpRequest request = requestFactory.buildGetRequest(url);
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept("application/json");
    headers.setAuthorization(oauth2AccessToken);
    request.setHeaders(headers);
    HttpResponse response = request.execute();
  	ObjectMapper objectMapper = new ObjectMapper();
  	ArtistResponse artistResponse = objectMapper.readValue(response.parseAsString(), ArtistResponse.class);

	if(!artistResponse.getArtists().getItems().isEmpty()) {
		return artistResponse.getArtists().getItems().get(0).getId();
	}
	
	return null;
    
  }

  public static void main(String[] args) {
    try {
      try {
        run("Me Mer Mo Monday with Kid Invincible");
        return;
      } catch (HttpResponseException e) {
        System.err.println(e.getMessage());
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
}
