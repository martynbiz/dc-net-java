package biz.rtyn.p2p;

import java.util.ArrayList;
//import java.util.List;

public class Main {

    private static final ArrayList<Client> clients = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Starting...");

        clients.add(new Client(1, "localhost", 8101));
        clients.add(new Client(2, "localhost", 8102));
        clients.add(new Client(3, "localhost", 8103));
        clients.add(new Client(4, "localhost", 8104));
        clients.add(new Client(5, "localhost", 8105));
        clients.add(new Client(6, "localhost", 8106));
        clients.add(new Client(7, "localhost", 8107));
        clients.add(new Client(8, "localhost", 8108));
        clients.add(new Client(9, "localhost", 8109));
        clients.add(new Client(10, "localhost", 8110));
        clients.add(new Client(11, "localhost", 8111));
        clients.add(new Client(12, "localhost", 8112));
        clients.add(new Client(13, "localhost", 8113));

        for (Client client: clients) {
            client.setClients(clients);
        }

        // first round: exchange secrets
        clients.getFirst().sendSecrets();

        // second round, generate public xor
        int result = 0;
        for (Client client: clients) {
            result ^= client.generatePublicValue();
        }

        System.out.println("result: " + result);

    }
}