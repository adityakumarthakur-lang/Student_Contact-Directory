import java.util.*;
import java.io.*;

public class StudentContactDirectory {

    // ArrayList stores all contacts
    static ArrayList<Contact> contacts = new ArrayList<>();

    // HashMap stores phone number as key
    static HashMap<String, Contact> phoneIndex = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    // Contact class
    static class Contact {
        String name;
        String phone;
        String email;

        Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }

    // Add Contact
    static void addContact() {

        System.out.print("Enter Name: ");
        String name = readField();

        String phone;
        while (true) {
            System.out.print("Enter Phone (10 digits): ");
            phone = sc.nextLine().trim();

            if (!phone.matches("[0-9]{10}")) {
                System.out.println("Invalid phone number. Enter 10 digits.");
            } else if (phoneIndex.containsKey(phone)) {
                System.out.println("Phone number already exists!");
            } else {
                break;
            }
        }

        System.out.print("Enter Email: ");
        String email = readField();

        Contact c = new Contact(name, phone, email);

        contacts.add(c);
        phoneIndex.put(phone, c);

        System.out.println("Contact added successfully!");
    }

    // Display All Contacts
    static void displayContacts() {

        if (contacts.isEmpty()) {
            System.out.println("Directory is now empty.");
            return;
        }

        System.out.println("\n--- Student Contact Directory ---");

        for (Contact c : contacts) {
            System.out.println("Name  : " + c.name);
            System.out.println("Phone : " + c.phone);
            System.out.println("Email : " + c.email);
            System.out.println("---------------------------");
        }
    }

    // Search Contact
    static void searchContact() {

        System.out.print("Enter Name or Phone: ");
        String search = sc.nextLine().trim();

        boolean found = false;

        for (Contact c : contacts) {

            if (c.name.toLowerCase().contains(search.toLowerCase())
                    || c.phone.equals(search)) {

                System.out.println("Contact Found!");
                System.out.println("Name  : " + c.name);
                System.out.println("Phone : " + c.phone);
                System.out.println("Email : " + c.email);

                found = true;
            }
        }

        if (!found) {
            System.out.println("Contact not found!");
        }
    }

    // Delete Contact
    static void deleteContact() {

        System.out.print("Enter Phone Number to Delete: ");
        String phone = sc.nextLine().trim();

        Contact c = phoneIndex.get(phone);

        if (c != null) {
            contacts.remove(c);
            phoneIndex.remove(phone);

            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Contact not found!");
        }
    }

    // Update Contact
    static void updateContact() {

        System.out.print("Enter Old Phone Number: ");
        String oldPhone = sc.nextLine().trim();

        Contact c = phoneIndex.get(oldPhone);

        if (c == null) {
            System.out.println("Contact not found!");
            return;
        }

        String newPhone;

        while (true) {
            System.out.print("Enter New Phone Number (10 digits): ");
            newPhone = sc.nextLine().trim();

            if (!newPhone.matches("[0-9]{10}")) {
                System.out.println("Invalid phone number!");
            } else if (!newPhone.equals(oldPhone)
                    && phoneIndex.containsKey(newPhone)) {
                System.out.println("Phone number already exists!");
            } else {
                break;
            }
        }

        System.out.print("Enter New Email: ");
        String newEmail = readField();

        phoneIndex.remove(oldPhone);

        c.phone = newPhone;
        c.email = newEmail;

        phoneIndex.put(newPhone, c);

        System.out.println("Contact updated successfully!");
    }

    // Read non-empty field
    static String readField() {
        while (true) {
            String value = sc.nextLine().trim();

            if (value.isEmpty() || value.contains("|")) {
                System.out.print("Enter valid information: ");
            } else {
                return value;
            }
        }
    }

    // Load Contacts From File
    static void loadContacts() {

        try (BufferedReader br = new BufferedReader(
                new FileReader("contacts.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length == 3
                        && !data[0].isEmpty()
                        && data[1].matches("[0-9]{10}")
                        && !data[2].isEmpty()
                        && !phoneIndex.containsKey(data[1])) {

                    Contact c = new Contact(
                            data[0], data[1], data[2]);

                    contacts.add(c);
                    phoneIndex.put(c.phone, c);
                }
            }

        } catch (IOException e) {
            System.out.println("Starting with a new directory.");
        }
    }

    // Save Contacts To File
    static void saveContacts() {

        try (PrintWriter pw = new PrintWriter(
                new FileWriter("contacts.txt"))) {

            for (Contact c : contacts) {
                pw.println(c.name + "|" + c.phone + "|" + c.email);
            }

            System.out.println("Data saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving contacts!");
        }
    }

    // Main Method
    public static void main(String[] args) {

        loadContacts();

        System.out.println("Welcome to Student Contact Directory!");

        while (true) {

            System.out.println("\n====== MAIN MENU ======");
            System.out.println("1. Add Contact");
            System.out.println("2. Display All Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Update Contact");
            System.out.println("6. Exit");

            System.out.print("Enter Your Choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    addContact();
                    break;

                case "2":
                    displayContacts();
                    break;

                case "3":
                    searchContact();
                    break;

                case "4":
                    deleteContact();
                    break;

                case "5":
                    updateContact();
                    break;

                case "6":
                    saveContacts();
                    System.out.println("Thank You!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid Choice! Try Again.");
            }
        }
    }
}