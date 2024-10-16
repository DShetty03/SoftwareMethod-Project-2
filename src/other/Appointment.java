/*
 * @author Richard Li - rl902
 */
package other;

import util.Date;

/**
 * Represents an appointment with a patient and a provider on a specific date and time.
 * The appointment includes details such as the date, timeslot, patient profile, and provider.
 */
public class Appointment implements Comparable<Appointment> {
    protected Date date;
    protected Timeslot timeslot;
    protected Person patient;
    protected Person provider;

    /**
     * Constructs an Appointment instance with the specified date, timeslot, patient, and provider.
     *
     * @param date the date of the appointment
     * @param timeslot the timeslot for the appointment
     * @param patient the Profile of the patient associated with the appointment
     * @param provider the Provider assigned to the appointment
     */
    public Appointment(Date date, Timeslot timeslot, Person patient, Person provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Returns the date of the appointment.
     *
     * @return the date of the appointment
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the timeslot of the appointment.
     *
     * @return the timeslot of the appointment
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Returns the patient profile associated with the appointment.
     *
     * @return the patient profile
     */
    public Patient getPatient() {
        if (patient instanceof Patient) {
            return (Patient) patient;
        } else {
            throw new ClassCastException("The object is not an instance of Patient");
        }
    }

    /**
     * Returns the provider assigned to the appointment.
     *
     * @return the provider for the appointment
     */
    public Provider getProvider() {
        return ((Provider) provider);
    }

    public Location getLocation() {
        if (provider instanceof Provider) {
            return ((Provider) provider).getLocation();  // Cast to Provider to access getLocation()
        }
        return null;
    }

    /**
     * Calculates the cost of the appointment based on the provider's specialty.
     *
     * @return the cost of the appointment
     */
    public Speciality getSpeciality() {
        if (provider instanceof Doctor) {
            return ((Doctor) provider).getSpeciality();  // Cast to Doctor to access getSpeciality()
        }
        return null;  // Return null if provider is not a Doctor
    }

    @Override
    public int compareTo(Appointment other) {
        // Compare by date first
        int dateCompare = this.date.compareTo(other.date);
        if (dateCompare != 0) return dateCompare;

        // If dates are the same, compare by timeslot
        int timeslotCompare = this.timeslot.compareTo(other.timeslot);
        if (timeslotCompare != 0) return timeslotCompare;

        // If timeslot is the same, compare by patient
        return this.patient.compareTo(other.patient);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Appointment other = (Appointment) obj;

        if (this.date == null || other.date == null) return false;

        return this.date.equals(other.date) &&
                this.timeslot.equals(other.timeslot) &&
                this.patient.equals(other.patient);
    }

    @Override
    public String toString() {
        // Extract the provider's name
        String providerName = provider.toString();  // Assuming provider's toString provides the name

        // Extract the provider's location (town) if the provider is a type of Provider
        String providerTown = (provider instanceof Provider) ? ((Provider) provider).getLocation().toString() : "N/A";

        // Extract the provider's speciality if the provider is a Doctor
        String providerSpec = (provider instanceof Doctor) ? ((Doctor) provider).getSpeciality().toString() : "N/A";

        // Extract the patient details
        String patientDetails = patient.toString();  // Assuming patient's toString provides relevant details

        // Format the output string
        return String.format("%s %s %s %s",
                date.toString(),
                timeslot.toString(),
                patientDetails,
                providerName
        );
    }


    /**
     * Testbed main method to demonstrate the functionality of the Appointment class.
     *
     * @param args command-line arguments (not used)
     */
    /*
    public static void main(String[] args) {
        // Create test Profile and Patient objects (Works)
        Profile profile1 = new Profile("John", "Doe", new Date(1989, Date.DECEMBER, 13));
        Profile profile2 = new Profile("Jane", "Smith", new Date(1990, Date.NOVEMBER, 5));

        System.out.println("Profile1: " + profile1);

        // Create test Appointments (Works)
        Appointment appointment1 = new Appointment(new Date(2024, Date.SEPTEMBER, 30), Timeslot.SLOT1, profile1, Provider.PATEL);
        Appointment appointment2 = new Appointment(new Date(2024, Date.SEPTEMBER, 30), Timeslot.SLOT2, profile2, Provider.LIM);
        Appointment appointment3 = new Appointment(new Date(2024, Date.OCTOBER, 1), Timeslot.SLOT3, profile1, Provider.ZIMNES);

        // Test the equals method (Works)
        System.out.println("Testing equals method:");
        System.out.println("appointment1 equals appointment2: " + appointment1.equals(appointment2)); //Expect: false
        System.out.println("appointment1 equals appointment1: " + appointment1.equals(appointment1)); //Expect: true

        // Test the compareTo method (Works)
        System.out.println("\nTesting compareTo method:");
        System.out.println("appointment1 compared to appointment2: " + appointment1.compareTo(appointment2)); //negative
        System.out.println("appointment2 compared to appointment3: " + appointment2.compareTo(appointment3)); //negative

        // Test toString method (Works)
        System.out.println("\nTesting toString method:");
        System.out.println(appointment1);
        System.out.println(appointment2);
        System.out.println(appointment3);
    }


     */

}