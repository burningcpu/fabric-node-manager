package com.webank.fabric.node.manager.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * common method of time.
 */
@Slf4j
public class TimeUtils {

    /**
     * convert timestamp to localDateTime.
     */
    public static LocalDateTime timestamp2LocalDateTime(Long inputTime) {
        if (inputTime == null) {
            log.warn("timestamp2LocalDateTime fail. inputTime is null");
            return null;
        }
        Instant instant = Instant.ofEpochMilli(inputTime);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * convert Date to localDateTime.
     */
    public static LocalDateTime LocalDateTimeFromDate(Date date) {
        if (date == null) {
            log.warn("LocalDateTimeFromDate fail. date is null");
            return null;
        }

        Instant instant = date.toInstant();
        TemporalAccessor temporalAccessor = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime.from(temporalAccessor);

        return LocalDateTime.from(temporalAccessor);
    }


    /**
     * getDateList
     *
     * @param start    date of start
     * @param listSize list of size
     * @param addFlag  1:date plus  -1: date minus
     * @return Arrays.asList(start.plusDays ( 1 * addFlag), start.plusDays(2 * addFlag)...)
     */
    public static List<LocalDate> getDateList(LocalDate start, int listSize, int addFlag) {
        if (start == null)
            start = LocalDate.now();

        List<Integer> flagValue = Arrays.asList(1, -1);
        if (!flagValue.contains(addFlag))
            return Lists.newArrayList();

        List<LocalDate> dateList = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            dateList.add(start.plusDays(i * addFlag));
        }
        return dateList;

    }
}
