package com.example.cashenvelope.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.example.cashenvelope.auth.SessionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Autowired
    private SessionMapper sessionRepository;

    /**
     * clear sessions that were updated more than 30 days ago
     * 
     * for now, because foundSessions are just deleted, we can just check the
     * created_at date instead of updated_at date.
     * 
     * This should probably be changed to check the updated_at (check SessionMapper
     * for more info)
     * 
     */
    private final Long CLEAR_SESSIONS_DAY = new Long(30);

    /**
     * schedule clearing of old sessions every day at 00:00:00 UTC
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearSessions() {
        final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).minusDays(CLEAR_SESSIONS_DAY);

        sessionRepository.clear(now.toString());
    }

}