/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package semesterprojektgalgeleg;

import brugerautorisation.transport.soap.Brugeradmin;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Andreas
 */
public class Login {
    
    String fornavn = "";
    String efternavn = "";
    
    public void logintjek() throws MalformedURLException {
        URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
        QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
        Service service = Service.create(url, qname);
        Brugeradmin ba = service.getPort(Brugeradmin.class);
        boolean tjek = false;
        while(tjek == false){
            System.out.println("Enter username");
            Scanner scan = new Scanner(System.in);
            String username = scan.nextLine();
            
            System.out.println("Enter password");
            Scanner scan2 = new Scanner(System.in);
            String password = scan.nextLine();
            
            if(ba.hentBruger(username, password)!=null){
                fornavn = ba.hentBruger(username, password).fornavn;
                efternavn = ba.hentBruger(username, password).efternavn;
                
                System.out.println("Du er logget ind som " + hentNavn());
                tjek = true;
            }else{
                System.out.println("prøv igen!");
            }}
    }
    
    
    public String hentNavn(){ 
        return fornavn + " " + efternavn;
    }
    
    
}
