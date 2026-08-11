import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomSchedulingSimulation {

    public static void main(String[] args) {

        try {

            int numUsers = 1;

            Calendar calendar = Calendar.getInstance();

            CloudSim.init(
                    numUsers,
                    calendar,
                    false
            );

            createDatacenter("Datacenter_0");

            DatacenterBroker broker =
                    new DatacenterBroker("Broker");

            List<Vm> vmList =
                    createVMs(broker.getId());

            List<Cloudlet> cloudletList =
                    createCloudlets(broker.getId());

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

    private static Datacenter createDatacenter(
            String name) throws Exception {

        List<Host> hostList =
                new ArrayList<Host>();

        for (int i = 0; i < 3; i++) {

            List<Pe> peList =
                    new ArrayList<Pe>();

            Pe pe =
                    new Pe(
                            0,
                            new PeProvisionerSimple(1000)
                    );

            peList.add(pe);

            Host host =
                    new Host(
                            i,
                            new RamProvisionerSimple(2048),
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
                new CustomVmAllocationPolicy(hostList),
                new ArrayList<Storage>(),
                0
        );
    }

    private static List<Vm> createVMs(
            int brokerId) {

        List<Vm> vmList =
                new ArrayList<Vm>();

        for (int i = 0; i < 5; i++) {

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
            int brokerId) {

        List<Cloudlet> cloudletList =
                new ArrayList<Cloudlet>();

        for (int i = 0; i < 10; i++) {

            UtilizationModel model =
                    new UtilizationModelFull();

            Cloudlet cloudlet =
                    new Cloudlet(
                            i,
                            100000,
                            1,
                            300,
                            300,
                            model,
                            model,
                            model
                    );

            cloudlet.setUserId(brokerId);

            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }

    private static void printResults(
            List<Cloudlet> cloudlets) {

        System.out.println();
        System.out.println(
                "======================================"
        );
        System.out.println(
                "   CLOUDSIM CUSTOM SCHEDULING RESULT"
        );
        System.out.println(
                "======================================"
        );

        System.out.println(
                "Total Cloudlets: "
                        + cloudlets.size()
        );

        System.out.println();

        for (Cloudlet cloudlet : cloudlets) {

            System.out.println(
                    "Cloudlet ID    : "
                            + cloudlet.getCloudletId()
            );

            System.out.println(
                    "VM ID          : "
                            + cloudlet.getVmId()
            );

            System.out.println(
                    "Status         : "
                            + getStatus(
                                    cloudlet.getStatus()
                            )
            );

            System.out.println(
                    "Start Time     : "
                            + cloudlet.getExecStartTime()
            );

            System.out.println(
                    "Finish Time    : "
                            + cloudlet.getFinishTime()
            );

            System.out.println(
                    "Execution Time : "
                            + cloudlet.getActualCPUTime()
            );

            System.out.println(
                    "--------------------------------------"
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

    public static class CustomVmAllocationPolicy
            extends VmAllocationPolicy {

        public CustomVmAllocationPolicy(
                List<? extends Host> hostList) {

            super(hostList);
        }

        @Override
        public boolean allocateHostForVm(Vm vm) {

            Host selectedHost = null;

            int maximumRam = -1;

            for (Host host : getHostList()) {

                int availableRam =
                        host.getRamProvisioner()
                                .getAvailableRam();

                if (
                        availableRam >= vm.getRam()
                                &&
                        availableRam > maximumRam
                ) {

                    selectedHost = host;
                    maximumRam = availableRam;
                }
            }

            if (selectedHost != null) {

                if (selectedHost.vmCreate(vm)) {

                    System.out.println(
                            "Custom Scheduler: VM "
                                    + vm.getId()
                                    + " -> Host "
                                    + selectedHost.getId()
                    );

                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean allocateHostForVm(
                Vm vm,
                Host host) {

            if (host.vmCreate(vm)) {
                return true;
            }

            return false;
        }

        @Override
        public void deallocateHostForVm(Vm vm) {

            Host host = getHost(vm);

            if (host != null) {
                host.vmDestroy(vm);
            }
        }

        @Override
        public Host getHost(Vm vm) {

            for (Host host : getHostList()) {

                if (host.getVmList().contains(vm)) {
                    return host;
                }
            }

            return null;
        }

        @Override
        public Host getHost(
                int vmId,
                int userId) {

            for (Host host : getHostList()) {

                for (Vm vm : host.getVmList()) {

                    if (
                            vm.getId() == vmId
                                    &&
                            vm.getUserId() == userId
                    ) {

                        return host;
                    }
                }
            }

            return null;
        }

        @Override
        public List<java.util.Map<String, Object>>
        optimizeAllocation(
                List<? extends Vm> vmList) {

            return null;
        }
    }
}