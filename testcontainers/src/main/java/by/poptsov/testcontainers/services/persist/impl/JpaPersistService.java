package by.poptsov.testcontainers.services.persist.impl;

import by.poptsov.testcontainers.services.persist.PersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TODO: description
 *
 * @author Alexey Poptsov
 */
@Service
public class JpaPersistService implements PersistService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private JpaPersistService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String name() {
        return "jdbc";
    }

    @Override
    public void store(String key, String value) {
        jdbcTemplate.update(
                "INSERT INTO STORAGE (KEY, VALUE) VALUES (?, ?)",
                key, value
        );
    }

    @Override
    public String fetch(String key) {
        try {
            return jdbcTemplate.queryForObject("SELECT VALUE FROM STORAGE WHERE KEY = ?", String.class, key);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }
}
