package com.information.rmiclient;

import com.information.rmiserver.RMIInterface;

import java.rmi.Naming;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Helper {
    public static void createUser() throws Exception {
        try{
            Scanner scan= new Scanner(System.in).useDelimiter("\n");
            System.out.println("Enter Username");
            String username=scan.next();
            System.out.println("Enter Password");
            String password=scan.next();
            String role = "";
            role = "admin";
            RMIInterface obj=(RMIInterface) Naming.lookup("sis");
            Map result = obj.createUser(username, password, role);
            if(result.size() > 1){
                System.out.println("User created successfully.");
                System.out.println(result);
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
    }

    public static void showStudents() {
        try{
            System.out.println("--------------------------------------------Student List--------------------------------------------\n");
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            List<Map> result = obj.studentList();
            System.out.format("%10s%24s%20s%20s%20s\n", "Id", "Name", "Tp Number", "DOB", "Course");
            for(Map row: result){
                System.out.format("%10s%24s%20s%20s%20s\n", row.get("studentId"), row.get("name"), row.get("tpNumber"),
                        row.get("dob"), row.get("course"));
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
        System.out.println();
    }

    public static void courseDetails(String course){
        try{
            System.out.println("-----------------------Course Details-----------------------\n");
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            List<Map> result = obj.courseDetails(course);
            System.out.format("%10s%24s%10s\n", "Course", "Subject", "Semester");
            for(Map row: result){
                System.out.format("%10s%24s%10d\n", row.get("course"), row.get("subject"), row.get("semester"));
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
        System.out.println();
    }

    public static void busSchedule(){
        try{
            System.out.println("-----------------------Bus Schedule-----------------------\n");
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            List<Map> result = obj.busSchedule();
            System.out.format("%15s%15s%15s\n", "From", "To", "Time");
            for(Map row: result){
                System.out.format("%15s%15s%15s\n", row.get("from"), row.get("to"), row.get("time"));
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
        System.out.println();
    }

    public static void feeDetails(int userId){
        try{
            System.out.println("-----------------------Fee Details-----------------------\n");
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            List<Map> result = obj.feeDetails(userId);
            System.out.format("%15s%15s\n", "Fee Amount", "Due Date");
            for(Map row: result){
                System.out.format("%15s%15s\n", row.get("fee_amount"), row.get("due_date"));
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
        System.out.println();
    }

    public static void holidays(){
        try{
            System.out.println("------------Upcoming Holidays------------\n");
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            List<Map> result = obj.holidays();
            System.out.format("%15s%15s\n", "Occasion", "Holiday Date");
            for(Map row: result){
                System.out.format("%15s%15s\n", row.get("title"), row.get("holiday_date"));
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
        System.out.println();
    }

    public static void myInfo(Map result){
        System.out.println("-----------------Profile-----------------\n");
        System.out.format("%15s|%15s\n", "Name", result.get("name"));
        System.out.format("%15s|%15s\n", "Tp Number", result.get("tpNumber"));
        System.out.format("%15s|%15s\n", "Username", result.get("username"));
        System.out.format("%15s|%15s\n", "Course", result.get("course"));
        System.out.format("%15s|%15s\n", "Date of Birth", result.get("dob"));
        System.out.println();
    }

    public static void addHoliday() throws Exception {
        try{
            Scanner scan= new Scanner(System.in).useDelimiter("\n");
            System.out.println("Enter Occasion");
            String title=scan.next();
            System.out.println("Enter Holiday Date (1994-01-02 - This format)");
            String holiday_date=scan.next();
            RMIInterface obj=(RMIInterface) Naming.lookup("sis");
            boolean result = obj.addHoliday(title, holiday_date);
            if(result){
                System.out.println("Holiday Added Successfully.");
            }else{
                System.out.println("Holiday Not Added.");
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
    }

    public static void addFee() throws Exception {
        try{
            Scanner scan= new Scanner(System.in).useDelimiter("\n");
            System.out.println("Enter StudentId");
            int studentId=scan.nextInt();
            System.out.println("Enter Amount");
            int amount=scan.nextInt();
            System.out.println("Enter Due Date (2020-01-02 - This format)");
            String dueDate=scan.next();
            RMIInterface obj=(RMIInterface) Naming.lookup("sis");
            boolean result = obj.addFee(studentId, amount, dueDate);
            if(result){
                System.out.println("Fee Added Successfully.");
            }else{
                System.out.println("Fee Not Added.");
            }
        }catch (Exception ex){
            if(ex.getMessage().contains("Cannot add or update a child row: a foreign key constraint fails")){
                System.out.println("*********ERROR: Please enter the valid student id.");
            }else{
                System.out.println("*********ERROR: "+ex.getMessage());
            }
        }
    }

}
