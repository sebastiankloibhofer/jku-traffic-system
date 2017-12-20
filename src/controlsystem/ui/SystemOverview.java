package controlsystem.ui;

import controlsystem.trafficparticipants.street.Crossing;
import controlsystem.trafficparticipants.street.Lane;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SystemOverview {

    private final java.util.List<Node> nodes;
    private final List<Edge> edges;
    private final GraphPanel graph;

    public SystemOverview(Collection<Crossing> crossings, Collection<Lane> lanes) {
        nodes = crossings.stream()
                .map(Node::new)
                .collect(Collectors.toList());
        edges = lanes.stream()
                .map(Edge::new)
                .collect(Collectors.toList());

        graph = new GraphPanel();
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
            main.setDefaultCloseOperation(EXIT_ON_CLOSE);
        });
    }

    public static void main(String[] args) throws
            ClassNotFoundException,
            UnsupportedLookAndFeelException,
            InstantiationException,
            IllegalAccessException {

        Crossing c0 = new Crossing(1, 10, 10);
        Crossing c1 = new Crossing(2, 530, 200);
        Crossing c2 = new Crossing(2, 800, 120);
        Crossing c3 = new Crossing(2, 400, 300);
        Set<Crossing> crossings = new HashSet<>();
        crossings.add(c0);
        crossings.add(c1);
        crossings.add(c2);
        crossings.add(c3);

        Lane l0 = new Lane(1, c0, c1);
        l0.setParticipants(100);
        Lane l1 = new Lane(2, c1, c0);
        Lane l2 = new Lane(3, c0, c3);
        Lane l3 = new Lane(4, c1, c3);
        Lane l4 = new Lane(5, c1, c2);
        l4.setParticipants(200);
        Lane l5 = new Lane(6, c2, c0);
        Lane l6 = new Lane(7, c3, c0);
        Lane l7 = new Lane(8, c2, c3);
        Lane l8 = new Lane(9, c3, c1);
        Lane l9 = new Lane(10, c3, c2);
        Lane l10 = new Lane(11, c0, c2);
        Lane l11 = new Lane(12, c2, c1);

        Set<Lane> lanes = new HashSet<>();
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

        SystemOverview so = new SystemOverview(crossings, lanes);
        so.init();
    }

    /**
     * Drawing panel to display the traffic network.
     * This class is based on
     * <a href="http://digital.cs.usu.edu/~allanv/cs5050/Graph.java">Utah State University - Dr. Vicki H. Allan</a>'s
     * implementation.
     */
    class GraphPanel extends JComponent {

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
            edges.forEach(e -> {
                int x1 = e.fromX();
                int y1 = e.fromY();
                int x2 = e.toX();
                int y2 = e.toY();
                g2.setStroke(new BasicStroke(3));
                g2.setColor(e.color());
                g2.drawLine(x1, y1, x2, y2);
            });

            // draw all nodes
            nodes.forEach(node -> paintNode(g2, node));

            // put the offscreen image to the screen
//            g.drawImage(img, 0, 0, null);
        }
    }
}
