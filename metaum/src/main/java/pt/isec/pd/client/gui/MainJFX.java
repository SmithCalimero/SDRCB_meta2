package pt.isec.pd.client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import pt.isec.pd.client.Main;
import pt.isec.pd.client.gui.view.LoginForm;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.model.data.Client;
import pt.isec.pd.server.data.Server;
import pt.isec.pd.server.rmi.ServerRemoteInterface;
import pt.isec.pd.shared_data.ServerAddress;
import pt.isec.pd.utils.Log;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainJFX extends Application {
    private final Log LOG = Log.getLogger(Client.class);
    ModelManager model;

    @Override
    public void start(Stage stage) throws RemoteException, NotBoundException {
        //Receive arguments from main
        Parameters params = getParameters();
        List<String> arguments = params.getRaw();
        ServerAddress udpConn = new ServerAddress(arguments.get(0),Integer.parseInt(arguments.get(1)));

        // setup rmi service
        Registry r = LocateRegistry.getRegistry(arguments.get(0), Registry.REGISTRY_PORT);
        ServerRemoteInterface remoteRef = (ServerRemoteInterface)
                r.lookup(ServerRemoteInterface.REGISTRY_BIND_NAME);

        model = new ModelManager(udpConn,remoteRef);

        BorderPane root = new RootPane(model);
        Scene scene = new Scene(root,640,380);
        stage.setScene(scene);
        stage.setTitle("PD-meta1");
        stage.sizeToScene();
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                model.disconnect();
            }
        });
    }
}
