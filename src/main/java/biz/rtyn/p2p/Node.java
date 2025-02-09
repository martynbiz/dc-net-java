package biz.rtyn.p2p;

public class Node {

    private String id;

    private String hostname;
    private int port;

    public Node(String id, String hostname, int port) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
