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
        Scanner scan= new Scanner(System.in).useDelimiter("\n");
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
        Scanner scan= new Scanner(System.in).useDelimiter("\n");
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
            System.out.println("Enter dob (1994-01-02 - This format)");
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
            Scanner scan= new Scanner(System.in).useDelimiter("\n");
            System.out.println("Enter Username");
            String username=scan.next();
            System.out.println("Enter Password");
            String password=scan.next();
            System.out.println("----------------------------------------------------------------");
            RMIInterface obj=(RMIInterface)Naming.lookup("sis");
            Map result = obj.login(username, password);
            if(result.size() > 1){
                System.out.println("Logged In Successfully. Welcome "+result.get("username")+"\n");
                System.out.println("----------------------------------------------------------------");
                String role = (String) result.get("role");
                if(role.equals("student")){
                    this.studentScreen(result);
                }else if(role.equals("admin")){
                    this.adminScreen();
                }
            }else{
                System.out.println("Login Failed......");
                System.out.println("--------------------------------");
            }
        }catch (Exception ex){
            System.out.println("*********ERROR: "+ex.getMessage());
        }
    }

    public void adminScreen() throws Exception {
        Scanner scan= new Scanner(System.in).useDelimiter("\n");
        while (true){
            System.out.print("Enter\n1. Create User\n2. Student List\n3. Bus Schedule\n4. Course List\n5. Holidays\n" +
                    "6. Add Holiday\n7. Add Fee\n8. Exit\n");
            int choice = scan.nextInt();
            if(choice == 1){
                Helper.createUser();
            }else if (choice == 2){
                Helper.showStudents();
            }else if (choice == 3){
                Helper.busSchedule();
            }else if (choice == 4){
                Helper.courseDetails("admin");
            }else if (choice == 5){
                Helper.holidays();
            }else if (choice == 6){
                Helper.addHoliday();
            }else if (choice == 7){
                Helper.addFee();
            }else if (choice == 8){
                System.out.print("BYE.......");
                break;
            }
        }
    }

    public void studentScreen(Map result) throws Exception {
        Scanner scan= new Scanner(System.in).useDelimiter("\n");
        while (true){
            System.out.print("Enter\n1. Course Details\n2. Bus Schedule\n3. Upcoming Fee\n4. Holidays\n5. My Info\n6. Exit\n");
            int choice = scan.nextInt();
            if(choice == 1){
                Helper.courseDetails((String) result.get("course"));
            }else if (choice == 2){
                Helper.busSchedule();
            }else if (choice == 3){
                Helper.feeDetails(Integer.parseInt(String.valueOf(result.get("studentId"))));
            }else if (choice == 4){
                Helper.holidays();
            }else if (choice == 5){
                Helper.myInfo(result);
            }else if (choice == 6){
                System.out.print("BYE.......");
                break;
            }
        }
    }
}