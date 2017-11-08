package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface IDAOMeals {
    List<Meal> getMeals();

    boolean addMeal(Meal meal);

    boolean editMeal(Meal meal);

    boolean deleteMeal(long id);
}