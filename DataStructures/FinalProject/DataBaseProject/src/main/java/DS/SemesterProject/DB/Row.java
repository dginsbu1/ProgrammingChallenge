package DS.SemesterProject.DB;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.InsertQuery;

import java.util.*;
/*
 * put all the ColumnValuePairs in a HasMap with names of Columns as Key
 * Iterate through each element of the columnDesciptions
 * 	check to see if it matches the key.
 * 		if so try/catch parsing the element in the set
 * 			if success then assign to the value in row
 * 			increment used counter
 * 			catch( then throw error)
 * 		else 1.check for default value
 * 			 2. make sure it can be null
 * 		verify that every element from query was used.
 * 		if not (that means the query contained an extra value pair not part of the table
 * 			therefore throw an error
 */
public class Row {
    //tooMany, tooFew, wrongType(column needs int and input string, wrongColumnName, notNullLeftNull,
    private Object[] row;
    private ColumnDescription[] CDs;
    private ColumnValuePair[] CVPs;
    private Map<String,Object> CVPMap;
    private int valuePairsUsed = 0;//used to verify that every valuePair was used (if not then the user inputted a column not in the table
    protected Row(Table table, InsertQuery insertQuery) throws Exception{
        CDs = table.getColumnDescriptions();
        row = new Object[table.getColumnDescriptions().length];
        CVPs = insertQuery.getColumnValuePairs();
        if(CVPs.length > row.length) {//verify not too many values
            throw new IllegalArgumentException("too many values");
        }
        //create HashMap with column names as keys and columnValuePair objects as buckets
        CVPMap = toHashMap(CVPs);
        //Iterate through each element of the columnDesciptions
        //and assign a value to the corresponding index of the row array
        for(int i = 0; i < CDs.length; i++) {
            if(assignValue(i) == false) {
                throw new Exception();//make more specific
            }
            //if the object in the row is null, verify that it is allowed to be null
            if(row[i] == null && valueCantBeNull(i)) {
                throw new IllegalArgumentException("The "+ CDs[i].getColumnName() +" may not be null");
            }
        }
        //verify that every element from query was used.
        //if not then the query contained an extra value pair not part of the table so throw error
        if(valuePairsUsed != CVPs.length) {
            System.out.println("You tried to input a column which doesn't exist for this table");
            throw new IllegalArgumentException("You tried to input a column which doesn't exist for this table");
        }
    }
    private boolean assignValue(int columnPosition) {
        ColumnDescription cd = CDs[columnPosition];
        //check to see if it matches a key (the user assigned a specific value for the field).
        if(CVPMap.containsKey(cd.getColumnName())){
            if(!assignSpecificValue(columnPosition, cd)) {
                return false;
            }
        }
        else {//if no value was specified for the field assign the default value or null
            assignDefaultValue(columnPosition);
        }
        return true;
    }

    //assigns a default value based on the column description or null if there isn't one
    private void assignDefaultValue(int columnPosition){
        ColumnDescription cd = CDs[columnPosition];
        if(cd.getHasDefault()) {
            row[columnPosition] = castDefaultValue(cd.getColumnType(),cd.getDefaultValue());//need to parse so that it is correct type

        }
        else{
            row[columnPosition] = null;
        }
    }
    //casts the value as it's true class instead of simply string
    private Object castDefaultValue(DataType columnType, String defaultValue) {
        switch (columnType) {
            case BOOLEAN:
                return  Boolean.valueOf(defaultValue);
            case INT:
                return Integer.valueOf(defaultValue);
            case DECIMAL:
                return Double.valueOf(defaultValue);
            case VARCHAR://removes the quotes
                return Table.removeSingleQuotes(defaultValue);
        }
        //make compiler happy
        return defaultValue;
    }

    /*
     * assigns a value to row[columnPosition] based on CVPs array
     * return true if successfull, otherwise return false
     */
    private boolean assignSpecificValue(int columnPosition, ColumnDescription cd) throws NumberFormatException, IllegalArgumentException{
        DataType dataType = cd.getColumnType();
        String value = CVPMap.get(cd.getColumnName()).toString();
        try {
            if(dataType == DataType.VARCHAR){
                //verify that the string starts and ends with single quotes
                if(value.length()-2 > cd.getVarCharLength() || !value.startsWith("'") || !value.endsWith("'"))
                {return false;}
                row[columnPosition] = value.substring(1, value.length()-1);//take off the ' from thte beginning and end
            } else if(dataType == DataType.INT) {
                row[columnPosition] = Integer.parseInt(value);
            } else if(dataType == DataType.DECIMAL) {//check to see if the number is too big should I trim or return false
                Double decimal = Double.parseDouble(value);
                double expandedNumber = decimal*Math.pow(10, cd.getFractionLength());
                if(decimal / Math.pow(10, cd.getWholeNumberLength()-1) >= 10 || (expandedNumber - (int)(expandedNumber) > 0)){
                    return false;
                }
                row[columnPosition] = decimal;
            } else if(dataType == DataType.BOOLEAN) {
                if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    return false;
                }
                row[columnPosition] = Boolean.parseBoolean(value);
            }
        }
        catch(Exception e) {
            return false;
        }
        valuePairsUsed++;
        return true;
    }
    private Map<String, Object> toHashMap(ColumnValuePair[] CVPArray) {
        HashMap<String,Object> CVPMap = new HashMap<String,Object>() ;
        //put all the ColumnValuePairs in a HasMap with names of Columns as Key
        for(ColumnValuePair cvp: CVPArray) {
            CVPMap.put(cvp.getColumnID().getColumnName(),cvp.getValue());
        }
        return CVPMap;
    }
    //checks to see if the columnPosition is allowed to be null or not
    private boolean valueCantBeNull(int columnPosition) {
        return CDs[columnPosition].isNotNull();
    }


    //returns a List version of the row to be added to the Table
    protected List<Object> getRowList(){
        return new ArrayList<Object>(Arrays.asList(row));
    }
    protected Object[] getRow() {
        return row;
    }
/*
	 * IGNORE FOR NOW. JUST AN ALTERNATIVE WAY TO ITTERATE THROUGH COLUMNS WITHOUT HASHMAP

	//assign values based on query
	int columnPosition;
	for(ColumnValuePair cvp: CVPs) {
		//verify the name of the column matches one of the column names in the table
		for(int i = 0; i < CDs.length; i++) {
			ColumnDescription cd = CDs[i];
			String name = cd.getColumnName();
			//if it matches assign the value
			if(cvp.getColumnID().getColumnName().equals(name)) {
				columnPosition = i;
				//assignment
				break;
			}
			//if columnValluePair mathches no Column in the table, throw an error
			else {
				throw new IllegalArgumentException("Column name mismatch");
			}

		}
		//for the rest of the assign the default
		 *

	}*/
}

