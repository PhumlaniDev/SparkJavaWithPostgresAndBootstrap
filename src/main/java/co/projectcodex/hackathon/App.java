package co.projectcodex.hackathon;

import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder() ;
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

        port(getHerokuAssignedPort());

        List<Prescription> prescriptionList = new ArrayList<>();
        Map<String, Object> appointmentsMap = new HashMap<>();
        Map<String, Object> prescriptionMap = new HashMap<>();

        try {
            Jdbi jdbi = getJdbiDatabaseConnection("jdbc:postgresql://localhost/spark_hbs_jdbi");

            get("/", (req, res) -> {

                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "elogin.handlebars");

            }, new HandlebarsTemplateEngine());

            get("/eDoctor", (req, res) -> {

                String firstName = req.queryParams("firstName");
                String lastName = req.queryParams("lastName");
                String docName = req.queryParams("docName");
                String location = req.queryParams("location");

                appointmentsMap.get(firstName);
                appointmentsMap.get(lastName);
                appointmentsMap.get(docName);
                appointmentsMap.get(location);

                return new ModelAndView(appointmentsMap, "edoctor.handlebars");

            }, new HandlebarsTemplateEngine());

            post("/eDoctor", (req, res) -> {

                String patient_name = req.queryParams("patient_name");
                String medicine_name = req.queryParams("medicine_name");
                String doctors_name = req.queryParams("doctors_name");

                prescriptionMap.put("patient_name", patient_name);
                prescriptionMap.put("medicine_name", medicine_name);
                prescriptionMap.put("doctors_name", doctors_name);

                return new ModelAndView(prescriptionMap, "edoctor.handlebars");

            }, new HandlebarsTemplateEngine());

            get("/ePatient", (req, res) -> {

                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "epatient.handlebars");

            }, new HandlebarsTemplateEngine());

            post("/ePatient", (req, res) -> {


                String firstName = req.queryParams("firstName");
                String lastName = req.queryParams("lastName");
                String docName = req.queryParams("docName");
                String location = req.queryParams("location");

                appointmentsMap.put("firstName", firstName);
                appointmentsMap.put("lastName", lastName);
                appointmentsMap.put("docName", docName);
                appointmentsMap.put("location", location);

                res.redirect("/ePatient");
                return new ModelAndView(appointmentsMap, "epatient.handlebars");

            }, new HandlebarsTemplateEngine());

            get("/ePharmacy", (req, res) -> {

                String patient_name = req.queryParams("patient_name");
                String medicine_name = req.queryParams("medicine_name");
                String doctors_name = req.queryParams("doctors_name");

                prescriptionMap.get(patient_name);
                prescriptionMap.get(medicine_name);
                prescriptionMap.get(doctors_name);

                return new ModelAndView(prescriptionMap, "epharmacy.handlebars");
            }, new HandlebarsTemplateEngine());
            post("/ePharmacy", (req, res) -> {

                String patient_name = req.queryParams("patient_name");
                String medicine_name = req.queryParams("medicine_name");
                String doctors_name = req.queryParams("doctors_name");

                prescriptionList.add(new Prescription(patient_name, doctors_name, medicine_name));
//                map.put("people", people);

                System.out.println(req.body());
                prescriptionMap.put("prescriptions", prescriptionList);
//                prescriptionMap.put("medicine_name",medicine_name);
//                prescriptionMap.put("doctors_name",doctors_name);

                return new ModelAndView(prescriptionMap, "epharmacy.handlebars");
            }, new HandlebarsTemplateEngine());


            post("/ePharmacy", (req, res) -> {

                String patient_name = req.queryParams("patient_name");
                String medicine_name = req.queryParams("medicine_name");
                String doctors_name = req.queryParams("doctors_name");

                prescriptionMap.put("patient_name", patient_name);
                prescriptionMap.put("medicine_name", medicine_name);
                prescriptionMap.put("doctors_name", doctors_name);
                return new ModelAndView(prescriptionMap, "epharmacy.handlebars");

            }, new HandlebarsTemplateEngine());

            post("/eLogin", (req, res) -> {

                // create the greeting message
                String role = req.queryParams("role");

                if (!role.isEmpty()){
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
                }

                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "elogin.handlebars");

            }, new HandlebarsTemplateEngine());

            get("/graph", (req, res) -> {

//                List<Person> people = jdbi.withHandle((h) -> {
//                    List<Person> thePeople = h.createQuery("select first_name, last_name, email from users")
//                            .mapToBean(Person.class)
//                            .list();
//                    return thePeople;
//                });


                Map<String, Object> map = new HashMap<>();
                map.put("data", "[5, 2, 12]");
                map.put("theGraphLabel", "The graph label");
                map.put("labels", "['Medium', 'Low', 'High']");

                return new ModelAndView(map, "graph.handlebars");

            }, new HandlebarsTemplateEngine());

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }


    }
}
