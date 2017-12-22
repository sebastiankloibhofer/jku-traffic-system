package controlsystem.scheduling;

import controlsystem.model.Edge;
import controlsystem.model.GraphPart;
import controlsystem.model.Node;
import controlsystem.model.Route;
import trafficParticipants.street.Lane;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

public class RoutePlanner implements Callable<Route> {

    // TODO approximate better / check in testing
    /** Default timeout for this kind of task. */
    public static final long TIMEOUT = 30000;

    private final GraphPart src;
    private final GraphPart dst;
    private final Node srcNode, destNode;
    ConcurrentMap<Integer, Node> crossings;
    ConcurrentMap<Integer, Edge> lanes;

    private Set<Node> settled;
    private Set<Node> unSettled;
    private Map<Node, Node> predecessors;
    private Map<Node, Double> dist;


    public RoutePlanner(ConcurrentMap<Integer, Node> crossings, ConcurrentMap<Integer, Edge> lanes, GraphPart src, GraphPart dst) {
        this.crossings = crossings;
        this.lanes = lanes;
        this.src = src;
        this.dst = dst;

        if(src instanceof Node) {
            srcNode = crossings.get(src.getId());
        } else if (src instanceof Lane) {
            srcNode = crossings.get(((Lane)src).getEnd().getId());
        } else {
            throw new RuntimeException("should not happen, src must either be Crossing or Lane");
        }

        if(dst instanceof Node) {
            destNode = crossings.get(dst.getId());
        } else if (src instanceof Lane) {
            destNode = crossings.get(((Lane)src).getStart().getId());
        } else {
            throw new RuntimeException("should not happen, dst must either be Node/Crossing or Lane");
        }
    }


    @Override
    public Route call() throws Exception {
        // calculate path from current traffic situation
        // TODO: Optimization, store derived paths and don't recalculate every time
        calcShortestPaths(srcNode);
        return getPath(destNode);
    }

    /** Dijkstra - Implementation based on
     * <a href="http://digital.cs.usu.edu/~allanv/cs5050/Graph.java">Utah State University - Dr. Vicki H. Allan</a>
     * @param start
     */
    private void calcShortestPaths(Node start) {
        settled = new HashSet<>();
        unSettled = new HashSet<>();
        dist = new HashMap<>();
        predecessors = new HashMap<>();



        dist.put(start, 0d);
        unSettled.add(start);
        while(unSettled.size() > 0) {
            Node node = getMin(unSettled);
            settled.add(node);
            unSettled.remove(node);
            findMinDist(node);
        }
    }


    private Node getMin(Set<Node> nodes) {
        Node min = null;
        for(Node n : nodes) {
            if(min == null) min = n;
            else {
                if(getShortestDist(n) < getShortestDist(min)) min = n;
            }
        }
        return min;
    }

    private void findMinDist(Node node) {
        List<Node> adjacent = getNeigbours(node);
        for(Node dest : adjacent) {
            if(getShortestDist(dest) > getShortestDist(node) + getDist(node, dest) ){
                dist.put(dest, getShortestDist(node) + getDist(node, dest));
                predecessors.put(dest, node);
                unSettled.add(dest);
            }
        }
    }

    private double getDist(Node node, Node dest) {
        for(Edge e : lanes.values()) {
            if( e.getStart().getId() == node.getId() &&
                    e.getEnd().getId() == dest.getId()) {
                return e.getUsageLevel();
            }
        }
        throw new RuntimeException("no distance found, should not happen");
    }

    private double getShortestDist(Node dest) {
        Double d = dist.get(dest);
        return d == null ? Double.MAX_VALUE : d;
    }

    private List<Node> getNeigbours(Node node) {
        List<Node> neighbours = new ArrayList<>();
        for(Edge e : lanes.values()) {
            if( e.getStart().getId() == node.getId() &&
                !isSettled(e.getEnd().getId()) ) {
                neighbours.add(crossings.get(e.getEnd().getId()));
            }
        }
        return neighbours;
    }

    private boolean isSettled(Integer id) {
        return settled.contains(crossings.get(id));
    }

    private Route getPath(Node dest) {
        LinkedList<Lane> path = new LinkedList<>();
        Node step = dest;

        if(predecessors.get(step) == null) return null;

        Node prev;
        do {
            prev = step;
            step = predecessors.get(step);
            path.add(getLaneFromNodes(step, prev)); //swap from/to as we're traversing starting at destination
        } while(predecessors.get(step) != null);
        Collections.reverse(path);
        return new Route(path);
    }

    private Lane getLaneFromNodes(Node from, Node to) {
        //TODO: return List of Lanes, for the case that multiple connections exist
        for (Lane out : from.getOut() ) {
            if(to.getIn().contains(out)) return out;
        }
        return null;
    }

}
