package rmi;

import utils.Log;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteListener extends UnicastRemoteObject implements ListenerInterface {
    public RemoteListener() throws RemoteException { }
    private final Log LOG = Log.getLogger(RemoteListener.class);
    @Override
    public void notify(String description) throws RemoteException {
        LOG.log(description);
    }
}
