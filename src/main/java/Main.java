import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * @author Céline MERAND
 * Created on 07/11/2018
 */

public class Main {

    public static void main(String[] args) {
        Connexion connexion = Connexion.getInstance();
        System.out.println("---------------MAVEN---------------");
        System.out.println("Version : " + connexion.getVersion());

        if(args.length == 1) {
            final List<Ville> villelist = readCSV(args[0]);
            System.out.println(" Récupération de " + villelist.size() + "villes en base de données.");

            BddConnector bddConnector = new BddConnector();
            bddConnector.saveOnDatabase(villelist);
        }
        else {
            System.out.println("Un paramètre est requis");
            System.exit(1);
        }
    }

    private static List<Ville> readCSV(String filename) {
        List<Ville> objects = new ArrayList<>();
        try(Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            final CSVParser parser = new CSVParserBuilder().withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build();

            String[] record;
            while((record = csvReader.readNext()) != null) {
                objects.add(new Ville(record));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERREUR : " + e);
            System.exit(1);
        }
        return objects;
    }
}
