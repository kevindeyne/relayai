package com.kevindeyne.tasker.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class CurrentTimeDateTimeService implements DateTimeService {

    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}