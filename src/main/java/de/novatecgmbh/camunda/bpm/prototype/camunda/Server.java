package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

public class Server {
    private String name;
    private String server;
    private String port;

    private Server() {}

    public Server(String name, String server, String port) {
        this.name = name;
        this.server = server;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


}
