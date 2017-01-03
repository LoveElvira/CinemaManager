package com.yyjlr.tickets.service;

/**
 * Created by Elvira on 8/5/16.
 */
public class RequestData {
    private String cmd;
    private String token;
    private String language;
    private String appDomain;
    private String appPushToken;
    private String appVersion;
    private String clientIdentifierCode;
    private String locationXy;
    private String resolution;

    public String getAppDomain() {
        return appDomain;
    }

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }

    public String getAppPushToken() {
        return appPushToken;
    }

    public void setAppPushToken(String appPushToken) {
        this.appPushToken = appPushToken;
    }

    public String getClientIdentifierCode() {
        return clientIdentifierCode;
    }

    public void setClientIdentifierCode(String clientIdentifierCode) {
        this.clientIdentifierCode = clientIdentifierCode;
    }

    public String getLocationXy() {
        return locationXy;
    }

    public void setLocationXy(String locationXy) {
        this.locationXy = locationXy;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    private IRequestMainData parameters;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public IRequestMainData getParameters() {
        return parameters;
    }

    public void setParameters(IRequestMainData parameters) {
        this.parameters = parameters;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }


    @Override
    public String toString() {
        if (parameters != null) {
            return "?cmd=" + cmd + "&token=" + token + "&" + parameters.toString();
        } else {
            return "?cmd=" + cmd + "&token=" + token;
        }
    }
}
