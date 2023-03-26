package space.amolchavan.dbHelper;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IDBClient {

    JdbcTemplate getJdbcTemplate();

    Connection connectDB();


    String executeScalarQueryGetString(String sql);

    List<Map<String, Object>> executeTableQuery(String sql);

    List<String> executeScalarQueryGetList(String sql);

    int executeScalarQueryGetInt(String sql);

    String getStringFromList(List<String> plcNames);

    int select(Connection con,String query);

    int getRowCount(Connection con,String tableName);

    ArrayList<String> selectItems(Connection con, String query);

    int selectUpdate(Connection con,String query);

    boolean create(Connection con,String query);

    void closeDB(Connection con);

    boolean drop(Connection con, String string);

    int compare(Connection con, String quer);

    Map<String, String> executeLinearQuery(String query);

    boolean batchUpdate(final List<String> sql) throws Exception;
}
