package controlsystem;

import controlsystem.model.Edge;
import controlsystem.model.Node;
import controlsystem.model.Route;
import controlsystem.persistence.ArchiveStore;
import controlsystem.scheduling.RoutePlanner;
import trafficparticipants.street.Crossing;
import trafficparticipants.street.Lane;
import controlsystem.util.Tuple.T2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static controlsystem.util.Tuple.t2;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Controller implements RoutingControl, ControlSystem {

    public static final long INIT_DELAY       = 10000;
    public static final long CONGESTION_DELAY = 30000;

    /** Thread pool for concurrently working scheduler instances. */
    private final ExecutorService exec = Executors.newFixedThreadPool(10);

    /** Thread pool for periodically running congestion detection. */
    private final ScheduledExecutorService schedExec = Executors.newSingleThreadScheduledExecutor();

    public final ConcurrentMap<Long, Node> crossings;
    public final ConcurrentMap<Long, Edge> lanes;
    private final ArchiveStore rep;

    private final GraphPanel graph;

    public Controller(ConcurrentMap<Long, Node> crossings, ConcurrentMap<Long, Edge> lanes, ArchiveStore rep) {
        this.crossings = crossings;
        this.lanes = lanes;
        this.rep = rep;

        schedExec.scheduleWithFixedDelay(() ->
                        lanes.values().forEach(l -> {
                            double lvl = l.getUsageLevel();

                            if (lvl >= Lane.CONGESTION_LVL)
                                System.out.println("Detected congestion at lane " + l);
                        }),
                INIT_DELAY,
                CONGESTION_DELAY,
                MILLISECONDS
        );

        graph = new GraphPanel();
    }

    public synchronized void shutdown() {
        exec.shutdown();
        schedExec.shutdown();
    }

    @Override
    public Route getRoute(long firstId, long thenId, long... nextIds) {
        Lane first = lanes.get(firstId);
        Lane then = lanes.get(thenId);

        if (first == null || then == null)
            return null;

        List<T2<Lane>> subRoutes = new LinkedList<>();
        subRoutes.add(t2(first, then));
        Lane last = then;

        for (long currId : nextIds) {
            Lane curr = lanes.get(currId);
            if (curr == null)
                return null;

            subRoutes.add(t2(last, curr));
            last = curr;
        }

        List<Future<Route>> pathPromise = subRoutes.stream()
                .map(p -> exec.submit(new RoutePlanner(crossings, lanes, p._0, p._1)))
                .collect(Collectors.toList());

        List<Route> routes = pathPromise.stream()
                .map(p -> {
                    try {
                        return p.get(RoutePlanner.TIMEOUT, MILLISECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        // TODO
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());

        return Route.of(routes);
    }

    @Override
    public double getLaneDensity(long laneId) {
        if (lanes.containsKey(laneId))
            return lanes.get(laneId).getUsageLevel();
        else
            return -1d;
    }

    @Override
    public void updateParticipantCount(long laneId, int count) {
        if (lanes.containsKey(laneId)) {
            lanes.get(laneId).setParticipants(count);
            graph.repaint();
        }
    }

    @Override
    public void reportDamage(long laneId, int severity) {
        // TODO notify road maintenance
    }

    @Override
    public void reportAccident(long laneId) {
        // TODO inform emergencies
        // TODO notify road maintenance
    }

    public void init() throws
            ClassNotFoundException,
            UnsupportedLookAndFeelException,
            InstantiationException,
            IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Make thread-safe
        SwingUtilities.invokeLater(() -> {
            JFrame main = new JFrame("Control System Overview");
            main.setSize(1000, 600);

            Container content = main.getContentPane();

            content.add(graph);

            main.setResizable(false);
            main.setVisible(true);
            main.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (JOptionPane.showConfirmDialog(main,
                            "Are you sure you want to exit the application?", "Exit application",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                        shutdown();
                        System.exit(0);
                    }
                }
            });
        });
    }

    public static void main(String[] args) throws
            ClassNotFoundException,
            UnsupportedLookAndFeelException,
            InstantiationException,
            IllegalAccessException {

        Node c0 = new Node(1, 10, 10);
        Node c1 = new Node(2, 530, 200);
        Node c2 = new Node(3, 800, 120);
        Node c3 = new Node(4, 400, 300);
        Set<Node> crossings = new HashSet<>();
        crossings.add(c0);
        crossings.add(c1);
        crossings.add(c2);
        crossings.add(c3);

        Edge l0 = new Edge(1, c0, c1);
        l0.setParticipants(500);
        Edge l1 = new Edge(2, c1, c0);
        Edge l2 = new Edge(3, c0, c3);
        Edge l3 = new Edge(4, c1, c3);
        Edge l4 = new Edge(5, c1, c2);
        l4.setParticipants(200);
        Edge l5 = new Edge(6, c2, c0);
        Edge l6 = new Edge(7, c3, c0);
        Edge l7 = new Edge(8, c2, c3);
        Edge l8 = new Edge(9, c3, c1);
        Edge l9 = new Edge(10, c3, c2);
        Edge l10 = new Edge(11, c0, c2);
        Edge l11 = new Edge(12, c2, c1);

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

        ConcurrentMap<Long, Edge> l = lanes.stream()
                .collect(Collectors.toConcurrentMap(Lane::getId, Function.identity()));

        ConcurrentMap<Long, Node> c = crossings.stream()
                .collect(Collectors.toConcurrentMap(Crossing::getId, Function.identity()));

        Controller con  = new Controller(c, l, null);
        con.init();


        try {
            Thread.sleep(5000);
            Lane lane = con.lanes.get(1L);

            System.out.println("Blocking lane " + lane);
            lane.setBlocked(true);
            con.graph.repaint();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drawing panel to display the traffic network.
     * This class is based on
     * <a href="http://digital.cs.usu.edu/~allanv/cs5050/Graph.java">Utah State University - Dr. Vicki H. Allan</a>'s
     * implementation.
     */
    class GraphPanel extends JComponent {

        public static final int STROKE_WIDTH = 5;

        GraphPanel() {
            setVisible(true);
        }

        public void paintNode(Graphics g, Node n) {
            final int x = n.x();
            final int y = n.y();
            g.setColor(n.color());
            final int r = n.r();
            g.fillOval(x - r, y - r, 2 * r, 2 * r);
        }

        @Override
        protected void paintComponent(Graphics g) {
            final Graphics2D g2 = (Graphics2D)g;
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);

            // draw each edge
            lanes.values().forEach(e -> {
                int x1 = e.fromX();
                int y1 = e.fromY();
                int x2 = e.toX();
                int y2 = e.toY();
                g2.setStroke(new BasicStroke(STROKE_WIDTH));
                g2.setColor(e.color());
                g2.drawLine(x1, y1, x2, y2);
            });

            // draw all nodes
            crossings.values().forEach(node -> paintNode(g2, node));
        }
    }
}
