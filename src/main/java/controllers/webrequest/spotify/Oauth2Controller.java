package controllers.webrequest.spotify;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.ClientCredentialsTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;

import controllers.webrequest.spotify.ArtistRequestClient.SpotifyUrl;

public class Oauth2Controller {

  private static final String APPLICATION_NAME = "Showlistaustinplaylist";
  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  //This authorization token needs to be replaced for each run
  private static String OAUTH2AUTHTOKEN = "Bearer BQBANvdef4wSkqSSs3HPWSeNZ7gegtcgpN7cUMRqJNqgHAJV0SxvHWQmm2Yc3K_Fk64UgqcPZ2E3sM6Fu5FFA8dO9RU3hEL06MixD7nwwTayLz7CKQ-Po3Sp5yPG0crbyQgH8YS3iT8ixOchRYbKutWfwz8nEukRE7AOpX9bK1KcAd8D9kXB_7FhIGBZ4p9I8KP2QrECQD_0MgiTYdVMNQaejyU7_mJMM0NCBy86gQ";

  /** Directory to store user credentials. */
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("/resources"), ".store/oauth2_spotify");
  
  private static FileDataStoreFactory dataStoreFactory;

  private static HttpTransport httpTransport;

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /** OAuth 2.0 scopes for authorization code flow. */
  private static final List<String> SCOPES = Arrays.asList("playlist-modify-public");

  private static Oauth2 oauth2;
  private static GoogleClientSecrets clientSecrets;
  private static String oauth2AccessToken = "No access token";

  public static String getOAUTH2AUTHTOKEN() {
	return OAUTH2AUTHTOKEN;
	}
  
  public static void setOAUTH2AUTHTOKEN(String oAUTH2AUTHTOKEN) {
	  OAUTH2AUTHTOKEN = oAUTH2AUTHTOKEN;
	  }

/*
   * This method retrieves the access token for the non private client credentials authorization flow.
   * https://developer.spotify.com/web-api/authorization-guide/#client-credentials-flow
   */
  public static String retrieveAccessToken() throws Exception{
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	  clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
		        new InputStreamReader(Oauth2Controller.class.getResourceAsStream("/SpotifyConfig.json")));
	  if(oauth2AccessToken.equals("No access token") || oauth2AccessToken.equals("")) {
		  TokenResponse response =
				  new ClientCredentialsTokenRequest(httpTransport, new JacksonFactory(),
						  new GenericUrl("https://accounts.spotify.com/api/token"))
				  			.setGrantType("client_credentials")
				  			.setClientAuthentication(
				  				new ClientParametersAuthentication(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret())).execute();
		  oauth2AccessToken = response.getAccessToken();
	  }
	
	  return oauth2AccessToken;
	  
  }
  
  /*
   * This method follows the authorization code flow. https://developer.spotify.com/web-api/authorization-guide/#authorization-code-flow
   * Currently in progress
   */
  public static String retrieveAuthorizationCode() throws Exception{
	  clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
		        new InputStreamReader(Oauth2Controller.class.getResourceAsStream("/SpotifyConfig.json")));

	  HttpRequestFactory requestFactory =
			  HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
		            @Override
		          public void initialize(HttpRequest request) {
		            request.setParser(new JsonObjectParser(JSON_FACTORY));
		          }
		        });
	  
	  String baseUrl = "https://accounts.spotify.com/authorize/?";
	  baseUrl =		baseUrl + 
					"response_type=code" + "&" +
					"client_id=" + clientSecrets.getDetails().getClientId() + "&" +
					"redirect_uri=https://127.0.0.1" + "&" +
					"scope=playlist-modify-public";
	  SpotifyUrl url = new SpotifyUrl(baseUrl);
	  HttpRequest request = requestFactory.buildGetRequest(url);
	  HttpResponse response = request.execute();
	  return null;
  }

  public static void main(String[] args) {
    try {
      System.out.print(retrieveAccessToken());
      return;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }

}