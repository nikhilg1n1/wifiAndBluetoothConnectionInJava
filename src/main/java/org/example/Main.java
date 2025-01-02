package org.example;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        try {
            List<String>allNetworks= listOfAllNetworks();

            if(allNetworks.isEmpty()){
                System.out.println("No Wifi Networks found");
                return;
            }
            System.out.println("Available Network");
            for (int i = 0; i < allNetworks.size() ; i++) {
                System.out.println((i+1) + "." +allNetworks.get(i));

            }

            Scanner input = new Scanner(System.in);
            System.out.println("Enter the number of network that you want to connect");
            int enterChoice= input.nextInt();
            input.nextLine();

            if(enterChoice < 1 || enterChoice > listOfAllNetworks().size()){
                System.out.println("Invalid choice");
                return;
            }
            String selectedNetwork = listOfAllNetworks().get(enterChoice - 1);

            System.out.println("Enter the Password for"+ selectedNetwork + ":");
            String password = input.nextLine();

            makeConnection(selectedNetwork, password);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public  static  List<String> listOfAllNetworks() throws  IOException{
        List<String> networks = new ArrayList<>();

        Process process = Runtime.getRuntime().exec("netsh wlan show networks");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;

        while ((line = bufferedReader.readLine())!= null){
            if(line.contains("SSID")){
            String network= line.split(":")[1].trim();
            networks.add(network);
            }
        }
        bufferedReader.close();
        return networks;


    }

    public static void makeConnection(String password, String ssid) throws  IOException{
        String command = String.format("netsh wlan connect name=\"%s\" key=\"%s\"",ssid,password);
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;
        boolean success = false;

        while((line = reader.readLine())!=null){
            System.out.println(line);
            if (line.contains("Connection request completed Successfully")){
                success=true;
            }
        }

        String errorline;
        while((errorline = error.readLine()) != null){
            System.out.println(errorline);

            if(errorline.contains("incorrect")){
                System.out.println("Password is incorrect");
                return;
            }
            else if(errorline.contains("network could not be found")){
                System.out.println("Network not found , please try other ssid");
                return;
            }

            if(success){
                System.out.println("Connected to the network "+ ssid);

            }
            else {
                System.out.println("could not connect to the network "+ ssid);
            }

        }
    }
 }


