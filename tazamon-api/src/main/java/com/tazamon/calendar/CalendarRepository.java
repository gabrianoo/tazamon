package com.tazamon.calendar;

import com.tazamon.user.User;

import java.util.List;

public interface CalendarRepository {

    Iterable<Calendar> findAllCalendars(User user);
}
