package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.itunes.model.Adiacenza;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {

	public List<Album> getAllAlbums() {
		final String sql = "SELECT * FROM Album";
		List<Album> result = new LinkedList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Artist> getAllArtists() {
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Playlist> getAllPlaylists() {
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Track> getAllTracks() {
		final String sql = "SELECT * FROM Track";
		List<Track> result = new ArrayList<Track>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), res.getString("Composer"),
						res.getInt("Milliseconds"), res.getInt("Bytes"), res.getDouble("UnitPrice")));

			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Genre> getAllGenres() {
		final String sql = "SELECT DISTINCT * FROM Genre";
		List<Genre> result = new LinkedList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<MediaType> getAllMediaTypes() {
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Track> getTracksByGenre(Map<Integer, Track> idMapTracks, Genre genre) {
		final String sql = "SELECT * " + "FROM track " + "WHERE GenreId = ?";

		List<Track> result = new ArrayList<Track>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, genre.getGenreId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if (!idMapTracks.containsKey(res.getInt("TrackID"))) {
					Track t = new Track(res.getInt("TrackId"), res.getString("Name"), res.getString("Composer"),
							res.getInt("Milliseconds"), res.getInt("Bytes"), res.getDouble("UnitPrice"));
					result.add(t);
					idMapTracks.put(t.getTrackId(), t);
				} else {
					Track t = idMapTracks.get(res.getInt("TrackID"));
					result.add(t);
				}

			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Adiacenza> getAllAdiacenzeByGenre(Map<Integer, Track> idMapTracks, Genre g) {
		final String sql = "SELECT t1.TrackId AS t1, t2.TrackId AS t2, ABS(t1.Milliseconds - t2.Milliseconds) AS peso "
				+ "FROM track t1, track t2 "
				+ "WHERE t1.MediaTypeId = t2.MediaTypeId AND t1.TrackId > t2.TrackId AND t1.GenreId = t2.GenreId AND t1.GenreId = ? "
				+ "GROUP BY t1, t2";
		List<Adiacenza> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, g.getGenreId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Track t1 = idMapTracks.get(res.getInt("t1"));
				Track t2 = idMapTracks.get(res.getInt("t2"));
				if (t1 == null || t2 == null) {
					System.out.println("DB non omogeneo");
				} else {
					Adiacenza adj = new Adiacenza(t1, t2, res.getDouble("peso"));
					result.add(adj);
				}

			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;

	}
}
