package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ListenerInterface extends Remote {
    void notify(String description) throws RemoteException;
}
