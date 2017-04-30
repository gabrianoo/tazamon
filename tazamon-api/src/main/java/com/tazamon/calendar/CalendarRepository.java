package com.tazamon.calendar;

import com.tazamon.common.User;

import java.util.List;

public interface CalendarRepository {

    List<Calendar> findAllCalendars(User user);
}
