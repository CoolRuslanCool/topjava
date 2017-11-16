package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealService {

    Meal getById(int mealId, int caloriesPerDay, int userId);
    Meal save(Meal meal, int userId);
    void delete(int meal, int userId);

    Collection<MealWithExceed> getAll(int calories, int userId);
    Collection<MealWithExceed> getFiltered(LocalDate dateFrom, LocalDate dateTo,
                                           LocalTime timeFrom, LocalTime timeTo,
                                           int caloriesPerDay, int userId);

    Collection<MealWithExceed> getByDescription(String descr, int userId);
}