package com.realestate.exception;

import java.time.LocalDateTime;

public class ThisIsNotAGameDayException extends  RuntimeException {

    private LocalDateTime currentTime;
    public ThisIsNotAGameDayException(LocalDateTime currentTime) {
        super("This is not a game day with day: " + currentTime);
        this.currentTime = currentTime;
    }
}
