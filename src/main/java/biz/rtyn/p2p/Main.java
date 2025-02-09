package biz.rtyn.p2p;

import java.util.ArrayList;
import java.util.UUID;
//import java.util.List;

public class Main {

    private static final ArrayList<Client> nodes = new ArrayList<>();

    public static void main(String[] args) {

        // this will create a local test network environment to test
        initNetworkTestEnvironment();



        // on app start
        Client client = new Client("localhost", 8200);

        // join the network through known nodes
        client.joinNetwork("localhost", 8101);

//        System.out.println("Starting...");
//
//        // simulate env
//        nodes.add(new Client(1, "localhost", 8101));
//        nodes.add(new Client(2, "localhost", 8102));
//        nodes.add(new Client(3, "localhost", 8103));
//
//        System.out.println("setClients");
//        for (Client client: nodes) {
//            client.setNodes(nodes);
//        }

//        // first round: exchange secrets
//        System.out.println("sendSecrets");
//        clients.getFirst().sendSecrets();
//
//        // second round, generate public xor
//        int result = 0;
//        for (Client client: clients) {
//            result ^= client.generatePublicValue();
//        }
//
//        System.out.println("result: " + result);

    }

    private static void initNetworkTestEnvironment() {

        // network nodes
        // they will be available on the network and have their nodes known
        Client client1 = new Client("localhost", 8101, UUID.randomUUID().toString());
        Client client2 = new Client("localhost", 8102, UUID.randomUUID().toString());
//        Client client3 = new Client("localhost", 8103, UUID.randomUUID().toString());

        client1.addNode(client2.getId(), client2.getHostname(), client2.getPort());
//        client1.addNode(client3.getId(), client3.getHostname(), client3.getPort());
        
        client2.addNode(client1.getId(), client1.getHostname(), client1.getPort());
//        client2.addNode(client3.getId(), client3.getHostname(), client3.getPort());

//        client3.addNode(client1.getId(), client1.getHostname(), client1.getPort());
//        client3.addNode(client2.getId(), client2.getHostname(), client2.getPort());

    }
}