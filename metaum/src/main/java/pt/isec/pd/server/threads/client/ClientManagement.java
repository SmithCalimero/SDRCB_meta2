package pt.isec.pd.server.threads.client;

import pt.isec.pd.server.data.HeartBeatController;
import pt.isec.pd.server.data.HeartBeatList;
import pt.isec.pd.server.data.Server;
import pt.isec.pd.server.data.database.DBHandler;
import pt.isec.pd.shared_data.HeartBeat;
import pt.isec.pd.utils.Log;
import rmi.IObservable;
import rmi.ListenerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientManagement extends UnicastRemoteObject implements IObservable,Runnable {
    private final Log LOG = Log.getLogger(Server.class);
    private ServerSocket serverSocket;
    private ClientPingHandler pingHandler;
    private int portUdp;
    private boolean isConnected = true;             // validate if the user is connected
    private DBHandler dbHandler;
    private Integer numConnections = 0;
    private HeartBeatController hbController;
    private List<ClientReceiveMessage> clientsThread = new ArrayList<>();
    private ArrayList<ClientReceiveMessage> viewingSeats = new ArrayList<>();
    private ArrayList<ListenerInterface> listeners = new ArrayList<>();

    public ClientManagement(int pingPort, DBHandler dataBaseHandler, HeartBeatList hbList, HeartBeatController hbController,String ip) throws RemoteException {
        try {
            this.hbController = hbController;
            this.serverSocket = new ServerSocket(0);
            this.pingHandler = new ClientPingHandler(pingPort, hbList, this);
            this.portUdp = pingPort;
            this.dbHandler = dataBaseHandler;
            this.clientsThread = new ArrayList<>();
            this.viewingSeats = new ArrayList<>();
            hbList.add(new HeartBeat(serverSocket.getLocalPort(),pingPort,true, dbHandler.getCurrentVersion(),0, ip));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Handles the connection of the clients and instantiates a new Thread for the client
    @Override
    public void run() {
        try {
            while (isConnected) {
                Socket socket = serverSocket.accept();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                notifyListeners("User " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort()
                + " connected to TCP service successfully");  // notify rmi listeners

                //Update db
                if((Integer) ois.readObject() == 0) {
                    int version = (Integer) ois.readObject();
                    Map<Integer,List<String>> versionQuerys = dbHandler.getListOfQuery(version,dbHandler.getCurrentVersion());
                    oos.writeObject(versionQuerys);
                } else {
                    incConnection();
                    LOG.log("New connection established: " + numConnections);

                    // Creates a thread for that client
                    ClientReceiveMessage clientRM = new ClientReceiveMessage(socket,oos,ois, dbHandler, this,hbController);
                    clientRM.start();

                    hbController.sendHeartBeat();
                    clientsThread.add(clientRM);
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException e ) {
            e.printStackTrace();
        }
    }

    public void startPingHandler() {
        pingHandler.start();
    }

    public int getServerPort() {
        return serverSocket.getLocalPort();
    }

    public int getPortUdp() { return portUdp; }

    public int getNumConnections() {
        return numConnections;
    }

    public synchronized List<ClientReceiveMessage> getClientsThread() {
        return clientsThread;
    }

    public synchronized void incConnection() {
        numConnections++;
    }

    public synchronized void subConnection() {
        numConnections--;
    }

    @Override
    public String listServers() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("Servers List\n");
        int index = 1;
        for (var s : hbController.getHbList().getServerInfoOrderList()) {
            sb.append(index + ")\t");
            sb.append("ip: " + s.getIp() + " ");
            sb.append(",tcpPort: " + s.getPort() + " ");
            sb.append(",updPort: " + s.getPortUdp() + " ");
            sb.append(", activeConnections: " + s.getActiveConnections() + " ");
            sb.append(", lastHeartBit: " + s.getLastHeartBit() + "\n");
            index++;
        }
        return sb.toString();
    }

    @Override
    public void addListener(ListenerInterface listener) throws RemoteException {
        LOG.log("Adding listener - " + listener);
        listeners.add(listener);
    }

    @Override
    public void removeListener(ListenerInterface listener) throws RemoteException {
        LOG.log("Removing listener - " + listener);
        listeners.remove(listener);
    }

    public void notifyListeners(String description) throws RemoteException {
        for (var listener : listeners)
            try {
                listener.notify(description);
            } catch (RemoteException e) {
                listeners.remove(listener);
            }
    }
}
