package com.coding.practice;
public class BackendInstance {
    private String ipAddress;

    public BackendInstance(String ipAddress){
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
