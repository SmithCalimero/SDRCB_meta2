package pt.isec.pd.server.data;

import pt.isec.pd.server.data.database.CreateDataBase;
import pt.isec.pd.server.data.database.DBHandler;
import pt.isec.pd.server.threads.client.ClientManagement;
import pt.isec.pd.server.threads.client.ClientReceiveMessage;
import pt.isec.pd.utils.Log;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class Server {
    private final Log LOG = Log.getLogger(Server.class);
    private HeartBeatList hbList;
    private String ip;
    private ClientManagement cm;
    private final String dbPath;
    private HeartBeatController hbController;
    private DBHandler dbHandler;
    private Scanf scanf;

    public Server(int pingPort,String dbPath) throws RemoteException, MalformedURLException {
        this.dbPath = dbPath;
        scanf = new Scanf();
        scanf.start();

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Device ip: " + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        init(pingPort);
        start();
    }

    public String getIp() {
        return ip;
    }

    public void init(int pingPort) throws RemoteException, MalformedURLException {
        hbList = new HeartBeatList();

        File f = new File(dbPath);
        if(!f.exists() && !f.isDirectory()) {
            LOG.log("Data base create: " + dbPath);
            new CreateDataBase(dbPath);
        }

        dbHandler = new DBHandler(dbPath);

        hbController = new HeartBeatController(hbList,this);
        cm = new ClientManagement(pingPort, dbHandler,hbList, hbController,ip);

        // Register with service so that clients can find us
        LocateRegistry.createRegistry(pingPort);
        String registry = ip + ":" + pingPort;
        String registration = "rmi://" + registry + "/SHOW_SERVICE_" + pingPort;
        Naming.rebind(registration, cm);
    }

    public void start() {
        hbController.start();

        cm.startPingHandler();
        cm.run();
    }

    public int getServerPort() {
        return cm.getServerPort();
    }

    public int getPortUdp() { return cm.getPortUdp(); }

    public int getDBVersion() {
        return dbHandler.getCurrentVersion();
    }

    public synchronized int getActiveConnections() {
        return cm.getNumConnections();
    }

    public synchronized DBHandler getDbHandler() {
        return dbHandler;
    }

    public synchronized List<ClientReceiveMessage> getClients() {
        return cm.getClientsThread();
    }
}
