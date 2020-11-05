package Algorithm;

import Model.Intersection;
import Model.Request;
import Model.Segment;
import Model.Map;

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
	public static HashMap<Long, Double> minHash;

	protected ArrayList<Long> recalculatedRequests;

	protected Double[][] graph;
	protected Double[][] newgraph;

	protected HashMap<String, ArrayList<Long>> newSolutions;

	public CompleteGraph(){
		toAddresses = new HashMap<>();
		toDistances = new HashMap<>();
		requests = new ArrayList<>();
		solutions = new HashMap<>();
		newSolutions= new HashMap<>();
		length = new HashMap<>();
		precedent = new HashMap<>();
		allRequests = new HashMap<>();
		recalculatedRequests = new ArrayList<>();
		min = 100000000;
		minHash = new HashMap<>();

	}

	public void reset() {
		toAddresses.clear();
		toDistances.clear();
		length.clear();
		precedent.clear();
		solutions.clear();
		newSolutions.clear();
		requests.clear();
		recalculatedRequests.clear();
		allRequests.clear();
		min = 100000000;
		minHash.clear();
	}

	public void setRecalculatedRequests(ArrayList<Long> addRequestList, LinkedList<Long> tour,Request newRequest) {
		Long before = addRequestList.get(0);
		Long after = addRequestList.get(3);
		this.allRequests.put(newRequest.getPickup().getId(),newRequest.getDelivery().getId());
		recalculatedRequests.add(addRequestList.get(0));
		recalculatedRequests.add(addRequestList.get(1));
		recalculatedRequests.add(addRequestList.get(2));
		boolean add = false;

		int x = 0;
		for (int i = 0; i < tour.size(); ++i) {
			if (add) {
				recalculatedRequests.add(tour.get(i));
				x++;
			}
			if (tour.get(i)-before == 0) {
				System.out.println("add");
				add = true;
			}
			if (tour.get(i)-after == 0) {
				add = false;
			}
		}

		/*
		for (int i = 1; i < recalculatedRequests.size()-1; ++i) {
			this.newAllRequests.put(recalculatedRequests.get(0),recalculatedRequests.get(i));
			this.newAllRequests.put(recalculatedRequests.get(i),recalculatedRequests.get(recalculatedRequests.size()-1));
		}
		this.newAllRequests.put(recalculatedRequests.get(0),recalculatedRequests.get(recalculatedRequests.size()-1));
		 */
		newgraph = new Double[recalculatedRequests.size()][recalculatedRequests.size()];
		for (int i = 0; i < newgraph.length; ++i) {
			for (int j = 0; j < newgraph[i].length; ++j) {
				newgraph[i][j] = -1.0;
			}
		}

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

	public ArrayList<Long> getRequests() {
		return requests;
	}

	public Double[][] getGraph() {
		return graph;
	}

	public void dijkstra(boolean recalcul) {
		min = 1000000;
		ArrayList<Long> requestsList = new ArrayList<>();
		Double[][] requestGraph;
		HashMap<String, ArrayList<Long>> solutionList = new HashMap<>();
		if (recalcul) {
			requestsList = recalculatedRequests;
			requestGraph = newgraph;
			solutionList = newSolutions;
		} else {
			requestsList = requests;
			requestGraph = graph;
			solutionList = solutions;
		}
		for(Long origin : requestsList) {
			int i = 0;
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
				if (requestsList.contains(grayAddress)) {
					System.out.println(origin + " " +grayAddress);
					Long d = grayAddress;
					ArrayList<Long> route = new ArrayList<>();
					while (precedent.get(d) != -1L) {
						route.add(0, d);
						d = precedent.get(d);
					}
					route.add(0, d);
					solutionList.put(origin + " " +grayAddress, route);
					if(min > length.get(grayAddress)) {
						min = length.get(grayAddress);
					}
					//updateMinHash(grayAddress, length.get(grayAddress));
					requestGraph[requestsList.indexOf(origin)][requestsList.indexOf(grayAddress)] = length.get(grayAddress);
					++i;
				}
				if (i == requestsList.size()) {
					System.out.println("break");
					break;
				}
			}
			initial();
		}

	}


	public void updateMinHash(Long id, Double length) {
		if (minHash.containsKey(id)) {
			if (minHash.get(id) > length) {
				minHash.put(id, length);
			}
		} else {
			minHash.put(id, length);
		}
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

	public HashMap<String, ArrayList<Long>> getSolutions(boolean recalcul) {
		if (recalcul)
			return newSolutions;
		return solutions;
	}

	@Override
	public int getNbVertices(boolean recalul) {
		if (recalul)
			return recalculatedRequests.size();
		return requests.size();
	}

	@Override
	public double getCost(long origin, long destination) {
		int i = requests.indexOf(origin);
		int j = requests.indexOf(destination);
		if (i == -1 || j == -1) {
			i = recalculatedRequests.indexOf(origin);
			j = recalculatedRequests.indexOf(destination);
			if (i<0 || i>=recalculatedRequests.size() || j<0 || j>=recalculatedRequests.size())
				return -1;
			return newgraph[i][j];
		}
		if (i<0 || i>=getNbVertices(false) || j<0 || j>=getNbVertices(false))
			return -1;

		return graph[i][j];
	}

	@Override
	public boolean isArc(long origin, long destination) {
//		System.out.println(origin+" "+destination);
		int i = requests.indexOf(origin);
		int j = requests.indexOf(destination);
		if (i == -1 || j == -1) {
			i = recalculatedRequests.indexOf(origin);
			j = recalculatedRequests.indexOf(destination);
			if (i<0 || i>=recalculatedRequests.size() || j<0 || j>=recalculatedRequests.size())
				return false;
			return i != j;
		}
		if (i<0 || i>=getNbVertices(false) || j<0 || j>=getNbVertices(false))
			return false;
		return i != j;
	}

	public ArrayList<Long> getAllAddresses(boolean recalcule){
		if (recalcule) {
			return recalculatedRequests;
		}
		return requests;
	}

	public Long getDepotAddress(boolean recalcule) {
		if (recalcule) {
			return recalculatedRequests.get(0);
		}
		return requests.get(0);
	}

	public boolean filter(Long nextVertex, Collection<Long> unvisited, boolean recalcul) {
		if (recalcul) {
			System.out.println(nextVertex+" "+unvisited.size());
			if (nextVertex-recalculatedRequests.get(recalculatedRequests.size()-1)==0 && unvisited.size()!=1){
				return false;
			}
		}

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
