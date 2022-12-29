package pt.isec.pd.server.data;

import pt.isec.pd.shared_data.HeartBeat;
import pt.isec.pd.shared_data.ServerAddress;
import pt.isec.pd.shared_data.ServerInfo;

import java.util.*;

public class HeartBeatList extends LinkedList<HeartBeat>{
    public synchronized boolean updateList(HeartBeat element) {
        // if returns true the heartbeat was changed or added
        element.addTimeStamp(new Date());

        int index = indexOf(element);
        if (index == -1) {
            add(element);
            return true;
        }

        HeartBeat old = remove(index);
        add(index,element);

        return old.isStatus() != element.isStatus() || old.getActiveConnections() != element.getActiveConnections();
    }

    public List<ServerAddress> getOrderList() {
        List<ServerAddress> servers = new ArrayList<>();
        List<HeartBeat> orderList = this;
        Collections.sort(orderList);

        for (HeartBeat event : orderList) {
            servers.add(new ServerAddress(event.getIp(),event.getPortTcp()));
        }

        return servers;
    }

    public List<ServerInfo> getServerInfoOrderList() {
        List<ServerInfo> servers = new ArrayList<>();
        List<HeartBeat> orderList = this;
        Collections.sort(orderList);

        for (HeartBeat event : orderList) {
            Date date = new Date();
            long secs = date.getTime()/1000 - event.getTimeout().getTime()/1000;
            servers.add(new ServerInfo(event.getIp(),event.getPortTcp(),event.getPortUdp(),event.getActiveConnections(),secs));
        }

        return servers;
    }
}
