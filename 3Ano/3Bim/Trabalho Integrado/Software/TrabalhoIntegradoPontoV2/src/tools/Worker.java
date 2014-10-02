package tools;

public abstract class Worker implements Runnable {
	private Thread thread;
	private boolean running;
	
	public Worker() {
		reset();
	}
	
	public void start() {
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.start();		
	}
	
	public void stop(boolean wait) {
		if (!running) return;
		running = false;
		if (wait) {
			try {
				thread.join();
			} catch(Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	protected void reset() {
		thread = null;
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
}













