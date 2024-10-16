package other;

/**
 * This class represents a Doctor, which extends the Provider class.
 * A Doctor has a specialty and an NPI (National Provider Identification).
 *
 * @author Divit Shetty (dps190)
 */
public class Doctor extends Provider {

    private Speciality speciality;
    private String npi;

    /**
     * Constructor for the Doctor class.
     *
     * @param profile The profile of the doctor.
     * @param location The practice location of the doctor.
     * @param speciality The specialty of the doctor.
     * @param npi The NPI (National Provider Identification) of the doctor.
     */
    public Doctor(Profile profile, Location location, Speciality speciality, String npi) {
        super(profile, location);
        this.speciality = speciality;
        this.npi = npi;
    }

    /**
     * Get the specialty of the doctor.
     *
     * @return The specialty of the doctor.
     */
    public Speciality getSpeciality() {
        return this.speciality;
    }

    /**
     * Get the NPI (National Provider Identification) of the doctor.
     *
     * @return The NPI of the doctor.
     */
    public String getNpi() {
        return this.npi;
    }

    /**
     * Set the NPI (National Provider Identification) of the doctor.
     *
     * @param npi The new NPI of the doctor.
     */
    public void setNpi(String npi) {
        this.npi = npi;
    }

    /**
     * Override the rate method to return the rate per visit for the doctor's specialty.
     *
     * @return The rate per visit based on the doctor's specialty.
     */
    @Override
    public int rate() {
        return this.speciality.getCharge();
    }

    /**
     * Return a string representation of the doctor's profile, specialty, and NPI.
     *
     * @return A string representing the doctor's details.
     */
    @Override
    public String toString() {
        return  super.toString() + "[" + speciality + " #" + this.npi + "]";
    }

}
