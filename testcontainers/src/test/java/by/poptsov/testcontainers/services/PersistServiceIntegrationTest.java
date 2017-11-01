package by.poptsov.testcontainers.services;

import by.poptsov.testcontainers.AbstractIntegrationTest;
import by.poptsov.testcontainers.services.persist.PersistService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;


/**
 * TODO: description
 *
 * @author Alexey Poptsov
 */
public class PersistServiceIntegrationTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistServiceIntegrationTest.class);

    @Autowired
    private List<PersistService> persistServices;

    @Test
    public void test() throws Exception {
        LOGGER.info("Found {} persist services", persistServices.size());
        persistServices.forEach(this::test);

    }

    private void test(PersistService persistService) {
        assertNotNull(persistService);

        LOGGER.info("Start checking persist service = {}", persistService.name());
        final String key = String.valueOf(System.currentTimeMillis());
        final String expectedValue = String.valueOf(new Random().nextInt());
        String actualValue = persistService.fetch(key);
        assertNull(actualValue);
        persistService.store(key, expectedValue);
        actualValue = persistService.fetch(key);
        assertEquals(expectedValue, actualValue);
        LOGGER.info("Successful finish checking persist service = {}", persistService.name());
    }


}