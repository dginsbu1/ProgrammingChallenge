package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class BusinessCardParser{
    private boolean hasName, hasEmail, hasNumber;
    private Set<String> nameSet;


    public BusinessCardParser(String namePath) throws FileNotFoundException {
        nameSet = new HashSet<String>();
        //if the default option is chosen set the
        // nameSet to the predefined list
        if(namePath.equalsIgnoreCase("default")){
            setDefaultNameSet();
            return;
        }
        //assuming a filepath was given, upload the names to nameSet
        File nameFile = new File(namePath);
        Scanner sc = new Scanner(nameFile);
        while (sc.hasNextLine())
            nameSet.add(sc.nextLine());
    }

    //sets nameSet to predefined list to fit with examples
    private void setDefaultNameSet(){
        nameSet.add("John");
        nameSet.add("Doe");
        nameSet.add("Bob");
        nameSet.add("Smith");
        nameSet.add("Jane");
    }

    //creates a new ContactInfo object, sets the name, email, and phoneNumber,
    //and returns the object
    public ContactInfo getContactInfo(String document){
        ContactInfo ci = new ContactInfo();
        //splits the the string into an array of individual lines
        String[] lines = document.split("\\r?\\n");
        parseName(ci, lines);
        parseEmail(ci, lines);
        parsePhoneNumner(ci, lines);
        return ci;
    }

    //extracts the name (if any) in lines and sets the name of ci to that name
    //returns the extracted name
    public String parseName(ContactInfo ci, String[] lines){
        for(String line: lines) {
            if (isName(line)) {
                ci.setName(line);
                return line;
            }
        }
        //if none of the lines are name return null
        return null;
    }
    //returns true if the given String/line is a name
    //otherwise return false
    private boolean isName(String line) {
        //split the line into individual words
        String[] words = line.split("\\s+");
        for(String word : words) {
            //if any word is not a name then the line is not a name
            //middle initials such as Bob E Smith or Bob E. Smith are valid names
            if (!(nameSet.contains(word) || word.matches("[A-Z][.]?"))) {
                return false;
            }
        }
        //if all the words are names then the line is a name
        return true;
    }
    //extracts the email (if any) in lines and sets the email of ci to that email
    //returns the extracted email
    public String parseEmail(ContactInfo ci, String[] lines){
        for(String line: lines){
            //check for the '@' somewhere after the first character
            int atPosition = line.indexOf("@", 1);
            //check for the '.' at least one space after the '@' because there must be at least
            // one char after the '@' before the '.'
            int dotPosition = line.indexOf(".", atPosition+2);
            //if the line contains both characters indicating it's an email address
            //then set the email of ci
            if(atPosition >= 0 && dotPosition >= 0){
                ci.setEmail(line);
                return line;
            }
        }
        //if no email found return null
        return null;
    }
    //extracts the phone number in lines and sets the number of ci to that number
    //returns the extracted number
    public String parsePhoneNumner(ContactInfo ci, String[] lines){
        for(String line: lines){
            //check line to see if it contains the phone number
            if(!isPhoneNumberLine(line)){
                continue;
            }
            //if this line contains the phone number,
            //remove all non-numeric characters
            String phoneNumber = line.replaceAll("[^0-9]", "");
            //simple check to make sure the number is at least 10 digits
            if(phoneNumber.length() < 10){
                continue;
            }
            ci.setPhoneNumber(phoneNumber);
            return phoneNumber;
        }
        //if no number found return null;
        return null;
    }
    //checks if the line contains a phone number and it is not a fax number
    private boolean isPhoneNumberLine(String line){
        String phoneRegEx = ".*\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})";
        //verify the line matched and the line is not a fax number
        if(line.matches(phoneRegEx) && !line.toLowerCase().contains("fax")) {
            return true;
        }
        return false;
    }
}
