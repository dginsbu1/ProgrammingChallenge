package DS.SemesterProject.DB;

public class DBTest{
    public static void main(String[] args) {
        Executer ex = new Executer();
        String string = "CREATE TABLE Student (" +
                "FirstName varchar(10) NOT NULL UNIQUE, " +
                "CurrentStudent boolean DEFAULT true, " +
                "GPA decimal(1,2) DEFAULT 0.00, " +
                "BannerID int, " +
                "PRIMARY KEY (BannerID)) ";
        //create Table
        System.out.println(string);
        ResultSet set = ex.execute(string);
        //print empty table
        System.out.println(set);

        //add columns to table
        string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Al',true, 2.0, 001)";
        System.out.println(string);
        ex.execute(string);
        string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Ben',false, 3.0, 002)";
        System.out.println(string);
        ex.execute(string);
        string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Cool',true, 4.0, 003)";
        System.out.println(string);
        ex.execute(string);
        string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Dude',false, 4.0, 004)";
        System.out.println(string);
        ex.execute(string);
        System.out.println();
        //print all of table using select
        string = "SELECT * FROM Student";
        ex.execute(string).print();

        //select all the Functions using select
        string = "SELECT AVG(BannerID), MAX(FirstName), COUNT(DISTINCT CurrentStudent), MIN(BannerID), SUM(GPA) FROM Student";
        System.out.println(string);
        ex.execute(string).print();//@@@

        //select using distinct
        string = "SELECT DISTINCT GPA FROM Student";
        System.out.println(string);
        ex.execute(string).print();
        System.out.println();

        //insert some values using default
        string = "INSERT INTO Student (BannerID, FirstName, GPA) VALUES (005, 'Edward', 5.6)";
        System.out.print(string);
        ex.execute(string).print();
        string = "SELECT * FROM Student ORDER BY GPA DESC";
        System.out.println(string);
        ex.execute(string).print();

        //update inserted row by changing FirsName and GPA
        //UPDATE Student SET CurrentStudent = true, FirstName = 'Test' WHERE BannerID = 002
        string = "UPDATE Student SET FirstName = 'Cookie' WHERE CurrentStudent = true AND FirstName > 'Dude'";
        System.out.println("update inserted row by changing FirsName and GPA");
        System.out.println(string);
        ex.execute(string).print();
        //display
        string = "SELECT * FROM Student ORDER BY GPA DESC";
        System.out.println(string);
        ex.execute(string).print();

        //update ALL rows to have same currentStatus
        string = "UPDATE Student SET CurrentStudent = false";
        System.out.println("Graduation!!");
        System.out.println(string);
        ex.execute(string).print();
        //display row
        string = "SELECT * FROM Student ORDER BY GPA DESC";
        System.out.println(string);
        ex.execute(string).print();

        string = "SELECT * FROM Student ORDER BY GPA ASC";
        System.out.println(string);
        ex.execute(string).print();

        //delete the first row
        string = "Delete FROM Student WHERE BannerID >= 1 AND GPA <= 2.0";
        System.out.println("Delete the last row");
        System.out.println(string);
        ex.execute(string).print();
        //display row
        string = "SELECT * FROM Student ORDER BY GPA DESC";
        System.out.println(string);
        ex.execute(string).print();

        //create an Index
        string = "CREATE INDEX FirstName_INDEX on Student (FirstName)";
        System.out.println(string);
        ex.execute(string).print();
    }
}
