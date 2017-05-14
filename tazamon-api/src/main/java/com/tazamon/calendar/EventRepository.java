package com.tazamon.calendar;

import com.tazamon.user.User;

public interface EventRepository {

    Iterable<Event> findAllEvents(User user);
}
