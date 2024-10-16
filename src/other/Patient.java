package other;

/**
 * This class represents a Patient, which extends the Person class.
 * A Patient has a profile and a linked list of visits.
 *
 * @author Divit Shetty (dps190)
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
     * Adds a new visit to the patient's list of visits.
     * This method maintains a linked list of visits, ensuring
     * that the most recent visit is added at the end of the list.
     *
     * @param newVisit The visit to be added.
     *                 If it's the first visit, it will be set as the head of the list.
     *                 If there are existing visits, it will be appended at the end.
     */
    public void addVisit(Visit newVisit) {
        if (visit == null) {
            visit = newVisit;
        } else {
            Visit current = visit;
            // Traverse till the last visit in the list
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newVisit);  // Link the new visit at the end
        }
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
    //Set visit might be unnecessary so commenting it out
    /*
    public void setVisit(Visit visit) {
        this.visit = visit;
    }
    */

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
        return this.profile.toString();
    }
}
