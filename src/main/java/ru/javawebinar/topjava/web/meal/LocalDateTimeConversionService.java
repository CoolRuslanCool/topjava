package ru.javawebinar.topjava.web.meal;

import org.springframework.format.Formatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component("conversionService")
public class LocalDateTimeConversionService extends DefaultFormattingConversionService {
    public LocalDateTimeConversionService() {
        super();
        addFormatter(new StringToLocalDateConverter());
        addFormatter(new StringToLocalTimeConverter());
    }

    private static class StringToLocalDateConverter implements Formatter<LocalDate> {
        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
            return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
        }

        @Override
        public String print(LocalDate object, Locale locale) {
            return object.toString();
        }
    }

    private static class StringToLocalTimeConverter implements Formatter<LocalTime> {
        @Override
        public LocalTime parse(String text, Locale locale) throws ParseException {
            return LocalTime.parse(text, DateTimeFormatter.ISO_TIME);
        }

        @Override
        public String print(LocalTime object, Locale locale) {
            return object.toString();
        }
    }
}
