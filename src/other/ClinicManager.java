package other;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import util.*;


public class ClinicManager {
    private List<Appointment> appointments;
    private List<Provider> providers; //doctors and technicians
    private Scanner scanner;

    public ClinicManager() {
        appointments = new List<>();
        providers = new List<>();
        scanner = new Scanner(System.in);
    }
    private void processCommand(String input) {
        String[] tokens = input.split(",");
        String command = tokens[0];

        switch (command) {
            case "D":

                break;
            case "T":
                handleTCommand(tokens);
                break;
            case "C":

                break;
            case "R":

                break;
            case "PA":

                break;
            case "PP":

                break;
            case "PL":

                break;
            case "PS":

                break;
            case "PO":

                break;
            case "PI":

                break;
            case "PC":

                break;
            case "Q":
                System.out.println("Scheduler is terminated.");
                System.exit(0);
            default:
                System.out.println("Invalid command!");
        }
    }



    /**
     * Handle the T command for technician appointments (imaging services).
     *
     * @param tokens the parsed input tokens
     */
    private void handleTCommand(String[] tokens) {
        try {
            // Parse the date and timeslot
            Date appointmentDate = parseDate(tokens[1]);
            Timeslot timeslot = new Timeslot(Integer.parseInt(tokens[2]));

            // Parse the patient information
            String firstName = tokens[3];
            String lastName = tokens[4];
            Date dob = parseDate(tokens[5]);
            Profile patientProfile = new Profile(firstName, lastName, dob);
            Patient patient = new Patient(patientProfile);

            // Parse the imaging service (xray, ultrasound, etc.)
            Radiology roomType = Radiology.valueOf(tokens[6].toUpperCase());

            // Find the next available technician for the requested room type and timeslot
            Technician assignedTechnician = assignTechnicianForService(timeslot, roomType);

            if (assignedTechnician == null) {
                System.out.println("No technician available for the requested service and timeslot.");
                return;
            }

            // Create and add the Imaging appointment
            Imaging newImagingAppointment = new Imaging(appointmentDate, timeslot, patient, assignedTechnician, roomType);
            appointments.add(newImagingAppointment);

            // Confirm appointment scheduling
            System.out.println("Imaging appointment scheduled: " + newImagingAppointment.toString());

        } catch (Exception e) {
            System.out.println("Error: Invalid command or data. Please check the input.");
        }
    }


    /**
     * Find an available technician for the requested service and timeslot.
     *
     * @param timeslot the requested timeslot
     * @param roomType the requested imaging service (xray, ultrasound, etc.)
     * @return the assigned technician, or null if no technician is available
     */
    // Method to assign the next available technician for a specific room type and timeslot
    private Technician assignTechnicianForService(Timeslot timeslot, Radiology roomType) {
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            if (provider instanceof Technician) {
                Technician technician = (Technician) provider;

                // Check if the technician is available at the requested timeslot
                if (isTechnicianAvailable(technician, timeslot, roomType)) {
                    return technician;
                }
            }
        }
        return null;  // No available technician found
    }

    // Check if the technician is available for the given timeslot and room type
    private boolean isTechnicianAvailable(Technician technician, Timeslot timeslot, Radiology roomType) {
        // First step, check if the technician is available for the requested timeslot
        for (Appointment appointment : appointments) {
            // Check if the appointment involves the same technician and timeslot
            if (appointment.getProvider().equals(technician) && appointment.getTimeslot().equals(timeslot)) {
                return false;  //Already booked

            }
        }
        // Then, check if the room at the technician's location is available for the requested timeslot and room type
        for (Appointment appointment : appointments) {
            // Check if the appointment is for the same location, room type, and timeslot
            if (appointment instanceof Imaging) {
                Imaging imagingAppointment = (Imaging) appointment;
                Provider appointmentProvider = (Provider) imagingAppointment.getProvider();
                if (appointmentProvider instanceof Technician &&
                        appointmentProvider.getLocation().equals(technician.getLocation()) &&
                        imagingAppointment.getRoom().equals(roomType) &&
                        imagingAppointment.getTimeslot().equals(timeslot)) {
                    return false;  // Room is already booked 
                }
            }
        }
        return true;
    }


    // Helper method to parse a date string into a Date object
    /**
     * Parses a date string in the format mm/dd/yyyy.
     *
     * @param dateStr the date string to parse
     * @return the Date object parsed from the string
     */
    private Date parseDate(String dateStr) {
        String[] parts = dateStr.split("/");
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return new Date(year, month, day);
    }

    //Read and load providers
    // Initialize and load providers (from providers.txt)
    public void initialize() {
        loadProviders();
        System.out.println("Providers loaded successfully.");
        displayProviders();
    }
    // Display the loaded providers
    private void displayProviders() {
        System.out.println("Loaded Providers:");
        for (Provider provider : providers) {
            System.out.println(provider.toString());
        }
    }

    // Load providers from providers.txt
    private void loadProviders() {
        try {
            Scanner scanner = new Scanner(new File("providers.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] tokens = line.split("\\s+");  // Split by spaces or tabs
                if (tokens[0].equals("D")) {  // It's a Doctor
                    Date dob = parseDate(tokens[3]);
                    Profile profile = new Profile(tokens[1], tokens[2], dob);
                    Location location = Location.valueOf(tokens[4]); // Town
                    Speciality speciality = Speciality.valueOf(tokens[5].toUpperCase());  // Speciality
                    String npi = tokens[6];
                    Doctor doctor = new Doctor(profile, location, speciality, npi);
                    providers.add(doctor);
                } else if (tokens[0].equals("T")) {  // It's a Technician
                    Date dob = parseDate(tokens[3]);
                    Profile profile = new Profile(tokens[1], tokens[2], dob);
                    Location location = Location.valueOf(tokens[4]);
                    int rate = Integer.parseInt(tokens[5]);  // Rate
                    Technician technician = new Technician(profile, location, rate);
                    providers.add(technician);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: providers.txt file not found.");
        }

    }



    // The run method to process user input commands
    /**
     * Main method to run the scheduler and process commands from the user.
     */
    public void run() {
        System.out.println("Scheduler is running:");
        initialize();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                processCommand(input);
            }
        }
    }


}
