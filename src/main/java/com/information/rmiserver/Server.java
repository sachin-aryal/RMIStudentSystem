package com.information.rmiserver;

import java.rmi.*;
public class Server
{
    public static void main(String args[]) throws Exception
    {
        RMIInterface obj=new RMIImplement();
        Naming.rebind("sis",obj);
        System.out.println("Server Started");
    }
}