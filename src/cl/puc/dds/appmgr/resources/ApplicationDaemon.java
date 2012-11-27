package cl.puc.dds.appmgr.resources;

import java.util.ArrayList;

public class ApplicationDaemon extends Thread{
	
	private Application app;
	
	public ApplicationDaemon(String s, Application app){
		super(s);
		this.app = app;
	}
	
	public void run(){
		// Si algún dispositivo se cae, lo retiramos de la aplicación
		ArrayList<DeviceState> nonConnected = new ArrayList<DeviceState>();
		for(DeviceState d : app.getConnectedDevices().values()){
			if(!app.getCommunicationMgr().isDeviceOnline(d.getDevice().getId())){
				app.removeDevice(d);
			}
		}
		
		// Guardar el estado de la app
		app.saveAppState();
		
	}

}
