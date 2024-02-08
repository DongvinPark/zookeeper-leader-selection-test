package com.example.leader.test.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {
    private final LeaderElectionService leaderElectionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        leaderElectionService.electLeader();
    }
}
