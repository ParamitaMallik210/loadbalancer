package org.example;

import org.example.utils.BackendServers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable{
    private Socket clientSocket;
    public ClientSocketHandler(final Socket socket){
        this.clientSocket= socket;
    }
    @Override
    public void run() {

        //this scoket will form a conctn with the client
        //it'll acpt th data from the client & frwrd to bcknd service
        //whtvr it gets from the bcknd servr it will frwrd it to the client
        //lets frst get the input stream frm client so tht this ld blncr cn get the dta tht clnt is sending

        try{
            InputStream clientToLoadBalancerInputstream = clientSocket.getInputStream();
            OutputStream loadBalancerToClientOutputStream = clientSocket.getOutputStream();


            //after getting the stream now we need the bcknd host

            String backendHost = BackendServers.getHost();
            System.out.println("Host selected to handle this request : "+backendHost);

            //now lets create a socket rqst basically a tcp cnctn to the bcknd server
            //TCP conctn to the backend server

            Socket backendSocket= new Socket(backendHost,8080);
            //inputstrea is to get the data from the client
            //as well get the data from the load balancer
            InputStream backendServerToLBIS= backendSocket.getInputStream();
             //output stream is to send data to the client from the loadbalancer
            //as well to send the data from the loadbalancer to the backend server
            OutputStream lbToBackendServerOS = backendSocket.getOutputStream();


            // Now whtvr th data is cming frm thr client  we shd parse the data from the load balancer to th bcknd server
            //& whtvr the data is cming from the bcknd server to load balancer parse the data to the client

            Thread clientDataHandler =new Thread(){
                public void run(){
                    try{
                        int data;
                        while((data=clientToLoadBalancerInputstream.read())!=-1){
                            lbToBackendServerOS.write(data);
                        }
                    } catch (IOException ex) {
                       ex.printStackTrace();
                    }
                }
            };
            //this thread will byte by byte from thr client and pass to bcknd server
            clientDataHandler.start();

            Thread backendDataHandler= new Thread(){
                public void run(){

                    try{
                        int data;
                        while((data=backendServerToLBIS.read())!=-1){
                            loadBalancerToClientOutputStream.write(data);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            //this thread will  read data bye byte from the backnd server to lb & pass to client
            backendDataHandler.start();












            //lets hv a bcknd cnctn
            //for tht we need to slct a bcknd host --->backend server.java



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
