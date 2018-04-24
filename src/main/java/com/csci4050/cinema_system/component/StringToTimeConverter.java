package com.csci4050.cinema_system.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class StringToTimeConverter implements Converter<String, Time> {

    @Override
    public Time convert(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long ms = sdf.parse(s).getTime();
            return new Time(ms);
        } catch (ParseException p) {
            return null;
        }
    }

}
