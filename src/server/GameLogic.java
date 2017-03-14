package server;

import brugerautorisation.transport.soap.Brugeradmin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.security.auth.login.LoginException;
import semesterprojektgalgeleg.MainInterface;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

@WebService(endpointInterface = "semesterprojektgalgeleg.MainInterface")
public class GameLogic implements MainInterface {
    private ArrayList<String> muligeOrd = new ArrayList<String>();
    private String ordet;
    private String uuid;
    ArrayList<String> brugteBogstaver = new ArrayList<String>();
    private String synligtOrd;
    private int antalForkerteBogstaver;
    int antalForsoeg = 6;
    int antalForsoegTilbage = 6;
    private boolean sidsteBogstavVarKorrekt;
    private boolean spilletErVundet;
    private boolean spilletErTabt;
    
    public ArrayList<String> getBrugteBogstaver(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return brugteBogstaver;
    }
    
    public String getSynligtOrd(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return synligtOrd;
    }
    
    public String getOrdet(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return ordet;
    }
    
    public ArrayList<String> login(String username, String password) throws LoginException{
        try {
            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service = Service.create(url, qname);
            Brugeradmin userAuth = service.getPort(Brugeradmin.class);
            
            ArrayList<String> userinfo = new ArrayList<String>();
            try {
                if(!userAuth.hentBruger(username, password).toString().isEmpty()){
                    userinfo.add(userAuth.hentBruger(username, password).fornavn);
                    userinfo.add(userAuth.hentBruger(username, password).efternavn);
                    uuid = UUID.randomUUID().toString();
                    userinfo.add(uuid);
                }
            } catch (Exception ex) {
//                Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
                throw new LoginException("Wrong username or password");
            }
            return userinfo;
        } catch (MalformedURLException ex) {
//            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
            throw new LoginException("No connection to login server");
        }
    }
    
    public int getAntalForkerteBogstaver(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return antalForkerteBogstaver;
    }
    
    public boolean erSidsteBogstavKorrekt(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return sidsteBogstavVarKorrekt;
    }
    
    public boolean erSpilletVundet(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return spilletErVundet;
    }
    
    public boolean erSpilletTabt(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return spilletErTabt;
    }
    
    public boolean erSpilletSlut(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return spilletErTabt || spilletErVundet;
    }
    
    
    public GameLogic() {
        try {
            hentOrdFraDr();
            
        } catch (Exception ex) {
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        nulstil();
    }
    
    private boolean stillLoggedIn (String uuid) throws LoginException {
        try {
            if (this.uuid.equals(uuid))
                return true;
            else 
                throw new LoginException("Not logged in");
        } catch (Exception ex){
            throw new LoginException("Not logged in");
        }
    }
    
    public void nulstil(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        nulstil();
    }
    private void nulstil(){
        brugteBogstaver.clear();
        antalForkerteBogstaver = 0;
        antalForsoegTilbage = 6;
        spilletErVundet = false;
        spilletErTabt = false;
       // ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
       ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
       opdaterSynligtOrd();
    }
    
    
    public void opdaterSynligtOrd(String uuid)  throws LoginException  {
        stillLoggedIn(uuid);
        opdaterSynligtOrd();
    }
    private void opdaterSynligtOrd() {
        synligtOrd = "";
        spilletErVundet = true;
        for (int n = 0; n < ordet.length(); n++) {
            String bogstav = ordet.substring(n, n + 1);
            if (brugteBogstaver.contains(bogstav)) {
                synligtOrd = synligtOrd + bogstav;
            } else {
                synligtOrd = synligtOrd + "*";
                spilletErVundet = false;
            }
        }
    }
    
    public void gætBogstav(String bogstav, String uuid) throws LoginException {
        stillLoggedIn(uuid);
        if (bogstav.length() != 1) return;
        System.out.println("Der gættes på bogstavet: " + bogstav);
        if (brugteBogstaver.contains(bogstav)) return;
        if (spilletErVundet || spilletErTabt) return;
        
        brugteBogstaver.add(bogstav);
        
        if (ordet.contains(bogstav)) {
         //if (ordet.contains(bogstav.toLowerCase()) || ordet.contains(bogstav.toUpperCase())) {

            sidsteBogstavVarKorrekt = true;
            System.out.println("Bogstavet var korrekt: " + bogstav);
        } else {
            // Vi gættede på et bogstav der ikke var i ordet.
            sidsteBogstavVarKorrekt = false;
            System.out.println("Bogstavet var IKKE korrekt: " + bogstav);
            antalForkerteBogstaver = antalForkerteBogstaver + 1;
            antalForsoegTilbage = antalForsoeg - antalForkerteBogstaver;
            if (antalForkerteBogstaver >= antalForsoeg) {
                spilletErTabt = true;
            }
        }
        opdaterSynligtOrd(uuid);
    }
    
    public int hentAntalForsoeg(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        return antalForsoegTilbage;
    }
    
    public void logStatus(String uuid) throws LoginException {
        stillLoggedIn(uuid);
        System.out.println("---------- ");
        System.out.println("- ordet (skult) = " + ordet);
        System.out.println("- synligtOrd = " + synligtOrd);
        System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
        System.out.println("- brugeBogstaver = " + brugteBogstaver);
        if (spilletErTabt) System.out.println("- SPILLET ER TABT");
        if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
        System.out.println("---------- ");
    }
    
    
    public static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }
    
    public void hentOrdFraDr() throws Exception {
        String data = hentUrl("http://dr.dk");
        System.out.println("data = " + data);
        
        data = data.replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ");
        System.out.println("data = " + data);
        muligeOrd.clear();
        muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
        
        System.out.println("muligeOrd = " + muligeOrd);
    }
}
