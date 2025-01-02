package org.example;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.util.List;
import java.util.Scanner;

public class BluetoothConnection {

    private static final Object inquiryEventCompleted = new Object();

    public static void main(String[] args) {
        try {
            System.out.println("Starting searching for the bluetooth device");
            MyDiscoverAgent discoverAgent = new MyDiscoverAgent(inquiryEventCompleted);
            DiscoveryAgent agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.GIAC,discoverAgent);

            synchronized (inquiryEventCompleted){
                inquiryEventCompleted.wait();
            }
            List<String>allDevice= discoverAgent.getDevices();
            if(allDevice.isEmpty()){
                System.out.println("No Device Found");
                return;
            }
            System.out.println("Discovered Devices");
            for (int i = 0; i < allDevice.size(); i++) {
                    System.out.println((i+1)+ "." + allDevice.get(i) );
                }

            System.out.println("Enter the number of Device that you want to connect ");
            Scanner input = new Scanner(System.in);
            int choice= input.nextInt()-1;

            if(choice < 0 || choice> allDevice.size()){
                System.out.println("Invalid choice");
                return;
            }

            RemoteDevice selectDevice = discoverAgent.getRemoteDevices().get(choice);

            System.out.println("Connecting to " +selectDevice.getBluetoothAddress());
            String url = "btspp://" + selectDevice.getBluetoothAddress() + ":1"; // Replace ":1" with the appropriate channel number if needed
            StreamConnection connection = (StreamConnection) Connector.open(url);

            System.out.println("Connected to Device");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

