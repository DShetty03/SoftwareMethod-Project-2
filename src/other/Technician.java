package other;

/**
 * This class extends Provider and represents a Technician.
 * A Technician has a rate per visit.
 *
 * @author Divit Shetty (dps190)
 */
public class Technician extends Provider {

    private int ratePerVisit;

    /**
     * Constructor for the Technician class.
     *
     * @param profile The profile of the technician (from Provider)
     * @param location The practice location of the technician (from Provider).
     * @param ratePerVisit The rate the technician charges per visit.
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location);
        this.ratePerVisit = ratePerVisit;
    }



    /**
     * Get the rate per visit for the technician.
     *
     * @return The rate per visit for this technician.
     */
    @Override
    public int rate() {
        return this.ratePerVisit;
    }

    /**
     * Set the rate per visit for the technician.
     *
     * @param ratePerVisit The new rate per visit for the technician.
     */
    public void setRatePerVisit(int ratePerVisit) {
        this.ratePerVisit = ratePerVisit;
    }


    /**
     * Return a string representation of the technician's profile and rate.
     *
     * @return A string representing the technician's profile and rate.
     */
    @Override
    public String toString() {
        return super.toString() + ", Rate per visit: $" + this.ratePerVisit;
    }
}
