package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealRepository {

    Meal save(Meal meal, int userId);
    boolean delete(int mealId, int userId);
    Meal get(int mealId, int userId, int caloriesPerDay);

    Collection<MealWithExceed> getAll(int caloriesPerDay, int userId);
    Collection<MealWithExceed> getBetweenDateTime(LocalDate dateFrom, LocalDate dateTo,
                                                  LocalTime timeFrom, LocalTime timeTo,
                                                  int caloriesPerDay, int userId);
}