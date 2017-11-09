package controllers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import controllers.webrequest.spotify.ArtistRequestClient;
import model.Playlist;
import model.spotify.Artist;

public class WebScrapeController {
	
	private static Playlist playlist;

	public static void main(String[] args) {
		
	    String searchDate = "2017November6" ;
		String baseUrl = "http://showlistaustin.com" ;
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			
			//Retrieve the page source
			String searchUrl = baseUrl + "/#" + searchDate;
			HtmlPage page = client.getPage(searchUrl);
			
			//Extract the current days data
			String xPath = "(//a[@name='" + searchDate + "']/following::*/td[@class='h4'])[1]";
			List<HtmlElement> eventDay = (List<HtmlElement>) page.getByXPath(xPath) ;

			if (eventDay.isEmpty()){
				System.out.println("No items found !");
			} else {
				
				for(HtmlElement item : eventDay){
					
					//Extract a the bands from the data
					String rawEvent = item.getTextContent();
					rawEvent = rawEvent.replaceAll("at\\s(\\w+|\\s)*\\(.+\\)\\s\\[\\+\\]", "");
					rawEvent = rawEvent.replaceAll("\\[.+\\]", "");
					String [] bands = rawEvent.split("(,|\\n)");
					for (int i = 0 ; i <bands.length; i++) {
						bands[i] = bands[i].trim();
					}
					
					//Create playlist
					playlist = new Playlist(searchDate);
					ArrayList<Artist> artists = new ArrayList<Artist>();
					
					for (String band : bands) {
						Artist a = new Artist();
						a.setName(band);
						artists.add(a);
					}
					
					ArtistRequestClient artistRequestClient = null;
					System.out.println(artistRequestClient.run(artists.get(1).getName()));

				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
