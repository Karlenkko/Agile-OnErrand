package Algorithm;

import Model.Intersection;
import Model.Request;
import Model.Segment;
import Model.Map;

import java.util.*;


public class MapGraph implements Graph {

	protected HashMap<Long, ArrayList<Long>> toAddresses;
	protected HashMap<Long, ArrayList<Double>> toDistances;
	protected HashMap<Long, Double> length;
	protected HashMap<Long, Long> precedent;
	protected HashMap<String, ArrayList<Long>> shortestPaths;
	protected ArrayList<Long> allAddresses;
	protected ArrayList<Long> gray = new ArrayList<>();
	protected ArrayList<Long> black = new ArrayList<>();

	protected HashMap<Long, Long> requestPairs;
	public static double minCost;
	public static HashMap<Long, Double> minHash;

	protected ArrayList<Long> recalculatedRequests;

	protected Double[][] costGraph;
	protected Double[][] newCostGraph;

	protected HashMap<String, ArrayList<Long>> newSolutions;

	public MapGraph(){
		toAddresses = new HashMap<>();
		toDistances = new HashMap<>();
		allAddresses = new ArrayList<>();
		shortestPaths = new HashMap<>();
		newSolutions= new HashMap<>();
		length = new HashMap<>();
		precedent = new HashMap<>();
		requestPairs = new HashMap<>();
		recalculatedRequests = new ArrayList<>();
		minCost = Double.MAX_VALUE;
		minHash = new HashMap<>();

	}

	public void reset() {
		toAddresses.clear();
		toDistances.clear();
		length.clear();
		precedent.clear();
		shortestPaths.clear();
		newSolutions.clear();
		allAddresses.clear();
		recalculatedRequests.clear();
		requestPairs.clear();
		minCost = 100000000;
		minHash.clear();
	}

	public void setRecalculatedRequests(ArrayList<Long> addRequestAddressList, LinkedList<Long> tour, Request newRequest) {
		Long before = addRequestAddressList.get(0);
		Long after = addRequestAddressList.get(3);
		this.requestPairs.put(newRequest.getPickup().getId(),newRequest.getDelivery().getId());
		recalculatedRequests.add(addRequestAddressList.get(0));
		recalculatedRequests.add(addRequestAddressList.get(1));
		recalculatedRequests.add(addRequestAddressList.get(2));
		boolean add = false;

		for (Long aLong : tour) {
			if (add) {
				recalculatedRequests.add(aLong);
			}
			if (aLong.equals(before)) {
				System.out.println("add");
				add = true;
			}
			if (aLong.equals(after)) {
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
		newCostGraph = new Double[recalculatedRequests.size()][recalculatedRequests.size()];
		for (Double[] doubles : newCostGraph) {
			Arrays.fill(doubles, -1.0);
		}

	}

	public void setRequests(List<Request> allRequests, Intersection depot) {
		allAddresses.add(depot.getId());
		for (Request r : allRequests) {
			allAddresses.add(r.getPickup().getId());
			allAddresses.add(r.getDelivery().getId());
			this.requestPairs.put(r.getPickup().getId(),r.getDelivery().getId());
		}

		costGraph = new Double[allAddresses.size()][allAddresses.size()];
		for (Double[] doubles : costGraph) {
			Arrays.fill(doubles, -1.0);
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

	public ArrayList<Long> getAllAddresses() {
		return allAddresses;
	}

	public Double[][] getCostGraph() {
		return costGraph;
	}

	public void dijkstra(boolean recalculate) {
		minCost = Double.MAX_VALUE;
		ArrayList<Long> requestsList;
		Double[][] requestGraph;
		HashMap<String, ArrayList<Long>> solutionList;
		if (recalculate) {
			requestsList = recalculatedRequests;
			requestGraph = newCostGraph;
			solutionList = newSolutions;
		} else {
			requestsList = allAddresses;
			requestGraph = costGraph;
			solutionList = shortestPaths;
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
					if(minCost > length.get(grayAddress)) {
						minCost = length.get(grayAddress);
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
		for (String id : shortestPaths.keySet()) {
			System.out.print(id+" ");
			for (Long l : shortestPaths.get(id)) {
				System.out.print(l+" ");
			}
			System.out.println();
		}
	}

	private Long getMinGray() {
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

	private void initial() {
		gray.clear();
		black.clear();
		length.clear();
		precedent.clear();
	}

	public HashMap<String, ArrayList<Long>> getShortestPaths(boolean recalculate) {
		if (recalculate)
			return newSolutions;
		return shortestPaths;
	}

	@Override
	public int getNbVertices(boolean recalculate) {
		if (recalculate)
			return recalculatedRequests.size();
		return allAddresses.size();
	}

	@Override
	public double getCost(long origin, long destination) {
		int i = allAddresses.indexOf(origin);
		int j = allAddresses.indexOf(destination);
		if (i == -1 || j == -1) {
			i = recalculatedRequests.indexOf(origin);
			j = recalculatedRequests.indexOf(destination);
			if (i<0 || i>=recalculatedRequests.size() || j<0 || j>=recalculatedRequests.size())
				return -1;
			return newCostGraph[i][j];
		}
		if (i<0 || i>=getNbVertices(false) || j<0 || j>=getNbVertices(false))
			return -1;

		return costGraph[i][j];
	}

	@Override
	public boolean isArc(long origin, long destination) {
//		System.out.println(origin+" "+destination);
		int i = allAddresses.indexOf(origin);
		int j = allAddresses.indexOf(destination);
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

	public ArrayList<Long> getAllAddresses(boolean recalculate){
		if (recalculate) {
			return recalculatedRequests;
		}
		return allAddresses;
	}

	public Long getDepotAddress(boolean recalculate) {
		if (recalculate) {
			return recalculatedRequests.get(0);
		}
		return allAddresses.get(0);
	}

	public boolean filter(Long nextVertex, Collection<Long> unvisited, boolean recalculate) {
		if (recalculate) {
			System.out.println(nextVertex+" "+unvisited.size());
			if (nextVertex-recalculatedRequests.get(recalculatedRequests.size()-1)==0 && unvisited.size()!=1){
				return false;
			}
		}

		if (requestPairs.containsKey(nextVertex)){
			return true;
		} else {
			for(Long origin : requestPairs.keySet()) {
				if(requestPairs.get(origin).equals(nextVertex)) {
					return !unvisited.contains(origin);
				}
			}
		}
		return true;
	}

}
