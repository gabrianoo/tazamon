package com.gability.tazamon.calendar;

import com.gability.tazamon.user.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("events")
public class EventController {

    private final EventRepository eventRepository;

    @Inject
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @RequestMapping
    public Iterable<Event> findAllCalendars(User user) {
        return eventRepository.findAllEvents(
                user
        );
    }
}
