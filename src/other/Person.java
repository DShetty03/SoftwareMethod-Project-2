package other;

/**
 * This class represents a Person (could be a patient or a Provider)
 * It serves as the superclass for the Patient and Provider classes.
 *
 * Author: Divit Shetty (dps190)
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
