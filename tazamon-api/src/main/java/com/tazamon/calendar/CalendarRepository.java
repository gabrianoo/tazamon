package com.tazamon.calendar;

import com.tazamon.user.User;

public interface CalendarRepository {

    Iterable<Calendar> findAllCalendars(User user);
}
