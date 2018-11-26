package ru.javawebinar.topjava.web.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToTimeConverter implements Converter<String, LocalTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm[:ss]");

    @Override
    public LocalTime convert(String s) {
        return LocalTime.parse(s, formatter);
    }
}
