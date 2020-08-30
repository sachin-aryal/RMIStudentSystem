package com.information.rmiserver;

import java.sql.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RMIImplement extends UnicastRemoteObject implements RMIInterface {
    String DB_NAME = "sis";
    String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME;
    String DB_USER = "root";
    String DB_PASS = "root";
    Connection con = null;

    public RMIImplement() throws Exception {
        super();
        Class.forName("com.mysql.jdbc.Driver");
        this.con=DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
    }

    public Statement getStatement() throws SQLException {
        return this.con.createStatement();
    }

    public Map insert(Map studentMap) throws Exception {
        Map result = createUser((String)studentMap.get("username"), (String)studentMap.get("password"), "student");
        Statement stmt = getStatement();
        stmt.executeUpdate("insert into students values(null ,'"+studentMap.get("name")+"',"+studentMap.get("tpNumber")+"," +
                "'"+studentMap.get("dob")+"', '"+studentMap.get("course")+"', "+result.get("userId")+")", Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            studentMap.put("studentId", rs.getLong(1));
        }
        studentMap.put("userId", result.get("userId"));
        return studentMap;
    }

    public Map login(String username, String password) throws Exception {
        String sql = "SELECT * from users where username = '"+username+"' and password = '"+password+"'";
        Map result = new HashMap();
        ResultSet rs = getStatement().executeQuery(sql);
        while (rs.next()) {
            result.put("username", username);
            result.put("userId", rs.getString("id"));
            result.put("role", rs.getString("role"));
        }
        return result;
    }

    public Map createUser(String username, String password, String role) throws Exception {
        Map result = new HashMap();
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate("insert into users values(null , '"+username+"','"+password+"','"+role+"')", Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            result.put("userId", rs.getLong(1));
            result.put("username", username);
            result.put("role", role);
        }
        return result;
    }

    @Override
    public List<Map> studentList() throws Exception {
        List studentList = new ArrayList();
        String sql = "SELECT * from students";
        ResultSet rs = getStatement().executeQuery(sql);
        while (rs.next()) {
            Map result = new HashMap();
            result.put("studentId", rs.getString("id"));
            result.put("name", rs.getString("name"));
            result.put("tpNumber", rs.getInt("tpNumber"));
            result.put("dob", rs.getDate("dob"));
            result.put("course", rs.getString("course"));
            result.put("userId", rs.getInt("user_id"));
            studentList.add(result);
        }
        return studentList;
    }
}