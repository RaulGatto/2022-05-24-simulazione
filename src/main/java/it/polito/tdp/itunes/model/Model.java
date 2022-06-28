package it.polito.tdp.itunes.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Track, DefaultWeightedEdge> graph;
	private Map<Integer, Track> idMapTracks;
	private List<Track> vertici;
	private ItunesDAO dao;
	private List<Adiacenza> adiacenze;
	private List<Track> best;
	
	
	public Model() {
		this.dao = new ItunesDAO();
		this.idMapTracks = new HashMap<>();
	}
	
	public String creaGrafo(Genre genre) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = this.dao.getTracksByGenre(idMapTracks, genre);
		
		Graphs.addAllVertices(this.graph, vertici);
		
		this.adiacenze = this.dao.getAllAdiacenzeByGenre(idMapTracks, genre);
		
		for(Adiacenza a : adiacenze) {
			if(!this.graph.containsEdge(this.graph.getEdge(a.getT1(), a.getT2()))) {
				Graphs.addEdge(this.graph, a.getT1(), a.getT2(), a.getPeso());
			}
		}
		
		return String.format("E stato creato un grafo con %d vertici e %d archi", this.graph.vertexSet().size(), this.graph.edgeSet().size());
	}

	public List<Adiacenza> getDurataMassima(){
		double durata = Double.MIN_VALUE;
		List<Adiacenza> result = new LinkedList<>();
		
		for(Adiacenza a : adiacenze) {
			if(a.getPeso() > durata) {
				durata = a.getPeso();
			}
		}
		
		for(Adiacenza a : adiacenze) {
			if(a.getPeso() == durata) {
				result.add(a);
			}
		}
		
		return result;
	}
	
	public List<Track> trovaPercorso(Track tStart, Integer memoriaMassima) {
		List<Track> parziale = new LinkedList<>();
		best = new LinkedList<>();
		Integer memoriaOccupata = 0;
		parziale.add(tStart);
		memoriaOccupata += tStart.getBytes();
		ConnectivityInspector<Track, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.graph);
		List<Track> componenteConn = new LinkedList<>(ci.connectedSetOf(tStart));
		avviaRicorsione(parziale, memoriaMassima, memoriaOccupata, componenteConn);
		return best;
	}
	
	

	private void avviaRicorsione(List<Track> parziale, Integer memoriaMassima, Integer memoriaOccupata,
			List<Track> componenteConn) {
		
		if(memoriaOccupata <= memoriaMassima) {
			if(parziale.size() > best.size()) {
				best = new LinkedList<>(parziale);
			}
		}
		
		if(memoriaOccupata > memoriaMassima) {
			return;
		}
		
		if(parziale.size() == componenteConn.size()) {
			return;
		}
		
		for(Track t : componenteConn) {
			if(!parziale.contains(t)) {
				parziale.add(t);
				memoriaOccupata += t.getBytes();
				avviaRicorsione(parziale, memoriaMassima, memoriaOccupata, componenteConn);
				parziale.remove(t);
				memoriaOccupata -= t.getBytes();
			}
		}
	}

	public List<Genre> getAllGenres() {
		// TODO Auto-generated method stub
		return this.dao.getAllGenres();
	}

	public List<Track> getVertici() {
		return vertici;
	}
	
}
