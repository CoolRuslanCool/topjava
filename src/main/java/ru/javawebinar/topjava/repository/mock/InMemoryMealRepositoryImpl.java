package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if (meal.getUserId() == null)
                meal.setUserId(userId);
            repository.put(meal.getId(), meal);
        }else if (meal.getUserId() == userId) {
            repository.put(meal.getId(), meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int mealId, int userId) {
        Meal meal = repository.get(mealId);
        ValidationUtil.checkNotFoundWithId(meal, mealId);
        return (meal.getUserId() == userId && (repository.remove(mealId) != null));
    }

    @Override
    public Meal get(int mealId, int userId, int caloriesPerDay) {
        Meal meal = repository.get(mealId);
        if (meal.getUserId() != userId) return null;
        return meal.getUserId() != userId ? null : meal;
    }

    @Override
    public Collection<MealWithExceed> getAll(int caloriesPerDay, int userId) {
        Map<LocalDate, Integer> calories = repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));

        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .map(meal -> convertWithExceeded(meal, calories.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MealWithExceed> getBetweenDateTime(LocalDate dateFrom, LocalDate dateTo,
                                                         LocalTime timeFrom, LocalTime timeTo,
                                                         int caloriesPerDay, int userId) {
        Map<LocalDate, Integer> mealsByDate = repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));


        List<MealWithExceed> collect = repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), dateFrom, dateTo))
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), timeFrom, timeTo))
                .map(meal -> convertWithExceeded(meal, mealsByDate.get(meal.getDate()) > caloriesPerDay))
                .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                .collect(toList());
        return collect;
    }

    private MealWithExceed convertWithExceeded(Meal meal, boolean exceed) {
        return new MealWithExceed(meal.getId(),
                meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                exceed);
    }
}