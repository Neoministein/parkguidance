package com.neo.parkguidance.core.impl.dataloader;

import com.neo.parkguidance.core.impl.LoadProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class loads data defined from json files into the database specified in the environment file
 */
public class DataloaderTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataloaderTool.class);

    public static final String VALUE = "v";
    public static final String TYPE = "t";

    public static final String JAVAX_PERSISTENCE_JDBC_DRIVER = "javax.persistence.jdbc.driver";
    public static final String JAVAX_PERSISTENCE_JDBC_URL = "javax.persistence.jdbc.url";
    public static final String JAVAX_PERSISTENCE_JDBC_USER = "javax.persistence.jdbc.user";
    public static final String JAVAX_PERSISTENCE_JDBC_PASSWORD = "javax.persistence.jdbc.password";

    private final Properties envProperties;
    private final List<JSONObject> configurationList;

    public DataloaderTool(String environment, String configuration) {
        envProperties = loadEnvironmentProperties(environment);
        configurationList = loadConfiguration(configuration);
    }

    public void start() {
        Connection con = connect();
        for (JSONObject jsonObject: configurationList) {
            executeFile(con, jsonObject);
        }
    }

    protected void executeFile(Connection con, JSONObject jsonObject) {
        JSONArray data = jsonObject.getJSONArray("list");
        for (int i = 0; i < data.length(); i++) {
            executeListEntry(con, data.getJSONObject(i));
        }
    }

    protected void executeListEntry(Connection con, JSONObject listEntry) {
        List<DataloaderColumn> columnList = getColumns(listEntry.getJSONArray("column"));
        String preQuery = generateQuery(listEntry.getString("table"), columnList);

        JSONArray data = listEntry.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            try {
                String query = preQuery + parseDataEntry(columnList, data.getJSONArray(i));
                Statement st = con.createStatement();
                st.executeUpdate(query);
                LOGGER.info(query);
            } catch (Exception ex) {
                LOGGER.error("Error executing SQL statement",ex);
                throw new IllegalArgumentException(ex);
            }
        }
    }

    protected String parseDataEntry(List<DataloaderColumn> columnList, JSONArray dataEntry) {
        StringBuilder entry = new StringBuilder();
        for (int i = 0; i < dataEntry.length(); i++) {
            entry.append(parseValue(columnList.get(i), dataEntry.getJSONObject(i)));
            entry.append(",");
        }
        return entry.substring(0, entry.length()-1) + ");";
    }

    protected Object parseValue(DataloaderColumn column, JSONObject entry) {
        switch (column.getDataType().toLowerCase()) {
        case "number":
            return Integer.parseInt(entry.getString(VALUE));
        case "boolean":
            return Boolean.parseBoolean(entry.getString(VALUE));
        case "string":
        default:
            return "'" + entry.getString(VALUE) + "'";
        }
    }

    protected String generateQuery(String table, List<DataloaderColumn> dataloaderColumnList) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(table);
        query.append(" (");
        for (DataloaderColumn column: dataloaderColumnList) {
            query.append(column.getValue()).append(",");
        }
        return query.substring(0, query.length()-1) + ") VALUES (";
    }

    protected List<DataloaderColumn> getColumns(JSONArray columns) {
        List<DataloaderColumn> columnList = new ArrayList<>();
        for (int i = 0; i < columns.length(); i++) {
            JSONObject column = columns.getJSONObject(i);
            columnList.add(new DataloaderColumn(column.getString(VALUE), column.getString(TYPE)));
        }
        return columnList;
    }

    protected Connection connect() {
        String url = envProperties.getProperty(JAVAX_PERSISTENCE_JDBC_URL);
        String user = envProperties.getProperty(JAVAX_PERSISTENCE_JDBC_USER);
        String password = envProperties.getProperty(JAVAX_PERSISTENCE_JDBC_PASSWORD);

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    protected List<JSONObject> loadConfiguration(String configuration) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        JSONArray jsonArray = readJSONObject(configuration).getJSONArray("files");
        for (int i = 0;i < jsonArray.length();i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            jsonObjectList.add(readJSONObject(o.getString("location")));
        }
        return jsonObjectList;
    }

    protected Properties loadEnvironmentProperties(String environment) {
        try (InputStream input = LoadProperties.class.getClassLoader().getResourceAsStream("environment\\" + environment + ".properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (Exception e) {
            LOGGER.error("Unable to load environment properties, the application will most likely not be working correctly", e);
            throw new IllegalArgumentException();
        }
    }

    protected JSONObject readJSONObject(String location) {
        try (InputStream input = DataloaderTool.class.getClassLoader().getResourceAsStream(location)) {
            return new JSONObject(new JSONTokener(input));
        } catch (Exception e) {
            LOGGER.error("Unable to load file {}", location);
            throw new IllegalArgumentException();
        }
    }
}
