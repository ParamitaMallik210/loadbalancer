package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class LoadBalancer
{
    public static void main( String[] args ) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        System.out.println( "Load Balancer started at port : "+8081 );

        while(true){
            Socket socket = serverSocket.accept();
            //this wait untill a client make rqst to this loadbalancer
            //three-way handshake take place and a connection is established
            System.out.println("TCP connection established with client" +socket.toString());
            //socket.tostring will give the client data
            //lets create thread to handle each tcp connection
            //each lient connection will create a thread that will handle the client rqst & tht thrd will cnct to the backend host & gv response

            handleSocket(socket);
        }
    }

    private static void handleSocket(Socket socket) {
        ClientSocketHandler clientSocketHandler =new ClientSocketHandler(socket);
        Thread clientSocketHandlerThread =new Thread(clientSocketHandler);
        clientSocketHandlerThread.start();
    }
}
//whnvr a client is nct the lb ,lb accpt the tcp cnxn & fgr it out one of th 2 IPR / 2 bcknd service
//or 2 remote host .it will pick one of the remote host thn crt out tcp cnxn to thr bcknf srvice & wtvr data it
//gets from the client it will pass to the srvr & srvr will process & return rspnse & thn lb return the sm response
//wthout chnging anythng to the client
//now lets deploy
// we will post the demo webservice on couple of ip ad on couple of posts and thn add their ad in the static block
//of the -->backndservers.java
//then dploy the lb in one of the server & thn we will try to accss the lb thrugh curl command