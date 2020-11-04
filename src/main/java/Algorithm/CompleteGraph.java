package Algorithm;

import Model.Intersection;
import Model.Request;
import Model.Segment;
import Model.Map;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;


public class CompleteGraph implements Graph {

	protected HashMap<Long, ArrayList<Long>> toAddresses;
	protected HashMap<Long, ArrayList<Double>> toDistances;
	protected HashMap<Long, Double> length;
	protected HashMap<Long, Long> precedent;
	protected HashMap<String, ArrayList<Long>> solutions;
	protected ArrayList<Long> requests;
	protected ArrayList<Long> gray = new ArrayList<>();
	protected ArrayList<Long> black = new ArrayList<>();

	protected HashMap<Long, Long> allRequests;
	public static double min;

	protected Double[][] graph;

	public CompleteGraph(){
		toAddresses = new HashMap<>();
		toDistances = new HashMap<>();
		requests = new ArrayList<>();
		solutions = new HashMap<>();
		length = new HashMap<>();
		precedent = new HashMap<>();
		allRequests = new HashMap<>();
		min = 100000000;
	}

	public void reset() {
		toAddresses.clear();
		toDistances.clear();
		length.clear();
		precedent.clear();
		solutions.clear();
		requests.clear();
		allRequests.clear();
	}

	public void setRequests(ArrayList<Request> allRequests, Intersection depot) {
		requests.add(depot.getId());
		for (Request r : allRequests) {
			requests.add(r.getPickup().getId());
			requests.add(r.getDelivery().getId());
			this.allRequests.put(r.getPickup().getId(),r.getDelivery().getId());
		}

		graph = new Double[requests.size()][requests.size()];
		for (int i = 0; i < graph.length; ++i) {
			for (int j = 0; j < graph[i].length; ++j) {
				graph[i][j] = -1.0;
			}
		}
	}

	public void fillGraph(Map map){
		for (Segment segment : map.getAllSegments()) {
			if (!toAddresses.containsKey(segment.getOrigin().getId())) {
				toAddresses.put(segment.getOrigin().getId(), new ArrayList<>());
				toDistances.put(segment.getOrigin().getId(), new ArrayList<>());
			}
			toAddresses.get(segment.getOrigin().getId()).add(segment.getDestination().getId());
			toDistances.get(segment.getOrigin().getId()).add(segment.getLength());
		}
	}

	public void dijkstra() {
		int i = 1;
		for(Long origin : requests) {
			gray.add(origin);
			length.put(origin, 0.0);
			precedent.put(origin, -1L);
			while (gray.size() != 0) {
				Long grayAddress = getMinGray();
				gray.remove(grayAddress);
				if (! toAddresses.containsKey(grayAddress)) {
					black.add(grayAddress);
					continue;
				}
				for (Long destination : toAddresses.get(grayAddress)) {
					double newLength = length.get(grayAddress) + toDistances.get(grayAddress).get(toAddresses.get(grayAddress).indexOf(destination));
					if (length.containsKey(destination)) {
						if (newLength < length.get(destination)) {
							length.put(destination, newLength);
							precedent.put(destination, grayAddress);
						}
					} else {
						length.put(destination, newLength);
						precedent.put(destination, grayAddress);
						gray.add(destination);
					}
				}
				black.add(grayAddress);
				if (requests.contains(grayAddress)) {
					Long d = grayAddress;
					ArrayList<Long> route = new ArrayList<>();
					while (precedent.get(d) != -1L) {
						route.add(0, d);
						d = precedent.get(d);
					}
					route.add(0, d);
					solutions.put(origin + " " +grayAddress, route);
					if(min > length.get(grayAddress)) {
						min = length.get(grayAddress);
					}
					graph[requests.indexOf(origin)][requests.indexOf(grayAddress)] = length.get(grayAddress);

				}
				if (solutions.size() == requests.size() * i) {
					++ i;
					break;
				}
			}
			initial();
		}
		for (int i1 = 0; i1 < graph.length; i1++) {
			for (int i2 = 0; i2 < graph.length; i2++) {
				System.out.print(graph[i1][i2] + " ");
			}
			System.out.println("\n");
		}
		System.out.println(solutions.size());
	}

	public void show() {
		for (String id : solutions.keySet()) {
			System.out.print(id+" ");
			for (Long l : solutions.get(id)) {
				System.out.print(l+" ");
			}
			System.out.println();
		}
	}

	public Long getMinGray() {
		Double min = length.get(gray.get(0));
		Long id = gray.get(0);
		for (int i = 1; i < gray.size(); ++i) {
			if (min > length.get(gray.get(i))) {
				min = length.get(gray.get(i));
				id = gray.get(i);
			}
		}
		return id;
	}

	public void initial() {
		gray.clear();
		black.clear();
		length.clear();
		precedent.clear();
	}

	public HashMap<String, ArrayList<Long>> getSolutions() {
		return solutions;
	}

	@Override
	public int getNbVertices() {
		return requests.size();
	}

	@Override
	public double getCost(long origin, long destination) {
		int i = requests.indexOf(origin);
		int j = requests.indexOf(destination);
		if (i<0 || i>=getNbVertices() || j<0 || j>=getNbVertices())
			return -1;
		return graph[i][j];
	}

	@Override
	public boolean isArc(long origin, long destination) {
//		System.out.println(origin+" "+destination);
		int i = requests.indexOf(origin);
		int j = requests.indexOf(destination);
		if (i<0 || i>=getNbVertices() || j<0 || j>=getNbVertices())
			return false;
		return i != j;
	}

	public ArrayList<Long> getAllAddresses(){
		return requests;
	}

	public Long getDepotAddress() {
		return requests.get(0);
	}

	public boolean filter(Long nextVertex, Collection<Long> unvisited) {
		if (allRequests.containsKey(nextVertex)){
			return true;
		} else {
			for(Long origin : allRequests.keySet()) {
				if(allRequests.get(origin) == nextVertex) {
					if (unvisited.contains(origin))
						return false;
					else
						return true;
				}
			}
		}
		return true;
	}

}
