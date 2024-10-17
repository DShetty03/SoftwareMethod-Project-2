package other;
import util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListTest {
    private List<Provider> providerList;
    private Doctor doctor;
    private Technician technician;

    @Before
    public void setUp() {
        providerList = new List<>(); // Initialize the List object
        Profile docProfile = new Profile("ANDREW", "PATEL", new Date(01,21,1989));
        Profile techProfile = new Profile("GARY  ", "JOHNSON", new Date(11,14,1987));
        doctor = new Doctor(docProfile, Location.valueOf("BRIDGEWATER"), Speciality.valueOf("FAMILY"), "01"); // Create a Doctor object
        technician = new Technician(techProfile, Location.valueOf("PISCATAWAY"), 110); // Create a Technician object
    }

    @Test
    public void testAddDoctor() {
        providerList.add(doctor); // Add Doctor to the list
        assertEquals(1, providerList.size()); // Verify size is now 1
        assertTrue(providerList.contains(doctor)); // Verify Doctor was added
    }

    @Test
    public void testAddTechnician() {
        providerList.add(technician); // Add Technician to the list
        assertEquals(1, providerList.size()); // Verify size is now 1
        assertTrue(providerList.contains(technician)); // Verify Technician was added
    }

    @Test
    public void testRemoveDoctor() {
        providerList.add(doctor); // First, add Doctor to the list
        providerList.remove(doctor); // Then, remove Doctor
        assertEquals(0, providerList.size()); // Verify size is back to 0
        assertFalse(providerList.contains(doctor)); // Verify Doctor was removed
    }

    @Test
    public void testRemoveTechnician() {
        providerList.add(technician); // First, add Technician to the list
        providerList.remove(technician); // Then, remove Technician
        assertEquals(0, providerList.size()); // Verify size is back to 0
        assertFalse(providerList.contains(technician)); // Verify Technician was removed
    }
}