package net;

public interface TCPServer {
	TCPClient accept() throws Exception;
	void setConnectionListener(ConnectionListener connectionListener);
	ConnectionListener getConnectionListener();
	void start();
	void stop();
	boolean isRun();
}
