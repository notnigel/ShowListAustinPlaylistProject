/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import controllers.webrequest.spotify.AddTracksRequestClient;
import controllers.webrequest.spotify.ArtistRequestClient;
import controllers.webrequest.spotify.CreatePlaylistRequestClient;
import controllers.webrequest.spotify.ReplaceTracksRequestClient;
import controllers.webrequest.spotify.TopTrackRequestClient;
import model.Playlist;
import model.spotify.Artist;

/*
 * This class creates a playlist for anyevent. Event line up must be comma or new line delimited.  
 */
public class AnyEventController {

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * !!!!!!!!!!Modify the list name and the artists before running!!!!!!!!!
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	final static String PLAYLIST_NAME = "ACL 2018";
	final static String EVENT_LINEUP = "";

	static ArrayList<Artist> artists = new ArrayList<Artist>();

	public static void main(String[] args) throws Exception {
		/*
		 * Split the event line-up string into separate bands.
		 */
		String[] bands = EVENT_LINEUP.split("(,|\\n)");

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
		 * Create playlist
		 */
		ArrayList<Playlist> playlists = new ArrayList();

		while (artists.size() > 100) {
			ArrayList<Artist> artists1 = new ArrayList<Artist>(artists.subList(0, 100));
			playlists.add(new Playlist(PLAYLIST_NAME, artists1));
			artists.subList(0, 100).clear();
		}

		playlists.add(new Playlist(PLAYLIST_NAME, artists));
		String playlistId = CreatePlaylistRequestClient.run(playlists.get(0));

		for (int i = 0; i < playlists.size(); i++) {
			playlists.get(i).setId(playlistId);
			AddTracksRequestClient.run(playlists.get(i));
		}

	}

}
