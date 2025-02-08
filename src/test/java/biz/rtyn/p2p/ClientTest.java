package biz.rtyn.p2p;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    public void testSetClients() {

        ArrayList<Client> clients = new ArrayList<>();
        clients.add(new Client(1, "localhost", 8100));
        clients.add(new Client(2, "localhost", 8101));
        clients.add(new Client(3, "localhost", 8101));

        Client client = clients.getFirst();
        client.setClients(clients);

        assertEquals(client.getClients().size(), clients.size() - 1);

    }

    @Test
    public void testSendSecrets() {

        ArrayList<Client> clients = new ArrayList<>();

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

        // one client to init sending
        clients.getFirst().sendSecrets(0);

        for (Client client: clients) {
            ArrayList<Integer> clientSecrets = client.getClientSecrets();
            for (int clientSecret: clientSecrets) {
                assertNotNull(clientSecret);
            }
        }

    }

    @Test
    public void testGeneratePublicValue() {

        ArrayList<Client> clients = new ArrayList<>();
        Client client1 = new Client(1, "localhost", 8101);
        Client client2 = new Client(2, "localhost", 8102);
        Client client3 = new Client(3, "localhost", 8103);

        clients.add(client1);
        clients.add(client2);
        clients.add(client3);

        client1.setClients(clients);
        client2.setClients(clients);
        client3.setClients(clients);

        client1.setSecret(client2.getId(), 3, false);
        client2.setSecret(client1.getId(), 3, false);

        client1.setSecret(client3.getId(), 4, false);
        client3.setSecret(client1.getId(), 4, false);

        client2.setSecret(client3.getId(), 5, false);
        client3.setSecret(client2.getId(), 5, false);

        assertEquals(client1.generatePublicValue(), 7);
        assertEquals(client2.generatePublicValue(), 6);
        assertEquals(client3.generatePublicValue(), 1);



    }
}