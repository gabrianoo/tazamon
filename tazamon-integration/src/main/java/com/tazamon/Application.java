package com.tazamon;

import com.tazamon.calendar.Calendar;
import com.tazamon.calendar.CalendarRepository;
import com.tazamon.common.User;
import com.tazamon.client.http.HttpTazamonAbstractFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Inject
    private HttpTazamonAbstractFactory appleHttpTazamonAbstractFactory;
    @Inject
    private CalendarRepository appleCalendarRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Optional<User> user = appleHttpTazamonAbstractFactory
                .provideUser("YOUR EMAIL", "YOUR PASSWORD");
        user.ifPresent(u -> log.info("{}", u));
        if (user.isPresent()) {
            List<Calendar> calendars = appleCalendarRepository.findAllCalendars(user.get());
            calendars.forEach(
                    calendar -> log.info("{}", calendar)
            );
        }
    }
}
