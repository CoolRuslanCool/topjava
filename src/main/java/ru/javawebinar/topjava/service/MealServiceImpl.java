package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {
    private Collection<MealWithExceed> currentMeals;

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal getById(int mealId, int caloriesPerDay, int userId) {
        Meal meal = repository.get(mealId, userId, caloriesPerDay);
        checkNotFoundWithId(meal, mealId);
        return meal;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.getUserId() != null && meal.getUserId() != userId)
            throw new IllegalArgumentException("Meal userId not correct");
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int mealId, int userId) {
        if (!repository.delete(mealId, userId))
            throw new NotFoundException("Meal not exist.");
    }

    @Override
    public Collection<MealWithExceed> getAll(int calories, int userId) {
        currentMeals = repository.getAll(calories, userId);
        return currentMeals;
    }

    @Override
    public Collection<MealWithExceed> getFiltered(LocalDate dateFrom, LocalDate dateTo,
                                                  LocalTime timeFrom, LocalTime timeTo,
                                                  int caloriesPerDay, int userId) {
        currentMeals = repository.getBetweenDateTime(dateFrom, dateTo, timeFrom, timeTo, caloriesPerDay, userId);
        return currentMeals;
    }

    @Override
    public Collection<MealWithExceed> getByDescription(String descr, int userId) {
        return currentMeals.stream().filter(mealWithExceed -> mealWithExceed.getDescription().contains(descr)).collect(Collectors.toList());
    }
}