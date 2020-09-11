package com.information.rmiserver;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

public interface RMIInterface extends Remote
{
    public abstract Map insert(Map student) throws Exception;

    public abstract Map login(String username, String password) throws Exception;

    public abstract Map createUser(String username, String password, String role) throws Exception;

    public abstract List<Map> studentList() throws Exception;

    public abstract List<Map> courseDetails(String course) throws Exception;

    public abstract List<Map> busSchedule() throws Exception;

    public abstract List<Map> feeDetails(int studentId) throws Exception;

    public abstract List<Map> holidays() throws Exception;

    public abstract boolean addHoliday(String title, String holiday_date) throws Exception;

    public abstract boolean addFee(int studentId, int amount, String due_date) throws Exception;

}