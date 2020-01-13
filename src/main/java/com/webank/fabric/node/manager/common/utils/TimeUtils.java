package com.webank.fabric.node.manager.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

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


}
