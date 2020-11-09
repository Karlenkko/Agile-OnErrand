package Algorithm;

import Model.Intersection;
import Model.Request;
import Model.Segment;
import Model.Map;

import java.util.*;


public class MapGraph implements Graph {

	protected HashMap<Long, ArrayList<Long>> intersectionToIntersections;
	protected HashMap<Long, ArrayList<Double>> intersectionToIntersectionDistances;
	protected HashMap<Long, Double> toIntersectionCosts;
	protected HashMap<Long, Long> intersectionPrecedents;
	protected HashMap<String, ArrayList<Long>> shortestPaths;
	protected ArrayList<Long> allAddresses;
	protected ArrayList<Long> gray = new ArrayList<>();
	protected ArrayList<Long> black = new ArrayList<>();

	protected HashMap<Long, Long> requestPairs;
	protected double minCost;
	protected HashMap<Long, Double> minHash;

	protected ArrayList<Long> recalculatedRequests;

	protected double[][] costGraph;
	protected double[][] newCostGraph;

	protected HashMap<String, ArrayList<Long>> newShortestPaths;

	public double getMinCost() {
		return minCost;
	}

	public HashMap<Long, Double> getMinHash() {
		return minHash;
	}

	public MapGraph(){
		intersectionToIntersections = new HashMap<>();
		intersectionToIntersectionDistances = new HashMap<>();
		allAddresses = new ArrayList<>();
		shortestPaths = new HashMap<>();
		newShortestPaths = new HashMap<>();
		toIntersectionCosts = new HashMap<>();
		intersectionPrecedents = new HashMap<>();
		requestPairs = new HashMap<>();
		recalculatedRequests = new ArrayList<>();
		minCost = Double.MAX_VALUE;
		minHash = new HashMap<>();

	}

	public void reset() {
		intersectionToIntersections.clear();
		intersectionToIntersectionDistances.clear();
		toIntersectionCosts.clear();
		intersectionPrecedents.clear();
		shortestPaths.clear();
		newShortestPaths.clear();
		allAddresses.clear();
		recalculatedRequests.clear();
		requestPairs.clear();
		minCost = Double.MAX_VALUE;
		minHash.clear();
		initial();
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
			if (add && (!recalculatedRequests.contains(aLong))) {
				recalculatedRequests.add(aLong);
			}
			if (aLong.equals(before)) {
				add = true;
			}
			if (aLong.equals(after)) {
				add = false;
			}
		}
		if (addRequestAddressList.get(3) == tour.get(0)) {
			recalculatedRequests.add(addRequestAddressList.get(3));
		}

		newCostGraph = new double[recalculatedRequests.size()][recalculatedRequests.size()];
		for (double[] doubles : newCostGraph) {
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

		costGraph = new double[allAddresses.size()][allAddresses.size()];
		for (double[] doubles : costGraph) {
			Arrays.fill(doubles, -1.0);
		}
	}

	public void fillGraph(Map map){
		for (Segment segment : map.getAllSegments()) {
			if (!intersectionToIntersections.containsKey(segment.getOrigin().getId())) {
				intersectionToIntersections.put(segment.getOrigin().getId(), new ArrayList<>());
				intersectionToIntersectionDistances.put(segment.getOrigin().getId(), new ArrayList<>());
			}
			intersectionToIntersections.get(segment.getOrigin().getId()).add(segment.getDestination().getId());
			intersectionToIntersectionDistances.get(segment.getOrigin().getId()).add(segment.getLength());
		}
	}

//	public ArrayList<Long> getAllAddresses() {
//		return allAddresses;
//	}

//	public double[][] getCostGraph() {
//		return costGraph;
//	}

	public void dijkstra(boolean recalculate) {
		minCost = Double.MAX_VALUE;
		ArrayList<Long> requestsList;
		double[][] requestGraph;
		HashMap<String, ArrayList<Long>> solutionList;
		if (recalculate) {
			requestsList = recalculatedRequests;
			requestGraph = newCostGraph;
			solutionList = newShortestPaths;

		} else {
			requestsList = allAddresses;
			requestGraph = costGraph;
			solutionList = shortestPaths;
		}
		for(Long origin : requestsList) {
			int i = 0;
			gray.add(origin);
			toIntersectionCosts.put(origin, 0.0);
			intersectionPrecedents.put(origin, -1L);
			while (gray.size() != 0) {
				Long grayAddress = getMinGray();
				gray.remove(grayAddress);
				if (! intersectionToIntersections.containsKey(grayAddress)) {
					black.add(grayAddress);
					continue;
				}
				for (Long destination : intersectionToIntersections.get(grayAddress)) {
					double newLength =
							toIntersectionCosts.get(grayAddress) +
									intersectionToIntersectionDistances.
											get(grayAddress).
											get(intersectionToIntersections.
													get(grayAddress).indexOf(destination));
					if (toIntersectionCosts.containsKey(destination)) {
						if (newLength < toIntersectionCosts.get(destination)) {
							toIntersectionCosts.put(destination, newLength);
							intersectionPrecedents.put(destination, grayAddress);
						}
					} else {
						toIntersectionCosts.put(destination, newLength);
						intersectionPrecedents.put(destination, grayAddress);
						gray.add(destination);
					}
				}
				black.add(grayAddress);
				if (requestsList.contains(grayAddress)) {
					// System.out.println(origin + " " +grayAddress);
					Long d = grayAddress;
					ArrayList<Long> route = new ArrayList<>();
					while (intersectionPrecedents.get(d) != -1L) {
						route.add(0, d);
						d = intersectionPrecedents.get(d);
					}
					route.add(0, d);
					solutionList.put(origin + " " +grayAddress, route);
					if (toIntersectionCosts.get(grayAddress) != 0) {
						if(minCost > toIntersectionCosts.get(grayAddress)) {
							minCost = toIntersectionCosts.get(grayAddress);
						}
						updateMinHash(grayAddress, toIntersectionCosts.get(grayAddress));
					}
					requestGraph[requestsList.indexOf(origin)][requestsList.indexOf(grayAddress)] = toIntersectionCosts.get(grayAddress);
					++i;
				}
				if (i == requestsList.size()) {
					break;
				}
			}
			initial();
		}
	}


	private void updateMinHash(Long id, Double length) {
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
		Double min = toIntersectionCosts.get(gray.get(0));
		Long id = gray.get(0);
		for (int i = 1; i < gray.size(); ++i) {
			if (min > toIntersectionCosts.get(gray.get(i))) {
				min = toIntersectionCosts.get(gray.get(i));
				id = gray.get(i);
			}
		}
		return id;
	}

	private void initial() {
		gray.clear();
		black.clear();
		toIntersectionCosts.clear();
		intersectionPrecedents.clear();
	}

	public HashMap<String, ArrayList<Long>> getShortestPaths(boolean recalculate) {
		if (recalculate)
			return newShortestPaths;
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

	public Long getStartAddress(boolean recalculate) {
		if (recalculate) {
			return recalculatedRequests.get(0);
		}
		return allAddresses.get(0);
	}

	public boolean filter(Long nextVertex, Collection<Long> unvisited, boolean recalculate) {
		if (recalculate) {
//			System.out.println(nextVertex+" "+unvisited.size());
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

	@Override
	public void updateGraph() {
		shortestPaths.putAll(newShortestPaths);
		int size = costGraph.length + 2;
		double[][] tempCostGraph = new double[size][size];
		for(int i = 0; i < costGraph.length; ++i) {
			for (int j = 0; j < costGraph[i].length; ++j) {
				tempCostGraph[i][j] = costGraph[i][j];
			}
		}

		for(int i = 0; i < newCostGraph.length; ++i) {
			for (int j = 0; j < newCostGraph[i].length; ++j) {
				int indexOrigin = 0;
				if (i == 1 || i == 2) {
					indexOrigin = i + costGraph.length - 1;
				}
				if (allAddresses.contains(recalculatedRequests.get(i))) {
					indexOrigin = allAddresses.indexOf(recalculatedRequests.get(i));
				}

				int indexDestination = 0;
				if (j == 1 || j == 2) {
					indexDestination = j + costGraph.length - 1;
				}
				if (allAddresses.contains(recalculatedRequests.get(j))) {
					indexDestination = allAddresses.indexOf(recalculatedRequests.get(j));
				}

				tempCostGraph[indexOrigin][indexDestination] = newCostGraph[i][j];

			}
		}

		for (int i = 0; i < costGraph.length; ++i) {

			if (tempCostGraph[i][costGraph.length] == 0.0){
				tempCostGraph[i][costGraph.length] = dijkstra(allAddresses.get(i), recalculatedRequests.get(1));
			}
			if (tempCostGraph[costGraph.length][i] == 0.0){
				tempCostGraph[costGraph.length][i] = dijkstra(recalculatedRequests.get(1), allAddresses.get(i));
			}
		}

		costGraph = tempCostGraph;

		allAddresses.add(recalculatedRequests.get(1));
		allAddresses.add(recalculatedRequests.get(2));

		recalculatedRequests.clear();
		newShortestPaths.clear();

	}

	public Long getDelivery(Long pickup) {
		return requestPairs.get(pickup);
	}

	public List<Long> getRoute(Long[] bestSolAddress) {
		LinkedList<Long> bestSolIntersection = new LinkedList<>();
		for (int i = 1; i < bestSolAddress.length; i++) {
			System.out.println(bestSolAddress[i-1]+" "+bestSolAddress[i]);
			bestSolIntersection.addAll(getShortestPaths(false).get(bestSolAddress[i-1]+" "+bestSolAddress[i]));
			bestSolIntersection.remove(bestSolIntersection.size() - 1);
		}
		bestSolIntersection.add(bestSolAddress[bestSolAddress.length-1]);
		return  bestSolIntersection;
	}

	public double[] getSolutionCost(Long[] bestSolAddress) {
		double[] bestSolAddressCost = new double[bestSolAddress.length];
		for (int i = 0; i < bestSolAddress.length; i ++) {
			bestSolAddressCost[i] = getCost(bestSolAddress[i], bestSolAddress[(i + 1 >= bestSolAddress.length? 0 : i + 1)]);
		}
		return bestSolAddressCost;
	}

	public void showPaths() {
		for (String s : shortestPaths.keySet()) {
			System.out.println(s+" : "+shortestPaths.get(s).toString());
		}

	}

	public double dijkstra(Long origin, Long destination) {
		minCost = Double.MAX_VALUE;
		initial();
		int i = 0;
		gray.add(origin);
		toIntersectionCosts.put(origin, 0.0);
		intersectionPrecedents.put(origin, -1L);
		while (gray.size() != 0) {
			Long grayAddress = getMinGray();
			gray.remove(grayAddress);
			if (!intersectionToIntersections.containsKey(grayAddress)) {
				black.add(grayAddress);
				continue;
			}
			for (Long d : intersectionToIntersections.get(grayAddress)) {
				double newLength =
						toIntersectionCosts.get(grayAddress) +
								intersectionToIntersectionDistances.
										get(grayAddress).
										get(intersectionToIntersections.
												get(grayAddress).indexOf(d));
				if (toIntersectionCosts.containsKey(d)) {
					if (newLength < toIntersectionCosts.get(d)) {
						toIntersectionCosts.put(d, newLength);
						intersectionPrecedents.put(d, grayAddress);
					}
				} else {
					toIntersectionCosts.put(d, newLength);
					intersectionPrecedents.put(d, grayAddress);
					gray.add(d);
				}
			}
			black.add(grayAddress);
			if (destination.equals(grayAddress)) {
				Long d = grayAddress;
				ArrayList<Long> route = new ArrayList<>();
				while (intersectionPrecedents.get(d) != -1L) {
					route.add(0, d);
					d = intersectionPrecedents.get(d);
				}
				route.add(0, d);
				shortestPaths.put(origin + " " + grayAddress, route);
				if (toIntersectionCosts.get(grayAddress) != 0) {
					if (minCost > toIntersectionCosts.get(grayAddress)) {
						minCost = toIntersectionCosts.get(grayAddress);
					}
					updateMinHash(grayAddress, toIntersectionCosts.get(grayAddress));
				}
				return toIntersectionCosts.get(grayAddress);
			}

		}
		return  0;
	}

}
