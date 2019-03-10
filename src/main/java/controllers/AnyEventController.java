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
	final static String PLAYLIST_NAME = "Austin Free Week 2019";
	final static String EVENT_LINEUP = "Fragile Rock, Honey & Salt, Tinnarose, ST37, Local Shadows, \n" + 
			"Do Your Homework, Divine Kegel, Subverter, \n" + 
			"Cheeky Orange, Indoor Creature, Vonne, Born Again Virgins, Mary Bryce, \n" + 
			"Jonathan Huggins, Oscar Ornela, \n" + 
			"Leather Girls, Go Fever, The Rotten Mangos, Lolita Lynne, The Boleys, \n" + 
			"Palm Daze, Groove Think, \n" + 
			"Bali Yaaah, \n" + 
			"Marfa Crush, Rival Waves,\n" + 
			"Modular Sun, FUVK, Camp Counselor, Pealds, \n" + 
			"Daze of Heaven, Shmu, Bragglights, Memory Keepers, Hatchet for the Honeymoon, DJ Malligator, Dj Boogiepop Phantom, \n" + 
			"Fanclub, Megafauna, Glaze, Borrisokane, Cartright, \n" + 
			"Ell, Deep Cross, Glassing, Grivo, \n" + 
			"Sailor Poon, Big Bill, Pelvis Wrestley, mamis, Slaughterstein, \n" + 
			"Unwritten, Wild Tinderbox, Ziggy Jones, \n" + 
			"The Reputations, Honey Made, Whit, Lord Buffalo, Caleb De Casper, Cheap Wave, Hong Kong Wigs, Oh Honey No\n" + 
			"Tinnarose, Nevil, Lake of Fire, Mountebank, \n" + 
			"TC Superstar, Shmu, Chill Russell, \n" + 
			"Ume, Darkbird, Cosmico, \n" + 
			"modern medicine, Mt. Grey, Long Tongue, \n" + 
			"Lisa Friedrich, Ralphie Hardesty, Austin Comic, \n" + 
			"Sharks In The Deep End, Belcurve, \n" + 
			"Think No Think, Billy King And The Bad Bad Bad, Ghost Wolves, White Dog, Rickshaw Billie's Burger Patrol, Booher, King Country, \n" + 
			"Bear on Bear, The Mystery Achievement, Mohawk Bends, BeckiJo Neill, Zac Brooks, \n" + 
			"T.I.A.N.T.G. Vol 3 Kick off with La Goony Chonga, \n" + 
			"Spice Poon, Flesh Lights, Andy Grant, Crooked Bangs, \n" + 
			"super thief, Leche, Ruiners, Dregs, Luvweb, \n" + 
			"Calliope Musicals, The Cuckoos, Matt Gilmour, Cosmico, \n" + 
			"The Reputations, Go Fever, Tinnarose, Lowin, A. Sinclair, The Oysters, booher, Girling, \n" + 
			"Genuine Leather, Oh Antonio & His Imaginary Friends, Big Coat, Indoor Creature, Ram Vela and the Easy Targets, Dr. Danny and the Patients, \n" + 
			"The Singularity, Mr. Kitty, Lov3rs, Boy Sim, \n" + 
			"duel, Amplified Heat, The Human Circuit, Flying Balalaika Brothers, Bridge Farmers, Destroyers of Light, Crimson Devils, Transit Method, \n" + 
			"Tyler Jordan, Sherry, Spirit Ghost, Dude Elsberry, \n" + 
			"PR Newman, The Oysters, Lola Tried, \n" + 
			"Horti & Friends with Slomo Drags, Belcurve, Daphne Tunes, \n" + 
			"Otis The Destroyer, Think No Think, Boyfriendz, Megafauna, \n" + 
			"Black Taffy, Botany, Lungfulls, Butcher Bear, Soundfounder, \n" + 
			"My Education, blxpltn, Shapescenes, \n" + 
			"Abhi The Nomad, The Bishops, Magna Carda, \n" + 
			"Chillbill, The Naked Tungs, The Boleys, Pelvis Wrestley, \n" + 
			"The Altitude, Kidlat, Boyd, Sherry, Little Thief, \n" + 
			"Nolan Potter's Nightmare Band, Leather Girls, Hidden Ritual, The Rotten Mangos, Nevil, \n" + 
			"Ringo Deathstarr, Narrow Head (Houston), Burnt Skull, Chronophage, \n" + 
			"The Gary, Economy Island, Apopka, \n" + 
			"My Golden Calf, Grape St, Housewarming, Grand Child, Caterpillar, Rachel Mallin & the Wild Type, \n" + 
			"Last Chance Riders, King Warbler, Batty jr., Jade and the Foxtones, Mardez, \n" + 
			"Adoration Destroyed, Single Lash, I Rock They Experience, \n" + 
			"Clyde and Clem's Whiskey Business, Worm Suicide, The Lewd Dudes, Harvest Thieves, Dr. Joe, The Cover Letter, Trump Card, The Swishbucklers, Lechuza, \n" + 
			"Afrofreque, \n" + 
			"The Stacks, Mean Jolene, Hong Kong Wigs, Cosmic Chaos, \n" + 
			"Megafauna, Belcurve, Dinner with Matt Gilchrest, \n" + 
			"The Bad Lovers, White Dog, Black Basements, High Heavens, \n" + 
			"The Western Civilization, Marijuana Sweet Tooth, Whit, Sprain, \n" + 
			"American Sharks, Not In The Face, Billy King & The Bad Bad Bad, Amplified Heat, \n" + 
			"The Vapor Caves, Amea, Alesia Lani, Jane Claire, Eimaral Sol, Britt, Torre Blake, Exhalants, The Black and White Years, The Clouds Are Ghosts, Lunar Gold, \n" + 
			"Tiarra Girls, Los Nahuatlatos, Hans Gruber And The Die Hards, Huerta Culture, \n" + 
			"Ali Holder, Carrie Fussell, Lolita Lynne, SMiiLE, Trouble in The Streets, Zettajoule, \n" + 
			"Crashing In With King Louie, \n" + 
			"Much 2 Much, Deer Fellow, \n" + 
			"Mayeux & Broussard, JD Clark, Cory Johnson, Soul Supporters, \n" + 
			"Exhalants, Hoaries, BULLS, Easy Prey, \n" + 
			"Ringo Deathstarr, Moving Panoramas, Why Bonnie, The Sour Notes, High Heavenns, \n" + 
			"Night Glitter, Deep Time, Blushing, Moist Flesh, Pataphysics, TV's Daniel, \n" + 
			"SoulXchange, JaRonMarshall, Loonar, John Wayne and the Possee, 80Rounds, \n" + 
			"DJ Sue, \n" + 
			"PRIMO, The Nimbus, Inseclude, \n" + 
			"Quin NFN, The Bishops, Infrared, Loony, Ben Buck, \n" + 
			"Tameca Jones, Shy Beast, Quin NFN, The Bishops, Como Las Movies, Infrared, Loony, Ben Buck, Drint, \n" + 
			"Vampyre, Borzoi, Pleasure Venom, Chromagnus, \n" + 
			"The Ripe and Loteria, \n" + 
			"Christian Bland & the Revelators, The Rotten Mangos, GO FEVER, The Stacks, and Mean Jolene, \n" + 
			"mj eazy, app juke, cabrini green, deezy lg, timmy thraxx, \n" + 
			"Communion, Ladykiller, Rickshaw Billie's, Monte Luna, \n" + 
			"Scorpion Child, The Well, Eagle Claw, Greenbeard, \n" + 
			"b l a c k i e, Black Mercy, Doula, Ingebrigt Haker Flaten, blk ops, \n" + 
			"Whiskey Shivers, Icing (Covering Cake), Madisons, The Oysters, Desilu, \n" + 
			"Ume, Calliope Musicals, blxpltn, Glaze, Teddy Glass, A. Sinclair, Fanclub, Marfa Crush, \n" + 
			"Knifight, Black Books, Hours Quiet, \n" + 
			"Swells, Night Cap, \n" + 
			"Distinguished Soundz, Anastasia, Interrobang, \n" + 
			"Jim Scarborough Band, \n" + 
			"DJ Orion Garcia, \n" + 
			"Future Museums, Sun June, Daphne Tunes, Oak Sun, Alex Dupree, Lizzie Buckley, \n" + 
			"Demi the Daredevil, Nick Shoulders, Love You B(r)unches, \n" + 
			"Nolan Potter’s Nightmare Band, Temple of Angels, Hidden Ritual, Borzoi, Ancient River, \n" + 
			"Harvest Thieves, Ben Ballinger, Otis Wilkins, \n" + 
			"Party Wolfe, Guacamole Police, Alex ALCO, Zinda, \n" + 
			"Thor & Friends, Cooper McBean, Jordan O'Jordan, Little Marzarn, \n" + 
			"Kathryn Legendre, Sour Bridges, Jomo & The Possum Posse, \n" + 
			"DJs No Kid$, Dandiggy Dutch\n";
			

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
