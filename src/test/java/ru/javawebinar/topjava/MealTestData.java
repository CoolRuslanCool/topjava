package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 2;
    public static final int USER_MEAL_NEW_ID = START_SEQ + 8;
    public static final int ADMIN_MEAL_ID = START_SEQ + 5;

    public static final Meal USER_MEAL = new Meal(USER_MEAL_ID, LocalDateTime.of(2016, Month.JULY, 14, 9, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_DUBLICATE = new Meal(LocalDateTime.of(2016, Month.JULY, 14, 9, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_NOT_CORRECT_FOR_UPDATE = new Meal(1, LocalDateTime.of(2016, Month.JULY, 14, 9, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_NEW = new Meal(LocalDateTime.of(2015, Month.JULY, 15, 15, 0), "Завтрак", 5000);
    public static final Meal ADMIN_MEAL = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2017, Month.MARCH, 28, 9, 0), "Завтрак", 500);
}
