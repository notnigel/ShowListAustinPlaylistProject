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
  
  static final String OAUTH2ACCESSTOKEN = "BQAzXLdIgNh9TzMeoooBIVzzjG3DjwNOpY4Xr0Xsl63zwyDyGeX-mZYpCaDmuXoWSfq_MpFFQr1EyOjefQXQHw";
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
    baseUrl = 	baseUrl + 
				"q=" + artist + "&" +
				"type=" + TYPE + "&" +
				"access_token=" + OAUTH2ACCESSTOKEN; 
    System.out.println(baseUrl);
    SpotifyUrl url = new SpotifyUrl(baseUrl);
    url.fields = "id,tags,title,url";
    HttpRequest request = requestFactory.buildGetRequest(url);
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept("application/json");
    request.setHeaders(headers);
    HttpResponse response = request.execute();
  	ObjectMapper objectMapper = new ObjectMapper();
  	ArtistResponse artistResponse = objectMapper.readValue(response.parseAsString(), ArtistResponse.class);
  		
  	return artistResponse.getArtists().getItems().get(0).getId();

    
  }

  public static void main(String[] args) {
    try {
      try {
        run("Muse");
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
