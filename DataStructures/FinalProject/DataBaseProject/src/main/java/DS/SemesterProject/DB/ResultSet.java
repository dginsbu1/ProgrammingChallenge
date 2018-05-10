package DS.SemesterProject.DB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResultSet {
    private Table table;
    private List<ArrayList<Object>> data;
    private SimpleColumnDescription[] simpleColumnDescriptions;
    private Exception exception;
    //make sure to get copy of original instead of actual original
    //constructs a ResultSet from the inputted Table (the columns requested by the Query)
    public ResultSet(Table table){
        this.table = table;
        this.data = table.getTable();
        this.simpleColumnDescriptions = table.getSimpleColumnDescriptions();
    }
    //constructor for Querys that have no return statement. Used to indicate if the Query worked or not
    public ResultSet(boolean success){
        data = new ArrayList<ArrayList<Object>>(1);
        ArrayList<Object> successList = new ArrayList<Object>(1);
        successList.add(success);
        data.add(successList);
    }
    //constrictor for Querys that threw error due to invalid data
    //adds exception to ResultSete
    public ResultSet(boolean success, Exception e){
        data = new ArrayList<ArrayList<Object>>(1);
        ArrayList<Object> successList = new ArrayList<Object>(1);
        successList.add(success);
        data.add(successList);
        exception = e;
    }
    public List<ArrayList<Object>> getData() {
        return data;
    }
    protected Table getTable(){
        return table;
    }
    public SimpleColumnDescription[] getSimpleColumnDescriptions() {
        return simpleColumnDescriptions;
    }
    public Exception getException() {
        return exception;
    }

    //gets hashCode using the data (2D arrayList) and the simpleColumnDescriptions
    //these are the only things needed to check if two resultSets are eqaul
    @Override
    public int hashCode() {
        int result = Objects.hash(data);
        result = 31 * result + Arrays.hashCode(simpleColumnDescriptions);
        return result;
    }

    //toString
    @Override
    public String toString(){

        String string = "";
        for(ArrayList<Object> row: data){
            //System.out.print(row.size());
            for(Object object:row){
                //if(object != null)
                    string+=(object.toString() + "      ");
            }
            System.out.println();
        }
        return string;
    }
    //prints out a string veresion of the ResultSet
    //used for DBTest
    public void print() {
        if(simpleColumnDescriptions != null){
            for(SimpleColumnDescription scd: simpleColumnDescriptions){
                scd.print();
            }
        }
        System.out.println();
        for(ArrayList<Object> row: data){
           //System.out.print(row.size());
            for(Object object:row){
                if(object == null){
                    System.out.print("null" + "       ");
                }else {
                    System.out.printf("%-20s",object.toString());
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //equals method
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            ResultSet other = (ResultSet) obj;
            return (this.hashCode() == other.hashCode());
        }
    }

}
