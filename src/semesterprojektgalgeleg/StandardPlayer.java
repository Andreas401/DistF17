/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package semesterprojektgalgeleg;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Thanh Pham
 */
public class StandardPlayer {
    public static void main(String[] args){
        try {
            
            // localhost bruges til test på egen computer og ubuntu4 bruges til når du tester
            // med anden server.
            //URL url = new URL("http://ubuntu4.javabog.dk:9978/galgeleg?wsdl");
            URL url = new URL("http://localhost:9978/galgeleg?wsdl");
            QName qname = new QName("http://server/", "GameLogicService");
            Service service = Service.create(url, qname);
            MainInterface gameService = service.getPort(MainInterface.class);
            
            String uuid = "";
            String firstname = "", lastname = "";

            while(uuid.isEmpty()){
                System.out.println("Indtast studienummer");
                String username = new Scanner(System.in).nextLine();
                
                System.out.println("Indtast password");
                String password = new Scanner(System.in).nextLine();
                
                try {
                    ArrayList<String> userinfo = gameService.login(username, password);
                    if (!userinfo.isEmpty()){
                        firstname = userinfo.get(0);
                        lastname = userinfo.get(1);
                        uuid = userinfo.get(2);
                    }
                } catch (LoginException ex) {
//                    System.out.println(ex.getMessage());
                    System.out.println("Der er ingen matchende bruger med de indtastede oplysninger. Prøv igen!");
                }
            }
            
            System.out.println("Du er logget ind som " + firstname + " " + lastname);
            
            try {
                gameService.nulstil(uuid);
                while(gameService.erSpilletSlut(uuid) == false){
                    System.out.println("" + gameService.getSynligtOrd(uuid));
                    gameService.logStatus(uuid);
                    System.out.println("Du har " + gameService.hentAntalForsoeg(uuid) + " forsøg tilbage!" );
                    System.out.println("Du har brugt disse bogstaver: " + gameService.getBrugteBogstaver(uuid));
                    
                    System.out.println("Indtast bogstav her: ");
                    Scanner scan = new Scanner(System.in);
                    String letter = scan.nextLine();
                    
                    gameService.gætBogstav(letter.toLowerCase(),uuid);
                    
//            System.out.println(l.hentNavn() + " gætter på: " + letter);


                }
                if(gameService.erSpilletTabt(uuid) == true){
                    System.out.println("Spillet er tabt!");
                }
                if(gameService.erSpilletVundet(uuid) == true){
                    System.out.println("Spillet er vundet!");
                }
                System.out.println("\nOrdet er følgende: " + gameService.getOrdet(uuid));
                
            } catch (LoginException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Du er ikke længere logget ind på serveren");
            }
            
        } catch (Exception ex) {
            System.err.println("Kunne ikke forbinde til serveren");
            Logger.getLogger(StandardPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
