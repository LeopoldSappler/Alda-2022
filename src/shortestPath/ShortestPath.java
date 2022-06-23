// O. Bittel;
// 01.04.2021

package shortestPath;

import graph.*;
import sim.SYSimulation;

import java.awt.*;
import java.util.*;
import java.util.List;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ

	Heuristic<V> heuristic;
	DirectedGraph<V> directedGraph;
	private V start;
	private V end;
	private boolean shortestPathCalculated = false; // true, wenn shortestPath ausgefuehrt wird



	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		directedGraph = g;
		heuristic = h;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		start = s;
		end = g;
		if (heuristic == null) {
			dijkstraAlgorithm(s);
		} else
			aStarAlgorithm(s, g);
		shortestPathCalculated = true;
	}

	private void dijkstraAlgorithm(V s) {
		cand = new IndexMinPQ<>();

		for (V v: directedGraph.getVertexSet()) {
			dist.put(v, Double.POSITIVE_INFINITY); //Einfuegen aller unbesuchten Knoten mit Distanz = Unendlich
			pred.put(v, null); // Predecessor noch nicht bekannt, also null einfuegen
		}

		dist.put(s, 0.0); //Einfuegen Startknoten mit Distanz 0
		cand.add(s, 0.0); //Einfuegen Startknoten Prioritaet 0

		while (!cand.isEmpty()) {
			V v = cand.removeMin(); //Loeschen des Knotens mit niedrigster Prioritaet und Speichern in v

			if (sim != null) //Wenn sim nicht null, dann wird der Knoten gezeichnet
				sim.visitStation((int) v, Color.BLUE);

			for (var w: directedGraph.getPredecessorVertexSet(v)) { //Gehe alle Vorgaengerknoten von v durch, also Knoten die eine Verbindung zu v haben
				if (dist.get(w).equals(Double.POSITIVE_INFINITY)) { //wenn Vorgaengerknoten w Distanz == unendlich
					pred.put(w, v); // Updaten der Vorgaengerknotenliste
					dist.put(w, dist.get(v) + directedGraph.getWeight(v, w)); //Updaten der Distanzliste mit dem kuerzesten Weg
					cand.add(w, dist.get(w)); //Hinzufuegen von w zur Kandidatenliste
				} else if (dist.get(v) + directedGraph.getWeight(v, w) < dist.get(w)) {
					pred.put(w, v); //Einfuegen in Predecessorliste
					dist.put(w, dist.get(v) + directedGraph.getWeight(v, w)); //kuerzester Weg nach w nun ueber v
					cand.change(w, dist.get(w)); //w schon in der Kandidatenliste, deswegen change() statt add()
				}
			}
		}
	}

	private void aStarAlgorithm(V s, V z) {
		cand = new IndexMinPQ<>();

		for (V v: directedGraph.getVertexSet()) {
			dist.put(v, Double.POSITIVE_INFINITY); //Einfuegen aller unbesuchten Knoten mit Distanz = Unendlich
			pred.put(v, null); // Predecessor noch nicht bekannt, also null einfuegen
		}

		dist.put(s, 0.0); //Einfuegen Startknoten mit Distanz 0
		cand.add(s, 0 + heuristic.estimatedCost(s, z)); //Einfuegen Startknoten mit Prioritaet estimated Cost

		while (!cand.isEmpty()) {
			V v = cand.removeMin();
			if (v == z) return; //Startknoten == Endknoten, also fertig

			if (sim != null) //Wenn sim nicht null, dann wird der Knoten gezeichnet
				sim.visitStation((int) v, Color.GREEN);

			for (var w: directedGraph.getPredecessorVertexSet(v)) { //Gehe alle Vorgaengerknoten von v durch, also Knoten die eine Verbindung zu v haben
				if (dist.get(w).equals(Double.POSITIVE_INFINITY)) { //wenn Vorgaengerknoten w Distanz == unendlich
					pred.put(w, v); // Updaten der Vorgaengerknotenliste
					dist.put(w, dist.get(v) + directedGraph.getWeight(v, w)); //Updaten der Distanzliste mit dem kuerzesten Weg
					cand.add(w, dist.get(w) + heuristic.estimatedCost(w, z)); //Hinzufuegen von w zur Kandidatenliste mit estimated Cost von w zu Endknoten
				} else if (dist.get(v) + directedGraph.getWeight(v, w) < dist.get(w)) {
					pred.put(w, v); //Einfuegen in Predecessorliste
					dist.put(w, dist.get(v) + directedGraph.getWeight(v, w)); //kuerzester Weg nach w nun ueber v
					cand.change(w, dist.get(w) + heuristic.estimatedCost(w, z)); //w schon in der Kandidatenliste, estimated Cost von w zu Endkosten
				}
			}
		}
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		if (!shortestPathCalculated) throw new IllegalArgumentException();
		List<V> shortestPath = new LinkedList<>();

		V next = end;
		while (next != start) {
			shortestPath.add(next);
			next = pred.get(next);
		}
		shortestPath.add(start);
		Collections.reverse(shortestPath);
		return Collections.unmodifiableList(shortestPath);
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		if (!shortestPathCalculated) throw new IllegalArgumentException();
		return dist.get(end);
	}

}
