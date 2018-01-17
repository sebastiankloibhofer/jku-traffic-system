package controlsystem.persistence;

/**
 * Factory to generate and keep track of persistence repositories.
 */
public class RepositoryFactory {

    /**
     * Singleton archive store instance.
     */
    private static ArchiveStore instance;

    public static Repository getArchiveRepository() {
        if (instance == null)
            instance = new ArchiveStore();

        return instance;
    }
}
