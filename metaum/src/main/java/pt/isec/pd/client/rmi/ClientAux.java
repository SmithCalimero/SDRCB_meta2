package pt.isec.pd.client.rmi;

import pt.isec.pd.client.model.data.ClientAction;
import pt.isec.pd.client.model.fsm.Context;
import pt.isec.pd.shared_data.ServerInfo;
import pt.isec.pd.utils.Log;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientAux extends UnicastRemoteObject implements ClientRemoteInterface {
    private final Log LOG = Log.getLogger(Context.class);
    private int id;
    private ClientAction action;

    public ClientAux(int id) throws RemoteException {
        this.id = id;
    }

    public int getId() { return id; }

    public void setAction(ClientAction action) { this.action = action; }

    public ClientAction getAction() { return action; }

    @Override
    public void listActiveServers(List<ServerInfo> serversAddr) throws RemoteException {
        LOG.log("[RMI] Available servers");
        for (var server : serversAddr)
            LOG.log("\tip: " + server.getIp() +
                    "\ttcpPort:" + server.getPort() +
                    "\tudpPort:" + server.getPortUdp() +
                    "\tactive connections: " + server.getActiveConnections() +
                    "\tlast heartBit: " + server.getLastHeartBit() + "s ago");
    }

    @Override
    public void loginResponse(boolean success) throws RemoteException {
        if (success)
            LOG.log("[RMI] Logged in successfully");
        else
            LOG.log("[RMI] Unable to login");
    }
}
