package util;
import other.Appointment;
import other.*;



/**
 * A generic Sort class for sorting elements in a List.
 *
 * @author Richard Li (rl902)
 */
public class Sort {
    /**
     * Sorts the list of appointments based on the given key.
     *
     * @param list the list of appointments to sort
     * @param key the sorting key:
     *            A - sort by date, time, provider's name
     *            P - sort by patient (last name, first name, dob, date, time)
     *            L - sort by county name, date, time
     *            O - sort office appointments first, then by county, date, time
     *            I - sort imaging appointments first, then by county, date, time
     */
    public static void appointment(List<Appointment> list, char key) {
        int n = list.size();

        // Bubble Sort
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Appointment a1 = list.get(j);
                Appointment a2 = list.get(j + 1);

                if (compareAppointments(a1, a2, key) > 0) {
                    // Swap using get() and set() methods
                    list.set(j, a2);
                    list.set(j + 1, a1);
                }
            }
        }
    }

    /**
     * Compares two appointments based on the provided sorting key.
     *
     * @param a1 the first appointment
     * @param a2 the second appointment
     * @param key the sorting key
     * @return negative if a1 < a2, 0 if a1 == a2, positive if a1 > a2
     */
    private static int compareAppointments(Appointment a1, Appointment a2, char key) {
        switch (key) {
            case 'A':
                // Sort by date, time, then provider's name
                int dateCompare = a1.getDate().compareTo(a2.getDate());
                if (dateCompare != 0) return dateCompare;

                int timeCompare = a1.getTimeslot().compareTo(a2.getTimeslot());
                if (timeCompare != 0) return timeCompare;

                return a1.getProvider().getLName().compareTo(a2.getProvider().getLName());

            case 'P':
                // Sort by patient (last name, first name, dob, then appointment date and time)
                int lastNameCompare = a1.getPatient().getLName().compareTo(a2.getPatient().getLName());
                if (lastNameCompare != 0) return lastNameCompare;

                int firstNameCompare = a1.getPatient().getFName().compareTo(a2.getPatient().getFName());
                if (firstNameCompare != 0) return firstNameCompare;

                int dobCompare = a1.getPatient().getDOB().compareTo(a2.getPatient().getDOB());
                if (dobCompare != 0) return dobCompare;

                dateCompare = a1.getDate().compareTo(a2.getDate());
                if (dateCompare != 0) return dateCompare;

                return a1.getTimeslot().compareTo(a2.getTimeslot());

            case 'L':
                // Sort by county name, then appointment date and time
                int countyCompare = a1.getProvider().getLocation().compareTo(a2.getProvider().getLocation());
                if (countyCompare != 0) return countyCompare;

                dateCompare = a1.getDate().compareTo(a2.getDate());
                if (dateCompare != 0) return dateCompare;

                return a1.getTimeslot().compareTo(a2.getTimeslot());

            case 'O':
                // Sort office appointments first, then by county name, date, and time
                if (a1 instanceof Imaging && !(a2 instanceof Imaging)) return 1; // Imaging goes after
                if (!(a1 instanceof Imaging) && a2 instanceof Imaging) return -1; // Office first

                // Now both are of the same type, so sort by county, date, and time
                countyCompare = a1.getProvider().getLocation().compareTo(a2.getProvider().getLocation());
                if (countyCompare != 0) return countyCompare;

                dateCompare = a1.getDate().compareTo(a2.getDate());
                if (dateCompare != 0) return dateCompare;

                return a1.getTimeslot().compareTo(a2.getTimeslot());

            case 'I':
                // Sort imaging appointments first, then by county name, date, and time
                if (a1 instanceof Imaging && !(a2 instanceof Imaging)) return -1; // Imaging first
                if (!(a1 instanceof Imaging) && a2 instanceof Imaging) return 1;  // Office goes after

                // Now both are of the same type, so sort by county, date, and time
                countyCompare = a1.getProvider().getLocation().compareTo(a2.getProvider().getLocation());
                if (countyCompare != 0) return countyCompare;

                dateCompare = a1.getDate().compareTo(a2.getDate());
                if (dateCompare != 0) return dateCompare;

                return a1.getTimeslot().compareTo(a2.getTimeslot());

            default:
                throw new IllegalArgumentException("Invalid sort key: " + key);
        }
    }

    /**
     * Sorts the list of providers by their last name.
     *
     * @param list the list of providers to sort
     */
    public static void provider(List<Provider> list) {
        int n = list.size();
        // Bubble sort implementation
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare providers by last name
                Provider currentProvider = list.get(j);
                Provider nextProvider = list.get(j + 1);

                // Compare last names, if current > next, swap them
                if (currentProvider.getLName().compareTo(nextProvider.getLName()) > 0) {
                    // Swap providers
                    Provider temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

}
