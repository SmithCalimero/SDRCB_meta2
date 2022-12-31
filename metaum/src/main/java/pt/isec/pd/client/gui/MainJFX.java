package pt.isec.pd.client.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pt.isec.pd.client.model.ModelManager;
import pt.isec.pd.client.model.data.Client;
import pt.isec.pd.shared_data.ServerAddress;
import pt.isec.pd.utils.Log;

import java.rmi.RemoteException;
import java.util.List;

public class MainJFX extends Application {
    private final Log LOG = Log.getLogger(Client.class);
    ModelManager model;

    @Override
    public void start(Stage stage) {
        //Receive arguments from main
        Parameters params = getParameters();
        List<String> arguments = params.getRaw();
        ServerAddress udpConn = new ServerAddress(arguments.get(0),Integer.parseInt(arguments.get(1)));

        model = new ModelManager(udpConn);

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
