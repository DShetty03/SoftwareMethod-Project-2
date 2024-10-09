package other;

/**
 * This class represents the timeslot for appointments.
 * A Timeslot is a number that represents a specific hour and minute
 *
 * @author Divit Shetty (dps190)
 */
public class Timeslot implements Comparable<Timeslot> {

    private int hour;    // The hour of the timeslot (0-23)
    private int minute;  // The minute of the timeslot (0 or 30)

    /**
     * Constructor for Timeslot class.
     * Maps the slot number to the corresponding hour and minute.
     *
     * @param slot The slot number (1-12).
     * @throws IllegalArgumentException if the slot is invalid.
     */
    public Timeslot(int slot) {
        if (slot < 1 || slot > 12) {
            throw new IllegalArgumentException(slot + " is not a valid timeslot");
        }
        // Set hour and minute based on slot number using helper method (needs to be written [use case])
        setTimeFromSlot(slot);
    }

    /**
     * Maps the slot number to the corresponding hour and minute.
     *
     * @param slot The slot number (1-12).
     */
    private void setTimeFromSlot(int slot) {
        switch (slot) {
            case 1:
                hour = 9; minute = 0; break;
            case 2:
                hour = 9; minute = 30; break;
            case 3:
                hour = 10; minute = 0; break;
            case 4:
                hour = 10; minute = 30; break;
            case 5:
                hour = 11; minute = 0; break;
            case 6:
                hour = 11; minute = 30; break;
            case 7:
                hour = 14; minute = 0; break;
            case 8:
                hour = 14; minute = 30; break;
            case 9:
                hour = 15; minute = 0; break;
            case 10:
                hour = 15; minute = 30; break;
            case 11:
                hour = 16; minute = 0; break;
            case 12:
                hour = 16; minute = 30; break;
            default:
                throw new IllegalArgumentException(slot + " is not a valid timeslot");
        }
    }

    /**
     * Get the hour of the timeslot.
     *
     * @return The hour of the timeslot.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Get the minute of the timeslot.
     *
     * @return The minute of the timeslot.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Compare two Timeslot objects by hour and minute.
     *
     * @param other The other Timeslot to compare to.
     * @return -1, 0, or 1 depending on whether this timeslot is earlier, the same, or later respectively.
     */
    @Override
    public int compareTo(Timeslot other) {
        if (this.hour != other.hour) {
            return Integer.compare(this.hour, other.hour);
        }
        return Integer.compare(this.minute, other.minute);
    }

    /**
     * Check if two Timeslot objects are equal based on hour and minute.
     *
     * @param obj The object to compare.
     * @return true if the timeslots are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Timeslot)) {
            return false;
        }
        Timeslot other = (Timeslot) obj;
        return this.hour == other.hour && this.minute == other.minute;
    }

    /**
     * Return a string representation of the timeslot in HH:MM AM/PM format.
     *
     * @return A string representing the timeslot.
     */
    @Override
    public String toString() {
        String amPm = (hour >= 12) ? "PM" : "AM";
        int displayHour = (hour == 0 || hour == 12) ? 12 : hour % 12;
        return String.format("%02d:%02d %s", displayHour, minute, amPm);
    }
}
