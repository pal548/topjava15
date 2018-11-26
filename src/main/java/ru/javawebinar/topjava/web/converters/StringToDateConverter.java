package ru.javawebinar.topjava.web.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToDateConverter implements Converter<String, LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String s) {
        return LocalDate.parse(s, formatter);
    }
}
