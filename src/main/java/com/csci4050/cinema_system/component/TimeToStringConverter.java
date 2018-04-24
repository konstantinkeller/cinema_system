package com.csci4050.cinema_system.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class TimeToStringConverter implements Converter<Time, String> {

    @Override
    public String convert(Time t) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.format(t);
    }

}
