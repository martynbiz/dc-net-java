package biz.rtyn.p2p;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;
//import java.util.List;

public class Client {
    
    private int id;
    
    private String hostname;
    private int port;
    private ArrayList<Client> clients = new ArrayList<>();

    private ArrayList<Integer> clientSecrets = new ArrayList<>();

    public Client(int id, String hostname, int port) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients.clear();
        clientSecrets.clear();
        for (Client client: clients) {
            if (client != this) {
                this.clients.add(client);
                clientSecrets.add(null);
            }
        }
    }

    public void sendSecrets() {
        sendSecrets(50);
    }

    public void sendSecrets(int millis) {

        try {

            int limit = Math.max(1, clients.size() - 1); // max
            int count = 0;
            boolean propogate = true;
            for (int i = 0; i < clients.size() && count < limit; i++) {
                if (clientSecrets.get(i) == null) {
                    Client clientToSendTo = clients.get(i);

                    Random random = new Random();
                    int secret = random.nextInt(256);

                    clientSecrets.set(i, secret);
                    sendSecret(clientToSendTo, secret, propogate, millis);

                    count++;
                    propogate = false;
                }

                // allow even distribution of sharing
                sleep(millis);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void sendSecret(Client clientToSendTo, int secret, boolean propogate, int millis) {
        clientToSendTo.setSecret(this.getId(), secret, propogate, millis);
    }

    void setSecret(int fromClientId, int secret, boolean propogate) {
        setSecret(fromClientId, secret, propogate, 50);
    }

    // replace with server socket
    void setSecret(int fromClientId, int secret, boolean propogate, int millis) {

        Client fromClient = null;
        for(Client client: clients) {
            if (client.getId() == fromClientId) {
                fromClient = client;
            }
        }

        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            if (client == fromClient) {
                clientSecrets.set(i, secret);
            }
        }

        if (propogate) {
            sendSecrets(millis);
        }
    }

    public ArrayList<Integer> getClientSecrets() {
        return clientSecrets;
    }

    public int generatePublicValue() {
        int result = 0;  // XOR identity value
        for (int clientSecret : clientSecrets) {
            result ^= clientSecret;  // XOR with each element
        }
        return result;
    }
}
