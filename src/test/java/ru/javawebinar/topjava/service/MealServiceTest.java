package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by ruslan on 21.11.17.
 */
@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService service;

    @Test
    public void get() throws Exception {
        assertThat(service.get(USER_MEAL_ID, USER_ID)).isEqualTo(USER_MEAL);
        assertThat(service.get(ADMIN_MEAL_ID, ADMIN_ID)).isEqualTo(ADMIN_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getAfterDelete() throws Exception {
        service.delete(100002, USER_ID);
        service.get(1000002, USER_ID);
    }

    @Test
    public void delete() throws Exception {
        List<Meal> all = service.getAll(USER_ID);
        assertThat(all).contains(USER_MEAL).hasSize(3);
        service.delete(100002, USER_ID);
        all = service.getAll(USER_ID);
        assertThat(all).doesNotContain(USER_MEAL).hasSize(2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByNotCorrectMealId() throws Exception {
        service.delete(1, USER_ID);
    }
    @Test(expected = NotFoundException.class)
    public void deleteByNotCorrectUserId() throws Exception {
        service.delete(100002, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() throws Exception {
        assertThat(service.getBetweenDates(LocalDate.of(2016, Month.JULY, 13), LocalDate.of(2016, Month.JULY, 15), USER_ID))
                .contains(USER_MEAL).doesNotContain(ADMIN_MEAL)
                .hasSize(3);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        assertThat(service.getBetweenDateTimes(LocalDateTime.of(2016, Month.JULY, 14, 8,0), LocalDateTime.of(2016, Month.JULY, 14, 10,0), USER_ID))
                .contains(USER_MEAL)
                .hasSize(1);
    }

    @Test
    public void getAll() throws Exception {
        assertThat(service.getAll(USER_ID))
                .contains(USER_MEAL)
                .doesNotContain(ADMIN_MEAL)
                .size().isEqualTo(3);
    }

    @Test(expected = NotFoundException.class)
    public void updateByNotCorrectUserId() throws Exception {
        service.update(USER_MEAL, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateByNotCorrectMealId() throws Exception {
        service.update(USER_MEAL_NOT_CORRECT_FOR_UPDATE, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        USER_MEAL_NEW.setId(USER_MEAL_ID);
        assertThat(service.getAll(USER_ID)).doesNotContain(USER_MEAL_NEW).hasSize(3);
        service.update(USER_MEAL_NEW, USER_ID);
        assertThat(service.getAll(USER_ID)).contains(USER_MEAL_NEW).hasSize(3);
    }

    @Test
    public void create() throws Exception {
        assertThat(service.getAll(USER_ID)).hasSize(3).doesNotContain(USER_MEAL_NEW);
        service.create(USER_MEAL_NEW, USER_ID);
        USER_MEAL_NEW.setId(USER_MEAL_NEW_ID);
        assertThat(service.getAll(USER_ID)).hasSize(4).contains(USER_MEAL_NEW);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createEqualsDateTimeAndUserId() throws Exception {
        service.create(USER_MEAL_DUBLICATE, USER_ID);
    }
}