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
                handleDCommand(tokens);
                break;
            case "T":
                handleTCommand(tokens);
                break;
            case "C":
                cancelAppointment(tokens);
                break;
            case "R":
                rescheduleAppointment(tokens);
                break;
            case "PA":
                sorting('A');
                break;
            case "PP":
                sorting('P');
                break;
            case "PL":
                sorting('L');
                break;
            case "PS":
                //printBillingStatements();
                //billing statements
                break;
            case "PO":
                sorting('O');
                break;
            case "PI":
                sorting('I');
                break;
            case "PC":
                sorting('C');
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
    private void handleDCommand(String[] tokens) {
        //D,9/30/2024,1,John,Doe,12/13/1989,120
        try {
            // Parse the date and timeslot
            Date appointmentDate = parseDate(tokens[1]);
            if (!appointmentDate.isValid()){
                System.out.println("Appointment Date: " + appointmentDate + " isn't a valid calendar date!");
                return;
            }
            try {
                appointmentDate.isTodayOrBefore();  // Will throw an exception if the date is invalid
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            try {
                appointmentDate.isWithinSixMonths();  // Will throw an exception if the date is not within 6 months
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            try {
                appointmentDate.isWeekend();  // Will throw an exception if the date is a weekend
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            Timeslot timeslot = new Timeslot(Integer.parseInt(tokens[2]));

            // Parse the patient information
            String firstName = tokens[3];
            String lastName = tokens[4];
            Date dob = parseDate(tokens[5]);
            try{
                dob.isTodayOrAfter();
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                return;
            }
            Profile patientProfile = new Profile(firstName, lastName, dob);
            Patient patient = new Patient(patientProfile);

            // Parse the NPI Number
            int npi = Integer.parseInt(tokens[6]);
            List<Doctor> officePs = new List<>();

            //sort the all doctors into one list
            for (int i = 0; i < providers.size(); i++){
                if (providers.get(i) instanceof Doctor){
                    Doctor doc = (Doctor) providers.get(i);
                    officePs.add(doc);
                }
            }

            Doctor assignedDoctor = new Doctor(null,null,null,null);
            for (int i=0; i < officePs.size(); i++){
                if (Integer.parseInt(officePs.get(i).getNpi())==(npi)){
                    assignedDoctor = officePs.get(i);
                }
            }

            if (assignedDoctor.getNpi() == null) {
                System.out.println("this NPI number doesnt exist!");
                return;
            }

            if (providerAvailableForTimeslot(assignedDoctor, appointmentDate, timeslot)) {
                Appointment newAppointment = new Appointment(appointmentDate, timeslot, patient, assignedDoctor);
                appointments.add(newAppointment);

                System.out.printf("%s %s %s %s [%s, %s, %s %s, %s] booked.%n",
                            appointmentDate,  // MM/DD/YYYY
                            timeslot,         // hh:mm AM/PM
                            patient.getFName(), patient.getLName(), patient.getDOB(),
                        assignedDoctor.getFName(),
                        assignedDoctor.getLocation().getCity(),
                        assignedDoctor.getLocation().getCounty(),
                        assignedDoctor.getLocation().getZip(),
                        assignedDoctor.getSpeciality().name());
            } else {
                System.out.println("Appointment couldn't be scheduled. Appointment already exists");
            }

        } catch (Exception e) {
            System.out.println("Error: Invalid command or data. Please check the input.");
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
            if (!appointmentDate.isValid()){
                System.out.println("Appointment Date: " + appointmentDate + " isn't a valid calendar date!");
                return;
            }
            try {
                appointmentDate.isTodayOrBefore();  // Will throw an exception if the date is invalid
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            try {
                appointmentDate.isWithinSixMonths();  // Will throw an exception if the date is not within 6 months
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            try {
                appointmentDate.isWeekend();  // Will throw an exception if the date is a weekend
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            Timeslot timeslot = new Timeslot(Integer.parseInt(tokens[2]));

            // Parse the patient information
            String firstName = tokens[3];
            String lastName = tokens[4];
            Date dob = parseDate(tokens[5]);
            try{
                dob.isTodayOrAfter();
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                return;
            }
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

    // Method to handle canceling an appointment (C command)
    /**
     * Cancels an existing appointment based on the given details.
     *
     * @param tokens the array of appointment details from the command line
     */
    private void cancelAppointment(String[] tokens) {
        try {
            // C,9/30/2024,1,John,Doe,12/13/1989
            Date appointmentDate = parseDate(tokens[1]);
            Timeslot timeslot = new Timeslot(Integer.parseInt(tokens[2]));
            Profile profile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));
            Person currentPatient = new Person(profile);
            // We create a temporary appointment object without a provider since it’s not needed for comparison.
            Appointment tempAppointment = new Appointment(appointmentDate, timeslot, currentPatient, null);

            // We need to manually check for a match in the appointments list by comparing date, timeslot, and profile.
            Appointment appointmentToCancel = null;
            for (int i = 0; i < appointments.size(); i++) {
                Appointment current = appointments.get(i);
                if (current.compareTo(tempAppointment)==0) {
                    appointmentToCancel = current;
                    break;
                }
            }

            if (appointmentToCancel != null) {
                appointments.remove(appointmentToCancel);
                System.out.println("Appointment canceled.");
            } else {
                System.out.println("Appointment not found.");
            }

        } catch (Exception e) {
            System.out.println("Invalid command!");
        }
    }

    private boolean providerAvailableForTimeslot(Provider provider, Date date, Timeslot timeslot) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            // Check if the appointment's provider is the same and the appointment is on the same date and timeslot
            if (appointment.getProvider().equals(provider) &&
                    appointment.getDate().equals(date) &&
                    appointment.getTimeslot().equals(timeslot)) {
                return false; // The provider is not available at this timeslot because of a conflict
            }
        }
        return true; // The provider is available
    }

    // Method to handle rescheduling an appointment (R command)
    /**
     * Reschedules an existing appointment to a new timeslot.
     *
     * @param tokens the array of appointment details from the command line
     */


    private void rescheduleAppointment(String[] tokens) {
        try {
            Date appointmentDate = parseDate(tokens[1]);
            Timeslot currentTimeslot = new Timeslot(Integer.parseInt(tokens[2]));
            Profile profile = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));
            Person currentPatient = new Person(profile);
            Timeslot newTimeslot = new Timeslot(Integer.parseInt(tokens[6]));

            Appointment tempAppointment = new Appointment(appointmentDate, currentTimeslot, currentPatient, null);
            boolean appointmentFound = false;

            // Loop through the appointments list to find the matching appointment
            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);

                // Check if this appointment matches by date, timeslot, and profile (ignore provider for search)
                if (tempAppointment.compareTo(appointment)==0) {

                    appointmentFound = true;

                    // Check if provider is available for the new timeslot
                    if (providerAvailableForTimeslot(appointment.getProvider(), appointmentDate, newTimeslot)) {
                        // Create a new appointment with the new timeslot
                        Appointment updatedAppointment = new Appointment(appointmentDate, newTimeslot, currentPatient, appointment.getProvider());

                        // Remove the old appointment and add the updated one
                        appointments.remove(appointment);
                        appointments.add(updatedAppointment);

                        System.out.println("Appointment rescheduled.");
                    } else {
                        System.out.println("Provider is not available for the new timeslot.");
                    }
                    break;
                }
            }

            if (!appointmentFound) {
                System.out.println("Appointment not found.");
            }
        } catch (IllegalArgumentException e) { //catch invalid timeslots
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid command!");
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

    private void sorting (char key){
        if (key == 'C'){
            //Sort.provider();
        }else {
            Sort.appointment(appointments, key);
        }

    }

    //Method to generate billing statements (PS command)
    /**
     * Prints the billing statements for all patients. (PS command)
     */
    private void printBillingStatements() {
        System.out.println("** Billing statement ordered by patient **");

        //counter for numbering the list
        int counter = 1;

        // Loop through each appointment in appointmentsList
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);  // Get the appointment
            Patient patient = appointment.getPatient();  // Get the profile of the patient

            if (patient != null) {
                System.out.println("(" + counter + ") " + patient);
                counter++;
            }
        }

        System.out.println("** end of list **");
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
