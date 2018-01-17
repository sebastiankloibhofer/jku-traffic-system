package controlsystem.persistence;

import controlsystem.model.Edge;
import controlsystem.model.Node;
import controlsystem.model.Route;
import controlsystem.persistence.domain.CrossingDTO;
import controlsystem.persistence.domain.LaneDTO;
import controlsystem.persistence.domain.RouteDTO;
import controlsystem.util.ThrowingConsumer;
import controlsystem.util.ThrowingFunction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static controlsystem.persistence.DataSource.PERSISTENCE_UNIT_NAME;
import static java.util.Collections.emptyList;
import static javax.persistence.Persistence.createEntityManagerFactory;

public class ArchiveStore implements Repository {

    /**
     * One factory that is used for any entity manager invocation.
     */
    private static final EntityManagerFactory emf = createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    /**
     * Helper method that wraps any query in a transaction
     * and handles the entity manager lifecycle.
     *
     * @param work The task to fulfill
     * @param <T>  The type of the returned result
     * @return The query result
     * @throws Exception if the entity manager invocation fails
     */
    private <T> T exec(ThrowingFunction<EntityManager, T> work) throws Exception {

        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            T result = work.tryApply(em);
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Helper method that wraps any query in a transaction and
     * handles the entity manager lifecycle.
     *
     * @param work The task to fulfill
     * @throws Exception if the entity manager invocation fails
     */
    private void exec(ThrowingConsumer<EntityManager> work) throws Exception {

        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            work.tryAccept(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    @Override
    public int calcAvgCapacity(Edge lane, Instant from, Instant to) {
        LaneDTO l = new LaneDTO(lane);

        try {
            return exec(em -> {
                Query q;

                if (from == null && to == null) {
                    q = em.createNativeQuery(
                            "SELECT avg(participant_count) " +
                            "FROM lanes " +
                            "WHERE nr = :nr");
                } else if (from == null) {
                    q = em.createNativeQuery(
                            "SELECT avg(participant_count) " +
                            "FROM lanes " +
                            "WHERE nr = :nr" +
                            "  AND created_at <= :to")
                            .setParameter("to", to);
                } else if (to == null) {
                    q = em.createNativeQuery(
                            "SELECT avg(participant_count) " +
                            "FROM lanes " +
                            "WHERE nr = :nr" +
                            "  AND created_at >= :from")
                            .setParameter("from", from);
                } else {
                    q = em.createNativeQuery(
                            "SELECT avg(participant_count) " +
                            "FROM lanes " +
                            "WHERE nr = :nr" +
                            "  AND created_at BETWEEN :from AND :to")
                            .setParameter("from", from)
                            .setParameter("to", to);
                }

                q.setParameter("nr", l.getNr());
                return ((BigInteger) q.getSingleResult()).intValue();
            });
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Route> getDeterminedPaths(Node src, Node dst, Instant from, Instant to) {
        CrossingDTO c1 = new CrossingDTO(src);
        CrossingDTO c2 = new CrossingDTO(dst);


        try {
            return exec(em -> {
                Query q;

                if (from == null && to == null) {
                    q = em.createNativeQuery(
                            "SELECT * " +
                            "FROM routes " +
                            "WHERE start_id = :start_id " +
                            "  AND end_id = :end_id",
                            Route.class);
                } else if (from == null) {
                    q = em.createNativeQuery(
                            "SELECT * " +
                            "FROM routes " +
                            "WHERE created_at <= :to " +
                            "  AND start_id = :start_id " +
                            "  AND end_id = :end_id",
                            Route.class)
                            .setParameter("to", to);
                } else if (to == null) {
                    q = em.createNativeQuery(
                            "SELECT * " +
                            "FROM routes " +
                            "WHERE created_at >= :from " +
                            "  AND start_id = :start_id " +
                            "  AND end_id = :end_id",
                            Route.class)
                            .setParameter("from", from);
                } else {
                    q = em.createNativeQuery(
                            "SELECT * " +
                            "FROM routes " +
                            "WHERE created_at BETWEEN :from AND :to " +
                            "  AND start_id = :start_id " +
                            "  AND end_id = :end_id",
                            Route.class)
                            .setParameter("from", from)
                            .setParameter("to", to);
                }

                q.setParameter("start_id", c1.getId());
                q.setParameter("end_id", c2.getId());

                return q.getResultList();
            });
        } catch (Exception e) {
            e.printStackTrace();
            return emptyList();
        }
    }

    @Override
    public void save(Node node) {
        try {
            exec(em -> {
                em.persist(new CrossingDTO(node));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveNodes(Collection<Node> nodes) {
        nodes.forEach(this::save);
    }

    @Override
    public void save(Route route) {
        try {
            exec(em -> {
                em.persist(new RouteDTO(route));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Edge edge) {
        try {
            exec(em -> {
                em.persist(new LaneDTO(edge));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveEdges(Collection<Edge> edges) {
        edges.forEach(this::save);
    }
}
