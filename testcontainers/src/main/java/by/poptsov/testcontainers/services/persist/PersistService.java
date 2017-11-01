package by.poptsov.testcontainers.services.persist;


/**
 * TODO: description
 *
 * @author Alexey Poptsov
 */
public interface PersistService {

    String name();

    void store(String key, String value);

    String fetch(String key);
}
