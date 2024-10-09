package other;

/**
 * This class represents a Patient, which extends the Person class.
 * A Patient has a profile and a linked list of visits.
 *
 * Author: Divit Shetty (dps190)
 */
public class Patient extends Person {

    private Visit visit;

    /**
     * Constructor for the Patient class.
     *
     * @param profile The profile of the patient.
     */
    public Patient(Profile profile) {
        super(profile);
        this.visit = null; // No visits initially
    }

    /**
     * Get the visit information for the patient.
     *
     * @return The visit associated with this patient.
     */
    public Visit getVisit() {
        return this.visit;
    }

    /**
     * Set the visit information for the patient.
     *
     * @param visit The visit to be associated with this patient.
     */
    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    /**
     * Check if two Patient objects are equal based on their profile.
     *
     * @param obj The object to compare.
     * @return true if the profiles are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Patient)) {
            return false;
        }
        Patient otherPatient = (Patient) obj;
        return this.profile.equals(otherPatient.profile);
    }

    /**
     * Return a string representation of the patient's profile.
     *
     * @return A string representing the patient's profile.
     */
    @Override
    public String toString() {
        return "Patient: " + this.profile.toString();
    }
}
