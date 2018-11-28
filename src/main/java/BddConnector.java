import java.sql.*;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static java.util.logging.Level.INFO;

/**
 * @author Céline MERAND
 * Created on 28/11/2018
 */
public class BddConnector {
    private static final String TABLE = "ville_france";


    public BddConnector() {
        throw new UnsupportedOperationException("Empty constructor");
    }

    private Connection getConnection(){
        Connexion connexion = Connexion.getInstance();
        Connection con = null;
        try {
            con = DriverManager.getConnection(
                    connexion.getJdbc(),connexion.getUser(),connexion.getPassword());
        } catch (SQLException e) {
            throw new UnsupportedOperationException("SQLExceptions" + e);
        }

        return con;
    }

    public void saveOnDatabase(List<Ville> objects) {
        Connection con = this.getConnection();

        //If table not exist, create
        if(!this.checkTableExist(con)) {
            this.createTable(con);
        }

        String queryI = "INSERT INTO " + TABLE + " VALUES(?, ?, ?, ?, ?, ?, ?)";
        String queryU = "UPDATE " + TABLE + " SET Nom_commune=?, Code_postal=?, Libelle_acheminement=?, Ligne_5=?, Latitude=?, Longitude=? WHERE Code_commune_INSEE=?";
        try (PreparedStatement stI = con.prepareStatement(queryI); PreparedStatement stU = con.prepareStatement(queryU)){
            int requestsU = 0;
            int requestsI = 0;
            int max = objects.size();
            int current = 0;

            int pState = 0;
            int state;
            for(Ville obj : objects) {
                current++;
                state = current * 100 / max;

                if(state != pState) {
                    pState = state;
                    LOGGER.log(INFO,"Insertion des entités... " + state + "%");
                }
                if(this.existInDatabase(con, obj)) {
                    stU.setString(1, obj.getNomCommune());
                    stU.setString(2, obj.getCodePostale());
                    stU.setString(3, obj.getLibelleAcheminement());
                    stU.setString(4, obj.getLigne5());
                    stU.setString(5, obj.getLatitude());
                    stU.setString(6, obj.getLongitude());
                    stU.setString(7, obj.getCodeCommune());

                    stU.execute();
                    requestsU++;

                } else {
                    stI.setString(1, obj.getCodeCommune());
                    stI.setString(2, obj.getNomCommune());
                    stI.setString(3, obj.getCodePostale());
                    stI.setString(4, obj.getLibelleAcheminement());
                    stI.setString(5, obj.getLigne5());
                    stI.setString(6, obj.getLatitude());
                    stI.setString(7, obj.getLongitude());

                    stI.execute();
                    requestsI++;
                }
            }

            LOGGER.log(INFO, "Insertion de " + requestsI + " entrées");
            LOGGER.log(INFO, "Mise à jour de " + requestsU + " entrées");

        } catch (SQLException e) {
            throw new UnsupportedOperationException("SQLException " + e);
        }
    }

    public boolean existInDatabase(Connection connection, Ville obj) {
        String query = "SELECT * FROM " + TABLE + " WHERE Code_commune_INSEE = ?";
        boolean exist = false;

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, obj.getCodeCommune());
            final ResultSet resultSet = ps.executeQuery();

            exist = resultSet.next();
        } catch (SQLException e) {
            throw new UnsupportedOperationException("SQLException " + e);
        }

        return exist;
    }

    public boolean checkTableExist(Connection connection) {
        boolean exist = true;
        try(Statement st = connection.createStatement()) {
            st.executeQuery("SELECT * FROM " + TABLE + " LIMIT 1");
        } catch (SQLException e) {
            exist = false;
        }

        return exist;
    }

    public void createTable(Connection connection) {
        String query = "create table " + TABLE + " " +
                "( " +
                "  Code_commune_INSEE   varchar(255) not null " +
                "    primary key, " +
                "  Nom_commune          varchar(255) not null, " +
                "  Code_postal          varchar(255) not null, " +
                "  Libelle_acheminement varchar(255) not null, " +
                "  Ligne_5              varchar(255) not null, " +
                "  Latitude             varchar(255) not null, " +
                "  Longitude            varchar(255) not null " +
                ") " +
                "  engine = InnoDB;";

        try (Statement stmt = connection.createStatement()){
            stmt.execute(query);
        } catch (SQLException e) {
            throw new UnsupportedOperationException("SQLException " + e);
        }
    }
}
