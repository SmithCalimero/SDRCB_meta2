package rmi;

import rmi.IObservable;
import rmi.RemoteListener;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Listener {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        String registry = "localhost";
        if (args.length >= 1) registry = args[0] + ":" + args[1];

        // Lookup the service in the registry, and obtain a remote service
        String registration = "rmi://" + registry + "/SHOW_SERVICE_" + args[1];
        Remote remoteService = Naming.lookup(registration);
        IObservable notifier = (IObservable) remoteService;

        RemoteListener remoteObserver = new RemoteListener();
        notifier.addListener(remoteObserver);

        Scanner sc = new Scanner(System.in);
        String op = "";
        while (!op.equalsIgnoreCase("stop")) {
            System.out.println("SERVICE RUNNING! ASYNCHRONOUS NOTIFICATIONS WILL BE PRINTED AUTOMATICALLY");
            System.out.println("OPTIONS:");
            System.out.println("\t'list' -> List all active servers");
            System.out.println("\t'stop' -> Stop the program");
            System.out.println();

            op = sc.nextLine();

            if (op.equalsIgnoreCase("list"))
                System.out.println(notifier.listServers());
        }

        // close rmi service
        try {
            notifier.removeListener(remoteObserver);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            UnicastRemoteObject.unexportObject(remoteObserver, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }
}
