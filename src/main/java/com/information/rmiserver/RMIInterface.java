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

}