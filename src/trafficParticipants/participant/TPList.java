package trafficParticipants.participant;

import java.util.Collection;
import java.util.Iterator;

/**
 * A representation of all traffic participants in a lane with their designated
 * position. They can be moved with the update method, however if a traffic
 * participant cannot enter the next lane he will wait at the end of the lane.
 * Traffic participants will queue up behind  each other with a fixed distance
 * of 1.
 *
 * @author Christoph Kroell
 */
public class TPList implements Iterable<TrafficParticipant>{

    private final int length;
    private TPNode first, last;
    private int size = 0;

    public TPList(int length) {
        this.length = length;
    }

    /**
     * Adds a traffic participant to the lane.
     *
     * @param tp The traffic participant.
     * @param pos The position.
     * @return True if {@link #canAdd()} is true, that is if there is no traffic
     * participant at position 0 or below.
     * @throws NullPointerException is thrown if the traffic participant is null.
     * @throws IllegalArgumentException  is thrown if the position is less than 0.
     */
    public boolean add(TrafficParticipant tp, int pos) throws NullPointerException, IllegalArgumentException {
        if(pos < 0) {
            throw new IllegalArgumentException("A traffic participant's position cannot be less than 0");
        }
        if(tp == null) {
            throw new NullPointerException("You cannot add null to a TPList");
        }
        if(canAdd()) {
            TPNode toAdd = new TPNode(tp, Math.min(last.pos - 1, pos));
            if(first == null) {
                first = toAdd;
                last = first;
            } else {
                last.next = toAdd;
                toAdd.prev = last;
                last = last.next;
            }
            size++;
            return true;
        }
        return false;
    }

    /**
     * Adds a traffic participant to the lane.
     *
     * @param tp The traffic participant.
     * @return True if {@link #canAdd()} is true, that is if there is no traffic
     * participant at position 0 or below.
     * @throws NullPointerException is thrown if the traffic participant is null.
     */
    public boolean add(TrafficParticipant tp) throws NullPointerException, IllegalArgumentException {
        if(tp == null) {
            throw new NullPointerException("You cannot add null to a TPList");
        }
        if(canAdd()) {
            TPNode toAdd = new TPNode(tp, last.pos - 1);
            if(first == null) {
                first = toAdd;
                last = first;
            } else {
                last.next = toAdd;
                toAdd.prev = last;
                last = last.next;
            }
            size++;
            return true;
        }
        return false;
    }

    /**
     * Adds a traffic participant to the lane.
     *
     * @param tps The traffic participants.
     * @throws NullPointerException is thrown if a traffic participant is null.
     */
    public void addAll(TrafficParticipant... tps) throws NullPointerException {
        for(TrafficParticipant tp : tps) {
            add(tp);
        }
    }

    /**
     * Adds a traffic participant to the lane.
     *
     * @param tps The traffic participants.
     * @throws NullPointerException is thrown if a traffic participant is null.
     */
    public void addAll(Collection<TrafficParticipant> tps) throws NullPointerException {
        for(TrafficParticipant tp : tps) {
            add(tp);
        }
    }

    /**
     * Removes a traffic participant if it is within the list and returns true
     * if this was successful.
     * @param tp The traffic participant to remove.
     * @return true if the traffic participant was within the list.
     */
    public boolean remove(TrafficParticipant tp) {
        if(first.tp.equals(tp)) {
            first = first.next;
        } else if(last.tp.equals(tp)) {
            last = last.prev;
        } else {
            TPNode toRemove = find(tp);
            if(toRemove == null) {
                return false;
            } else {
                detachNode(toRemove);
            }
        }
        size--;
        return true;
    }

    /**
     * Removes a traffic participant if it is within the list and returns true
     * if this was successful.
     * @param tps The traffic participants to remove.
     */
    public void removeAll(TrafficParticipant... tps) {
        for(TrafficParticipant tp : tps) {
            remove(tp);
        }
    }

    /**
     * Removes a traffic participant if it is within the list and returns true
     * if this was successful.
     * @param tps The traffic participants to remove.
     */
    public void removeAll(Collection<TrafficParticipant> tps) {
        for(TrafficParticipant tp : tps) {
            remove(tp);
        }
    }

    /**
     * Replaces one traffic participant with another.
     * @param tp0 The traffic participant, which will be replaced.
     * @param tp1 The traffic participant that will be inserted.
     * @return True if the replacement was successful. This means that tp0 has
     * been found.
     * @throws NullPointerException is thrown if tp0 or t1 are null.
     */
    public boolean replace(TrafficParticipant tp0, TrafficParticipant tp1) throws NullPointerException{
        if(tp0 == null || tp1 == null) {
            throw new NullPointerException("Traffic participant cannot be null.");
        }
        TPNode toReplace = find(tp0);
        if(toReplace != null) {
            toReplace.tp = tp1;
            return true;
        }
        return false;
    }

    private void detachNode(TPNode node) {
        if(node == null) {
            return;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * Returns true if the traffic participant has been found within the
     * list.
     * @param tp The traffic participant to search.
     * @return true if the traffic participant is contained in the list.
     */
    public boolean contains(TrafficParticipant tp) {
        return find(tp) != null;
    }

    private TPNode find(TrafficParticipant tp) {
        for(Iterator<TPNode> iterator = getNodeIterator(); iterator.hasNext();) {
            TPNode cur = iterator.next();
            if(cur.tp.equals(tp)) {
                return cur;
            }
        }
        return null;
    }

    /**
     * Removes all traffic participants, that have finished and moves all
     * others forward.
     */
    public void update() {
        removeFinished();
        moveParticipants();
    }

    private void removeFinished() {
        TPNode cur;
        for(Iterator<TPNode> iterator = getNodeIterator(); iterator.hasNext();) {
            cur = iterator.next();
            if(cur.tp.getAction() == participant.TPAction.FIN) {
                detachNode(cur);
            }
        }
    }

    private Iterator<TPNode> getNodeIterator() {
        return new Iterator<TPNode>() {

            private TPNode next = first.deepCopy();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public TPNode next() {
                TPNode cur;

                cur = next;
                next = next.next;

                return cur;
            }
        };
    }

    public int getSize() {
        return size;
    }

    @Override
    public Iterator<TrafficParticipant> iterator() {
        return new Iterator<TrafficParticipant>() {

            private final Iterator<TPNode> nodeIterator = getNodeIterator();

            @Override
            public boolean hasNext() {
                return nodeIterator.hasNext();
            }

            @Override
            public TrafficParticipant next() {
                return nodeIterator.next().tp;
            }
        };
    }

    private void moveParticipants() {
        for(Iterator<TPNode> iterator = getNodeIterator(); iterator.hasNext();) {
            TPNode cur = iterator.next();
            if(cur.tp.getAction() == participant.TPAction.STAY) {
                continue;
            }
            // If there is somebody in front move at most directly behind that
            // person.
            if(cur.prev != null) {
                cur.pos = Math.min(cur.prev.pos - 1, cur.pos + cur.tp.getSpeed());
            } else {
                cur.pos += cur.tp.getSpeed();
            }
            // If the expected position is beyond the end of the lane, try to
            // move into the next one.
            if(cur.pos > length) {
                if(cur.tp.getNextLane().canEnter(cur.tp)) {
                    cur.tp.getNextLane().getTrafficParticipants().add(cur.tp, cur.pos - length);
                    remove(cur.tp);
                } else {
                    cur.pos = 0;
                }
            }
        }
    }

    /**
     * Checks wether it is possible to add another traffic participant to this
     * lane.
     * @return True if there is no traffic participant at position 0.
     */
    public boolean canAdd() {
        return last.pos > 0;
    }

    private class TPNode {
        private TrafficParticipant tp;
        private int pos;
        private TPNode next, prev;

        public TPNode(TrafficParticipant tp, int pos) {
            this.tp = tp;
            this.pos = pos;
        }

        private TPNode deepCopy() {
            TPNode copy = new TPNode(tp, pos);
            copyPrev(this, copy);
            copyNext(this, copy);
            return copy;
        }

        private void copyPrev(TPNode cur, TPNode copy) {
            if(cur.prev != null) {
                copy.prev = new TPNode(cur.prev.tp, cur.prev.pos);
                copy.prev.next = copy;
                copyPrev(cur.prev, copy.prev);
            }
        }

        private void copyNext(TPNode cur, TPNode copy) {
            if(cur.next != null) {
                copy.next = new TPNode(cur.next.tp, cur.next.pos);
                copy.next.prev = copy;
                copyNext(cur.next, copy.next);
            }
        }
    }
}
