package co.projectcodex.hackathon;

import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    static Jdbi getJdbiDatabaseConnection(String defualtJdbcUrl) throws URISyntaxException, SQLException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String database_url = processBuilder.environment().get("DATABASE_URL");
        if (database_url != null) {

            URI uri = new URI(database_url);
            String[] hostParts = uri.getUserInfo().split(":");
            String username = hostParts[0];
            String password = hostParts[1];
            String host = uri.getHost();

            int port = uri.getPort();

            String path = uri.getPath();
            String url = String.format("jdbc:postgresql://%s:%s%s", host, port, path);

            return Jdbi.create(url, username, password);
        }

        return Jdbi.create(defualtJdbcUrl);

    }

    public static void main(String[] args) {


        staticFiles.location("/public");
        port(getHerokuAssignedPort());
        try {
            Jdbi jdbi = getJdbiDatabaseConnection("jdbc:postgresql://localhost/spark_hbs_jdbi?username=sesethu&password=coder123");

            get("/", (req, res) -> {

                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "elogin.handlebars");

            }, new HandlebarsTemplateEngine());

            get("/eDoctor", (req, res) -> {

                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "edoctor.handlebars");

            }, new HandlebarsTemplateEngine());

            get("/ePatient", (req, res) -> {

                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "patient.handlebars");


            }, new HandlebarsTemplateEngine());

        post("/eDoctor", (req, res) -> {

            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "eDoctor.handlebars");

        }, new HandlebarsTemplateEngine());


            post("/ePharmacy", (req, res) -> {

                List<Person> fullName = jdbi.withHandle((h) ->{
                    List<Person> theName= h.createQuery("select first_name,last_name from patients").mapToBean(Person.class).list();

                    return theName;

                });


                List<Person> prescriptionBody = jdbi.withHandle((h) ->{
                    List<Person> prescription= h.createQuery("select medicine_name from prescriptions").mapToBean(Person.class).list();

                    return prescription;

                });


                List<Person> doctorWhoGavePrescription = jdbi.withHandle((h) ->{
                    List<Person> prescriptionIssuedBy= h.createQuery("select doctors_name from doctors" +
                                    "join prescriptions on doctors.doctors_id=prescription.doctors_id" +
                                    "join doctors on prescriptions.doctors_id=doctors.doctors_id"
                            ).mapToBean(Person.class).list();

                    return prescriptionIssuedBy;

                });


                Map<String, Object> map = new HashMap<>();
                map.put("fullName",fullName);
                map.put("prescription",prescriptionBody);
                map.put("doctorWhoGavePrescription",doctorWhoGavePrescription);


                return new ModelAndView(map, "epharmacy.handlebars");

            }, new HandlebarsTemplateEngine());

            post("/eLogin", (req, res) -> {

                // create the greeting message
                String role = req.queryParams("role");


                switch (role) {
                    case "eDoctor":
                        res.redirect("/eDoctor");
                        break;

                    case "ePatient":
                        res.redirect("/ePatient");
                        break;

                    case "ePharmacy":
                        res.redirect("/ePharmacy");
                        break;

                    default:
                        break;
                }


                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "elogin.handlebars");

            }, new HandlebarsTemplateEngine());

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
