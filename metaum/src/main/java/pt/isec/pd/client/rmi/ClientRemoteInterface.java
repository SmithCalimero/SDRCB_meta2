package pt.isec.pd.client.rmi;

import pt.isec.pd.shared_data.ServerInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientRemoteInterface extends Remote {
    // 1) display current active servers
    void listActiveServers(List<ServerInfo> serversAddr) throws RemoteException;

    // 2) login validation response
    void loginResponse(boolean success) throws RemoteException;
}
