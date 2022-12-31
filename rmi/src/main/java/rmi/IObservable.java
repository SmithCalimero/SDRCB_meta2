package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObservable extends Remote {
    String listServers() throws RemoteException;

    void addListener(ListenerInterface listener) throws RemoteException;
    void removeListener(ListenerInterface listener) throws RemoteException;
}