/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import controllers.webrequest.spotify.AddTracksRequestClient;
import controllers.webrequest.spotify.ArtistRequestClient;
import controllers.webrequest.spotify.ReplaceTracksRequestClient;
import controllers.webrequest.spotify.TopTrackRequestClient;
import model.Playlist;
import model.spotify.Artist;

/*
 * This class adds the top tracks of the artists playing on Thursday, Friday, 
 * and Saturday in Austin to the latest ShowList Austin Spotify playlist.
 */
public class WeekendPlaylistController {
	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * !!!!!!!!!!Modify these dates for the weekend before running!!!!!!!!!!!
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	final static String[] DAYS = { "2019February28", "2019March1", "2019March2" };
	final static String BASEURL = "http://showlistaustin.com";
	static WebClient client = new WebClient();
	static ArrayList<Artist> artists = new ArrayList<Artist>();

	public static void main(String[] args) throws Exception {

		/*
		 * Set options to get only HTML source from the page
		 * 
		 */
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

		/*
		 * Put the list of artists together
		 */
		for (String day : DAYS) {
			try {

				/*
				 * Retrieve the page source
				 */
				String searchUrl = BASEURL + "/#" + day;
				HtmlPage page = client.getPage(searchUrl);

				/*
				 * Extract the current days show data
				 */
				String xPath = "(//a[@name='" + day + "']/following::*/td[@class='h4'])[1]";
				List<HtmlElement> eventDay = (List<HtmlElement>) page.getByXPath(xPath);

				if (eventDay.isEmpty()) {
					System.out.println("No items found !");
				} else {

					for (HtmlElement item : eventDay) {

						/*
						 * Extract a the bands from the data
						 */
						String rawEvent = item.getTextContent();
						rawEvent = rawEvent.replaceAll("at\\s(\\w+|\\s)*\\(.+\\)\\s\\[\\+\\]", "");
						rawEvent = rawEvent.replaceAll("\\[.+\\]", "");
						rawEvent = rawEvent.replaceAll("with", ",");
						String[] bands = rawEvent.split("(,|\\n)");
						for (int i = 0; i < bands.length; i++) {
							bands[i] = bands[i].replaceAll("\\(.+\\)", "");
							bands[i] = bands[i].replaceAll("#", "");
							bands[i] = bands[i].replaceAll(".*presents", "");
							bands[i] = bands[i].trim();
						}

						/*
						 * Add artists to artists array
						 */
						for (String band : bands) {
							Artist a = new Artist();
							if (!band.equals("")) {
								a.setName(band);
								artists.add(a);
							}
						}

						/*
						 * Retrieve artist id's and add them to artists object array
						 */
						String artistId = "";
						for (int i = 0; i < artists.size(); i++) {
							artistId = ArtistRequestClient.run(artists.get(i).getName());
							if (artistId != null && !artistId.equals("")) {
								artists.get(i).setId(artistId);
							}
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				client.close();
			}
		}

		/*
		 * Remove artists without Spotify artist IDs
		 */
		for (Iterator<Artist> iterator = artists.iterator(); iterator.hasNext();) {
			Artist a = iterator.next();
			if (a.getId() == null) {
				iterator.remove();
			}
		}

		/*
		 * Retrieve artist top track URI
		 */
		String topTrackUri = "";
		for (int i = 0; i < artists.size(); i++) {
			topTrackUri = TopTrackRequestClient.run(artists.get(i).getId());
			if (topTrackUri != null && !topTrackUri.equals("")) {
				artists.get(i).setTrackUris(topTrackUri);
			}
		}

		/*
		 * Remove artists with no top track URIs or duplicate URIs
		 */
		Set<String> attributes = new HashSet<String>();
		List duplicates = new ArrayList<Artist>();

		for (Artist a : artists) {
			if (attributes.contains(a.getTrackUris())) {
				duplicates.add(a);
			}
			attributes.add(a.getTrackUris());
		}

		artists.removeAll(duplicates);

		/*
		 * Replace playlist tracks with the new list of tracks
		 */
		if (artists.size() < 101) {
			Playlist playlist = new Playlist(artists);
			ReplaceTracksRequestClient.run(playlist);
		} else {
			ArrayList<Playlist> playlists = new ArrayList();
			while (artists.size() > 100) {
				ArrayList<Artist> artists1 = new ArrayList<Artist>(artists.subList(0, 100));
				playlists.add(new Playlist(artists1));
				artists.subList(0, 100).clear();
			}
			playlists.add(new Playlist(artists));
			ReplaceTracksRequestClient.run(playlists.get(0));
			for (int i = 1; i < playlists.size(); i++) {
				AddTracksRequestClient.run(playlists.get(i));
			}
		}

	}

}
