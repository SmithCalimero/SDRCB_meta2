package pt.isec.pd.server.rmi;

import pt.isec.pd.client.model.data.ClientAction;
import pt.isec.pd.client.rmi.ClientRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemoteInterface extends Remote {
    String REGISTRY_BIND_NAME = "ServerService";

    // 1) UDP client reception (with IP and PORT)
    void receiveUdpConnection(ClientRemoteInterface clientReference, String ip, int port) throws RemoteException;

    // 2) TCP accepted connection requests by clients
    void acceptTcpConnection(ClientRemoteInterface clientReference, String ip, int port) throws RemoteException;

    // 3) TCP connections losses
    void notifyTcpDisconnection(ClientRemoteInterface clientReference, String ip, int port) throws RemoteException;

    // 4) clients logins and logouts
    void notifyClientLog(ClientRemoteInterface clientReference, ClientAction action, String ip, int port) throws RemoteException;
}
