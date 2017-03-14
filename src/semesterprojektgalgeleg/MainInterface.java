/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package semesterprojektgalgeleg;
import java.io.IOException;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.security.auth.login.LoginException;
@WebService
public interface MainInterface {
    @WebMethod ArrayList<String> getBrugteBogstaver(String uuid) throws LoginException;
    @WebMethod ArrayList<String> login(String username, String password) throws LoginException;
    @WebMethod String getSynligtOrd(String uuid) throws LoginException;
    @WebMethod String getOrdet(String uuid) throws LoginException;
    @WebMethod int getAntalForkerteBogstaver(String uuid) throws LoginException;
    @WebMethod boolean erSidsteBogstavKorrekt(String uuid) throws LoginException;
    @WebMethod boolean erSpilletVundet(String uuid) throws LoginException;
    @WebMethod boolean erSpilletTabt(String uuid) throws LoginException;
    @WebMethod boolean erSpilletSlut(String uuid) throws LoginException;
    @WebMethod void nulstil(String uuid) throws LoginException;
    @WebMethod void opdaterSynligtOrd(String uuid) throws LoginException;
    @WebMethod void gætBogstav(String bogstav, String uuid) throws LoginException;
    @WebMethod void logStatus(String uuid) throws LoginException;
    @WebMethod int hentAntalForsoeg(String uuid) throws LoginException;
   // @WebMethod String hentUrl(String url) throws IOException;
    @WebMethod void hentOrdFraDr() throws Exception;
    //@WebMethod void login();
}
