package biz.rtyn.p2p;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    
    private String id;
    
    private String hostname;
    private int port;
    private ArrayList<Node> nodesList = new ArrayList<Node>();

    public Client(String hostname, int port, String id) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;

        // start server
        new Thread(() -> startServer(this, port)).start();

    }

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;

        // start server
        new Thread(() -> startServer(this, port)).start();

    }


    private static void startServer(Client thisClient, int port) {

        // set up server socket
        try (ExecutorService executor = Executors.newFixedThreadPool(10);
            ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(() -> handleIncomingRequest(thisClient, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handleIncomingRequest(Client thisClient, Socket socket) {

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {

            // Read request line
            // e.g. "BIZ.RTYN.DCP/1 200 OK"
            String requestLine = reader.readLine();
            System.out.println("Received: " + requestLine);

            String[] parts = requestLine.split("\\s+");
            String version = parts[0];
            String method = parts[1];
            String[] args = Arrays.copyOfRange(parts, 2, parts.length);

//            String header;
//            while ((header = reader.readLine()) != null && !header.isEmpty()) {
//                System.out.println(header);
//            }

            switch (method) {

                // return peer network list
                case "JOIN_NETWORK":

                    // add the joining node to nodesList
                    String id = UUID.randomUUID().toString();
                    String hostname = args[0];
                    int port = Integer.parseInt(args[1]);
                    thisClient.addNode(id, hostname, port);

                    // TODO broadcast new node

                    // output headers + body
                    writer.write("BIZ.RTYN.DCP/1 200 OK\n");

                    // assign ID
                    writer.write(id + "\n"); // new node id

                    // output nodesList
                    for (Node node: thisClient.nodesList) {
                        writer.write(node.getId() + ":" + node.getHostname() + ":" + node.getPort() + "\n");
                    }

                    writer.flush();

                    break;

//                // exchange secrets
//                case "SECRET_EXCHANGE":
//
//                    int fromClientId = Integer.parseInt(args[0]);
//                    int secret = Integer.parseInt(args[1]);
//                    boolean propogate = Integer.parseInt(args[2]) == 1;
//
////                    System.out.println(".. fromClientId: " + fromClientId);
////                    System.out.println(".. secret: " + secret);
////                    System.out.println(".. propogate: " + propogate);
//
//                    thisClient.setSecret(fromClientId, secret, propogate);
//
////                    // output headers + body
////                    writer.write("BIZ.RTYN.DCP/1 200 OK");
////                    writer.flush();
//
//                    break;
            }

        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }

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

    public void joinNetwork(String knHostname, int knPort) {

        try (Socket clientSocket = new Socket(knHostname, knPort);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // Send message to the server
            String message = "BIZ.RTYN.DCP/1 JOIN_NETWORK " + hostname + " " + port;

            System.out.println("Sending: " + message);

            writer.write(message);
            writer.newLine();  // Ensure the server can detect the end of the message
            writer.flush();

            // Receive response from the server
            // e.g. BIZ.RTYN.DCP/1 200 OK
            String responseLine = reader.readLine();
            System.out.println("Server Response: " + responseLine);
            String[] parts = responseLine.split("\\s+");
//            String version = parts[0];
//            String statusCode = parts[1];
//            String statusMessage = parts[2];

            // next line is the assigned id
            this.id = reader.readLine();
            System.out.println("  " + this.id);

            nodesList.clear();
            String nodeLine;
            while ((nodeLine = reader.readLine()) != null) {
                System.out.println("  " + nodeLine);

                String[] nodeParts = nodeLine.split(":");
                String id = nodeParts[0];
                String hostname = nodeParts[1];
                int port = Integer.parseInt(nodeParts[2]);
                nodesList.add(new Node(id, hostname, port));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addNode(String id, String hostname, int port) {
        nodesList.add(new Node(id, hostname, port));
    }

//     public void sendSecrets() {

//         int limit = Math.max(1, nodesList.size() - 1); // max
//         int count = 0;
//         boolean propogate = true;
//         for (int i = 0; i < nodesList.size() && count < limit; i++) {
//             if (clientSecrets.get(i) == null) {
//                 Client clientToSendTo = nodesList.get(i);

//                 Random random = new Random();
//                 int secret = random.nextInt(256);

//                 clientSecrets.set(i, secret);
//                 sendSecret(clientToSendTo, secret, propogate);

//                 count++;
//                 propogate = false;
//             }
//         }

//     }

//     // client socket
//     void sendSecret(Client clientToSendTo, int secret, boolean propogate) {
//         String hostname = clientToSendTo.getHostname();
//         int port = clientToSendTo.getPort();

//         try (Socket clientSocket = new Socket(hostname, port);
//              BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//              BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

//             // Send message to the server
//             String message = "BIZ.RTYN.DCP/1 SECRET_EXCHANGE " + this.getId() + " " + secret + " " + (propogate ? "1" : "0") + "\n";

//             System.out.println("Sending (" + this.getId() + "): " + message);

//             writer.write(message);
//             writer.newLine();  // Ensure the server can detect the end of the message
//             writer.flush();

// //            // Receive response from the server
// //            String serverResponse = reader.readLine();
// //            System.out.println("Server Response: " + serverResponse);

//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//     }

    // // replace with server socket
    // void setSecret(int fromClientId, int secret, boolean propogate) {

    //     Client fromClient = null;
    //     for(Client client: clients) {
    //         if (client.getId() == fromClientId) {
    //             fromClient = client;
    //         }
    //     }

    //     for (int i = 0; i < nodesList.size(); i++) {
    //         Client client = nodesList.get(i);
    //         if (client == fromClient) {
    //             clientSecrets.set(i, secret);
    //         }
    //     }

    //     if (propogate) {
    //         sendSecrets();
    //     }
    // }

    // public int generatePublicValue() {
    //     int result = 0;  // XOR identity value
    //     for (int node : nodeList) {
    //         result ^= node.getSecret();  // XOR with each element
    //     }
    //     return result;
    // }
}
