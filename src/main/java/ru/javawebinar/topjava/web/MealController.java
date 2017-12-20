package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class MealController {
    private static final Logger log = getLogger(MealController.class);

    @Autowired
    private MealService service;

    @GetMapping
    public String get(Model model) {
        int userId = AuthorizedUser.id();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @PostMapping("filter")
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        int userId = AuthorizedUser.id();

        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenDates(
                startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId);


        model.addAttribute("meals", MealsUtil.getFilteredWithExceeded(mealsDateFiltered,
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay())
        );
        return "meals";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable String id, Model model) {
        int _id = Integer.valueOf(id);
        model.addAttribute("meal", service.get(_id, AuthorizedUser.id()));
        return "mealForm";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 100));
        return "mealForm";
    }

    @PostMapping("save")
    public String save(Meal meal) {
        if (meal.isNew()) {
            log.info("create new");
            service.create(meal, AuthorizedUser.id());
        } else {
            log.info("update {}", meal.getId());
            service.update(meal, AuthorizedUser.id());
        }
        return "redirect:/meals";
    }
//    @PostMapping("save")
//    public String save(@RequestParam Integer id,
//                       @RequestParam String description,
//                       @RequestParam String dateTime,
//                       @RequestParam Integer calories,
//                       HttpServletRequest request) {
//        Meal meal = new Meal(id, LocalDateTime.parse(dateTime), description, calories);
//        if (meal.isNew()) {
//            log.info("create new");
//            service.create(meal, AuthorizedUser.id());
//        } else {
//            log.info("update {}", meal.getId());
//            service.update(meal, AuthorizedUser.id());
//        }
//        return "redirect:/meals";
//    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable String id) {
        int _id = Integer.valueOf(id);
        int userId = AuthorizedUser.id();
        log.info("delete meal {} for user {}", _id, userId);
        service.delete(_id, userId);
        return "redirect:/meals";
    }

    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Nullable
        @Override
        public LocalDateTime convert(String source) {
            return LocalDateTime.parse(source);
        }
    }

    //    https://stackoverflow.com/questions/13778099/register-converters-and-converterfactories-with-annotations-in-spring-3
    @Component("conversionService")
    public static class ApplicationConversionService extends DefaultFormattingConversionService {
        public ApplicationConversionService() {
            super();
            addConverter(new StringToLocalDateTimeConverter());
        }
    }
}