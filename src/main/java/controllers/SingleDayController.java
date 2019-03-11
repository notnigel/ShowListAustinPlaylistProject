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
import controllers.webrequest.spotify.CreatePlaylistRequestClient;
import controllers.webrequest.spotify.TopTrackRequestClient;
import model.Playlist;
import model.spotify.Artist;

public class SingleDayController {

    private static Playlist playlist;

    public static void main(String[] args) {

        String playlistName = "SXSW Unofficial: Thursday Shows";
        String searchDate = "2019March14";
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
                    rawEvent = rawEvent.replaceAll("with", ",");
                    String[] bands = rawEvent.split("(,|\\n)");
                    for (int i = 0; i < bands.length; i++) {
                        bands[i] = bands[i].replaceAll("\\(.+\\)", "");
                        bands[i] = bands[i].replaceAll("#", "");
                        bands[i] = bands[i].replaceAll(".*(p|P)resents(:)?", "");
                        bands[i] = bands[i].replaceAll(".*:", "");
                        bands[i] = bands[i].replaceAll(".*(featuring)", "");
                        bands[i] = bands[i].trim();
                    }

                    //Create playlist
                    ArrayList<Artist> artists = new ArrayList<Artist>();

                    for (String band : bands) {
                        Artist a = new Artist();
                        if(!band.equals("")) {
                            a.setName(band);
                            artists.add(a);
                        }
                    }

                    //Retrieve artist ids
                    String artistId = "";
                    for (int i = 0; i <artists.size(); i++) {
                        artistId = ArtistRequestClient.run(artists.get(i).getName());
                        if(artistId != null && !artistId.equals("")) {
                            artists.get(i).setId(artistId);
                        }
                    }

                    //Remove artists without Spotify artist IDs
                    for (Iterator<Artist> iterator = artists.iterator(); iterator.hasNext(); ) {
                        Artist a = iterator.next();
                        if(a.getId() == null){
                            iterator.remove();
                        }
                    }

                    //Retrieve artist URIs
                    String topTrackUri = "";
                    for (int i = 0; i <artists.size(); i++) {
                        topTrackUri = TopTrackRequestClient.run(artists.get(i).getId());
                        if(topTrackUri != null && !topTrackUri.equals("")) {
                            artists.get(i).setTrackUris(topTrackUri);
                        }
                    }

                    //Remove empty URIs or duplicate URIs
                    Set<String> attributes = new HashSet<String>();
                    List duplicates = new ArrayList<Artist>();

                    for(Artist a : artists) {
                        if(attributes.contains(a.getTrackUris())) {
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
                        playlists.add(new Playlist(artists1, playlistName));
                        artists.subList(0, 100).clear();
                    }

                    playlists.add(new Playlist(artists, playlistName));
                    String playlistId = CreatePlaylistRequestClient.run(playlists.get(0));

                    for (int i = 0; i < playlists.size(); i++) {
                        playlists.get(i).setId(playlistId);
                        AddTracksRequestClient.run(playlists.get(i));
                    }

                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            client.close();
        }

    }

}