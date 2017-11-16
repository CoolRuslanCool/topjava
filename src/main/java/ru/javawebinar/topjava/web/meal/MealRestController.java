package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

@Controller
public class MealRestController {

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void delete(int mealId) {
        service.delete(mealId, AuthorizedUser.getId());
    }

    public Meal save(Meal meal) {
        return service.save(meal, AuthorizedUser.getId());
    }

    public Meal getById(int mealId) {
        return service.getById(mealId, AuthorizedUser.getCaloriesPerDay(), AuthorizedUser.getId());
    }

    public Collection<MealWithExceed> getByDescription(String descr) {
        return service.getByDescription(descr, AuthorizedUser.getId());
    }

    public Collection<MealWithExceed> getAll() {
        return service.getAll(AuthorizedUser.getCaloriesPerDay(), AuthorizedUser.getId());
    }

    public Collection<MealWithExceed> getFiltered(String dateFrom, String dateTo,
                                                  String timeFrom, String timeTo) {
        return service.getFiltered(
                isEmpty(dateFrom) ? LocalDate.MIN : LocalDate.parse(dateFrom),
                isEmpty(dateTo) ? LocalDate.MAX : LocalDate.parse(dateTo),
                isEmpty(timeFrom) ? LocalTime.MIN : LocalTime.parse(timeFrom),
                isEmpty(timeTo) ? LocalTime.MAX : LocalTime.parse(timeTo),
                AuthorizedUser.getCaloriesPerDay(),
                AuthorizedUser.getId()
        );
    }

    private boolean isEmpty(String data) {
        return data == null || data.isEmpty();
    }
}