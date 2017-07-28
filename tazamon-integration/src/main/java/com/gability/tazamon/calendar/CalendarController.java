package com.gability.tazamon.calendar;

import com.gability.tazamon.user.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("calendars")
public class CalendarController {

    private final CalendarRepository calendarRepository;

    @Inject
    public CalendarController(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    @RequestMapping
    public Iterable<Calendar> findAllCalendars(User user) {
        return calendarRepository.findAllCalendars(user);
    }
}
