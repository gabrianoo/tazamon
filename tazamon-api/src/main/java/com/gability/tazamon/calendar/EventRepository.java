package com.gability.tazamon.calendar;

import com.gability.tazamon.user.User;

public interface EventRepository {

    Iterable<Event> findAllEvents(User user);
}
