package com.xmcy.crypto.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;

import com.xmcy.crypto.exception.MonthNotSupportedException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Months {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private final int month;

    public static Months fromValue(int month) {
        return Arrays.stream(Months.values())
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseThrow(() -> new MonthNotSupportedException("Invalid month value: " + month));
    }

    public long getStartingOfMonthInMillis() {
        var firstDay = LocalDate.of(LocalDate.now().getYear(), this.getMonth(), 1);
        var zonedDateTime = firstDay.atStartOfDay(ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
        
    }

    public long getEndOfMonthInMillis() {
        var lastDay = LocalDate.of(LocalDate.now().getYear(), this.getMonth(), 1)
                                .withDayOfMonth(LocalDate.of(LocalDate.now().getYear(), this.getMonth(), 1).lengthOfMonth());
        var zonedDateTime = lastDay.atTime(23, 59, 59, 999999999)
                            .atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }

}
