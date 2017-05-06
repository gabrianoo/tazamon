package com.tazamon.calendar;

import com.tazamon.user.User;

import java.util.List;

public interface CalendarRepository {

    List<Calendar> findAllCalendars(User user);
}
