package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import semesterprojektgalgeleg.MainInterface;

@WebService(endpointInterface = "semesterprojektgalgeleg.MainInterface")
public class GameLogic implements MainInterface {
    private ArrayList<String> muligeOrd = new ArrayList<String>();
    private String ordet;
    ArrayList<String> brugteBogstaver = new ArrayList<String>();
    private String synligtOrd;
    private int antalForkerteBogstaver;
    int antalForsoeg = 6;
    int antalForsoegTilbage = 6;
    private boolean sidsteBogstavVarKorrekt;
    private boolean spilletErVundet;
    private boolean spilletErTabt;
    
    
    
    public ArrayList<String> getBrugteBogstaver() {
        return brugteBogstaver;
    }
    
    public String getSynligtOrd() {
        return synligtOrd;
    }
    
    public String getOrdet() {
        return ordet;
    }
    
    public int getAntalForkerteBogstaver() {
        return antalForkerteBogstaver;
    }
    
    public boolean erSidsteBogstavKorrekt() {
        return sidsteBogstavVarKorrekt;
    }
    
    public boolean erSpilletVundet() {
        return spilletErVundet;
    }
    
    public boolean erSpilletTabt() {
        return spilletErTabt;
    }
    
    public boolean erSpilletSlut() {
        return spilletErTabt || spilletErVundet;
    }
    
    
    public GameLogic() {
        try {
            hentOrdFraDr();
        } catch (Exception ex) {
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
    
    
    
    public void nulstil() {
        brugteBogstaver.clear();
        antalForkerteBogstaver = 0;
        spilletErVundet = false;
        spilletErTabt = false;
       // ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
       ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
       opdaterSynligtOrd();

    }
    
    
    public void opdaterSynligtOrd() {
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
    
    public void gætBogstav(String bogstav) {
        
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
        opdaterSynligtOrd();
    }
    
    public int hentAntalForsoeg(){
        return antalForsoegTilbage;
    }
    
    public void logStatus() {
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
        nulstil();
    }
}
