package DS.SemesterProject.DB;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

//calling methods on empty table
//TEST: wrong table for each command Update, Insert, Delete, Select, CreateIndex
//
public class TableTest{
    private Executer ex;
    private ResultSet fail;
    private ResultSet success;
    private Table emptyTable, emptyTableCopy, tableCopy, normalTable;
    //Test: normal, varcharNegative, DecimalNegative, notNull-defaultNull,
// defaultWrongBoolean, defaultWrongVarchar, defaultWrongDecimal, defaultWrongInt,
//WrongPrimaryKey,
    //MAKE TABLE TESTS
    @Before
    public void setUp() throws Exception {
        ex = new Executer();
        String createEmptyTableString = "CREATE TABLE Empty ("+
                "FirstName varchar(10) NOT NULL UNIQUE," +
                "CurrentStudent boolean DEFAULT true," +
                "GPA decimal(1,2) DEFAULT 0.00," +
                "BannerID int," +
                "PRIMARY KEY (BannerID))";
        ex.execute(createEmptyTableString);
        emptyTable = new Table(ex.getParser().parse(createEmptyTableString));
        String createTableString = "CREATE TABLE Student ("+
                "FirstName varchar(10) NOT NULL UNIQUE," +
                "CurrentStudent boolean DEFAULT true," +
                "GPA decimal(1,2) DEFAULT 0.00," +
                "BannerID int," +
                "PRIMARY KEY (BannerID))";
        ex.execute(createTableString);

        //add some rows
        ex.execute("INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Al',true, 2.0, 001)");
        ex.execute("INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Ben',false, 3.0, 002)");
        ex.execute("INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Cool',true, 4.0, 003)");
        ex.execute("INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Dude',false, 4.0, 004)");
        normalTable = ex.getTable("Student");
        //copy of the current Student table
        createTableString = "CREATE TABLE StudentCopy ("+
                "FirstName varchar(255)," +
                "CurrentStudent boolean DEFAULT true," +
                "GPA decimal(1,2) DEFAULT 0.00," +
                "BannerID int," +
                "PRIMARY KEY (BannerID))";
        ex.execute(createTableString);
        ex.execute("INSERT INTO StudentCopy (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Al',true, 2.0, 001)");
        ex.execute("INSERT INTO StudentCopy (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Ben',false, 3.0, 002)");
        ex.execute("INSERT INTO StudentCopy (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Cool',true, 4.0, 003)");
        ex.execute("INSERT INTO StudentCopy (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Dude',false, 4.0, 004)");
        
        tableCopy = ex.getTable("StudentCopy");

        //used for true/false tests
        fail = new ResultSet(false, new Exception());
        success = new ResultSet(true);
    }
    private void makeEmptyTable(){
        makeEmptyTable("Empty2");
    }
    private void makeEmptyTable(String str){
        String createEmptyTableString = "CREATE TABLE "+str+"("+
                "FirstName varchar(10) NOT NULL UNIQUE," +
                "CurrentStudent boolean DEFAULT true," +
                "GPA decimal(1,2) DEFAULT 0.00," +
                "BannerID int," +
                "PRIMARY KEY (BannerID))";
        ex.execute(createEmptyTableString);
    }
    //create a table
    //make a nw result Set that verifies this one
    @Test
    public void tableCopy(){
        String createTableString = "CREATE TABLE Test ("+
                "BannerID int," +
                "SSNum int UNIQUE," +
                "FirstName varchar(255)," +
                "LastName varchar(255) NOT NULL," +
                "GPA decimal(1,2) DEFAULT 0.00," +
                "Class varchar(255), " +
                "CurrentStudent boolean DEFAULT true," +
                "PRIMARY KEY (BannerID))";
        assertNotEquals(fail, ex.execute(createTableString));


    }
    //bad Default
    //create a table with negative varchar length
    @Test
    public void varcharNegative(){
        String string = "CREATE TABLE Student (FirstName varchar(-255))";
        assertEquals(fail, ex.execute(string));
    }
    //create table with negative decimal length
    @Test
    public void DecimalNegative(){
        String string = "CREATE TABLE Student (FirstName decimal(-1,2)";
        assertEquals(fail, ex.execute(string));
        String string2 = "CREATE TABLE Student (FirstName decimal(1,-2)";
        assertEquals(fail, ex.execute(string2));
    }
    //create table which is not null with a null default
    @Test
    public void notNullDefaultNull(){
        //varchar, decimal,int, boolean
        String string1 = "CREATE TABLE Student (LastName varchar(255) NOT NULL DEFAULT null)";
        assertEquals(fail, ex.execute(string1));
        String string2 = "CREATE TABLE Student (LastName decimal(1,2) NOT NULL DEFAULT null)";
        assertEquals(fail, ex.execute(string2));
        String string3 = "CREATE TABLE Student (LastName int NOT NULL DEFAULT null)";
        assertEquals(fail, ex.execute(string3));
        String string4 = "CREATE TABLE Student (LastName boolean NOT NULL DEFAULT null)";
        assertEquals(fail, ex.execute(string4));
    }
    //create table with default Boolean that is incorrect
    @Test
    public void defaultWrongBoolean(){
        String string = "CREATE TABLE Student (Column1 boolean DEFAULT six)";
        assertEquals(fail, ex.execute(string));
    }
    //create table with Vdefault carchar without ''
    @Test
    public void defaultWrongVarchar(){
        String string = "CREATE TABLE Student (Column1 varchar(255) DEFAULT six)";// not 'six'
        assertEquals(fail, ex.execute(string));
    }
    @Test//createTable with wrong decimal default
    public void defaultWrongDecimal(){
        String string = "CREATE TABLE Student (Column1 double(1,2) DEFAULT six)";
        assertEquals(fail, ex.execute(string));
    }
    @Test//createTable with wrong int Default
    public void defaultWrongInt(){
        String string = "CREATE TABLE Student (Column1 int DEFAULT six)";
        assertEquals(fail, ex.execute(string));
    }
    @Test//Create Table with not existent primary key
    public void WrongPrimaryKey(){
        String string = "CREATE TABLE Student (Column1 int, PRIMARY KEY (Col2)";
        assertEquals(fail, ex.execute(string));
    }

                    ///TEST DELETE///
    @Test
    public void deleteEmptyTable(){//good
        String delete = "DELETE FROM Empty WHERE GPA <> 6.00";
        assertEquals(success, ex.execute(delete));
        makeEmptyTable();
        assertEquals(emptyTable, ex.getTable("Empty2"));
    }
    @Test
    public void deleteInvalidWhere(){//bad
        String string= "DELETE FROM Student WHERE GPA == b";
        assertEquals(fail, ex.execute(string));
    }
    @Test
    //check for tre %%
    public void deleteSingleCondition(){//good
        String string = "DELETE FROM Student WHERE CurrentStudent = true";
        assertEquals(success, ex.execute(string));
        //verify tables actually changed
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(0);
        System.out.println(tableCopy.hashCode() + " " + normalTable.hashCode());
        assertEquals(tableCopy, normalTable);
    }
    @Test // delete with multiple conditions
    public void deleteMultipleCondition(){
        String string = "DELETE FROM Student WHERE CurrentStudent = true AND GPA > 2";
        assertEquals(success, ex.execute(string));
        //verify tables actually changed
        tableCopy.getTable().remove(2);
        assertEquals(tableCopy, normalTable);
    }

    @Test//delete all rows
    //It is supposed to work but the parser library has a bug
    public void deleteAll(){
        String string = "DELETE * FROM Student";
        assertEquals(fail,ex.execute(string));
        //verify change
        assertEquals(tableCopy, normalTable);
    }
    @Test //delete no rows
    public void deleteNone(){//good
        String string = "DELETE FROM Student WHERE FirstName <= 60000";
        assertEquals(success, ex.execute(string));
        //verify change
        assertEquals(tableCopy, normalTable);
    }


    //Bad: InsertNoPrimaryKey, InsertPrimaryKeyNotUnique, InsertPrimamrykeyNull, InsertNomralNotUnique,
    // InsertWrongColumnName, InsertVarCharTooLong, InsertNotNullDefaultNull,
    //Good: InsertAllData, InsertSomeData, InsertOutOfOrder,
          //TEST INSERT
    //ex.execute("INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Al',true, 2.0, 001)";
    @Test//insert without setting a Primary Key
    public void InsertNoPrimaryKey(){//bad
        String string = "INSERT INTO Student (FirstName, CurrentStudent, GPA) VALUES ('Al',true, 2.0)";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//insert with Primary key that isn't unique
    public void insertPrimaryKeyNotUnique(){
        String string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('c',true, 2.0, 001)";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test // insert with Null primaryKey
    public void insertPrimaryKeyNull(){
        String string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('D',true, 2.0, null)";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//insert with FirstName not unique even though it has to be
    public void insertNormalNotUnique(){
        String string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Al',true, 2.0, 123)";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//insert with column name that doesn't exist
    public void insertWrongColumnName(){
        String string = "INSERT INTO Student (FirstName, LastName, GPA, BannerID) VALUES ('Al','Mistake', 2.0, null)";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//insert with too many columnNames
    public void insertTooManyColumnNames(){
        String string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID, Test) VALUES ('Ed',true, 2.0, 005, 'Test')";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//insert with too many columnNames
    public void insertTooManyColumnNames2(){
        String string = "INSERT INTO Student (FirstName,Test, CurrentStudent, GPA, BannerID) VALUES ('Ed', 'Test', true, 2.0, 005)";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//insert with VARCHAR that is longer than allowed
    public void insertVarCharTooLong(){
        String string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('thisIsTooLong',true, 2.0, 005)";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    //Happy Path
    //@@@ order might need to be changed
    @Test//insert with all the columns set
    public void insertAllData(){
        String string = "INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Edward',true, 2.0, 005)";
        assertEquals(success, ex.execute(string));
        //change tableCopy
        ArrayList<Object> newRow = new ArrayList<>();
        newRow.add(true);
        newRow.add(2.0);
        newRow.add("Edward");
        newRow.add(5);
        tableCopy.getTable().add(newRow);
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test//insert relying on the Default values
    public void insertSomeData(){
        ex.execute("INSERT INTO Student (FirstName, CurrentStudent, GPA, BannerID) VALUES ('Cool',true, 4.0, 003)");
        String string = "INSERT INTO Student (FirstName, BannerID) VALUES ('Test',10)";
        assertEquals(success, ex.execute(string));
        //change tableCopy
        ArrayList<Object> newRow = new ArrayList<>();
        newRow.add(true);
        newRow.add(0.00);
        newRow.add("Test");
        newRow.add(10);
        tableCopy.getTable().add(newRow);
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test//insert with the column Names not in he same order as they are in the table
    public void insertOutOfOrder(){
        String string = "INSERT INTO Student (BannerID, GPA, FirstName, CurrentStudent) VALUES ( 011,  2.00, 'Test',true)";
        //change tableCopy
        ArrayList<Object> newRow = new ArrayList<>();
        newRow.add(true);
        newRow.add(2.00);
        newRow.add("Test");
        newRow.add(11);
        tableCopy.getTable().add(newRow);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }

        ////Test UPDATE/////
    @Test//Update Table with invalid Where clause
    public void updateInvalidWhere(){
        String string = "UPDATE Student SET GPA = 3.0 WHERE GPA = t";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//Update table with invalid columnName
    public void updateInvalidColumnName() {
        String string = "UPDATE Student SET Test = 3.0 WHERE GPA = 3.0";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//Update table with invalid dataType
    public void updateInvalidType() {
        String string = "UPDATE Student SET GPA = true WHERE FirstName = 'Al'";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//Update table with invalid table name
    public void updateInvalidTable() {
        String string = "UPDATE Test SET GPA=3.0 WHERE GPA = 4.0";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void updateNullNotNull() {
        String string = "UPDATE Student SET FirstName = NULL WHERE GPA = 4.0";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }

    @Test//update a unique column to not unique
    public void updateToNotUnique(){
        String string = "UPDATE Student SET BannerID = 001 WHERE CurrentStudent = false";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//update with VARCHAR as the Where Clause
    public void updateVarcharIsWhere(){
        String string = "UPDATE Student SET BannerID = 123 WHERE FirstName = 'Al'";
        tableCopy.getTable().get(0).set(3,123);

        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test//update one column of of some rows
    public void updateRegular() {
        String string = "UPDATE Student SET CurrentStudent = true WHERE BannerID = 002";
        //change currentStudent to true
        tableCopy.getTable().get(1).set(0, true);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change

    }
    @Test//update ALL rows in some columns
    public void updateAllRows() {
        String string = "UPDATE Student SET GPA = 1.23";
        //change GPA to 1.23
        for(ArrayList<Object> row: tableCopy.getTable()){
            row.set(1, 1.23);
        }
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test//Update multiple columns in some rows
    public void updateMultipleColumns(){
        String string = "UPDATE Student SET CurrentStudent = true, FirstName = 'Test' WHERE BannerID = 002";
        //change currentStudent to true and name to Test
        tableCopy.getTable().get(1).set(0, true);
        tableCopy.getTable().get(1).set(2, "Test");
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test//update table where no rows meet where clause (aka do nothing)
    public void updateNone() {
        String string = "UPDATE Student SET CurrentStudent = true WHERE BannerID = 123";
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }


    @Test
    public void selectBTree(){
        String string = " SELECT GPA FROM Student WHERE BannerID = 2";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA", DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> num1 = new ArrayList<Object>(asList(3.0));
        data.add(num1);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
        assertTrue(normalTable.getUsedBTree());
        }

    @Test
    public void selectBannerIDOrderBy(){
        String string = "SELECT GPA FROM Student WHERE FirstName > 'Aa' ORDER BY GPA DESC";// ORDER BY GPA DESC;
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA", DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> num1 = new ArrayList<Object>(asList(4.0));
        data.add(num1);
        ArrayList<Object> num2 = new ArrayList<Object>(asList(4.0));
        data.add(num2);
        ArrayList<Object> num3 = new ArrayList<Object>(asList(3.0));
        data.add(num3);
        ArrayList<Object> num4 = new ArrayList<Object>(asList(2.0));
        data.add(num4);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
        //assertTrue(normalTable.getUsedBTree());
    }@Test
    public void selectBannerIDOB2() {
        String string = "SELECT BannerID FROM Student WHERE FirstName > 'Aa' ORDER BY BannerID ASC";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("BannerID", INT)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> num1 = new ArrayList<Object>(asList(1));
        data.add(num1);
        ArrayList<Object> num2 = new ArrayList<Object>(asList(2));
        data.add(num2);
        ArrayList<Object> num3 = new ArrayList<Object>(asList(3));
        data.add(num3);
        ArrayList<Object> num4 = new ArrayList<Object>(asList(4));
        data.add(num4);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
        //assertTrue(normalTable.getUsedBTree());
    }
    @Test
    public void selectBannerIDOB3() {
        String string = "SELECT BannerID FROM Student WHERE FirstName > 'Aa' ORDER BY BannerID DESC";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("BannerID", INT)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> num1 = new ArrayList<Object>(asList(4));
        data.add(num1);
        ArrayList<Object> num2 = new ArrayList<Object>(asList(3));
        data.add(num2);
        ArrayList<Object> num3 = new ArrayList<Object>(asList(2));
        data.add(num3);
        ArrayList<Object> num4 = new ArrayList<Object>(asList(1));
        data.add(num4);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
        //assertTrue(normalTable.getUsedBTree());
    }
    @Test//Select the average of the GPA's (3.25)
    public void selectStar() {
        String string = "SELECT * FROM Student";
        SimpleColumnDescription[] simpleCDs = normalTable.getSimpleColumnDescriptions();
        List<ArrayList<Object>> data = new ArrayList<>();
        for(ArrayList<Object> row: normalTable.getTable()){
           data.add(row);
        }
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void selectStarWhere() {
        String string = "SELECT * FROM Student WHERE GPA <> 4.0";
        SimpleColumnDescription[] simpleCDs = normalTable.getSimpleColumnDescriptions();
        List<ArrayList<Object>> data = new ArrayList<>();
        for(int i = 0; i < normalTable.getTable().size()-2;i++) {
            data.add(normalTable.getTable().get(i));
        }
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test//select all with multiple where clauses
    public void selectStarWhere2() {
        String string = "SELECT * FROM Student WHERE GPA <> 4.0 OR FirstName = 'Cool'";
        SimpleColumnDescription[] simpleCDs = normalTable.getSimpleColumnDescriptions();
        List<ArrayList<Object>> data = new ArrayList<>();
        for(int i = 0; i < normalTable.getTable().size()-1;i++) {
            data.add(normalTable.getTable().get(i));
        }
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void selectInvalidTable() {
        String string = "SELECT GPA FROM Wrong";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectInvalidColumnName() {
        String string = "SELECT Test FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectInvalidColumnName2() {
        String string = "SELECT GPA, Test FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    //AVG
    @Test//Select the average of the GPA's (3.25)
    public void selectAVGDouble() {
        String string = "SELECT AVG(GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("AVG(GPA)",DECIMAL)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(3.25));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test//Select the avg of the BannerId's (2)
    public void selectAVGInt() {
        String string = "SELECT AVG(BannerID) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("AVG(BannerID)",INT)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(2));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test//Select tje avg of distinct GPA's (3.00)
    public void selectAVGDistinct() {//@@@ try and change the title to AVG(DISTINCT GPA) instead of AVG(GPA)
        String string = "SELECT AVG(DISTINCT GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("AVG(GPA)",DECIMAL)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(3.00));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void selectAVGVarcharBoolean() {
        String string = "SELECT AVG(FirstName) FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
        string = "SELECT AVG(CurrentStudent) FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
                    //COUNT

    @Test
    public void selectCOUNTBoolean() {
        String string = "SELECT COUNT(CurrentStudent) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("COUNT(CurrentStudent)",INT)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(4));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectCOUNTVarchar() {
        String string = "SELECT COUNT(FirstName) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("COUNT(FirstName)",INT)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(4));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectCOUNTInt() {
        String string = "SELECT COUNT(BannerID) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("COUNT(BannerID)",INT)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(4));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectCOUNTDouble() {
        String string = "SELECT COUNT(GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("COUNT(GPA)",INT)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(4));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test //counts distinct GPAs
    public void selectCOUNTDistinct() {
        String string = "SELECT COUNT(DISTINCT GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("COUNT(GPA)",INT)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(3));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }

             //SUM//

    @Test
    public void selectSUMDouble() {
        String string = "SELECT SUM(GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("SUM(GPA)",DECIMAL)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(13.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectSUMInt() {
        String string = "SELECT SUM(BannerID) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("SUM(BannerID)",INT)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(10));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test//@@@ addDISTINCT TO NAME
    public void selectSUMDistinct() {
        String string = "SELECT SUM(DISTINCT GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("SUM(GPA)",DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(9.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectSUMVarcharBoolean() {
        String string = "SELECT SUM(FirstName) FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
        string = "SELECT SUM(CurrentStudent) FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }

            //MAX///

    @Test
    public void selectMAXDouble() {
        String string = "SELECT MAX(GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MAX(GPA)",DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(4.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectMAXInt() {
        String string = "SELECT MAX(BannerID) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MAX(BannerID)",INT)};
        ArrayList<Object> num = new ArrayList<Object>(asList(4));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectMAXVarchar() {
        String string = "SELECT MAX(FirstName) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MAX(FirstName)", VARCHAR)};
        ArrayList<Object> num = new ArrayList<Object>(asList("Dude"));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectMAXDistinct() {
        String string = "SELECT MAX(DISTINCT GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MAX(GPA)", DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(4.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectMAXBoolean() {
        String string = "SELECT MAX(CurrentStudent) FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
               //MIN///
    @Test
    public void selectMINDouble() {
        String string = "SELECT MIN(GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MIN(GPA)",DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(2.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectMINInt() {
        String string = "SELECT MIN(BannerID) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MIN(BannerID)",INT)};
        ArrayList<Object> num = new ArrayList<Object>(asList(1));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectMINVarchar() {
        String string = "SELECT MIN(FirstName) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MIN(FirstName)",INT)};
        ArrayList<Object> num = new ArrayList<Object>(asList("Al"));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }@Test
    public void selectMINDistinct() {
        String string = "SELECT MIN(DISTINCT GPA) FROM Student";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("MIN(GPA)",DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(2.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void selectMINBoolean() {
        String string = "SELECT MIN(CurrentStudent) FROM Student";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }

    @Test
    public void selectEmptyTable() {
        String string = "SELECT GPA FROM Empty";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        makeEmptyTable();
        assertEquals(set, ex.execute(string));
        assertEquals(emptyTable, ex.getTable("Empty2"));//verify no change
    }
    @Test
    public void updateEmptyTable() {
        String string = "UPDATE Empty SET GPA=3.0 WHERE GPA = 4.0";
        makeEmptyTable();
        assertEquals(success, ex.execute(string));
        assertEquals(emptyTable, ex.getTable("Empty2"));//verify no change

    }

    // WHERE CLAUSES

    // AND OR Multiple
    @Test
    public void whereAnd() {
        String string = "DELETE FROM Student WHERE GPA > BannerID AND FirstName = 'Al'";
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }
    @Test
    public void whereBTree() {
        String string = "DELETE FROM Student WHERE BannerID = 1";
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
        assertTrue(normalTable.getUsedBTree());
    }
    @Test
    public void whereBTree2() {
        String string = "UPDATE Student SET CurrentStudent = false WHERE BannerID = 1";
        tableCopy.getTable().get(0).set(0, false);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
        assertTrue(normalTable.getUsedBTree());
    }
    @Test
    public void whereTwoAnd() {
        String string = "DELETE FROM Student WHERE GPA > BannerID AND FirstName = 'Al' AND CurrentStudent = false";
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereOr() {
        String string = "DELETE FROM Student WHERE GPA <= BannerID OR BannerID = 3";
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(2);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }@Test
    public void whereTwoOr() {
        String string = "DELETE FROM Student WHERE GPA <= BannerID OR BannerID = 3 OR FirstName > 'Ba'";
        tableCopy.getTable().remove(3);
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(1);
        ResultSet set = ex.execute(string);
       // assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }
    @Test
    public void whereAndOr() {
        String string = "DELETE FROM Student WHERE (CurrentStudent = true AND GPA = 2.0) OR BannerID = 2 ";
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }
    @Test
    public void whereAndOr2() {
        String string = "DELETE FROM Student WHERE CurrentStudent = true AND (GPA = 2.0 OR BannerID = 2)";
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }
    @Test
    public void whereAndOr3() {
        String string = "DELETE FROM Student WHERE CurrentStudent = true AND GPA = 2.0 OR BannerID = 2";
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }

    @Test
    public void whereAndOr4() {
        String string = "DELETE FROM Student WHERE CurrentStudent = true OR GPA = 2.0 AND GPA = 2.0";
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(0);
        //ResultSet set = ex.execute(string);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }
    @Test
    public void whereMultipleClauses() {
        String string = "DELETE FROM Student WHERE GPA > BannerID OR FirstName = 'Cool' OR GPA <> 3 AND BannerID >= 3 OR CurrentStudent = false";
        tableCopy.getTable().remove(3);
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(1);
        tableCopy.getTable().remove(0);
        //ResultSet set = ex.execute(string);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereMultipleClauses2() {
        String string = "DELETE FROM Student WHERE (GPA > BannerID OR FirstName = 'Cool' OR GPA <> 3) AND (BannerID >= 3 OR CurrentStudent = false)";
        tableCopy.getTable().remove(3);
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(1);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }

    //Compare column to each other
    @Test
    public void whereCompareColumns() {
        String string = "DELETE FROM Student WHERE GPA > BannerID";
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereCompareColumns2() {
        String string = "DELETE FROM Student WHERE BannerID < GPA";
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereCompareColumns3() {
        String string = "DELETE FROM Student WHERE BannerID = GPA";
        tableCopy.getTable().remove(3);
        //ResultSet set = ex.execute(string);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }

    @Test
    public void whereCompareColumnsBooleanString() {
        String string = "DELETE FROM Student WHERE CurrentStudent < FirstName";
        assertEquals(fail, ex.execute(string));
        //System.out.println(ex.execute(string).getException());
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereCompareColumnsIntString() {
        String string = "DELETE FROM Student WHERE BannerID < FirstName";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereCompareColumnsStringDecimal() {
        String string = "DELETE FROM Student WHERE FirstName >= GPA ";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
    }
    @Test
    public void whereCompareEmptyTable() {
        String string = "DELETE FROM Empty WHERE BannerID < 2";
        makeEmptyTable();
        assertEquals(success, ex.execute(string));
        assertEquals(emptyTable, ex.getTable("Empty2"));//verify change
    }
         ///whereLessOrEqual
    @Test
    public void whereLessOrEqualInt() {
        String string = "UPDATE Student SET GPA = NULL WHERE BannerID <= 1";
        tableCopy.getTable().get(0).set(1,null);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereLessOrEqualDouble() {
        String string = "UPDATE Student SET GPA = 2.0 WHERE GPA <= 3.0";
        tableCopy.getTable().get(0).set(1,2.0);
        tableCopy.getTable().get(1).set(1,2.0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereLessOrEqualBoolean() {
        String string = "UPDATE Student SET GPA = 2.0 WHERE CurrentStudent <= false";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereLessOrEqualVarchar() {
        String string = "UPDATE Student SET CurrentStudent = true WHERE FirstName <= 'Elf'";
        tableCopy.getTable().get(0).set(0,true);
        tableCopy.getTable().get(1).set(0,true);
        tableCopy.getTable().get(2).set(0,true);
        tableCopy.getTable().get(3).set(0,true);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
       //whereGreaterOrEqual//
    @Test
    public void whereGreaterOrEqualInt() {
        String string = "DELETE FROM Student WHERE BannerID >= 2";
        tableCopy.getTable().remove(1);
        tableCopy.getTable().remove(1);
        tableCopy.getTable().remove(1);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereGreaterOrEqualDouble() {
        String string = "DELETE FROM Student WHERE GPA >= 4.0";
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(2);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereGreaterOrEqualBoolean() {
        String string = "DELETE FROM Student WHERE CurrentStudent >= true";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereGreaterOrEqualVarchar() {
        String string = "DELETE FROM Student WHERE FirstName >= 'Cool'";
        tableCopy.getTable().remove(2);
        tableCopy.getTable().remove(2);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
          //whereLessThan//
    @Test
    public void whereLessThanInt() {
        String string = "DELETE FROM Student WHERE BannerID < 3";
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereLessThanDouble() {
        String string = "DELETE FROM Student WHERE GPA < 4.0";
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
    @Test
    public void whereLessThanBoolean(){
        String string = "DELETE FROM Student WHERE CurrentStudent < true";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereLessThanVarchar() {
        String string = "DELETE FROM Student WHERE FirstName < 'Cool'";
        tableCopy.getTable().remove(0);
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify change
    }
        //GREATER THAN
    @Test
    public void whereGreaterThanInt() {
        String string = "SELECT GPA FROM Student WHERE BannerID > 2";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> avg = new ArrayList<Object>(asList(4.0));
        data.add(avg);
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereGreaterThanDouble() {
        String string = "SELECT GPA FROM Student WHERE GPA > 3.0";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> avg = new ArrayList<Object>(asList(4.0));
        data.add(avg);
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereGreaterThanBoolean() {
        String string = "SELECT GPA FROM Student WHERE CurrentStudent > true";
        assertEquals(fail, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereGreaterThanVarchar() {
        String string = "SELECT GPA FROM Student WHERE FirstName > 'C'";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> avg = new ArrayList<Object>(asList(4.0));
        data.add(avg);
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
               //EQUALS//
    @Test
    public void whereEqualsInt() {
        String string = "SELECT GPA FROM Student WHERE BannerID = 001";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(2.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereEqualsDouble() {
        String string = "SELECT GPA FROM Student WHERE GPA = 2.0";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(2.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereEqualsBoolean() {
        String string = "SELECT GPA FROM Student WHERE CurrentStudent = true";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(2.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ArrayList<Object> num2 = new ArrayList<Object>(asList(4.0));
        data.add(num2);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereEqualsVarchar() {
        String string = "SELECT GPA FROM Student WHERE FirstName = 'Al'";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        ArrayList<Object> avg = new ArrayList<Object>(asList(2.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(avg);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
        // NOT EQUALS
    public void whereNotEqualsInt() {
        String string = "SELECT GPA FROM Student WHERE BannerID <> 001";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> num1 = new ArrayList<Object>(asList(3.0));
        data.add(num1);
        ArrayList<Object> num2 = new ArrayList<Object>(asList(3.0));
        data.add(num2);
        ArrayList<Object> num3 = new ArrayList<Object>(asList(4.0));
        data.add(num3);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereNotEqualsDouble() {
        String string = "SELECT GPA FROM Student WHERE GPA <> 2.0";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> num1 = new ArrayList<Object>(asList(3.0));
        data.add(num1);
        ArrayList<Object> num2 = new ArrayList<Object>(asList(4.0));
        data.add(num2);
        ArrayList<Object> num3 = new ArrayList<Object>(asList(4.0));
        data.add(num3);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereNotEqualsBoolean() {
        String string = "SELECT GPA FROM Student WHERE CurrentStudent <> true";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        ArrayList<Object> num = new ArrayList<Object>(asList(3.0));
        List<ArrayList<Object>> data = new ArrayList<>();
        data.add(num);
        ArrayList<Object> num2 = new ArrayList<Object>(asList(4.0));
        data.add(num2);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        //ResultSet setA = ex.execute(string);
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void whereNotEqualsVarchar() {
        String string = "SELECT GPA FROM Student WHERE FirstName <> 'Al'";
        SimpleColumnDescription[] simpleCDs = {new SimpleColumnDescription("GPA",DECIMAL)};
        List<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> avg = new ArrayList<Object>(asList(3.0));
        data.add(avg);
        ArrayList<Object> avg2 = new ArrayList<Object>(asList(4.0));
        data.add(avg2);
        ArrayList<Object> avg3 = new ArrayList<Object>(asList(4.0));
        data.add(avg3);
        ResultSet set = new ResultSet(new Table(data, simpleCDs));
        assertEquals(set, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify no change
    }
    @Test
    public void createIndex() {
        String string = "CREATE INDEX FirstName_INDEX on Student (FirstName)";
        ex.execute(string);
        string = "DELETE FROM Student WHERE BannerID = 1 AND FirstName = 'Al'";
        tableCopy.getTable().remove(0);
        assertEquals(success, ex.execute(string));
        assertEquals(tableCopy, normalTable);//verify  change
        assertTrue(normalTable.getUsedBTree());
    }


}
