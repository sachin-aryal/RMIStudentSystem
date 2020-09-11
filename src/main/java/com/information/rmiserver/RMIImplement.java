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
        if(((String) result.get("role")).equals("student")){
            sql = "SELECT * FROM students WHERE user_id="+result.get("userId");
            rs = getStatement().executeQuery(sql);
            while (rs.next()) {
                result.put("studentId", rs.getInt("id"));
                result.put("name", rs.getString("name"));
                result.put("tpNumber", rs.getString("tpNumber"));
                result.put("dob", rs.getString("dob"));
                result.put("course", rs.getString("course"));
            }
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

    @Override
    public List<Map> courseDetails(String course) throws Exception {
        List courseDetailList = new ArrayList();
        String sql = "";
        if(course.equals("admin")){
            sql = "SELECT * from course_details";
        }else{
            sql = "SELECT * from course_details WHERE course = '"+course+"'";
        }
        sql += " ORDER BY semester";
        ResultSet rs = getStatement().executeQuery(sql);
        while (rs.next()) {
            Map result = new HashMap();
            result.put("course", rs.getString("course"));
            result.put("subject", rs.getString("subject"));
            result.put("semester", rs.getInt("semester"));
            courseDetailList.add(result);
        }
        return courseDetailList;
    }

    @Override
    public List<Map> busSchedule() throws Exception {
        List busScheduleList = new ArrayList();
        String sql = "SELECT * FROM sis.bus_schedule;";
        ResultSet rs = getStatement().executeQuery(sql);
        while (rs.next()) {
            Map result = new HashMap();
            result.put("from", rs.getString("from"));
            result.put("to", rs.getString("to"));
            result.put("time", rs.getString("time"));
            busScheduleList.add(result);
        }
        return busScheduleList;
    }

    @Override
    public List<Map> feeDetails(int studentId) throws Exception {
        List feeDetailList = new ArrayList();
        String sql = "SELECT * FROM sis.fee WHERE student_id = "+studentId+"";
        ResultSet rs = getStatement().executeQuery(sql);
        while (rs.next()) {
            Map result = new HashMap();
            result.put("fee_amount", rs.getString("fee_amount"));
            result.put("due_date", rs.getString("due_date"));
            feeDetailList.add(result);
        }
        return feeDetailList;
    }

    @Override
    public List<Map> holidays() throws Exception {
        List holidayList = new ArrayList();
        String sql = "SELECT * FROM sis.holidays WHERE holiday_date >= CURDATE() ORDER BY holiday_date ASC";
        ResultSet rs = getStatement().executeQuery(sql);
        while (rs.next()) {
            Map result = new HashMap();
            result.put("title", rs.getString("title"));
            result.put("holiday_date", rs.getString("holiday_date"));
            holidayList.add(result);
        }
        return holidayList;
    }

    @Override
    public boolean addHoliday(String title, String holiday_date) throws Exception {
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate("insert into holidays values(null , '"+title+"','"+holiday_date+"')", Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean addFee(int studentId, int amount, String due_date) throws Exception {
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate("insert into fee values(null , "+studentId+","+amount+", '"+due_date+"')", Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            return true;
        }
        return false;
    }
}