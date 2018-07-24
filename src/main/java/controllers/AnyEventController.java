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
	final static String PLAYLIST_NAME = "Discover Hot Summer Nights 2018";
	final static String EVENT_LINEUP = "Emily Wolfe, Quiet Company, Darkbird, Good Field, Think No Think, Chakra Kahn, Lord Buffalo, MAMAHAWK," +
			"Holy Wave, Annabelle Chairlegs, Nolan Potter's Nightmare Band, Dollar General," +
			"Gio Chamba, Migrant Kids, Como Las Movies, Volcán, Stella, Late, llinx," +
			"Hovvdy, Why Bonnie, Loafer, Tåsi," +
			"Cilantro Boombox, Big Wy's Brass Band, Skymomma, Zoumountchi, Just Blaze, Black Milk, DJ Rapid Ric, Kid Slyce," +
			"Fuss Ricket, The Young Something, anatomi, Jessie Frye," +
			"Big Bill, Moving Panoramas, Leather Girls," +
			"Jess Williamson, Marijuana Sweet Tooth, Cowboy Crisis, RF SHANNON, Daphne tunes, Con Davison, Dreamspook" +
			"Tia Carrera, Dixie Witch, Greenbeard, Duel Crypt Trip, Blastfamous USA, BLXPLTN, Megafauna, Honey and Salt, Deals, Elevaded," +
			"Golden Dawn Arkestra, Mamafesta," +
			"Pleasure Venom, Nervous Exits, King Country," +
			"Riverboat Gamblers, Sealion, Dentist, Hey Jellie," +
			"Chromagnus, Billy King & The Bad Bad Bad, Rickshaw Billie's Burger Patrol, Slow Seers," +
			"Ringo Deathstarr, Growl, Shmu, SAILOR POON, Flesh Lights, Being Dead," +
			"Eagle Claw, Mandate, Fanclub, The Ghost Wolves, Chief White Lightning, BOOHER, Altamesa," +
			"Dude Elsberry, Why Bonnie, Datenight, Bleary Eyed," +
			"The Reputations, minihorse, Say Girl Say, Pearl Crush, Lola Tried, Royal Forest, Dan Gentile, Flying Turns," +
			"Moving Panoramas, Good Field, Fragile Rock, SMiiLE, Sometimes a Legend, Sphynx, BUHU, Haulm, TC Superstar," +
			"the Teddys, Big Britches, The Sun Machine, Friendly the band," +
			"Blushing, Mopac," +
			"Fat Tony, Mike Melinoe, Dj Big Daddy B, DJ Dorito, DJ DICK WOLF";
			

	static ArrayList<Artist> artists = new ArrayList<Artist>();

	public static void main(String[] args) throws Exception {
		/*
		 * Split the event line-up string into separate bands.
		 */
		String[] bands = EVENT_LINEUP.split("(,|\\n)");

		/*
		 * Trim off whitespace
		 */
		for(int i = 0; i < bands.length; i++)
			bands[i] = bands[i].trim();
		
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
			playlists.add(new Playlist(artists1, PLAYLIST_NAME));
			artists.subList(0, 100).clear();
		}

		playlists.add(new Playlist(artists, PLAYLIST_NAME));
		String playlistId = CreatePlaylistRequestClient.run(playlists.get(0));

		for (int i = 0; i < playlists.size(); i++) {
			playlists.get(i).setId(playlistId);
			AddTracksRequestClient.run(playlists.get(i));
		}

	}

}
