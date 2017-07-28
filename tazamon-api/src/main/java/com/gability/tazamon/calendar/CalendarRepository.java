package com.gability.tazamon.calendar;

import com.gability.tazamon.user.User;

public interface CalendarRepository {

    Iterable<Calendar> findAllCalendars(User user);
}
