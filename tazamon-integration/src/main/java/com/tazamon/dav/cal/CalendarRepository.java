package com.tazamon.dav.cal;

import com.tazamon.dav.web.User;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository {

    Optional<List<Calendar>> findAllCalendars(User user);
}
