package ru.javawebinar.topjava.service.jdbc;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Override
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Override
    public void update() throws Exception {
        super.update();
    }

    @Ignore
    @Test
    @Override
    public void testValidation() throws Exception {
    }
}