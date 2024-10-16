package other;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import util.Date;
import util.List;
import util.Sort;

/**
 * This class represents the entire scheduler of the program.
 * This runs the command line and takes in commands.
 *
 * @author Divit Shetty (dps190)
 * @author Richard Li (rl902)
 */

public class ClinicManager {
    private List<Appointment> appointments;
    private List<Provider> providers; //doctors and technicians
    private List<Technician> technicianRotation;
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
                sortingApp('A');
                break;
            case "PP":
                sortingApp('P');
                break;
            case "PL":
                sortingApp('L');
                break;
            case "PS":
                printBillingStatements();
                break;
            case "PO":
                sortingApp('O');
                break;
            case "PI":
                sortingApp('I');
                break;
            case "PC":
                printCreditStatements();
                break;
            case "Q":
                System.out.println("Scheduler is terminated.");
                System.exit(0);
            default:
                System.out.println("Invalid command!");
        }
    }

    /**
     * Handle the D command for technician appointments (imaging services).
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
            if (!dob.isValid()){
                System.out.println("Patient dob: " + dob + " isn't a valid calendar date!");
                return;
            }
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
            if (!dob.isValid()){
                System.out.println("Patient dob: " + dob + " isn't a valid calendar date!");
                return;
            }
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
            Date appointmentDateCAN = parseDate(tokens[1]);
            Timeslot timeslotCAN = new Timeslot(Integer.parseInt(tokens[2]));
            Profile profileCAN = new Profile(tokens[3], tokens[4], parseDate(tokens[5]));
            Person currentPatientCAN = new Person(profileCAN);
            // We create a temporary appointment object without a provider since it’s not needed for comparison.
            // We need to manually check for a match in the appointments list by comparing date, timeslot, and profile.

            Appointment appointmentToCancel = null;
            for (int i = 0; i < appointments.size(); i++) {
                Appointment current = appointments.get(i);
                String currUpperF = current.getPatient().getFName().toUpperCase();
                String upperF = currentPatientCAN.getFName().toUpperCase();

                String currUpperL = current.getPatient().getLName().toUpperCase();
                String upperL = currentPatientCAN.getLName().toUpperCase();

                if (current.getDate().equals(appointmentDateCAN) && current.getTimeslot().equals(timeslotCAN) && currUpperF.equals(upperF) && currUpperL.equals(upperL)) {
                    appointmentToCancel = current;
                    break;
                }
            }

            if (appointmentToCancel != null) {
                appointments.remove(appointmentToCancel);
                System.out.println( appointmentToCancel + " --- Appointment has been canceled.");
            } else {
                System.out.println("Appointment not found.");
            }

        } catch (Exception e) {
            System.out.println("Invalid command!");
        }
    }
    /**
     * checks if this provider has an opening at this time slot
     *
     * @param provider the provider specified
     * @param date the date that needs to be checked
     * @param timeslot  the time slot that needs to be checked
     */
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
            Person currentPatient = new Patient(profile);
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

                        System.out.println("Rescheduled to " + updatedAppointment.toString());
                    } else {
                        System.out.println("Provider is not available for the new timeslot.");
                    }
                    break;
                }
            }

            if (!appointmentFound) {
                System.out.println(tempAppointment + " does not exist");
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
    private int technicianRotationIndex = 0;
    private Technician assignTechnicianForService(Timeslot timeslot, Radiology roomType) {
        int technicianCount = technicianRotation.size();

        // Start from the current technician index and rotate through the list
        for (int i = 0; i < technicianCount; i++) {
            // Calculate the technician's index in a circular manner
            int currentIndex = (technicianRotationIndex + i) % technicianCount;
            Provider provider = technicianRotation.get(currentIndex);

            if (provider instanceof Technician) {
                Technician technician = (Technician) provider;

                // Check if the technician is available at the requested timeslot and room type
                if (isTechnicianAvailable(technician, timeslot, roomType)) {
                    // Update the rotation index to point to the next technician for future assignments
                    technicianRotationIndex = (currentIndex + 1) % technicianCount;
                    return technician;
                }
            }
        }
        return null;  // No available technician found
    }

    // Check if the technician is available for the given timeslot and room type
    /**
     * Checks if the given technician is available for the specified timeslot and room type.
     *
     * This method first checks whether the technician has an appointment at the requested
     * timeslot. If the technician is already booked, it returns false.
     *
     * It then checks if the requested room type at the technician's location is already booked
     * for the same timeslot by another technician. If so, it returns false, indicating that the
     * room is unavailable.
     *
     * If both checks pass, it returns true, indicating that the technician and room are available.
     *
     * @param technician The technician to check availability for.
     * @param timeslot The timeslot for which availability is being checked.
     * @param roomType The type of room (Radiology) required for the appointment.
     * @return {@code true} if the technician and room are available for the timeslot,
     *         {@code false} otherwise.
     */
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
    /**
     * Sorts the list of appointments based on the given key.
     * prints out all the appointments according to the key's order
     *
     * @param key the sorting key:
     *            A - sort by date, time, provider's name
     *            P - sort by patient (last name, first name, dob, date, time)
     *            L - sort by county name, date, time
     *            O - sort office appointments first, then by county, date, time
     *            I - sort imaging appointments first, then by county, date, time
     */
    private void sortingApp (char key){
        Sort.appointment(appointments, key);
        if (appointments.isEmpty()){
            System.out.println("Schedule calendar is empty.");
            return;
        }
        if (key == 'A'){
            System.out.println("** All Appointments ordered by date/time/provider **");
            for(int i = 0; i<appointments.size(); i++){
                System.out.println(appointments.get(i).toString());
            }
            System.out.println("** end of list **");

        } else if (key == 'P'){
            System.out.println("** All Appointments ordered by patient/date/time **");
            for(int i = 0; i<appointments.size(); i++){
                System.out.println(appointments.get(i).toString());
            }
            System.out.println("** end of list **");

        } else if (key == 'L'){
            System.out.println("** All Appointments ordered by county/date/time **");
            for(int i = 0; i<appointments.size(); i++){
                System.out.println(appointments.get(i).toString());
            }
            System.out.println("** end of list **");

        } else if (key == 'O'){
            System.out.println("** List of office appointments ordered by county/date/time **");
            for(int i = 0; i<appointments.size(); i++){
                if (!(appointments.get(i) instanceof Imaging)){
                    System.out.println(appointments.get(i).toString());
                }

            }
            System.out.println("** end of list **");

        } else if (key == 'I'){
            System.out.println("** List of radiology appointments ordered by county/date/time **");
            for(int i = 0; i<appointments.size(); i++){
                if (appointments.get(i) instanceof Imaging){
                    System.out.println(appointments.get(i).toString());
                }
            }
            System.out.println("** end of list **");
        }
    }

    /**
     * Prints the credit statements for all providers. (PC command)
     */
    private void printCreditStatements () {
        if (appointments.isEmpty()){
            System.out.println("Schedule calendar is empty.");
            return;
        }
        System.out.println("** Credit amount ordered by provider **");
        Sort.provider(providers);
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            int appointmentCount = 0;

            // Loop through appointments to count the ones for the current provider
            for (int j = 0; j < appointments.size(); j++) {
                Appointment appointment = appointments.get(j);

                // If the appointment is for the current provider, increment the count
                if (appointment.getProvider().equals(provider)) {
                    appointmentCount++;
                }
            }

            // Calculate total payment due for this provider
            double totalDue = appointmentCount * provider.rate();

            // Print out the total amount due for the provider
            System.out.println(provider.getFName() + " " + provider.getLName() + " " + provider.getDOB() + " " + "[Credit amount: $"  + totalDue + "]");
        }
        System.out.println("** end of list **");
    }

    //Method to generate billing statements (PS command)
    /**
     * Prints the billing statements for all patients. (PS command)
     */
    private void printBillingStatements() {
        if (appointments.isEmpty()){
            System.out.println("Schedule calendar is empty.");
            return;
        }
        System.out.println("** Billing statement ordered by patient **");
        List<Patient> patients = new List<>();

        //make a list of patients
        for (int i = 0; i < appointments.size(); i++) {
            // Get the patient from each appointment
            Patient currentPatient = appointments.get(i).getPatient();

            // Check if the patient is already in the uniquePatients list
            if (!patients.contains(currentPatient)) {
                // If not, add the patient to the uniquePatients list
                patients.add(currentPatient);
            }
        }

        //add the visits for each patient
        for (int i = 0; i < patients.size(); i++) {
            Patient currentPatient = patients.get(i);

            // Loop through the list of appointments
            for (int j = 0; j < appointments.size(); j++) {
                Appointment currentAppointment = appointments.get(j);

                // Check if the current appointment is for the current patient
                if (currentAppointment.getPatient().equals(currentPatient)) {
                    // Add this appointment as a visit to the patient
                    Visit newVisit = new Visit(currentAppointment);
                    currentPatient.addVisit(newVisit);
                }
            }
        }

        //Sort the patients by last name
        int n = patients.size();
        boolean swapped;

        // Bubble sort
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                Patient p1 = patients.get(i);
                Patient p2 = patients.get(i + 1);

                // Compare the last names
                if (p1.getLName().compareTo(p2.getLName()) > 0) {
                    // Swap the patients if they are in the wrong order
                    Patient temp = p1;
                    patients.set(i, p2);
                    patients.set(i + 1, temp);
                    swapped = true;
                }
            }
            n--;  // Reduce the range of comparisons as largest element is already in place
        } while (swapped);

        //for each patient, count the money due and print it out.
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            Visit head = patient.getVisit();

            // Sum the total cost for this patient's visits
            double totalCost = 0;
            Visit current = head;
            while (current != null) {
                totalCost += current.getAppointment().getProvider().rate();
                current = current.getNext();
            }

            // Print the total cost for this patient
            System.out.println(patient.getFName() + " " + patient.getLName() + " " + patient.getDOB() + " [Credit amount: $" + totalCost + "]");
        }

        List<Appointment> empty = new List<>();
        appointments = empty;

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
    /**
     * Initializes the system by loading providers and displaying them.
     * This method also displays the rotation list for the technicians.
     */
    public void initialize() {
        loadProviders();
        System.out.println("Providers loaded successfully.");
        displayProviders();
        displayTechnicianRotation();

    }
    // Display the loaded providers
    /**
     * Displays the loaded providers.
     * This method iterates through the list of providers and prints their details to the console.
     */
    private void displayProviders() {
        System.out.println("Loaded Providers:");
        for (Provider provider : providers) {
            System.out.println(provider.toString());
        }
    }

    // Load providers from providers.txt
    /**
     * Loads the providers from the "providers.txt" file.
     * The method parses each line in the file, identifies if the line corresponds to a Doctor or Technician,
     * and creates an instance of the respective class. The providers are then added to the providers list.
     *
     * The file format should follow:
     * D <firstName> <lastName> <dob> <location> <speciality> <npi> for Doctors
     * T <firstName> <lastName> <dob> <location> <rate> for Technicians
     *
     * @throws FileNotFoundException If the "providers.txt" file is not found.
     */
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

    // Method to initialize and display the rotation list for technicians
    /**
     * Displays the rotation list of technicians.
     * This method filters the providers list to include only technicians, adds them to the
     * technicianRotation list, and prints the rotation order of technicians.
     */
    private void displayTechnicianRotation() {
        technicianRotation = new List<>();

        // Add only technicians to the technician rotation list
        for (Provider provider : providers) {
            if (provider instanceof Technician) {
                technicianRotation.add((Technician) provider);
            }
        }

        // Display the technician rotation list
        System.out.println("Rotation list for the technicians:");
        for (int i = 0; i < technicianRotation.size(); i++) {
            Technician tech = technicianRotation.get(i);
            System.out.print(tech.getProfile() + " (" + tech.getLocation() + ")");

            // Add arrows between technicians, and wrap around at the end
            if (i < technicianRotation.size() - 1) {
                System.out.print(" --> ");
            } else {
                System.out.println();  // End the line after the last technician
            }
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