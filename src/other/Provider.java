package other;

/**
 * This abstract class represents a Provider, which extends the Person class.
 * A Provider has a profile and a practice location. This class also declares the
 * abstract method rate() to be implemented by subclasses.
 *
 * @author Divit Shetty (dps190)
 */
public abstract class Provider extends Person {

    private Location location;

    /**
     * Constructor for the Provider class.
     *
     * @param profile The profile of the provider.
     * @param location The practice location of the provider.
     */
    public Provider(Profile profile, Location location) {
        super(profile);
        this.location = location;
    }

    /**
     * Get the practice location of the provider.
     *
     * @return The location of the provider.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Set the practice location of the provider.
     *
     * @param location The new location of the provider.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Abstract method to get the rate per visit for the provider.
     * This method must be implemented by the subclasses (Doctor and Technician).
     *
     * @return The rate per visit.
     */
    public abstract int rate();

    /**
     * Return a string representation of the provider's profile and location.
     *
     * @return A string representing the provider's profile and location.
     */
    @Override
    public String toString() {
        return "[" + this.profile.toString() + ", Location: " + this.location.toString() + "] ";
    }
}
