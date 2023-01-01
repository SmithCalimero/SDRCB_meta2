package pt.isec.pd.shared_data;

public class ServerInfo extends ServerAddress {
    private int portUdp;
    private int activeConnections;
    private String lastHeartBit;

    public ServerInfo(String ip, int port, int portUdp, int activeConnections, String lastHeartBit) {
        super(ip, port);
        this.portUdp = portUdp;
        this.activeConnections = activeConnections;
        this.lastHeartBit = lastHeartBit;
    }

    public int getPortUdp() { return portUdp; }

    public void setPortUdp(int portUdp) { this.portUdp = portUdp; }

    public int getActiveConnections() { return activeConnections; }

    public void setActiveConnections(int activeConnections) { this.activeConnections = activeConnections; }

    public String getLastHeartBit() { return lastHeartBit; }

    public void setLastHeartBit(String lastHeartBit) { this.lastHeartBit = lastHeartBit; }
}
