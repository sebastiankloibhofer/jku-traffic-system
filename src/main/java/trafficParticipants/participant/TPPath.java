package participant;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import street.Lane;

/**
 *
 * @author Christoph Kroell
 */
public class TPPath {
    
    public static Queue<Lane> get(Lane start, Lane goal) {
        Queue<LaneNode> open;
        Set<Lane> closed;
        Queue<Lane> path;
        LaneNode cur;
        
        path = new ArrayDeque<>();
        open = new PriorityQueue<>();
        closed = new HashSet<>();
        cur = new LaneNode(start, goal);
        
        open.add(cur);
        while(cur != null && !cur.lane.equals(goal) && !cur.lane.getTwins().contains(goal) && !cur.lane.getInverseTwins().contains(goal)) {
            for(Lane next : cur.lane.getEnd().getOut()) {
                if(!closed.contains(next)) {
                    open.add(new LaneNode(next, goal, cur));
                }
            }
            closed.add(cur.lane);
            cur = open.poll();
        }
        Stack<Lane> temp = new Stack<>();
        while(cur != null) {
            temp.add(cur.lane);
            cur = cur.parent;
        }
        
        while(!temp.isEmpty()) {
            path.add(temp.pop());
        }
        
        return path;
    }
    
    private static class LaneNode implements Comparable<LaneNode>{
        private final Lane lane, goal;
        private LaneNode parent;
        private int length = -1;

        public LaneNode(Lane lane, Lane goal) {
            this(lane, goal, null);
        }
        
        public LaneNode(Lane lane, Lane goal, LaneNode parent) {
            this.goal = goal;
            this.lane = lane;
            this.parent = parent;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.lane);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LaneNode other = (LaneNode) obj;
            if (!Objects.equals(this.lane, other.lane)) {
                return false;
            }
            return true;
        }
        
        private int length() {
            if(length != -1) {
                return length;
            }
            LaneNode cur;
            int length;
            
            length = 0;
            cur = this;
            while(cur != null) {
                length += cur.lane.getEnd().getPosition().subtract(cur.lane.getStart().getPosition()).length();
                cur = cur.parent;
            }
            this.length = length;
            return length;
        }

        @Override
        public int compareTo(LaneNode o) {
            return Integer.compare(length(), o.length());
        }
    }
}
