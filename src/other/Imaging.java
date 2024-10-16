package other;

import util.Date;

/**
 * This class represents an imaging appointment, which extends the Appointment class.
 * An imaging appointment has a date, timeslot, patient, provider, and a room type.
 *
 * @author Richard Li (rl902)
 */
public class Imaging extends Appointment {
    private Radiology room;

    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room = room;
    }

    public Radiology getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return super.toString() + " for " + room + " imaging.";
    }
}
