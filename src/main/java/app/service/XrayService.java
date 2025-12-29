package app.service;

public interface XrayService {
    int runXray(String config);

    boolean isAlive();

    void stop();
}
