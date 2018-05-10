package DS.SemesterProject.DB;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import static edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.*;

public class SimpleColumnDescription {
    private String columnName;
    private ColumnDescription.DataType columnDataType;

    public SimpleColumnDescription(){}
    //Constructor when I have a preexisting ColumnDescription
    //It simply copies the name and dataType
    public SimpleColumnDescription(ColumnDescription columnDescription){
        columnName = columnDescription.getColumnName();
        columnDataType = columnDescription.getColumnType();
    }
    //Manual Constructor
    //@param columnName, dataType
    public SimpleColumnDescription(String columnName, DataType dataType){
        this.columnName = columnName;
        this.columnDataType = dataType;
    }

    public String getColumnName(){
        return columnName;
    }

    public ColumnDescription.DataType getColumnDataType() {
        return columnDataType;
    }
    //Static method that converts ColumnDescription[] into SimpleColumnDescription[]
    public static SimpleColumnDescription[] makeSimpleColumnDescriptions(ColumnDescription[] columnDescriptions){
        SimpleColumnDescription[] simpleColumnDescriptions = new SimpleColumnDescription[columnDescriptions.length];
        for(int i = 0; i < columnDescriptions.length; i++){
            ColumnDescription cd = columnDescriptions[i];
            simpleColumnDescriptions[i] = new SimpleColumnDescription(cd);
        }
        return simpleColumnDescriptions;
    }
    //gets hashCode of this object
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.columnName == null ? 0 : this.columnName.hashCode());
        return result;
    }
    //prints itself out/ Used for DBTest
    public void print(){
        System.out.printf("%-20s",columnName);
    }

    //equals
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            SimpleColumnDescription other = (SimpleColumnDescription) obj;
            //if both are NULL or have same pointer then they are equal
            if(this.columnName == other.getColumnName() && this.columnDataType == other.getColumnDataType()){
                return true;
            } //else if columnName or columnDataType is null and other isnt null then can't be equal
            if(this.columnName == null && other.getColumnName() != null  || this.columnDataType == null && other.getColumnDataType() != null){
                return false;
            }//if nothing is null then do normal comparison
            if(this.columnName != null && this.columnDataType != null) {
                return (this.columnName.equals(other.getColumnName()) && this.columnDataType.toString().equals(other.getColumnDataType().toString()));
            }//only gets here if matching are null (ex.columnName of both this and other are null, but dataType isn't or Vice Versa)
            if(this.columnName != null){
                return this.columnName.equals(other.getColumnName());
            } else{
                return this.columnDataType.toString().equals(other.getColumnDataType().toString());
            }
        }
    }
}
