package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class LocalDAOImpl implements IDAOMeals {
    private static final Logger log = getLogger(LocalDAOImpl.class);
    private static final List<Meal> MEALS;
    private static volatile Long mealCountId = 0L;

    static {
        log.debug("init database");

        MEALS = Collections.synchronizedList(new ArrayList<>());
        MEALS.add(new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        MEALS.add(new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        MEALS.add(new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        MEALS.add(new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        MEALS.add(new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        MEALS.add(new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));

    }

    public LocalDAOImpl() {
        log.debug("create local DAO impl");

    }

    @Override
    public List<Meal> getMeals() {
        log.debug("get all");

        return Collections.unmodifiableList(MEALS);
    }

    @Override
    public synchronized boolean addMeal(Meal meal) {
        log.debug("add meal");

        meal.setId(getId());
        return MEALS.add(meal);
    }

    @Override
    public synchronized boolean editMeal(Meal meal) {
        log.debug("edit meal");

        if (deleteMeal(meal.getId()))
            return MEALS.add(meal);
        else
            return false;
    }

    @Override
    public synchronized boolean deleteMeal(long id) {
        log.debug("delete meal");

        return MEALS.removeIf(meal1 -> meal1.getId() == id);
    }

    private static synchronized Long getId() {
        log.debug("get id");

        return ++mealCountId;
    }
}