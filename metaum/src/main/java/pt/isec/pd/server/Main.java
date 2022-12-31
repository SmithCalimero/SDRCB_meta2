package pt.isec.pd.server;

import pt.isec.pd.server.data.Server;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        new Server(Integer.parseInt(args[0]),args[1]);
    }
}
