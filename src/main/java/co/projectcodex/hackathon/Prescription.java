package co.projectcodex.hackathon;

public class Prescription {
    String patientName;
    String doctorName;
    String medicine;

    public Prescription() {
    }

    public Prescription(String patientName, String doctorName, String medicine) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.medicine = medicine;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
}
