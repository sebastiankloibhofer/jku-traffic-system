package controlsystem.scheduling;

import controlsystem.model.Edge;
import controlsystem.model.Node;
import controlsystem.model.Route;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoutePlannerTest {
    public static void main(String[] args) {

        Node c0 = new Node(1, 10, 10);
        Node c1 = new Node(2, 530, 200);
        Node c2 = new Node(3, 800, 120);
        Node c3 = new Node(4, 400, 300);
        Set<Node> crossings = new HashSet<>();
        crossings.add(c0);
        crossings.add(c1);
        crossings.add(c2);
        crossings.add(c3);

        Edge l0 = new Edge(c0, c1);
        l0.setNParticipants(500);
        Edge l1 = new Edge(c1, c0);
        Edge l2 = new Edge(c0, c3);
        Edge l3 = new Edge(c1, c3);
        Edge l4 = new Edge(c1, c2);
        l4.setNParticipants(200);
        Edge l5 = new Edge(c2, c0);
        Edge l6 = new Edge(c3, c0);
        Edge l7 = new Edge(c2, c3);
        Edge l8 = new Edge(c3, c1);
        Edge l9 = new Edge(c3, c2);
        Edge l10 = new Edge(c0, c2);
        Edge l11 = new Edge(c2, c1);

        Set<Edge> lanes = new HashSet<>();
        lanes.add(l0);
        lanes.add(l1);
        lanes.add(l2);
        lanes.add(l3);
        lanes.add(l4);
        lanes.add(l5);
        lanes.add(l6);
        lanes.add(l7);
        lanes.add(l8);
        lanes.add(l9);
        lanes.add(l10);
        lanes.add(l11);

        ConcurrentMap<Integer, Edge> l = lanes.stream()
                .collect(Collectors.toConcurrentMap(Edge::getId, Function.identity()));

        ConcurrentMap<Integer, Node> c = crossings.stream()
                .collect(Collectors.toConcurrentMap(Node::getId, Function.identity()));

        RoutePlanner planner = new RoutePlanner(c, l, c.get(2), c.get(4));
        try {
            Route r = planner.call();
            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
