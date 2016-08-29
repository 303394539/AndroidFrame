package com.baic.net.api;

/**
 * Created by baic on 16/4/21.
 */
public class ApiConfig {

    private boolean isLoggable;
    private String baseUrl;
    private String pathname;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public boolean isLoggable() {
        return isLoggable;
    }

    public void setIsLoggable(boolean isLoggable) {
        this.isLoggable = isLoggable;
    }
}
