package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private final static Map<String, List<String>> testStatistic = new HashMap<>();
    private static final Logger log = getLogger(MealServiceTest.class);

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Rule
    public final TestRule watchman = new TestWatcher() {
        private long start;

        @Override
        public Statement apply(Statement base, Description description) {
            testStatistic.put(description.getMethodName(), new ArrayList<>());
            return super.apply(base, description);
        }

        @Override
        protected void succeeded(Description description) {
            testStatistic.get(description.getMethodName()).add("Method success.");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            testStatistic.get(description.getMethodName()).add("Failed!!!");
            testStatistic.get(description.getMethodName()).add(String.format("\tThrow Exception: %s", e.toString()));
        }

        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
            testStatistic.get(description.getMethodName()).add("Skipped.");
        }

        @Override
        protected void starting(Description description) {
            start = System.nanoTime();
        }

        @Override
        protected void finished(Description description) {
            String evaluateTime = String.format("Duration: %d ms.", (System.nanoTime() - start)/1000000);
            testStatistic.get(description.getMethodName()).add(evaluateTime);
            log.debug("Method '{}' {}",description.getMethodName(), evaluateTime.replace('D', 'd'));
        }
    };

    @AfterClass
    public static void printStatistic() {
        StringBuilder builder = new StringBuilder("Statistic:\n");
        testStatistic.forEach((s, strings) -> {
            builder.append("\t").append(s).append(":\n");
            for (String string : strings) {
                builder.append("\t\t").append(string).append("\n");
            }
        });
        System.out.println(builder.toString());
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL2, MEAL3, MEAL4, MEAL5, MEAL6);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        service.delete(MEAL1_ID, 1);
    }

    @Test
    public void testSave() throws Exception {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6, created);
    }

    @Test
    public void testGet() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() throws Exception {
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void testGetAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void testGetBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL1, MEAL2, MEAL3);
    }
}