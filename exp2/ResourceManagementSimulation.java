import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

import java.util.*;

public class ResourceManagementSimulation {

    public static void main(String[] args) {

        try {
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();

            CloudSim.init(numUsers, calendar, false);

            createDatacenter("Datacenter_0");

            DatacenterBroker broker =
                    new DatacenterBroker("Broker");

            List<Vm> vmList =
                    createVMs(broker.getId());

            List<Cloudlet> cloudletList =
                    createCloudlets(broker.getId(), vmList);

            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            CloudSim.startSimulation();

            CloudSim.stopSimulation();

            List<Cloudlet> results =
                    broker.getCloudletReceivedList();

            printResults(results);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Datacenter createDatacenter(String name)
            throws Exception {

        List<Host> hostList =
                new ArrayList<Host>();

        for (int i = 0; i < 5; i++) {

            List<Pe> peList =
                    new ArrayList<Pe>();

            peList.add(
                    new Pe(
                            0,
                            new PeProvisionerSimple(1000)
                    )
            );

            Host host =
                    new Host(
                            i,
                            new RamProvisionerSimple(4096),
                            new BwProvisionerSimple(10000),
                            1000000,
                            peList,
                            new VmSchedulerTimeShared(peList)
                    );

            hostList.add(host);
        }

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics(
                        "x86",
                        "Linux",
                        "Xen",
                        hostList,
                        10.0,
                        3.0,
                        0.05,
                        0.001,
                        0.0
                );

        return new Datacenter(
                name,
                characteristics,
                new VmAllocationPolicySimple(hostList),
                new ArrayList<Storage>(),
                0
        );
    }

    private static List<Vm> createVMs(int brokerId) {

        List<Vm> vmList =
                new ArrayList<Vm>();

        for (int i = 0; i < 10; i++) {

            Vm vm =
                    new Vm(
                            i,
                            brokerId,
                            1000,
                            1,
                            512,
                            1000,
                            10000,
                            "Xen",
                            new CloudletSchedulerTimeShared()
                    );

            vmList.add(vm);
        }

        return vmList;
    }

    private static List<Cloudlet> createCloudlets(
            int brokerId,
            List<Vm> vmList) {

        List<Cloudlet> cloudletList =
                new ArrayList<Cloudlet>();

        for (int i = 0; i < 20; i++) {

            UtilizationModel utilizationModel =
                    new UtilizationModelFull();

            Cloudlet cloudlet =
                    new Cloudlet(
                            i,
                            100000,
                            1,
                            300,
                            300,
                            utilizationModel,
                            utilizationModel,
                            utilizationModel
                    );

            cloudlet.setUserId(brokerId);

            cloudlet.setVmId(
                    vmList.get(i % vmList.size()).getId()
            );

            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }

    private static void printResults(
            List<Cloudlet> cloudlets) {

        System.out.println();
        System.out.println(
                "=============================================="
        );
        System.out.println(
                "       CLOUDSIM RESOURCE MANAGEMENT"
        );
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Number of Cloudlets: "
                        + cloudlets.size()
        );

        System.out.println();

        for (Cloudlet cloudlet : cloudlets) {

            System.out.println(
                    "Cloudlet ID : "
                            + cloudlet.getCloudletId()
            );

            System.out.println(
                    "VM ID       : "
                            + cloudlet.getVmId()
            );

            System.out.println(
                    "Status      : "
                            + getStatus(cloudlet.getStatus())
            );

            System.out.println(
                    "Start Time  : "
                            + cloudlet.getExecStartTime()
            );

            System.out.println(
                    "Finish Time : "
                            + cloudlet.getFinishTime()
            );

            System.out.println(
                    "CPU Time    : "
                            + cloudlet.getActualCPUTime()
            );

            System.out.println(
                    "----------------------------------------------"
            );
        }
    }

    private static String getStatus(int status) {

        if (status == Cloudlet.SUCCESS) {
            return "SUCCESS";
        }

        if (status == Cloudlet.FAILED) {
            return "FAILED";
        }

        if (status == Cloudlet.CANCELED) {
            return "CANCELED";
        }

        return "UNKNOWN";
    }
}