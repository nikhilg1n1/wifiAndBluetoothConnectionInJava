    package org.example;

    import javax.bluetooth.*;

    import java.util.ArrayList;
    import java.util.List;

    public class MyDiscoverAgent implements DiscoveryListener {
        private final Object inquiryCompleted;
        List<String> devices = new ArrayList<>();
        List<RemoteDevice> remoteDevices = new ArrayList<>();

        public List<String> getDevices(){
            return devices;
        }

        public List<RemoteDevice> getRemoteDevices(){
            return  remoteDevices;
        }

        public MyDiscoverAgent(Object inquiryEventCompleted) {
            this.inquiryCompleted= inquiryEventCompleted;
        }

        @Override
        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
            try {
                String friendlyName = remoteDevice.getFriendlyName(true);
                devices.add(friendlyName);
                remoteDevices.add(remoteDevice);
                System.out.println("Founded Devices: " + remoteDevice.getFriendlyName(true) + "("+ remoteDevice.getBluetoothAddress() +")" );
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

        }
        @Override
        public void serviceSearchCompleted(int i, int i1) {

        }
        @Override
        public void inquiryCompleted(int i) {
            synchronized (inquiryCompleted){
                inquiryCompleted.notify();
            }

        }
    }
