package App;
public class ContactInfo{
    private String name, email, phoneNumber;

    public ContactInfo(){}

    //prints out the name, phone, and email of the person
    public void display(){
        System.out.printf("Name: %s\nPhone: %s\nEmail: %s\n", name, phoneNumber, email);
    }

    //SETTERS
    //sets variable name to the String name provided
    public void setName(String name){
        this.name = name;
    }
    //sets variable phoneNumber to the String number provided
    public void setPhoneNumber(String number){
        this.phoneNumber = number;
    }
    //sets variable email to the String email provided
    public void setEmail(String email){
        this.email = email;
    }

    //GETTERS

    //returns the full name of the individual (eg. John Smith, Susan Malick)
    public String getName(){
        return name;
    }
    //returns the phone number formatted as a sequence of digits
    public String getPhoneNumber(){
        return phoneNumber;
    }
    //returns the email address
    public String getEmailAddress(){
        return email;
    }

}


