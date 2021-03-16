package com.neo.parkguidance.core;

import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

public class Dataloader {

    public static void main(String[] args) {
            String url = "jdbc:postgresql://localhost:5432/parkguidance";
            String user = "DBACC";
            String password = "DBACC";

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                addAdminUser(con);
                addNormalUser(con);
                addParkingGarage(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    public static void addAdminUser(Connection con) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate(
                    "INSERT INTO " + Permission.TABLE_NAME
                            + " ("+Permission.C_NAME+") "
                            + "VALUES (\'superUser\');");
            st.executeUpdate(
                    "INSERT INTO "+RegisteredUser.TABLE_NAME
                            + " ("+RegisteredUser.C_USERNAME +", "+RegisteredUser.C_PASSWORD +") "
                            + "VALUES (\'admin\', \'" + new DigestUtils(SHA_224).digestAsHex("admin") + "\');");
            st.executeUpdate("INSERT INTO registereduser_permissions (registereduser_id,permissions_id) VALUES (1,1)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void addNormalUser(Connection con) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate(
                    "INSERT INTO "+RegisteredUser.TABLE_NAME
                            + " ("+RegisteredUser.C_USERNAME +", "+RegisteredUser.C_PASSWORD +") "
                            + "VALUES (\'normal\', \'" + new DigestUtils(SHA_224).digestAsHex("normal") + "\');");
        }catch (SQLException ex) {

        }
    }

    public static void addParkingGarage(Connection con) {
            try {
            Statement st = con.createStatement();
            st.executeUpdate(
                    "INSERT INTO address(city_name, street, number, plz) VALUES (\'Niederlenz\',\'Alter Schützenweg\',17,5702);");

            st.executeUpdate(
                    "INSERT INTO parkinggarage (accesskey ,name ,spaces ,address) VALUES (\'abc\',\'Home\',255,1);");
                st.executeUpdate(
                        "INSERT INTO parkinggarage (accesskey ,name ,spaces ,address) VALUES (\'cba\',\'BBB\',255,1);");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
