package DS.SemesterProject.DB;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

import java.util.HashMap;
import java.util.Map;
public class Executer {
    private SQLParser parser;
    private SQLQuery sqlQuery;
    private Map<String, Table> tableMap;//used to hold all the Tables in the dataBase
    //Constructs an Executer that will perform the given SQL command
    public Executer() {
        parser = new SQLParser();
        tableMap = new HashMap<String, Table>();
    }
    protected SQLParser getParser() {
        return parser;
    }
    protected SQLQuery getQuery(){
        return sqlQuery;
    }
    //USED FOR TESTING
    //returns a table contained in the Executer tableMap bases on name
    protected Table getTable(String name){
        return tableMap.get(name);
    }
    //creates the SQLQuery and calls the appropriate method from the table class based on the Query Type
    protected ResultSet execute(String SQL){
        try {
            SQLQuery sqlQuery = parser.parse(SQL);
            Table table = null;
            //for CreateTable, create a table then add it to the hashMap for future reference
            if(sqlQuery instanceof CreateTableQuery){
                table = new Table((CreateTableQuery) sqlQuery);
                return makeTable(table, sqlQuery);
            } else {
                String tableName = getTableName(sqlQuery);
                table = tableMap.get(tableName);
                if(table == null){
                    System.out.println("That table does not exist");
                    return new ResultSet(false, new IllegalArgumentException("That table does not exist"));
                }

                if      (sqlQuery instanceof UpdateQuery)      { return table.update((UpdateQuery)(sqlQuery));}
                else if (sqlQuery instanceof DeleteQuery)      { return table.delete((DeleteQuery)(sqlQuery));}
                else if (sqlQuery instanceof InsertQuery)      { return table.insert((InsertQuery) (sqlQuery));}
                else if (sqlQuery instanceof SelectQuery)      { return table.select((SelectQuery) (sqlQuery));}
                else if (sqlQuery instanceof CreateIndexQuery) { return table.createIndex((CreateIndexQuery)(sqlQuery));}

            }
        } catch(Exception e){//if the Query did not work
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new ResultSet(false, e);
        }
        return null; //to make compiler Happy
    }
    //retrieves the name of the Table the query is refering to
    private String getTableName(SQLQuery sqlQuery) {
        if      (sqlQuery instanceof UpdateQuery)      { return ((UpdateQuery)(sqlQuery)).getTableName();}
        else if (sqlQuery instanceof DeleteQuery)      { return ((DeleteQuery)(sqlQuery)).getTableName();}
        else if (sqlQuery instanceof InsertQuery)      { return ((InsertQuery) (sqlQuery)).getTableName();}
        else if (sqlQuery instanceof SelectQuery)      { return ((SelectQuery) (sqlQuery)).getFromTableNames()[0];}
        else if (sqlQuery instanceof CreateIndexQuery) { return ((CreateIndexQuery)(sqlQuery)).getTableName();}
        //for compiler
        return null;
    }
    //constructs a new table and adds it to the tableMap
    private ResultSet makeTable(Table table, SQLQuery sqlQuery) {
        tableMap.put(table.getTableName(), table);
        return new ResultSet(table);
    }


    /*
       ******IGNORE THIS CODE. FOR TESTING PURPOSES ONLY*****

    public static void main(String[] args) throws Exception{
        Executer ex = new Executer();

        String createTableString = "CREATE TABLE Student ("+
                "BannerID int," +
                "SSNum int UNIQUE," +
                "FirstName varchar(255)," +
                "LastName varchar(255) NOT NULL," +
                "GPA decimal(1,2) DEFAULT 0.00," +
                "Class varchar(255), " +
                "CurrentStudent boolean DEFAULT true," +
                "PRIMARY KEY (BannerID))";
        TEST CREATE TABLE PARSER
        SQLParser parser1 = new SQLParser();
        CreateTableQuery tableQuery = (CreateTableQuery)parser1.parse(createTableString);
        for(ColumnDescription cd: tableQuery.getColumnDescriptions()){
            System.out.print(cd.getColumnName().toString()+" ");
        }
        System.out.println(tableQuery.getPrimaryKeyColumn().isUnique());

        ResultSet set =  ex.execute(createTableString);
        set = ex.execute("INSERT INTO Student (FirstName, LastName, GPA, Class, BannerID) VALUES ('Ploni','Almoni',5.0, 'Senior',800012345)");
        //set.print();
        set = ex.execute("INSERT INTO Student (FirstName, LastName, GPA, Class, BannerID) VALUES ('Plhoni','Almoni',4.0, 'Senior',801019345)");
        //set.print();
        set = ex.execute("INSERT INTO Student (FirstName, GPA, LastName, Class, BannerID) VALUES ('first3',1.2, 'last3', 'Junior',800012341)");
        set = ex.execute("INSERT INTO Student (FirstName, LastName, GPA, Class, BannerID) VALUES ('first4','Last4',1.0, 'Freshman',800012342)");
        set = ex.execute("INSERT INTO Student (FirstName, LastName, GPA, Class, BannerID) VALUES ('Mike','Tyson',1.0, 'Senior',800012343)");
        //set = ex.execute(" DELETE '*' FROM Student");
        // SelectQuery query = (SelectQuery)ex.getParser().parse("SELECT * FROM Student");
        //System.out.println(query.getSelectedColumnNames()[0].toString());
        //set = ex.execute("SELECT GPA FROM Student WHERE FirstName = 'Mike' OR GPA < 5");
        //set = ex.execute("DELETE * FROM Student");
        set.print();
        //ex.upda
        // ex.execute("SELEC")
        //set = ex.execute("INSERT INTO STUDENT (FristName, LastName, GPA, Class, BannerID) VALUES ('Ploni','Almoni',4.0,'Senior', 800012345)");
        // Table table = set.getTable();

        // */
        //table.string();

       /* select = (SelectQuery)(select);
        for(ColumnID id: ((SelectQuery) select).getSelectedColumnNames()) {//ColumnID[]
            System.out.print(id.toString() +"  ");
        }
        System.out.println("");
        for(SelectQuery.FunctionInstance fi: ((SelectQuery) select).getFunctions()) {
            System.out.println(fi.column.toString() + "  ");
            System.out.print(fi.function.toString());
        }
        System.out.println("");
        System.out.print(select.getQueryString());
    }*/
}