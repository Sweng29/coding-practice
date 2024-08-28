package com.coding.practice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoadBalancerTest {

    LoadBalancer loadBalancer;

    @Mock
    Random random;

    @BeforeEach
    void setUp(){
        this.random = Mockito.mock(Random.class);
        this.loadBalancer = new SimpleLoadBalancer(10, random);
    }

    @Test
    void givenValidIpAddress_shouldRegisterBackendInstance(){
        boolean isRegistered = loadBalancer.registerLoadBalancer("http://localhost:8080");
        assertTrue(isRegistered, "Instance registered successfully");
    }

    @Test
    void givenInvalidIpAddress_shouldThrowIlligalArgumentException(){
        assertThrows(IllegalArgumentException.class, () ->  loadBalancer.registerLoadBalancer(null));
    }

    @Test
    void givenEmptyIpAddress_shouldThrowIlligalArgumentException(){
        assertThrows(IllegalArgumentException.class, () ->  loadBalancer.registerLoadBalancer(""));
    }

    @Test
    void givenSameIPAddress_shouldOverrideTheExistingBackendInstance(){
        String ipAddress = "http://localhost:8080";
        loadBalancer.registerLoadBalancer(ipAddress);
        loadBalancer.registerLoadBalancer(ipAddress);
        int totalRegisteredInstances = loadBalancer.getRegisteredInstancesCount();
        assertEquals(1, totalRegisteredInstances);
    }

    @Test
    void givenMaxRegisteredInstances_shouldThrowException(){
        for(int i=0; i<10; i++){
            loadBalancer.registerLoadBalancer("http://localhost:808"+i);
        }
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            loadBalancer.registerLoadBalancer("http://localhost:8888");
        });
        assertEquals("Can not register anymore instances", illegalArgumentException.getMessage());
    }

    @Test
    void getRandomInstance_whenNoInstancesRegistered_shouldThrowException(){
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> loadBalancer.get());
        assertEquals("There are no instances", illegalArgumentException.getMessage());
    }

    @Test
    void getRandomInstance_whenThereAreRegisteredInstances(){
        for(int i=0; i<10; i++){
            loadBalancer.registerLoadBalancer("http://localhost:808"+i);
        }
        BackendInstance backendInstance = loadBalancer.get();
        assertNotNull(backendInstance);
    }

    @Test
    void getRandomInstances_whenMultipleInstancesRegistered(){
        for(int i=0; i<10; i++){
            loadBalancer.registerLoadBalancer("http://localhost:808"+i);
        }
        when(random.nextInt(anyInt())).thenReturn(5);
        BackendInstance backendInstance = loadBalancer.get();
        assertNotNull(backendInstance);
        verify(random, times(1)).nextInt(anyInt());
    }

}
