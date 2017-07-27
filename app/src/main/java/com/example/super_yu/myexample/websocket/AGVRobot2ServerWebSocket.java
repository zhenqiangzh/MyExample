package com.example.super_yu.myexample.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.Field;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.SocketChannel;

/**
 * Created by super_yu on 2017/6/20.
 */

public class AGVRobot2ServerWebSocket extends WebSocketClient {

    private AGVClient.AGV2ServerConnectionStatusListener listener;


    public AGVRobot2ServerWebSocket(URI serverURI) {
        super(serverURI);
        listener = null;
    }

    public static AGVRobot2ServerWebSocket create(String URIString) {
        AGVRobot2ServerWebSocket client = null;
        try {
            URI uri = new URI(URIString);
            client = new AGVRobot2ServerWebSocket(uri);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return client;
    }


    public void setListener(AGVClient.AGV2ServerConnectionStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (listener != null)
            listener.onAGV2ServerConnect();
    }

    @Override
    public void onMessage(String s) {
        if (listener != null)
            listener.onAGV2ServerMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        if (listener != null)
            listener.onAGV2ServerDisconnect(b, s, i);
    }

    @Override
    public void onError(Exception e) {
        if (listener != null)
            listener.onAGV2ServerError(e);
    }

    @Override
    public void closeBlocking() throws InterruptedException {
        super.closeBlocking();
        try {
            Field channelField = this.getClass().getSuperclass().getDeclaredField("channel");
            channelField.setAccessible(true);
            SocketChannel channel = (SocketChannel) channelField.get(this);
            if (channel != null && channel.isOpen()) {
                Socket socket = channel.socket();
                if (socket != null)
                    socket.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception in Websocket close hack.");
            ex.printStackTrace();
        }
    }

    public void sendServer4Message(String msg) {
        send(msg);
    }
}
