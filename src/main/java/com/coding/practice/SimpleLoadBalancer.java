package com.coding.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SimpleLoadBalancer implements LoadBalancer{
    private Map<String, BackendInstance> backendInstances;

    private final int MAX_ALLOWED_INSTANCES;

    private Random random;

    public SimpleLoadBalancer(int maxAllowedInstance, Random random){
        this.backendInstances = new HashMap<>();
        this.MAX_ALLOWED_INSTANCES = maxAllowedInstance;
        this.random = random;
    }

    @Override
    public boolean registerLoadBalancer(String ipAddress) {
        // O(1)
        if (ipAddress == null || ipAddress.isEmpty())
            throw new IllegalArgumentException("Invalid ip address");

        if(backendInstances.size() >= MAX_ALLOWED_INSTANCES)
            throw new IllegalArgumentException("Can not register anymore instances");

        backendInstances.put(ipAddress, new BackendInstance(ipAddress));
        return backendInstances.containsKey(ipAddress);
    }

    @Override
    public int getRegisteredInstancesCount() {
        return backendInstances.values().size();
    }

    @Override
    public BackendInstance get() {
        if(backendInstances.isEmpty())
            throw new IllegalArgumentException("There are no instances");
        return backendInstances.values().stream().toList().get(random.nextInt(backendInstances.size()));
    }


}
