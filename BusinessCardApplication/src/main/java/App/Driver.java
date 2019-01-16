package App;

import java.io.*;
import java.nio.file.*;

public class Driver{
    public static void main(String[] args) throws IOException {
        //There must be at least two arguments (name list, and contact info)
        //in order to run
        if(args.length < 2) {
            System.out.println("There are no arguments");
            return;
        }
        String namePath = args[0];
        //Create a new BusinessCardParser with the namePath
        BusinessCardParser parser = new BusinessCardParser(namePath);
        String document = "";
        /*Each argument is a path to a document containing the info of one contact
          For each one display ALL the contact info, create a ContactInfo with the Name, Email, and PhoneNumber
          and display the results
         */
        for(int i = 1; i < args.length; i++){
            String path = args[i];
            //read the file into a single String
            document = new String(Files.readAllBytes(Paths.get(path)));
            ContactInfo ci = parser.getContactInfo(document);
            //display the results
            System.out.println(document);
            System.out.println("==>\n");
            ci.display();
            System.out.println("-------------------");
        }
    }
}
