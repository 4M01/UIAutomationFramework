package space.amolchavan.dbHelper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Reporter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTemplateBaseClient implements IDBClient{

    protected JdbcTemplate jdbcTemplate;
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public Connection connectDB() {
        Connection connection = null;
        try{
            connection = dataSource.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public String executeScalarQueryGetString(String sql) {
        Reporter.log("SQL query: "+ sql, true);
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    @Override
    public List<Map<String, Object>> executeTableQuery(String sql) {
        return null;
    }

    @Override
    public List<String> executeScalarQueryGetList(String sql) {
        Reporter.log("SQL query: "+ sql, true);
        return jdbcTemplate.queryForList(sql,String.class);
    }

    @Override
    public int executeScalarQueryGetInt(String sql) {
        Reporter.log("SQL query: "+ sql, true);
        return jdbcTemplate. queryForObject(sql, Integer.class);
    }

    public boolean executeUpdateQuery(String sql) {
        Reporter.log("SQL query: "+ sql, true);
        return jdbcTemplate.batchUpdate(sql).length>0;
    }

    public Boolean executeScalarQueryGetBoolean(String sql) {
        Reporter.log("SQL query: "+ sql, true);
        return jdbcTemplate. queryForObject(sql, Boolean.class);
    }

    @Override
    public String getStringFromList(List<String> plcNames) {
        return null;
    }

    @Override
    public int select(Connection con, String query) {
        return 0;
    }

    @Override
    public int getRowCount(Connection con, String tableName) {
        return 0;
    }

    @Override
    public ArrayList<String> selectItems(Connection con, String query) {
        return null;
    }

    @Override
    public int selectUpdate(Connection con, String query) {
        return 0;
    }

    @Override
    public boolean create(Connection con, String query) {
        return false;
    }

    @Override
    public void closeDB(Connection con) {

    }

    @Override
    public boolean drop(Connection con, String string) {
        return false;
    }

    @Override
    public int compare(Connection con, String quer) {
        return 0;
    }

    @Override
    public Map<String, String> executeLinearQuery(String query) {
        return null;
    }

    @Override
    public boolean batchUpdate(List<String> sql) throws Exception {
        return false;
    }
}
