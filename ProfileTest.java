package other;

import org.junit.Test;
import util.Date;

import static org.junit.Assert.*;

/**
 * This class tests the compareTo() method of the Profile class.
 * It includes tests for profiles with different first names, last names,
 * and dates of birth to ensure proper ordering.
 *
 * @author Divit Shetty (dps190)
 */

public class ProfileTest {

    /**
     * Test case where Profile 1 has an earlier date of birth than Profile 2.
     * The expected result is -1 because Profile 1 should be less than Profile 2.
     */
    @Test
    public void testEarlierDOB() {
        Profile profile1 = new Profile("John", "Doe", new Date(1989, 12, 13));
        Profile profile2 = new Profile("John", "Doe", new Date(1990, 12, 13));
        assertEquals(-1, profile1.compareTo(profile2));
    }

    /**
     * Test case where Profile 1 has an earlier last name than Profile 2.
     * The expected result is -1 because Profile 1 should be less than Profile 2 based on the last name.
     */
    @Test
    public void testEarlierLastName() {
        Profile profile1 = new Profile("John", "Smith", new Date(1989, 12, 13));
        Profile profile2 = new Profile("John", "Zoe", new Date(1989, 12, 13));
        assertEquals(-1, profile1.compareTo(profile2));
    }

    /**
     * Test case where Profile 1 has a later date of birth than Profile 2.
     * The expected result is 1 because Profile 1 should be greater than Profile 2 based on the later DOB.
     */
    @Test
    public void testLaterDOB() {
        Profile profile1 = new Profile("John", "Doe", new Date(1990, 12, 13));
        Profile profile2 = new Profile("John", "Doe", new Date(1989, 12, 13));
        assertEquals(1, profile1.compareTo(profile2));
    }

    /**
     * Test case where Profile 1 has a later last name than Profile 2.
     * The expected result is 1 because Profile 1 should be greater than Profile 2 based on the last name.
     */
    @Test
    public void testLaterLastName() {
        Profile profile1 = new Profile("John", "Zoe", new Date(1989, 12, 13));
        Profile profile2 = new Profile("John", "Smith", new Date(1989, 12, 13));
        assertEquals(1, profile1.compareTo(profile2));
    }

    /**
     * Test case where two profiles are equal (same first name, last name, and date of birth).
     * The expected result is 0 because Profile 1 should be equal to Profile 2.
     */
    @Test
    public void testEqualProfiles() {
        Profile profile1 = new Profile("John", "Doe", new Date(1989, 12, 13));
        Profile profile2 = new Profile("John", "Doe", new Date(1989, 12, 13));
        assertEquals(0, profile1.compareTo(profile2));
    }

    /**
     * Test case where Profile 1 has an earlier first name than Profile 2.
     * The expected result is -1 because Profile 1 should be less than Profile 2 based on the first name.
     */
    @Test
    public void testEarlierFirstName() {
        Profile profile1 = new Profile("Alice", "Doe", new Date(1989, 12, 13));
        Profile profile2 = new Profile("John", "Doe", new Date(1989, 12, 13));
        assertEquals(-1, profile1.compareTo(profile2));
    }

    /**
     * Test case where Profile 1 has a later first name than Profile 2.
     * The expected result is 1 because Profile 1 should be greater than Profile 2 based on the first name.
     */
    @Test
    public void testLaterFirstName() {
        Profile profile1 = new Profile("John", "Doe", new Date(1989, 12, 13));
        Profile profile2 = new Profile("Alice", "Doe", new Date(1989, 12, 13));
        assertEquals(1, profile1.compareTo(profile2));
    }

}