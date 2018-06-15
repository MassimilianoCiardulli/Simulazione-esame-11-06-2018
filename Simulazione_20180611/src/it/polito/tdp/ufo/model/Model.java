package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private List<AnnoCount> anniAvvistamenti ;
	private Graph<String, DefaultEdge> graph ;
	private List<String> stati ;
	
	private List<String> ottima ;
	
	public List<AnnoCount> getAnniAvvistamenti(){
		SightingsDAO dao = new SightingsDAO();
		this.anniAvvistamenti = dao.getAnni();
		return this.anniAvvistamenti;
	}
	
	public void createGraph(Year anno) {
		SightingsDAO dao = new SightingsDAO();

		graph = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		stati = dao.getStati(anno);
		
		Graphs.addAllVertices(this.graph, stati);
		
		for(String stato1 : graph.vertexSet()) {
			for(String stato2 : graph.vertexSet()) {
				if(!stato1.equals(stato2) && dao.esisteArco(stato1, stato2, anno))
					graph.addEdge(stato1, stato2);
			}
		}
	}
	
	public List<String> getStati(){
		return stati;
	}
	
	public List<String> getStatiPrecedenti(String stato) {
		return Graphs.predecessorListOf(this.graph, stato);
	}
	
	public List<String> getStatiSuccessivi(String stato) {
		return Graphs.successorListOf(this.graph, stato);
	}
	
	public List<String> getStatiRaggiungibili(String stato) {
		BreadthFirstIterator<String, DefaultEdge> bfv = new BreadthFirstIterator<>(this.graph, stato);
		List<String> raggiungibili = new ArrayList<>();
		bfv.next();
		while(bfv.hasNext()) {
			raggiungibili.add(bfv.next());
		}
		return raggiungibili ;
	}
	
	//SOLUZIONE PUNTO 2
	
	public List<String> getPercorsoMassimo(String stato){
		ottima = new ArrayList<String>();
		List<String> parziale = new ArrayList<>();
		parziale.add(stato);
		this.cercaSequenza(parziale);
		return ottima ;
	}
	
	private void cercaSequenza(List<String> parziale) {
		//caso terminale
		if(parziale.size()>ottima.size()) {
			ottima = new ArrayList<>(parziale) ;
		}
		
		//passo ricorsivo
		List<String> candidati = this.getStatiSuccessivi(parziale.get(parziale.size()-1));
		
		for(String prova : candidati) {
			if(!parziale.contains(prova)) {
				parziale.add(prova);
				cercaSequenza(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
	}
}
