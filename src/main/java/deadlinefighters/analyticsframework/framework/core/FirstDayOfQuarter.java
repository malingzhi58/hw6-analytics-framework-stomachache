package deadlinefighters.analyticsframework.framework.core;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class FirstDayOfQuarter implements TemporalAdjuster {
    @Override
    public Temporal adjustInto(Temporal temporal) {
        int currentQuarter = YearMonth.from(temporal).get(
            IsoFields.QUARTER_OF_YEAR);

        return LocalDate.from(temporal).with(TemporalAdjusters.firstDayOfMonth()).withMonth(Month.of((currentQuarter - 1) * 3 + 1).getValue());
    }
}
