package server;


import brugerautorisation.transport.soap.Brugeradmin;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.util.Scanner;
import semesterprojektgalgeleg.MainInterface;
import javax.xml.ws.Endpoint;

public class MainServer {
    public static void main(String[] args) throws MalformedURLException {
        
        System.out.println("Start server!");
        MainInterface game = new GameLogic();
        
        
        Endpoint.publish("http://[::]:9978/galgeleg", game);
       System.out.println("Bottom of the server code!");
}

}