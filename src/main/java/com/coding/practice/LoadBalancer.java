package com.coding.practice;
public interface LoadBalancer {
    boolean registerLoadBalancer(String ipAddress);

    int getRegisteredInstancesCount();

    BackendInstance get();
}
