/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import stamboom.domain.Administratie;
import stamboom.domain.Gezin;
import stamboom.domain.Persoon;

public class DatabaseMediator implements IStorageMediator {

    private Properties props;
    private Connection conn;

    
    @Override
    public Administratie load() throws IOException {
        //todo opgave 4
        return null;
    }

    @Override
    public void save(Administratie admin) throws IOException {
        //todo opgave 4
        if (this.isCorrectlyConfigured() == false)
        {
            throw new IOException("Incorectly configured");
        }
        
        try
        {
            //Initialize
            this.initConnection();
            
            //Delete everything
            Statement deletePersonen = conn.createStatement();
            Statement deleteGezinnen = conn.createStatement();
            
            deletePersonen.execute("DELETE * FROM personen");
            deleteGezinnen.execute("DELETE * FROM gezinnen");
            
            //Insert
            Statement insertPersoon = conn.createStatement();
            String baseInsertPersoonQuery = "INSERT INTO personen (persoonsNummer, achternaam, voornamen, tussenvoegsel, geboortedatum, geboorteplaats, geslacht, ouders) VALUES (";
            String insertPersoonQuery = "";
            
            for(Persoon p : admin.getPersonen())
            {
                insertPersoonQuery = baseInsertPersoonQuery;
                insertPersoonQuery += p.getNr() + ", ";
                insertPersoonQuery += p.getAchternaam() + ", ";
                insertPersoonQuery += p.getVoornamen() + ", ";
                insertPersoonQuery += p.getTussenvoegsel() + ", ";
                insertPersoonQuery += p.getGebDat() + ", ";
                insertPersoonQuery += p.getGebPlaats() + ", ";
                insertPersoonQuery += p.getGeslacht() + ", ";
                
                if (p.getOuderlijkGezin() == null)
                {
                    insertPersoonQuery += "null";
                }
                else
                {
                    insertPersoonQuery += p.getOuderlijkGezin().getNr();
                }
                
                insertPersoonQuery += ")";
                
                insertPersoon.execute(insertPersoonQuery);
            }
            
            //Insert
            Statement insertGezin = conn.createStatement();
            String baseInsertGezinQuery = "INSERT INTO gezinnen (gezinsNummer, ouder1, ouder2, huwelijksdatum, scheidingsdatum) VALUES (";
            String insertGezinQuery = "";
            
            for(Gezin g : admin.getGezinnen())
            {
                insertGezinQuery = baseInsertGezinQuery;
                insertGezinQuery += g.getNr() + ", ";
                insertGezinQuery += g.getOuder1().getNr() + ", ";
                
                if (g.getOuder2() == null)
                {
                    insertGezinQuery += "null" + ", ";
                }
                else
                {
                    insertGezinQuery += g.getOuder2().getNr() + ", ";
                }
                
                if (g.getHuwelijksdatum() == null)
                {
                    insertGezinQuery += "null" + ", ";
                }
                else
                {
                    insertGezinQuery += g.getHuwelijksdatum() + ", ";
                }
                
                if (g.getHuwelijksdatum() == null)
                {
                    insertGezinQuery += "null";
                }
                else
                {
                    insertGezinQuery += g.getScheidingsdatum();
                }
                
                
                insertGezinQuery += ")";
                
                insertGezin.execute(insertGezinQuery);
            }
            
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    @Override
    public final boolean configure(Properties props) {
        this.props = props;

        try {
            initConnection();
            return isCorrectlyConfigured();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            this.props = null;
            return false;
        } finally {
            closeConnection();
        }
    }

    @Override
    public Properties config() {
        return props;
    }

    @Override
    public boolean isCorrectlyConfigured() {
        if (props == null) {
            return false;
        }
        if (!props.containsKey("driver")) {
            return false;
        }
        if (!props.containsKey("url")) {
            return false;
        }
        if (!props.containsKey("username")) {
            return false;
        }
        if (!props.containsKey("password")) {
            return false;
        }
        return true;
    }

    private void initConnection() throws SQLException {
        //opgave 4
        String url = "";
        String username = "";
        String password = "";
        
        try
        {
            url = this.props.getProperty(url);
            username = this.props.getProperty(username);
            password = this.props.getProperty(password);
            
            if (!url.equals("") && !username.equals("") && !password.equals(""))
            {
                this.conn = DriverManager.getConnection(url, username, password);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }
        
        
    }

    private void closeConnection() {
        try {
            conn.close();
            conn = null;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
