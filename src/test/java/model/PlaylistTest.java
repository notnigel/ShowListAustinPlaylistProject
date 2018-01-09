package model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import model.spotify.Artist;

public class PlaylistTest {

	Playlist playlist;
	
	@Mock
	ArrayList<Artist> artists;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

	}
	
	@Test
	public void testConvertDateFormat1() throws ParseException {
		playlist = new Playlist("2018January8", artists);
		assertEquals("January 08, 2018", playlist.convertDateFormat("2018January8"));
	}
	
	@Test
	public void testConvertDateFormat2() throws ParseException {
		playlist = new Playlist("2018January18", artists);
		assertEquals("January 18, 2018", playlist.convertDateFormat("2018January18"));
	}

}
