package com.example.leader.test.configs;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LeaderElectionService {
    @Value("${zookeeper.connectionString}")
    private String zookeeperConnectionString;

    public void electLeader() throws Exception {
        int sleepMsBetweenRetries = 100;
        int maxRetries = 3;
        RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);

        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zookeeperConnectionString, retryPolicy
                );
        client.start();

        LeaderLatch leaderLatch = new LeaderLatch(client, "/leader");
        leaderLatch.start();

        // Wait until this node becomes the leader
        leaderLatch.await();

        // Perform actions that should only be executed by the leader node
        if (leaderLatch.hasLeadership()) {
            // This node is the leader
            System.out.println("This node is the leader");
        } else {
            // This node is not the leader
            System.out.println("This node is not the leader");
        }
    }
}
