package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.dao.LocalDAOImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {

    public static List<MealWithExceed> getFilteredWithExceeded(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<Meal> meals = new LocalDAOImpl().getMeals();
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static boolean addMeal(Meal meal) {
        return new LocalDAOImpl().addMeal(meal);
    }

    public static boolean deleteMeal(long id) {
        return new LocalDAOImpl().deleteMeal(id);
    }

    public static boolean editMeal(Meal meal) {
        return new LocalDAOImpl().editMeal(meal);
    }

    private static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}