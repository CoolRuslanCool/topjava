package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(MealRestController.REST_URL)
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/meals";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithUri(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getBetween(@RequestParam LocalDate startDate,
                                           @RequestParam LocalDate endDate,
                                           @RequestParam LocalTime startTime,
                                           @RequestParam LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
//
//    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<MealWithExceed> getBetween(@RequestBody FilterDate filterDate) {
//        LocalDate startDate = filterDate.start.toLocalDate();
//        LocalDate endDate = filterDate.end.toLocalDate();
//        LocalTime startTime = filterDate.start.toLocalTime();
//        LocalTime endTime = filterDate.end.toLocalTime();
//        return super.getBetween(startDate, startTime, endDate, endTime);
//    }

    //    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<MealWithExceed> getBetween(HttpServletRequest request, Model model) {
//        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
//        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
//        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
//        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
//        return super.getBetween(startDate, startTime, endDate, endTime);
//    }
    private static class FilterDate {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime start;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime end;
    }
//
//    private static class StringToLocalDateConverter implements Formatter<LocalDate> {
//        @Override
//        public LocalDate parse(String text, Locale locale) throws ParseException {
//            return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
//        }
//
//        @Override
//        public String print(LocalDate object, Locale locale) {
//            return object.toString();
//        }
//    }
//
//    private static class StringToLocalTimeConverter implements Formatter<LocalTime> {
//        @Override
//        public LocalTime parse(String text, Locale locale) throws ParseException {
//            return LocalTime.parse(text, DateTimeFormatter.ISO_TIME);
//        }
//
//        @Override
//        public String print(LocalTime object, Locale locale) {
//            return object.toString();
//        }
//    }

//    @Component("conversionService")
//    public static class LocalDateTimeConversionService extends DefaultFormattingConversionService {
//        public LocalDateTimeConversionService() {
//            super();
//            addFormatter(new StringToLocalDateConverter());
//            addFormatter(new StringToLocalTimeConverter());
//        }
//    }
}