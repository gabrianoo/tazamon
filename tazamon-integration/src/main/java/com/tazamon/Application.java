package com.tazamon;

import com.tazamon.calendar.Calendar;
import com.tazamon.calendar.CalendarRepository;
import com.tazamon.calendar.Event;
import com.tazamon.calendar.EventRepository;
import com.tazamon.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;

    @Inject
    public Application(
            UserRepository userRepository,
            CalendarRepository calendarRepository,
            EventRepository eventRepository
    ) {
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
        this.eventRepository = eventRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        userRepository
                .findUser(
                        "YOUR EMAIL",
                        "YOUR PASSWORD"
                )
                .ifPresent(user -> {
                    log.info("{}", user);
                    Iterable<Calendar> calendars = calendarRepository.findAllCalendars(user);
                    calendars.forEach(
                            calendar -> log.info("{}", calendar)
                    );
                    Iterable<Event> events = eventRepository.findAllEvents(user);
                    events.forEach(
                            event -> log.info("{}", event)
                    );
                });
    }
}
