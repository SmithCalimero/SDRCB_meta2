package pt.isec.pd.server.data;

import pt.isec.pd.client.model.data.ClientAction;
import pt.isec.pd.client.rmi.ClientRemoteInterface;
import pt.isec.pd.server.data.database.CreateDataBase;
import pt.isec.pd.server.data.database.DBHandler;
import pt.isec.pd.server.rmi.ServerRemoteInterface;
import pt.isec.pd.server.threads.client.ClientManagement;
import pt.isec.pd.server.threads.client.ClientReceiveMessage;
import pt.isec.pd.utils.Log;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class Server extends UnicastRemoteObject implements ServerRemoteInterface {
    private final Log LOG = Log.getLogger(Server.class);
    private HeartBeatList hbList;
    private String ip;
    private ClientManagement cm;
    private final String dbPath;
    private HeartBeatController hbController;
    private DBHandler dbHandler;
    private Scanf scanf;

    public Server(int pingPort,String dbPath) throws RemoteException {
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

    public void init(int pingPort) {
        hbList = new HeartBeatList();

        File f = new File(dbPath);
        if(!f.exists() && !f.isDirectory()) {
            LOG.log("Data base create: " + dbPath);
            new CreateDataBase(dbPath);
        }

        dbHandler = new DBHandler(dbPath);

        hbController = new HeartBeatController(hbList,this);
        cm = new ClientManagement(pingPort, dbHandler,hbList, hbController,ip);
    }

    public void start() {
        hbController.start();

        cm.startPingHandler();
        cm.start();
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

    @Override
    public void receiveUdpConnection(ClientRemoteInterface clientReference, String ip, int port) throws RemoteException {
        LOG.log("[RMI] Client " + ip + ":" + port + " sent UDP datagram packets");

        // retrieve list of active servers
        clientReference.listActiveServers(hbList.getServerInfoOrderList());
    }

    @Override
    public void acceptTcpConnection(ClientRemoteInterface clientReference, String ip, int port) throws RemoteException {
        LOG.log("[RMI] TCP connection of client on " + ip + ":" + port + " has been accepted");
    }

    @Override
    public void notifyTcpDisconnection(ClientRemoteInterface clientReference, String ip, int port) throws RemoteException {
        LOG.log("[RMI] Client on " + ip + ":" + port + " has lost his TCP connection");
    }

    @Override
    public void notifyClientLog(ClientRemoteInterface clientReference, ClientAction action, String ip, int port) throws RemoteException {
        switch (action) {
            case LOGIN -> LOG.log("[RMI] Client " + ip + ":" + port + " logged in");
            case DISCONNECTED -> LOG.log("Client " + ip + ":" + port + " logged out");
            default -> LOG.log("RMI client login/logout notification error");
        }
    }
}
