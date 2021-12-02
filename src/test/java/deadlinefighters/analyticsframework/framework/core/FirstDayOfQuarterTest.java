package deadlinefighters.analyticsframework.framework.core;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FirstDayOfQuarterTest {
    @Test
    public void firstQuarter() {
        LocalDate date = LocalDate.of(2009, Month.FEBRUARY, 28);

        LocalDate firstQuarter = date.with(new FirstDayOfQuarter());

        assertEquals(
            LocalDate.of(2009, Month.JANUARY, 1),
            firstQuarter);
    }

    @Test
    public void secondQuarter() {
        LocalDate date = LocalDate.of(2009, Month.MAY, 31);

        LocalDate firstQuarter = date.with(new FirstDayOfQuarter());

        assertEquals(
            LocalDate.of(2009, Month.APRIL, 1),
            firstQuarter);
    }

    @Test
    public void thirdQuarter() {
        LocalDate date = LocalDate.of(2009, Month.SEPTEMBER, 1);

        LocalDate firstQuarter = date.with(new FirstDayOfQuarter());

        assertEquals(
            LocalDate.of(2009, Month.JULY, 1),
            firstQuarter);
    }

    @Test
    public void fourthQuarter() {
        LocalDate date = LocalDate.of(2009, Month.OCTOBER, 1);

        LocalDate firstQuarter = date.with(new FirstDayOfQuarter());

        assertEquals(
            LocalDate.of(2009, Month.OCTOBER, 1),
            firstQuarter);
    }
}
