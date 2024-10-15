package other;

import util.Date;

/**
 * This class represents a Person (could be a patient or a Provider)
 * It serves as the superclass for the Patient and Provider classes.
 *
 * @author Divit Shetty (dps190)
 */
public class Person implements Comparable<Person> {

    protected Profile profile;

    /**
     * Constructor for the Person class.
     *
     * @param profile The profile of the person.
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Returns the first name of the profile.
     *
     * @return the first name
     */
    public String getFName() {
        return profile.getFname();
    }

    /**
     * Returns the last name of the profile.
     *
     * @return the last name
     */
    public String getLName() {
        return profile.getLname();
    }

    /**
     * Returns the date of birth of the profile.
     *
     * @return the date of birth
     */
    public Date getDOB() {
        return profile.getDob();
    }

    /**
     * Compare two Person objects based on their profile.
     *
     * @param otherPerson The other Person to compare to.
     * @return -1, 0, or 1 depending on whether this person is less than, equal to, or greater than the other person.
     */
    @Override
    public int compareTo(Person otherPerson) {
        return this.profile.compareTo(otherPerson.profile);
    }

    /**
     * Check if two Person objects are equal based on their profile.
     *
     * @param obj The object to compare to.
     * @return true if the profiles are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Person)) {
            return false;
        }
        Person otherPerson = (Person) obj;
        return this.profile.equals(otherPerson.profile);
    }

    /**
     * Return a string representation of the Person's profile.
     *
     * @return A string representing the Person's profile.
     */
    @Override
    public String toString() {
        return this.profile.toString();
    }
}
