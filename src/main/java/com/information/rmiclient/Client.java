package com.information.rmiclient;

import com.information.rmiserver.RMIInterface;

import java.rmi.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Client
{
    public static void main(String args[]) throws Exception
    {

        Client client = new Client();
        Scanner scan= new Scanner(System.in);
        while (true){
            System.out.print("Enter\n1. Login\n2. Student Register\n3. Exit\n");
            int choice = scan.nextInt();
            if(choice == 1){
                client.login();
            }else if (choice == 2){
                client.registerStudent();
            }else if (choice == 3){
                System.out.print("BYE.......");
                break;
            }
        }
    }

    public void registerStudent() throws Exception{
        Scanner scan= new Scanner(System.in).useDelimiter("\n");;
        System.out.println("Enter Username");
        String username=scan.next();
        System.out.println("Enter Password");
        String password=scan.next();
        System.out.println("Enter Name");
        String name=scan.next();
        System.out.println("Enter tpNumber");
        int tpNumber = scan.nextInt();
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String dobString = null;
        while (true){
            System.out.println("Enter dob (yyyy-MM-dd)");
            dobString = scan.next();
            try{
                DateFor.parse(dobString);
                break;
            }catch (Exception ex){
                System.out.println("Invalid Date Format...");
            }
        }
        System.out.println("Enter Course");
        String course = scan.next();

        Map student = new HashMap();
        student.put("username", username);
        student.put("password", password);
        student.put("name", name);
        student.put("tpNumber", tpNumber);
        student.put("dob", dobString);
        student.put("course", course);
        RMIInterface obj=(RMIInterface)Naming.lookup("sis");
        try {
            Map newStudent = obj.insert(student);
            System.out.println("Student Registered Successfully.");
            System.out.println(newStudent);
        }catch (Exception exception){
            System.out.println("*********ERROR occurred while registering user...."+exception.getMessage());
        }
    }

    public void login() throws Exception {
        try{
            Scanner scan= new Scanner(System.in);
            System.out.println("Enter Username");
            String username=scan.next();
            System.out.println("Enter Password");
            String password=scan.next();
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            Map result = obj.login(username, password);
            System.out.println(result);
            if(result.size() > 1){
                System.out.println("Logged In Successfully.");
                String role = (String) result.get("role");
                System.out.println("You are a: "+role);
                if(role.equals("student")){
                    System.out.println("You are a student...... Now show student screen.");
                }else if(role.equals("admin")){
                    System.out.println("Inside admin panel...");
                    this.adminScreen();
                }else if(role.equals("staff")){

                }
            }else{
                System.out.println("Login Failed......");
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
    }

    public void adminScreen() throws Exception {
        Scanner scan= new Scanner(System.in);
        while (true){
            System.out.print("Enter\n1. Create User\n2. Student List\n3. Exit\n");
            int choice = scan.nextInt();
            if(choice == 1){
                this.createUser();
            }else if (choice == 2){
                this.showStudents();
            }else if (choice == 3){
                System.out.print("BYE.......");
                break;
            }
        }
    }

    public void createUser() throws Exception {
        try{
            Scanner scan= new Scanner(System.in);
            List roleList = new ArrayList();
            roleList.add("admin");
            roleList.add("staff");
            System.out.println("Enter Username");
            String username=scan.next();
            System.out.println("Enter Password");
            String password=scan.next();
            String role = "";
            while (true){
                System.out.println("Enter Role");
                role=scan.next();
                if(roleList.contains(role)){
                    break;
                }else{
                    System.out.println("Role should be either admin or staff.");
                }
            }
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            Map result = obj.createUser(username, password, role);
            if(result.size() > 1){
                System.out.println("User created successfully.");
                System.out.println(result);
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
    }

    public void showStudents() {
        try{
            System.out.println("----------Student List----------");
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            List<Map> result = obj.studentList();
            for(Map row: result){
                System.out.println(row);
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
    }
}