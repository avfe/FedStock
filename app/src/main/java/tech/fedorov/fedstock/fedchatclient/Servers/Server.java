package tech.fedorov.fedstock.fedchatclient.Servers;

import java.io.Serializable;

public class Server implements Serializable {
    private String IP;
    private String PORT;
    private String name;

    public Server(String name, String IP, String PORT) {
        this.name = name;
        this.IP = IP;
        this.PORT = PORT;
    }

    public String getIP() {
        return IP;
    }

    public String getPORT() {
        return PORT;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return IP + ":" + PORT;
    }
}
