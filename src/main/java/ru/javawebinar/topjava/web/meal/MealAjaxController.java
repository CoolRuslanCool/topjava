package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = MealAjaxController.REST_URL)
public class MealAjaxController extends AbstractMealController {
    private static final Logger log = getLogger(MealAjaxController.class);

    static final String REST_URL = "/ajax/profile/meals";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @Override
    @PutMapping(value = "/{id}")
    public void update(Meal meal, @PathVariable("id") int id) {
        log.debug(meal.toString());
        super.update(meal, id);
    }

    @PostMapping
    public Meal create(Meal meal, @RequestParam LocalDateTime datetime) {
        meal.setDateTime(datetime);
        if (meal.isNew())
            return super.create(meal);
        else
            super.update(meal, meal.getId());
        return meal;
    }

    @Override
    @GetMapping(value = "/filter")
    public List<MealWithExceed> getBetween(
            @RequestParam(value = "startDate", required = false) LocalDate startDate, @RequestParam(value = "startTime", required = false) LocalTime startTime,
            @RequestParam(value = "endDate", required = false) LocalDate endDate, @RequestParam(value = "endTime", required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}