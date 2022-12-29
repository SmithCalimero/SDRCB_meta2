module fxml {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires java.rmi;


    opens pt.isec.pd.client.gui.view to javafx.fxml;
    exports pt.isec.pd.client.gui.view;
    exports pt.isec.pd.client.model;
    exports pt.isec.pd.client.gui;
    exports pt.isec.pd.shared_data;
    exports pt.isec.pd.server.rmi to java.rmi;
    exports pt.isec.pd.client.rmi to java.rmi;
}