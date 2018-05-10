package DS.SemesterProject.DB;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition.Operator;

import java.util.*;

import static edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType.*;
import static edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionInstance;
import static edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.OrderBy;

//Test: normal, varcharNegative, DecimalNegative, notNull-defaultNull, defaultWrongBoolean, defaultWrongVarchar, defaultWrongDecimal, defaultWrongInt,
//WrongPrimaryKey,
//make sure that it matches my own veresion.
public class Table {
    private ColumnDescription[] columnDescriptions;
    private ColumnDescription primaryKeyColumn;
    private String tableName;
    private List<ArrayList<Object>> table;
    private Map<String, Integer> columnPositionMap;//stores column names as keys and positions as values
    private SimpleColumnDescription[] simpleColumnDescriptions;

    private Boolean onlyEquals;//used for DBTrees and WHERE clause. u
    private Boolean usedBTree = false;
    private Map<String, BTree> BTreeMap;

    //private List<ArrayList<Object>> validRowz;//used for select
    //private Integer rowPozition;//used to keep track of rows in select

    //creates a table based on the given Query
    //@param query the given SQLQuery
    //Test: WrongQuery, missingColumnDescriptions
    protected Table(SQLQuery query) throws IllegalArgumentException {
        //check for valid input
        if(!(query  instanceof CreateTableQuery)) {//need to fix this
            throw new IllegalArgumentException("Wrong query type");
        }
        CreateTableQuery tableQuery = (CreateTableQuery) query;
        tableName = tableQuery.getTableName();
        columnDescriptions = tableQuery.getColumnDescriptions();
        //verifies the default values are valid
        if(!validDefaultValues(columnDescriptions)){
            System.out.println("The default values you inputted are invalid");
            throw new IllegalArgumentException("The default values you inputted are invalid");
        }
        simpleColumnDescriptions = SimpleColumnDescription.makeSimpleColumnDescriptions(columnDescriptions);
        primaryKeyColumn = tableQuery.getPrimaryKeyColumn();
        table = new ArrayList<>();
        columnPositionMap = new HashMap<String, Integer>();
        //sets all the keys and values for the columnPositionMap
        for(int i = 0; i < columnDescriptions.length; i++){
            columnPositionMap.put(columnDescriptions[i].getColumnName(), i);
        }
        //update primaryKey by setting values to notNull and unique
        //also crerate index for primamry key
         BTreeMap = new HashMap<String, BTree>();
        primaryKeyColumn.setNotNull(true);
        primaryKeyColumn.setUnique(true);
        createIndex(primaryKeyColumn.getColumnName());//@@@
    }

    protected Boolean getUsedBTree() {
        return usedBTree;
    }

    //verifies that the default column values match the restrictions of the columns
    private boolean validDefaultValues(ColumnDescription[] columnDescriptions) {
        for(ColumnDescription cd: columnDescriptions){
            if(!cd.getHasDefault()){continue;}//if no default then automatically valid
            DataType dataType = cd.getColumnType();
            String value = cd.getDefaultValue();
            try {
                if (dataType == DataType.VARCHAR) {
                    //verify that the string starts and ends with single quotes
                    if (value.length() > cd.getVarCharLength() || !value.startsWith("'") || !value.endsWith("'")) {
                        return false;
                    }
                } else if (dataType == DataType.INT) {
                    Integer.parseInt(value);//simply used to see if it throws an error
                } else if (dataType == DataType.DECIMAL) {//check to see if the number is too big should I trim or return false
                    Double decimal = Double.parseDouble(value);
                    double expandedNumber = decimal * Math.pow(10, cd.getFractionLength());
                    if (decimal / Math.pow(10, cd.getWholeNumberLength() - 1) >= 10 || (expandedNumber - (int) (expandedNumber) > 0)) {
                        return false;
                    }
                } else if (dataType == DataType.BOOLEAN) {
                    if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    //privateConstructor for ResultSet/ Takes a 2D arrayList and simpleColumnDescriptions and makes a ResultSet
    protected Table(List<ArrayList<Object>> table, SimpleColumnDescription[] simpleColumnDescriptions){
        this.table = table;
        this.simpleColumnDescriptions = simpleColumnDescriptions;
        // this.columnDescriptions = CDs;
        columnPositionMap = new HashMap<String, Integer>();
        //sets all the keys and values for the columnPositionMap
        for(int i = 0; i < getSimpleColumnDescriptions().length; i++){
            columnPositionMap.put(simpleColumnDescriptions[i].getColumnName(), i);
        }
    }

    //creates index with query as param
    protected ResultSet createIndex(CreateIndexQuery query){
        return createIndex(query.getColumnName());
    }
    //Creates a new BTree index on the column in the table requested
    //Keys are columnValues and values are rows of the table
    //adds BTree to the hashMap of BTrees
    protected ResultSet createIndex(String columnName){
        //check for valid columnName
        //String columnName = query.getColumnName();
        Integer columnPosition = columnPositionMap.get(columnName);
        if(columnPosition == null){//name isn't in table
            throw new IllegalArgumentException(columnName+" is not a valid columnName");
        }//create index on column
        try{
            BTree bTree = new BTree();
            for(ArrayList<Object> row: table){
                Object value = row.get(columnPosition);
                bTree.put(getTypedVersion(value), row);
            }
            BTreeMap.put(columnName, bTree);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e);
        }
        return new ResultSet(true);
    }
    //returns the object casted to its true class.
    //Strings, booleans, Integers, and doubles are all saved as objects so to compare them
    //they need to be casted
    protected Comparable getTypedVersion(Object value){
        if(value instanceof Integer) {
            return (Integer)value;
        }
        if(value instanceof Double) {
            return (Double)value;
        }
        if(value instanceof String) {
            return (String)value;
        }
        if(value instanceof Boolean){
            return (Boolean)value;
        }//never happens
        return null;
    }

    //deletes specific rows from the table based on the conditions of the where clause
    protected ResultSet delete(DeleteQuery query) throws Exception{usedBTree = false;//used for tests
        //if the where clause is null, delete all rows
        Condition whereClause = query.getWhereCondition();
        if(whereClause == null){//change to start
            table = new ArrayList<>();//MAY NEED TO FIX@@@
            return new ResultSet(true);
        }
        //iterate through the rows of the table and delete them if they fit the where clause
        try{
            //check for BTree use
            if(isTableBTreeWorthy(whereClause)){
                usedBTree = true;
                deleteUsingBTree(whereClause);
            }
            for(int i = 0; i < table.size(); i++){
                if(isValidRow(table.get(i), whereClause)){
                    deleteRowFromBtrees(table.get(i));
                    table.remove(i);
                    i--;//if the row is deleted then the index of the other rows will all be decremented.
                }
            }
            return new ResultSet(true);
        }
        //indicates that one of the conditions was invalid
        catch(Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }
    //deletes the given row from all BTrees
    private void deleteRowFromBtrees(ArrayList<Object> rowToDelete) {
        for(String string: BTreeMap.keySet()) {
            BTree bTree = BTreeMap.get(string);
            bTree.delete(getTypedVersion(rowToDelete.get(columnPositionMap.get(string))), rowToDelete);
        }
        //        for(String string: BTreeMap.keySet()) {
//            BTree bTree = BTreeMap.get(string);
//            List<ArrayList<Object>> rows = bTree.get(getTypedVersion(rowToDelete.get(columnPositionMap.get(string))));
//            if (rows.contains(rowToDelete)) {
//                rows.remove(rowToDelete);
//            }
//        }
    }

    //deletes the that meet the where condition using a BTree
    private ResultSet deleteUsingBTree(Condition whereClause) {
        List<ArrayList<Object>> table1 = getValidTableFromBtree(whereClause);
        //String columnName = getFirstColumnName(where)
        for(int i = 0; i < table.size(); i++){
            if(table1.contains(table.get(i))){
                table.remove(i);
                i--;//if the row is deleted then the index of the other rows will all be decremented.
            }
        }
        deleteRowsFromBTrees(table1);

        return new ResultSet(true);
    }
    //deletes the given rows from the BTrees
    private void deleteRowsFromBTrees(List<ArrayList<Object>> table1) {
        for(int i = 0; i <table1.size(); i++){
            ArrayList<Object> row = table1.get(i);
            deleteRowFromBtrees(row);
        }
    }

    //attempts to insert a new row into the table using the query and
    //checking that the row meets the requirements of the table
        protected ResultSet insert(InsertQuery insertQuery) throws Exception {usedBTree = false;//used for tests
        //create a new row entry
        List<Object> row = new Row(this, insertQuery).getRowList();
        //verify there are no duplicates for column labeled as Unique or primary Key
        for(int columnPosition = 0; columnPosition< columnDescriptions.length; columnPosition++){
            ColumnDescription cd = columnDescriptions[columnPosition];
            if(cd.isUnique() && !isValueUnique(columnPosition, row)){
                System.out.println("The column " + cd.getColumnName()+ " must be unique");
                throw new IllegalArgumentException("The column " + cd.getColumnName()+ " must be unique");
            }//assuming the row is valid, add it to the table
        }
        //insert into BTrees
        ArrayList<Object> rowList = (ArrayList<Object>)(row);
        table.add(rowList);//@@@
        insertBTree(rowList);
        return new ResultSet(true);
    }
    //go's every Btree and updates it after insert completed
    private void insertBTree(ArrayList<Object> row) {
        for(String columnName:BTreeMap.keySet()){
            //gets the BTree corresponding to the columnName and the value of the row and inserts the row under that value
            BTreeMap.get(columnName).put(getTypedVersion(row.get(columnPositionMap.get(columnName))), row);
        }
    }

    //compares the rowValue of the given position to all the values of that column table
    private boolean isValueUnique(int columnPosition, List<Object> row) {
        if(row.get(columnPosition) == null){return true;}//null is considered unique
        for(ArrayList<Object> tableRow: table){//if it has the same value return false
            //If either one is null then considered unique
            if(tableRow.get(columnPosition) == null){continue;}
            if(tableRow.get(columnPosition).equals((row.get(columnPosition)))){
                return false;
            }
        }
        return true;//if unique then return true
    }
    //compares the value at the given position to all the values of that column table
    private boolean isValueUnique(int columnPosition, Object value) {
        if(value == null){return true;}//null is considered unique
        for(ArrayList<Object> tableRow: table){//if it has the same value return false
            //If either one is null then considered unique
            if(tableRow.get(columnPosition) == null){continue;}
            if(tableRow.get(columnPosition).equals(value)){
                return false;
            }
        }
        return true;//if unique then return true
    }
    //returns true or false depending of the number matches the operator. Negatives match with less than, positives match
    //with greater than and 0 matches with =.
    private boolean isCompareValueOK(Integer compareValue, Operator operator) {
        if(compareValue < 0 && (operator.equals(Operator.LESS_THAN_OR_EQUALS) || operator.equals(Operator.LESS_THAN) || operator.equals(Operator.NOT_EQUALS))){
            return true;
        }
        if(compareValue == 0 && (operator.equals(Operator.EQUALS)|| operator.equals(Operator.GREATER_THAN_OR_EQUALS) || operator.equals(Operator.LESS_THAN_OR_EQUALS))){
            return true;
        }
        if (compareValue > 0 && (operator.equals(Operator.NOT_EQUALS) || operator.equals(Operator.GREATER_THAN) || operator.equals(Operator.GREATER_THAN_OR_EQUALS))) {
            return true;
        }
        //if it doesn't match any of these then its not valis
        return false;
    }
    //acts as a compareTo() method for two ColumnIDs
    private Integer compareLeftToRight(Object leftElement, Operator operator, Object rightElement) {
        Class clazz = leftElement.getClass();
        try{
            switch (clazz.toString()) {
                case "class java.lang.Integer"://Ints an be compared to Doubles
                    return (Double.valueOf(leftElement.toString()).compareTo((Double)(rightElement)));
                case "class java.lang.Double":
                    return ((Double) leftElement).compareTo(Double.valueOf(rightElement.toString()));
                case "class java.lang.String":
                    return ((String) leftElement).compareTo((String)rightElement);
                case "class java.lang.Boolean":
                    if(!(operator.equals(Operator.NOT_EQUALS) || operator.equals(Operator.EQUALS)))//can only use = and <> for boolean
                        throw new IllegalArgumentException("Can't compare Booleans");
                    return ((Boolean) leftElement).compareTo((Boolean)(rightElement));
            }
            return null;
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        catch(Exception e){
            System.out.println(rightElement +" did not match corresponding operand");
            throw new IllegalArgumentException(rightElement +" did not match corresponding operand");
        }
    }
    //Acts as a compareTo() method for ColumnID and string.
    private Integer compareLeftToRight(Object leftElement, Operator operator, String rightElement){
        Class clazz = leftElement.getClass();
        try {
            switch (clazz.toString()) {
                case "class java.lang.Integer"://Ints can be compared to Doubles. Also can't cast from Integer to Double so use cast to string first
                    return ((Double.valueOf(leftElement.toString())).compareTo(Double.valueOf(rightElement)));
                case "class java.lang.Double":
                    return ((Double) leftElement).compareTo(Double.parseDouble(rightElement));
                case "class java.lang.String":
                    return ((String) leftElement).compareTo(rightElement);
                case "class java.lang.Boolean":
                    if(!(operator.equals(Operator.NOT_EQUALS) || operator.equals(Operator.EQUALS)))//can only use = and <> for boolean
                        throw new IllegalArgumentException("Can't compare Booleans");
                    return ((Boolean) leftElement).compareTo((Boolean.valueOf(rightElement)));
            }
            return null;
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(rightElement +" did not match corresponding operand");
        }
    }
    //returns whether the operator is AND || OR
    protected static boolean isBooleanOperator(Operator operator){
        return(operator.equals(Operator.AND) || operator.equals(Operator.OR));
    }
    //returns whether the operator is not (AND || OR)
    protected static boolean isComparator(Operator operator){
        return (operator.equals(Operator.NOT_EQUALS) ||
                operator.equals(Operator.EQUALS) ||
                operator.equals(Operator.GREATER_THAN) ||
                operator.equals(Operator.GREATER_THAN_OR_EQUALS) ||
                operator.equals(Operator.LESS_THAN) ||
                operator.equals(Operator.LESS_THAN_OR_EQUALS));
    }

    //Given a table (2DList) and a whereClause, return a new table with only the valid rows in it
    //Go through each one and of valid add to new 2DList, return list
    //Do this by simply calling isValid row on each row
    private List<ArrayList<Object>> getValidTable(List<ArrayList<Object>> table, Condition whereClause){
        ArrayList<ArrayList<Object>> validRows = new ArrayList<ArrayList<Object>>();
        if(isTableBTreeWorthy(whereClause)){//checks if a DBTree should be used, and how it should be used
            usedBTree = true;//used for tests
            return getValidTableFromBtree(whereClause);

        }
        for(ArrayList<Object> row : table){
            if(isValidRow(row, whereClause)){
                validRows.add(row);
            }
        }
        return validRows;
    }
    //acts as getValidTable but uses DBTrees. Gets the rows that meet the criteria of the whereClause
    private List<ArrayList<Object>> getValidTableFromBtree(Condition whereClause) {
        //if the whereClause involves any calculations that don't use '=' then simply get all the entries and
        //run them through the normal methods for the whereClause
        if(!onlyEquals){
            return itterationBTreeTable(whereClause);
        }//assuming that every case of comparison in the whereClause is only an equals
        //then call the get function on the different indexes and get the union or intersection of the two
        else{
            Object leftOperand = whereClause.getLeftOperand();
            Object rightOperand = whereClause.getRightOperand();
            Condition.Operator operator = whereClause.getOperator();
            //Base case. The operator is comparator and at least one operand is an ID
            if(isComparator(operator)){
               return baseCaseBTree(whereClause);
            }//recursive case
            if(isBooleanOperator(operator) && leftOperand instanceof Condition && rightOperand instanceof  Condition){
                if(operator == Operator.AND){
                    return getIntersection(getValidTableFromBtree((Condition)(leftOperand)), getValidTableFromBtree((Condition)(rightOperand)));
                }
                if(operator == Operator.OR){
                    return getUnion(getValidTableFromBtree((Condition)(leftOperand)), getValidTableFromBtree((Condition)(rightOperand)));
                }
            }
            throw new IllegalArgumentException("WHERE statement invalid");//invalid input
        }
    }
    //Itterates through one of the Entry sets of a map and forms a table from the valid rows according to the wherClause
    private List<ArrayList<Object>> itterationBTreeTable(Condition whereClause) {
        String columnName = getFirstColumnName(whereClause);
        BTree bTree = BTreeMap.get(columnName);
        ArrayList<BTree.Entry> BTreeTable = bTree.getOrderedEntries();
        ArrayList<ArrayList<Object>> validRows = new ArrayList<ArrayList<Object>>();
        for(BTree.Entry entry : BTreeTable){
            for(Object rowObject: entry.getValue()){
                ArrayList<Object> row = (ArrayList<Object>)rowObject;
                if(isValidRow(row, whereClause)){
                    validRows.add(row);
                }
            }
        }
        return validRows;
    }
    //creates a new List<ArrayList<Object>> of all the values that either table contains
    private List<ArrayList<Object>> getUnion(List<ArrayList<Object>> table1, List<ArrayList<Object>> table2) {
        List<ArrayList<Object>> intersectionTable = table2;
        for (ArrayList<Object> row : table1) {
            if (!table2.contains(row)) {//adds all the new values
                intersectionTable.add(row);
            }
        }
        return intersectionTable;
    }

    //creates a new List<ArrayList<Object>> of all the values that both tables contain
    private List<ArrayList<Object>> getIntersection(List<ArrayList<Object>> table1, List<ArrayList<Object>> table2) {
        List<ArrayList<Object>> intersectionTable = new ArrayList<>();
        for (ArrayList<Object> row : table1) {
            if (table2.contains(row)) {
                intersectionTable.add(row);
            }
        }
        return intersectionTable;
    }

    //the recursive case for BTree when getting the valid rows using a where clause
    protected List<ArrayList<Object>> baseCaseBTree(Condition whereClause) throws IllegalArgumentException {
        Object leftOperand = whereClause.getLeftOperand();
        Object rightOperand = whereClause.getRightOperand();
        Integer compareValue = -2;//used as a random int for comparing later
        if(leftOperand instanceof ColumnID){//Left is ColumnID
            ColumnID leftColumnID = (ColumnID)(leftOperand);
            BTree bTree = BTreeMap.get(leftColumnID.getColumnName());
            //get the table that matches the whereClause. ex. WHERE GPA = 4.0 will return all rows where GPA = 4.0
            if(rightOperand instanceof String) {//leftColumnID right String
                List<ArrayList<Object>> table1 = null;//@@@
                String rightString = rightOperand.toString();
                DataType dataType = columnDescriptions[columnPositionMap.get(leftColumnID.getColumnName())].getColumnType();
                if(dataType.equals(INT)){
                    table1 = (ArrayList<ArrayList<Object>>)(bTree.get(Integer.valueOf(removeSingleQuotes(rightString))));
                }else  if(dataType.equals(DECIMAL)){
                    table1 = (ArrayList<ArrayList<Object>>)bTree.get(Double.valueOf(removeSingleQuotes(rightString)));
                }else if(dataType.equals(VARCHAR)){
                    table1 = (ArrayList<ArrayList<Object>>) bTree.get((removeSingleQuotes(rightString)));
                }else if (dataType.equals(BOOLEAN)){
                    if((rightString.equalsIgnoreCase("true") || rightString.equalsIgnoreCase("false"))){
                        List<ArrayList<Object>> table = (ArrayList<ArrayList<Object>>)bTree.get(Boolean.valueOf(removeSingleQuotes(rightString)));
                    }else
                        throw new IllegalArgumentException("Boolean van only be true or false");
                }
                return table1;
            } else if(rightOperand instanceof ColumnID && leftOperand instanceof String){//left String right ColumnId. NOT ALLOWED
                throw new IllegalArgumentException(leftOperand.toString() +" is not a valid ColumnName");
            }
        }
        throw new IllegalArgumentException("Invalid where");
    }
    //gets the first columnName referenced by the whereCondition.
    //This is used to retrieve a list of rows from the BTree
    //recursively goes to the left of the where clause until it finds a columnName
    private String getFirstColumnName(Condition whereClause){
        if(whereClause.getLeftOperand() instanceof ColumnID){
            ColumnID leftOperand = (ColumnID)(whereClause.getLeftOperand());
            return leftOperand.getColumnName();
        }
        else {
            return getFirstColumnName((Condition) (whereClause.getLeftOperand()));
        }
    }

    //checks all the ColumnIDs to see if they have been indexed.
    //if yes then return true
    private boolean isTableBTreeWorthy(Condition whereClause) {
        if(whereClause == null){return false;}
        this.onlyEquals = true;//used to determine how BTree will be used assuming it is used
        return isTableBTreeWorthyRecursive(whereClause);
    }
    //breaks down the whereClause to check if columns have been indexed
    private boolean isTableBTreeWorthyRecursive(Condition condition) {
        Object leftOperand = condition.getLeftOperand();
        Object rightOperand = condition.getRightOperand();
        Condition.Operator operator = condition.getOperator();
        if(isComparator(operator)){//is it <,>,=,<>,>=,<=
            if(!operator.equals(Operator.EQUALS)){
                this.onlyEquals = false;
            }
            //base cases: either both left and right are ColumnIDs or just left i columnID, middle is Operator
            if(leftOperand instanceof ColumnID){//Left is ColumnID
                ColumnID leftColumnID = (ColumnID)(leftOperand);
                if(BTreeMap.get(leftColumnID.getColumnName()) == null) {//not indexed
                    return false;
                }//both columnIDs. In this case I believe it doesnt make any sense to
                //use the functionality of the BTree because one must traverse all the rows anyway to get the respective
                //values to compare. Therefore treat as the same case as if the comparator was not Equals so simple
                //get all Entries and itterate through them
                if(rightOperand instanceof ColumnID){
                    this.onlyEquals = false;
                    ColumnID rightColumnID = (ColumnID)(rightOperand);
                    if(columnPositionMap.get(rightColumnID.getColumnName()) == null)//not existent
                        throw new IllegalArgumentException("One column doesn't exist");
                }
                return true;
            }//left not columnID
            else{
                return false;
            }
        }else{//recursively breaks down the where and checks those
            return isTableBTreeWorthyRecursive((Condition)leftOperand) && isTableBTreeWorthyRecursive((Condition)rightOperand);
        }
    }

    //verifies that a columnName is valid by checking it against the columnMap
        private boolean isValidColumnName(String string){
            return columnPositionMap.get(string) != null;
        }
    /*
   validates that the row is valid based on the conditions of the where clause
   @throw IllegalArgumentException if the where clause has an invalid condition
   @return valid. true if the row meets the requirements of the whee clause, false otherwise.
   recursively call itself on the whereClause.
   Don't forget to validate whereClause
    */
    private boolean isValidRow(ArrayList<Object> row, Condition whereClause) throws IllegalArgumentException {//need to make new method to validate rows based on where clause
        if(whereClause == null){return true;}
        Object leftOperand = whereClause.getLeftOperand();
        Object rightOperand = whereClause.getRightOperand();
        Condition.Operator operator = whereClause.getOperator();
        //Base case. The operator is comparator and at least one operand is an ID
        if(isComparator(operator)){
            return baseCase(row, whereClause);
        }//recursive case
        if(isBooleanOperator(operator) && leftOperand instanceof Condition && rightOperand instanceof  Condition){
            if(operator == Operator.AND){
                return isValidRow(row, (Condition)(leftOperand)) && isValidRow(row, (Condition)(rightOperand));
            }
            if(operator == Operator.OR){
                return isValidRow(row, (Condition)(leftOperand)) || isValidRow(row, (Condition)(rightOperand));
            }
        }
        throw new IllegalArgumentException("WHERE statement invalid");//invalid input
    }
    //the base case for determining if a row fits the where clause.
    protected boolean baseCase(ArrayList<Object> row, Condition whereClause) throws IllegalArgumentException {
        Object leftOperand = whereClause.getLeftOperand();
        Object rightOperand = whereClause.getRightOperand();
        Condition.Operator operator = whereClause.getOperator();
        Integer compareValue = -2;//used as a random int for comparing later
        if(leftOperand instanceof ColumnID){//Left is ColumnID
            ColumnID leftColumnID = (ColumnID)(leftOperand);
            Object leftElement = row.get(columnPositionMap.get(leftColumnID.getColumnName()));
            if(leftElement == null) {
                System.out.println("The column name " + leftColumnID.getColumnName() + " is not in the table");
                throw new IllegalArgumentException("The column name " + leftColumnID.getColumnName() + " is not in the table");
            }//both columnIDs
            if(rightOperand instanceof ColumnID){
                ColumnID rightColumnID = (ColumnID)(rightOperand);
                Object rightElement = row.get(columnPositionMap.get(rightColumnID.getColumnName()));//the column from the row
                compareValue = compareLeftToRight(leftElement, operator, rightElement);
            }//leftColumnID right String
            else if(rightOperand instanceof String) {
                //compare the left element with the right and take off the starting and ending ''
                compareValue = compareLeftToRight(leftElement, operator, removeSingleQuotes((String)(rightOperand)));
            }
        }//left String right ColumnId. NOT ALLOWED
        else if(rightOperand instanceof ColumnID && leftOperand instanceof String){
            System.out.println(leftOperand.toString() +" is not a valid ColumnName");
            throw new IllegalArgumentException(leftOperand.toString() +" is not a valid ColumnName");
        }
        return (isCompareValueOK(compareValue, operator));
    }
    //removes the single quotes of a string if it has them
    protected static String removeSingleQuotes(String str){
        if(str.length() < 2){//if its size is less than two than it does not have single quotes
            return str;
        }//if it has' then remove them
        if(str.startsWith("'") && str.endsWith("'")){
            return str.substring(1, str.length()-1);
        }//if it doesn't have ' then return it as it is
        return str;
    }
    //updates the table based on the conditions of the UpdateQuery
    protected ResultSet update(UpdateQuery query) throws IllegalArgumentException, Exception{usedBTree = false;
        ColumnValuePair[] CVPs = query.getColumnValuePairs();
        Condition where = query.getWhereCondition();
        Map<Integer, Object> updatedValues = new HashMap<Integer, Object>();//holds the updated values
        for (ColumnValuePair cvp : query.getColumnValuePairs()) {
            //find columnPosition of each of the columns to be affected
            Integer columnPosition = columnPositionMap.get(cvp.getColumnID().getColumnName());
            //columnPosition==null means the column name doesn't exist
            //updateUpdatedValues only returns true if the new value is valid
            if (columnPosition == null || !updateUpdatedValues(updatedValues, columnPosition, cvp)){//verify columnName is valid. ColumnPositionMap(key, element)(columnName, index in ColumnDesciptionArray)
                System.out.println("One of the columns you inputted was invalid");
                throw new IllegalArgumentException("One of the columns you inputted was invalid");
            }
        }
        Set<Integer> set = updatedValues.keySet();
        //verify that the values are unique
        for(Integer position: set){
            //check for unique values
            if(columnDescriptions[position].isUnique() && !isValueUnique(position, updatedValues.get(position))){
                System.out.println("You tried to update a unique value with an existing value");
                throw new IllegalArgumentException("You tried to update a unique value with an existing value");
            }
        }
        //if row is invalid then continue to next row
        List<ArrayList<Object>> rows = getValidTable(table, where);
        if(rows != null) {
            for (ArrayList<Object> row : rows) {
                //assuming the row meets the requirements of the where clause
                for (Integer position : set) {
                    row.set(position, updatedValues.get(position));
                }
            }
        }
        makeBTrees();
        return new ResultSet(true);
    }
    //recreates the BTrees after update is applied
    //original idea received from another student but implementation completely mine
    private void makeBTrees() {
        for(String columnName: BTreeMap.keySet()){
            BTree bTree = new BTree();
            for(ArrayList<Object> row: table){
                Object value = row.get(columnPositionMap.get(columnName));
                bTree.put(getTypedVersion(value), row);
            }
            BTreeMap.put(columnName, bTree);
        }
    }
    //return true of false depending if the column names in the query are all in the table
    private boolean isValidColumnNames(SQLQuery query){
        if(query instanceof SelectQuery){
            SelectQuery selectQuery = (SelectQuery)(query);
            for(ColumnID id: selectQuery.getSelectedColumnNames()){
                if(columnPositionMap.get(id.getColumnName()) == null){return false;}
            }
        }
        if(query instanceof CreateIndexQuery){
            CreateIndexQuery createIndexQuery = (CreateIndexQuery) (query);
            if(columnPositionMap.get(createIndexQuery.getColumnName()) == null){return false;}
        }
        return true;
    }
    //select the columns that are requested from the selectQuery.
    protected ResultSet select(SelectQuery selectQuery) throws IllegalArgumentException, Exception{usedBTree = false;//used foor testing
        //check to see if  * was used and verify the column names from the query are in the table
        if(selectQuery.getSelectedColumnNames().length > 0 && selectQuery.getSelectedColumnNames()[0].toString().equals("*"))
            return selectWithStar(selectQuery);
        if (!isValidColumnNames(selectQuery)) {
            System.out.println("the column names you entered don't match the table");
            throw new IllegalArgumentException("the column names you entered don't match the table");
        }
        ColumnID[] functionSelectRowID = new ColumnID[selectQuery.getFunctions().size()];//holds ColumnID's for the functions
        //remove all the ColumnDescriptions that appear both in the Functions list and the regular select
        ArrayList<FunctionInstance> functions = selectQuery.getFunctions();
        ArrayList<ColumnID> columnIDs = new ArrayList<ColumnID>(Arrays.asList(selectQuery.getSelectedColumnNames())); //first convert array to arrayList for searching purposes
        deleteRepeatedIDs(functions, columnIDs);
        ColumnID[] columnIDArray = new ColumnID[columnIDs.size()];
        ColumnID[] normalSelectRowID = (ColumnID[])columnIDs.toArray(columnIDArray);//Return to array
        //assigns values to functionSelectRowID
        List<ArrayList<Object>> normalSelectRows = new ArrayList<ArrayList<Object>>();
        List<ArrayList<Object>> functionSelectRows = new ArrayList<ArrayList<Object>>(1);
        //List<ArrayList<Object>> validRows = new ArrayList<ArrayList<Object>>();//used to store the rows after applying where clause
        List<ArrayList<Object>> validRows = new ArrayList<ArrayList<Object>>();
        try {
            validRows = getValidTable(table, selectQuery.getWhereCondition());//get all the valid rows after whereCondition applied
            //get the specific rows that don't have functions
            normalSelect(validRows, normalSelectRows, normalSelectRowID, selectQuery);//assigns values to normalSelectRows and normalSelectRowID based on selectQuery
            //get the rows that are part of the function
            functionSelect(validRows, functionSelectRows, functionSelectRowID, selectQuery);//assigns values to functionSelectRows and functionSelectRowID based on selectQuery
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        //get the columnDescriptions in the order they were requested with the changes based on the functions
        // ex. AVG(GPA) instead of GPA
        SimpleColumnDescription[] selectDescriptions = makeColumnDescriptions(selectQuery.getSelectedColumnNames(), functions);
        //combine all the values from the normalSelect and the functionSelect into one table
        List<ArrayList<Object>> allSelectRows = normalSelectRowscombineSelectedRows(normalSelectRows, functionSelectRows, selectQuery.getSelectedColumnNames(), functions);
        //make a map of the columnNames of allSelectRows and the columnPositions
        //it will be used for the orderBy methods
        Map<String, Integer> selectPositionMap = makeColumnPositionMap(selectDescriptions);
        return orderBy(selectDescriptions, allSelectRows,selectPositionMap, selectQuery);
    }
    //Performs the select Command when the "*" symbol is used
    private ResultSet selectWithStar(SelectQuery selectQuery) throws IllegalArgumentException{
        //If the Query is distinct than it is invalid
        if(selectQuery.isDistinct()){
            System.out.println("can't use distinct with * in query");
            throw new IllegalArgumentException("can't use distinct with * in query");
        }
        List<ArrayList<Object>> validRows = new ArrayList<ArrayList<Object>>();//used to store the rows after applying where clause
        List<ArrayList<Object>> allSelectRows = new ArrayList<ArrayList<Object>>();
        try {
            validRows = getValidTable(table, selectQuery.getWhereCondition());//get all the valid rows after whereCondition applied
            //add all the rows to the new ArrayList
            for(ArrayList<Object> row: validRows){
                allSelectRows.add(row);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        //transform columnDescriptions into SimpleColumnDescriptions
        SimpleColumnDescription[] selectDescriptions = SimpleColumnDescription.makeSimpleColumnDescriptions(columnDescriptions);
        Map<String, Integer> selectPositionMap = makeColumnPositionMap(selectDescriptions);
        return orderBy(selectDescriptions, allSelectRows,selectPositionMap, selectQuery);
    }
    //orders the rows based on the query and returns a ResultSet
    private ResultSet orderBy(SimpleColumnDescription[] selectDescriptions, List<ArrayList<Object>> allSelectRows, Map<String, Integer> selectPositionMap, SelectQuery selectQuery) {
        //Now order the rows based on the orderBy statement.
        OrderBy[] orderBys = selectQuery.getOrderBys();
        if (orderBys.length == 0) {//if no orderBys then simply make the resultSet using the current table
            ResultSet resultSet = new ResultSet(new Table(allSelectRows, selectDescriptions));
            return resultSet;
        }
        //verify the names of the columns and the dataTypes. if bad then print and error
        //stored the values of the columnPositions of the orderBys.
        Integer[] orderByColumnPositions = new Integer[orderBys.length];
        if(!getColumnPositions(orderBys, selectPositionMap, orderByColumnPositions))
        {//both validates the names and finds the columnPositions
            //return new ResultSet(false, new IllegalArgumentException("One of the columnNames not a columnName in the selected Data"));
            return new ResultSet(false, new IllegalArgumentException("One of the columnNames not a columnName in the table"));
        }
        if (!isValidDataTypes(orderBys, selectPositionMap, orderByColumnPositions, selectDescriptions)) {
            return new ResultSet(false, new IllegalArgumentException("one columnName is of type BOOLEAN, which is invalid for orderBy"));
        }
        //assuming there are no issues with the request
        List<ArrayList<Object>> orderedRows = new ArrayList<ArrayList<Object>>(allSelectRows.size());
        putInOrder(orderedRows, allSelectRows, orderBys, selectPositionMap, 0, orderByColumnPositions);
        return new ResultSet(new Table(orderedRows, selectDescriptions));
    }
    //Ittereate throw allSelectRows and puts them, in order in a orderedRows
    private void putInOrder(List<ArrayList<Object>> orderedRows,List<ArrayList<Object>> allSelectRows, OrderBy[] orderBys, Map<String, Integer> selectPositionMap, int orderByPosition, Integer[] orderByColumnPositions) {
        //for(int rowPosition = 0; rowPosition < allSelectRows.size();  rowPosition++){
        //ArrayList<Object> row = allSelectRows.get(rowPosition);
        for(ArrayList<Object> row: allSelectRows){
            //if orderedRows is empty then just add it to the beginning
            if (orderedRows.size() == 0) {
                orderedRows.add(row);
                continue;
            }
            callAscendOrDescend(orderedRows, row, orderBys, selectPositionMap, orderByPosition, orderByColumnPositions);

        }
    }
    //call the ascend ot descend method based on the orderBy object
    private void callAscendOrDescend(List<ArrayList<Object>> orderedRows, ArrayList<Object> row, OrderBy[] orderBys, Map<String, Integer> selectPositionMap, int orderByPosition, Integer[] orderByColumnPositions){
        OrderBy orderBy = orderBys[orderByPosition];
        if(orderBy.isAscending()){
            orderAscending(orderedRows,row,orderBys,selectPositionMap,orderByPosition, orderByColumnPositions);
        }else {
            orderDescending(orderedRows,row,orderBys,selectPositionMap,orderByPosition, orderByColumnPositions);
        }
    }
    //places the given row in it's rightful spot in ordered rows assuming order is ASC
    private void orderAscending(List<ArrayList<Object>> orderedRows, ArrayList<Object> row, OrderBy[] orderBys, Map<String, Integer> selectPositionMap, int orderByPosition, Integer[] orderByColumnPositions) {
        //i= the specific row we are up to
        for(int i=0; i<orderedRows.size();i++){//i= the specific row we are up to
            Object value = (row.get(orderByColumnPositions[orderByPosition]));
            Integer compareValue = null;
            compareValue = compare(value, (Comparable) value, orderedRows.get(i), orderByColumnPositions, orderByPosition);
            //if the this value is greater than that value or they are equal but there are no more order bys then place it in this spot
            if( compareValue < 0 || compareValue == 0 && orderByPosition == orderBys.length-1){
                orderedRows.add(i, row);
                break;
            }else if(compareValue > 0 ){
                if(i == orderedRows.size()-1) { //if it at the end just at it to the end
                    orderedRows.add(row);
                    break;
                }
                continue;
            }else if(compareValue == 0){
                //call the function again but increase orderByPosition to compare the next orderBy
                callAscendOrDescend(orderedRows,row,orderBys,selectPositionMap,orderByPosition+1, orderByColumnPositions);
            }
        }
    }
    //places the given row in it's rightful spot in ordered rows assuming order is DES
    private void orderDescending(List<ArrayList<Object>> orderedRows, ArrayList<Object> row, OrderBy[] orderBys, Map<String, Integer> selectPositionMap, int orderByPosition, Integer[] orderByColumnPositions) {
        for(int i=0; i<orderedRows.size();i++){//i= the specific row we are up to
            Object value = (row.get(orderByColumnPositions[orderByPosition]));
            Integer compareValue = null;
            compareValue = compare(value, (Comparable) value, orderedRows.get(i), orderByColumnPositions, orderByPosition);//if the this value is greater than that value or they are equal but there are no more order bys then place it in this spot
            if(compareValue > 0 || compareValue == 0 && orderByPosition == orderBys.length-1){
                orderedRows.add(i, row);
                break;
            }else if( compareValue < 0){
                if(i == orderedRows.size()-1) {//if it at the end just at it to the end
                    orderedRows.add(row);
                    break;
                }
                continue;
            } else if(compareValue == 0){
                //call the function again but increase orderByPosition to compare the next orderBy
                callAscendOrDescend(orderedRows,row,orderBys,selectPositionMap,orderByPosition+1, orderByColumnPositions);
            }
        }
    }
    //converts object to its 'true' class, instead of object
    //SOURCE-https://stackoverflow.com/questions/14524751/cast-object-to-generic-type-for-returning
    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> Integer compare(Object o, T classObject, ArrayList compareRow, Integer[] orderByColumnPositions, Integer orderByPosition) {
        T typedValue = (T)(o);
        //compare the value from the allSelectRows to the row in the orderedRow list.
        //return typedValue.compareTo(((T)(compareRow.get(orderByColumnPositions[orderByPosition]))));
        return typedValue.compareTo(((T)(compareRow.get(orderByColumnPositions[orderByPosition]))));
    }
    //creates a columnPositionMap which maps names to positions using the simpleColumnPosition[]
    private Map<String,Integer> makeColumnPositionMap(SimpleColumnDescription[] selectDescriptions) {
        Map<String, Integer> selectPositionMap = new HashMap<String,Integer>();
        for(int i = 0; i< selectDescriptions.length; i++){
            selectPositionMap.put(selectDescriptions[i].getColumnName(), i);
        }
        return selectPositionMap;
    }

    //itterate through the orderBys and add the columnPosition to the orderByColimnPositions
    //if it suceeds return true.
    private boolean getColumnPositions(OrderBy[] orderBys, Map<String, Integer> selectPositionMap,  Integer[] orderByColumnPositions){
        for (int i = 0; i < orderBys.length; i++) {
            OrderBy orderBy = orderBys[i];
            String columnName = orderBy.getColumnID().getColumnName();
            orderByColumnPositions[i] = selectPositionMap.get(columnName);
            if (orderByColumnPositions[i] == null) {//the columnName doesn't exist in this set
                System.out.println(columnName + " is not a columnName in the selected Data");
                //System.out.println(columnName + " is not a columnName in the table");
                return false;
            }
        }
        return true;
    }
    //validates that none of the values fromm orBy is a boolean
    private boolean isValidDataTypes(OrderBy[] orderBys, Map<String, Integer> selectPositionMap, Integer[] orderByColumnPositions, SimpleColumnDescription[] selectDescriptions){
        for(int i =0; i<orderBys.length;i++){
            OrderBy orderBy = orderBys[i];
            String name = orderBy.getColumnID().getColumnName();
            if(selectDescriptions[selectPositionMap.get(name)].getColumnDataType().equals(DataType.BOOLEAN)){
                System.out.println(name + " is of type BOOLEAN, which is invalid for orderBy");
                return false;
            }
        }
        return true;
    }

    //takes the columnIDs of the original selectQuerry and creates a simpleColumnDescription[]
    private SimpleColumnDescription[] makeColumnDescriptions(ColumnID[] selectedColumnNames, ArrayList<FunctionInstance> functions) {
        int functionPosition = 0;
        int position = 0;
        SimpleColumnDescription[] selectDescriptions = new SimpleColumnDescription[selectedColumnNames.length];
        SimpleColumnDescription selectDescription;
        for(ColumnID columnID: selectedColumnNames){
            //check to make sure there are more then one funciton
            //if the function column is == to the selectedColumnID then chage the name to corralate to the function
            //while creating the simpleColumnDescription
            FunctionInstance function = null;
            if(functions.size() > 0){function = functions.get(functionPosition);}
            if(functions.size() > 0 && function.column == columnID){
                //ex. changes GPA to AVG(GPA)
                String name = function.function.toString()+"("+function.column.getColumnName()+")";
                //create SimpleColumnDescription to return to ResultSet
                selectDescription = new SimpleColumnDescription(name, columnDescriptions[columnPositionMap.get(columnID.getColumnName())].getColumnType());
                functionPosition++;//go to next function
            }else {
                selectDescription = new SimpleColumnDescription(columnID.getColumnName(), columnDescriptions[columnPositionMap.get(columnID.getColumnName())].getColumnType());
            }
            selectDescriptions[position] = selectDescription;
            position++;
        }
        return selectDescriptions;
    }
    //combines the results from the normalSelect and the functionSelect
    private List<ArrayList<Object>> normalSelectRowscombineSelectedRows(List<ArrayList<Object>> normalSelectRows, List<ArrayList<Object>> functionSelectRows, ColumnID[] selectedColumnNames, ArrayList<FunctionInstance> functions) {
        int normalPosition = 0;
        int functionPosition = 0;
        List<ArrayList<Object>> allSelectData = new ArrayList<ArrayList<Object>>(normalSelectRows.size());
        //if there are no elements in the normalSelectRows then just return the functionSelectRows.
        if (normalSelectRows.size() == 0) {
            return functionSelectRows;
        }
        //sets the size of allSelectedData
        for (int i = 0; i < normalSelectRows.size(); i++) {
            allSelectData.add(new ArrayList<Object>());
        }

        for (ColumnID columnID : selectedColumnNames) {
            //if the function column is == to the selectedColumnID then add the row from the functionRows
            //to all rows of allSelectRows, otherwise
            if (functionPosition < functions.size() && functions.get(functionPosition).column == columnID) {
                for (ArrayList<Object> row : allSelectData) {
                    row.add(functionSelectRows.get(functionPosition));
                }
                functionPosition++;
            } else {
                for (int i = 0; i < normalSelectRows.size(); i++) {
                    allSelectData.get(i).add(normalSelectRows.get(i).get(normalPosition));
                }
                normalPosition++;
            }
        }
        return allSelectData;
    }

    //computes all the functions and adds them to the functionSelectRows
    private void functionSelect(List<ArrayList<Object>> validRows, List<ArrayList<Object>> functionSelectRows, ColumnID[] functionSelectRowID, SelectQuery selectQuery){
        for(FunctionInstance function: selectQuery.getFunctions()){
            functionSelectRows.add(new ArrayList<Object>());
            //gets the dataType of the function based on the corresponding position in the ColumnDescriptions[] of the table.
            DataType functionDataType = columnDescriptions[columnPositionMap.get(function.column.getColumnName())].getColumnType();
            //storageList holds the values that the function will take place on. (takes Distinct into account)
            List<Object> storageList = addValues(validRows, function);
            //call the specific function and add it to the first row of the functionSelectRow (each result will be a single value)
            if("AVG".equals(function.function.toString())){
                functionSelectRows.get(0).add(getAVG(storageList, functionDataType));
            }else if("COUNT".equals(function.function.toString())){
                functionSelectRows.get(0).add(getCOUNT(storageList));
            }else if("MAX".equals(function.function.toString())){
                functionSelectRows.get(0).add(getMAX(storageList, functionDataType));
            } else if("MIN".equals(function.function.toString())){
                functionSelectRows.get(0).add(getMIN(storageList, functionDataType));
            } else if("SUM".equals(function.function.toString())){
                functionSelectRows.get(0).add((getSUM(storageList, functionDataType)));
            }
        }
    }
    //creates an intialMax objwct based on the type (String, Double..) and calls a
    //generic method to compare the values.
    private Object getMAX(List<Object> storageList, DataType functionDataType) {
        //if there are no elements than there is no MAX
        if(storageList.size() == 0){
            return null;
        }
        //Booleans have no MAX
        if(functionDataType == BOOLEAN){
            System.out.println("Can't take the MAX of BOOLEAN");
            throw new IllegalArgumentException("Can't take the MAX of BOOLEAN");
        }

        if(functionDataType == VARCHAR) {
            String initialMax = "";
            return getMaxBasedOnType(initialMax, storageList);
        }
        if(functionDataType == INT){
            Integer initialMax = Integer.MIN_VALUE;
            return getMaxBasedOnType(initialMax, storageList);
        }
        if(functionDataType == DECIMAL){
            Double initialMax = Double.MIN_VALUE;
            return getMaxBasedOnType(initialMax, storageList);
        }
        //never gets to this line but makes compiler happy
        return null;
    }
    //This code itterates through the storageList and casts the elements to the appropiate type
    //to take adavantage of the compareTo method.
    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> T getMaxBasedOnType(T initialMax, List<Object> storageList){
        T max = initialMax;
        for(Object value : storageList) {
            T typedValue = (T) value;
            if (max.compareTo(typedValue) < 0) {
                max = typedValue;
            }
        }
        return max;
    }

    //creates an intialMin objwct based on the type (String, Double..) and calls a
    //generic method to compare the values.
    private Object getMIN(List<Object> storageList, DataType functionDataType) {
        //if there are no elements than there is no MIN
        if(storageList.size() == 0){
            return null;
        }
        //Booleans have no MIN
        if(functionDataType == BOOLEAN){
            System.out.println("Can't take the MIN of BOOLEAN");
            throw new IllegalArgumentException("Can't take the MIN of BOOLEAN");
        }

        if(functionDataType == VARCHAR) {
            String initialMin = (String)storageList.get(0);
            return getMinBasedOnType(initialMin, storageList);
        }
        if(functionDataType == INT){
            Integer initialMin = Integer.MAX_VALUE;
            return getMinBasedOnType(initialMin, storageList);
        }
        if(functionDataType == DECIMAL){
            Double initialMin = Double.MAX_VALUE;
            return getMinBasedOnType(initialMin, storageList);
        }
        //never gets to this line but makes compiler happy
        return null;
    }
    //This code itterates through the storageList and casts the elements to the appropiate type
    //to take adavantage of the compareTo method.
    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> T getMinBasedOnType(T initialMin, List<Object> storageList){
        T min = initialMin;
        for(Object value : storageList) {
            T typedValue = (T) value;
            if (min.compareTo(typedValue) > 0) {
                min = typedValue;
            }
        }
        return min;
    }

    //returns the size of the list
    private Integer getCOUNT(List<Object> storageList){
        return storageList.size();
    }
    //returns the sum of the items in the list
    private Object getSUM(List<Object> storageList, DataType functionDataType) throws IllegalArgumentException{
        if(functionDataType == VARCHAR || functionDataType == BOOLEAN){
            System.out.println("Can't take the sum of VARCHAR or BOOLEAN");
            throw new IllegalArgumentException("Can't take the sum of VARCHAR or BOOLEAN");
        }
        Double sum = 0.0;
        for(Object value: storageList){
            Double doubleValue;
            if(value instanceof Integer){
                Integer intValue = (Integer)(value);
                doubleValue = intValue*1.0;
            }else{
                doubleValue  = (Double)(value);
            }
            sum += doubleValue;
        }
        //sum the values as doubles then cast as int if needed
        if(functionDataType == INT){
            return sum.intValue();
        }
        return sum;
    }
    //returns the average of the values by calling the SUM and COUNT methods
    private Object getAVG(List<Object> storageList, DataType functionDataType) throws IllegalArgumentException{
        if(functionDataType == DECIMAL){
            return (Double)(getSUM(storageList, functionDataType))/getCOUNT(storageList);
        }else if(functionDataType == INT){//returns int
            return  ((Integer)(getSUM(storageList, functionDataType)))/getCOUNT(storageList);//@@@
        }
        else{
            System.out.println("Can't take the sum of VARCHAR or BOOLEAN");
            throw new IllegalArgumentException("Can't take the sum of VARCHAR or BOOLEAN");
        }
    }

    //adds the appropiate elements from the validRows to a new list for calculations of the function
    private List<Object> addValues(List<ArrayList<Object>> validRows,FunctionInstance function) {
        int columnPosition = columnPositionMap.get(function.column.getColumnName());//gets the position of the column
        //add the corresponding elements from the validRows to an array to do the calculations
        List<Object> storageList = new ArrayList<Object>();
        for(ArrayList<Object> validRow:validRows){
            Object value = validRow.get(columnPosition);
            if (!function.isDistinct || isDistinctValue(value, storageList)){
                storageList.add(value);
            }
        }
        return storageList;
    }


    //compare the element from the original row to the existing table based on DataType
    //used for the SELECT
    private boolean isDistinctValue(Object value, List<Object> storageList) {
        for(Object element: storageList){
            if(value.equals(element)){
                return false;
            }
        }
        return true;
    }

    //collects all the normally selected rows into one 2D array with the accompaning selectedRows
    private void normalSelect(List<ArrayList<Object>> validRows, List<ArrayList<Object>> normalSelectRows, ColumnID[] normalSelectRowID, SelectQuery selectQuery) {
        if(normalSelectRowID.length == 0){return;}//FIXED@@@
        for(ArrayList<Object> row : validRows) {//make a new row from each of the validRows
            ArrayList<Object> selectRow = new ArrayList<Object>();
            for (ColumnID name : normalSelectRowID) {//add each column from the select command to the row
                selectRow.add(row.get(columnPositionMap.get(name.getColumnName())));//FIXED@@@
            }
            //add the new row to the normalSelectRows
            if (!selectQuery.isDistinct() || isDistinctRow(selectRow, normalSelectRows, normalSelectRowID)) {
                normalSelectRows.add(selectRow);
            }
        }
    }
    private void deleteRepeatedIDs(ArrayList<FunctionInstance> functions, ArrayList<ColumnID> columnIDs) {
        for(FunctionInstance function: functions){
            //removes the duplicate element if it exists. Otherwise, does nothing
            for(ColumnID id: columnIDs){
                if(function.column.equals(id)){
                    columnIDs.remove(id);
                    break;
                }
            }
        }
    }


    //compares the inputted ArrayList to every row of the 2D arrayList. If it is equal to any of them return true, else return false
    private boolean isDistinctRow(ArrayList<Object> originalRow, List<ArrayList<Object>> table, ColumnID[] normalSelectRowID ) {
        //if the table is empty then the new entry is obviusly distinct
        if (table.size() == 0) { return true;}
        for(ArrayList<Object> tableRow: table){//go through each row in the table
            for(int i=0; i < normalSelectRowID.length; i++){//go through each columnId corresponding to the entries in the IDs
                //if any corresponding entry is not equal then the new doesnt match the current row
                if(!originalRow.get(i).equals(tableRow.get(i))){
                    break;
                    //   }
                }//ita at the last column and matchs all values
                if(originalRow.get(i).equals(tableRow.get(i)) && i == normalSelectRowID.length-1){
                    return false;
                }

            }
        }
        return true;
    }
    //verifies that the value from the ColumnValue pair fits the parameters of the column. If so, it maps a (key, value) pair
    //to the map (int valuePosition in the row, the value)
    //returns true if successful, otherwise returns false.@@@
    private boolean updateUpdatedValues(Map<Integer, Object> map, Integer position, ColumnValuePair cvp){
        ColumnDescription cd = columnDescriptions[position];
        DataType dataType = cd.getColumnType();
        String value = cvp.getValue();
        if(cd.isNotNull() && cvp.getValue().equalsIgnoreCase("null"))//deal with null case
                return false;//check for null values
        if(cvp.getValue().equalsIgnoreCase("null")){
            map.put(position, null);
            return true;
        }
        try {
            if(dataType == DataType.VARCHAR){//VARCHAR
                if(value.length() > cd.getVarCharLength())  //verify that the VARCHAR isn't too long
                    return false;
                map.put(position, removeSingleQuotes(value));
            } else if(dataType == INT) {//Integer
                map.put(position, Integer.parseInt(value));
            } else if(dataType == DECIMAL) {//Double //check to see if the number is too big should I trim or return false
                Double decimal = Double.parseDouble(value);
                if(inValidDouble(decimal,cd)){return false;}
                map.put(position, decimal);
            } else if(dataType == BOOLEAN) {//Boolean can be true, false, or null
                if(!(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))){return false;}
                map.put(position, Boolean.parseBoolean(value));
            }
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;  //if it doesn't match any other row then it is novel
    }

    //checks that a given double is within the requirements of the table
    private boolean inValidDouble(Double decimal,ColumnDescription cd) {
        //verify that the double does't have too many numbers before or after the decimal
        double expandedNumber = decimal*Math.pow(10, cd.getFractionLength());
        if(decimal / Math.pow(10, cd.getWholeNumberLength()-1) >= 10 || (expandedNumber - (int)(expandedNumber) > 0))
            return true;
        return false;//valid number
    }

    protected ColumnDescription[] getColumnDescriptions() {
        return columnDescriptions;
    }

    protected ColumnDescription getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    protected String getTableName() {
        return tableName;
    }
    //gets string version of the table
    protected void string(){
        for(ArrayList<Object> row: table){
            for(Object data: row){
                System.out.print(data+", ");
            }
            System.out.println("");
        }

    }
    protected List<ArrayList<Object>> getTable(){
        return table;
    }
    protected Map<String, BTree> getBTreeMap(){
        return BTreeMap;
    }
    protected SimpleColumnDescription[] getSimpleColumnDescriptions() {
        return simpleColumnDescriptions;
    }
    //Makes a hash code based on the significant variable of th table
    //doesn't use table name
    @Override
    public int hashCode() {
        int result = Objects.hash(primaryKeyColumn, table, columnPositionMap);
        result = 31 * result + Arrays.hashCode(columnDescriptions);
        result = 31 * result + Arrays.deepHashCode(simpleColumnDescriptions);
        return result;
    }

    //compares all the significant variables of the table
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Table other = (Table) (obj);
            return this.hashCode() == other.hashCode();
        }
    }

    //used to compare tables by the ResultSet
    //only compares the table variable and the simpleColumnDesciption
    public boolean simpleEquals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Table other = (Table)(obj);
            //if both are NULL or have same pointer then they are equal
            if(this.table == other.getTable() && this.getSimpleColumnDescriptions() == other.getSimpleColumnDescriptions()){
                return true;
            } //else if columnName or simpleColumnDescriptions is null and other isnt null then can't be equal
            if(this.table == null && other.getTable() != null  || this.simpleColumnDescriptions == null && other.getSimpleColumnDescriptions() != null){
                return false;
            }//if nothing is null then do normal comparison
            if(this.table != null && this.simpleColumnDescriptions != null) {
                return (isEqual(this.table, other.getTable())) && this.simpleColumnDescriptions.equals(other.getSimpleColumnDescriptions());
            }//only gets here if matching are null (ex.table of both this and other are null, but dataType isn't or Vice Versa)
            if(this.table != null){
                return this.table.equals(other.getTable());
            } else{
                return this.simpleColumnDescriptions.equals(other.getSimpleColumnDescriptions());
            }
        }
    }
    //compares to sets of Data (2D lists and checks for equivalency
    //only called when neither is null
    private boolean isEqual(List<ArrayList<Object>> table1, List<ArrayList<Object>> table2) {
        //first check size
        if(table1.size() != table2.size()){
            return false;
        }
        if(table.size() == 0){
            return true;
        }
        if( table1.get(0).size() != table2.get(0).size()){
            return false;
        }
        //now compares each element of the ArrayLists. if any are not equal then the tables aren't equal
        for(int i=0; i< table1.size(); i++){
            if(!table1.get(i).equals(table2.get(i))){//CEHCK TO SEE IF WE CAN USE .equals @@@
                return false;
            }
        }
        return true;
    }
    //adds a new row to the table using hte Row class
    private class ColDescriptionWithPos{
        int position;
        ColumnDescription columnDescription;
        protected ColDescriptionWithPos(int pos, ColumnDescription cd){
            position = pos;
            columnDescription = cd;
        }

        protected ColumnDescription getColumnDescription() {
            return columnDescription;
        }

        protected int getPosition() {
            return position;
        }
    }

}

