package pt.isec.pd.server;

import pt.isec.pd.server.data.Server;
import pt.isec.pd.server.rmi.ServerRemoteInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, RemoteException {
        // register rmi service
        Registry r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        r.rebind(ServerRemoteInterface.REGISTRY_BIND_NAME, new Server(Integer.parseInt(args[0]),args[1]));
    }
}
