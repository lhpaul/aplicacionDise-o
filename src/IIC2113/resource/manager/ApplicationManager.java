package IIC2113.resource.manager;

import externos.Controlador;
import modelo.Persistencia;
import usermanager.UserManager;

//import IIC2113.resource.manager.R.string;

public class ApplicationManager implements IAppObs{

	private ResourceManager resourceManager;
	private UserManager userManager;
	private Persistencia persistencia;
	private Controlador controladorPersistencia;
	private int device_id = 1337;
	
	
	public void init()
	{
		this.controladorPersistencia = new Controlador();
		this.resourceManager = new ResourceManager();
		this.resourceManager.setAppObserver((IAppObs)this);
		//this.resourceManager.setPersistencia((IPersistencia)this.persistencia);
		this.resourceManager.setDeviceId(device_id);
	}

	public void takePicture()
	{
		resourceManager.resourceAction(0, 0, null); //foto,tomar,param
	}
	
	public void startRecording()
	{
		resourceManager.resourceAction(0, 1, null);//video,grabar,param
	}
	
	public void stopRecording()
	{
		resourceManager.resourceAction(0, 2, null);//video,parar_grabacion,param
	}
	
	public void endResources()
	{
		resourceManager.endResources();
	}
	
	public void resourceFinished(int resource_id, Object object) {
		System.out.println("Application Manager - Resource:"+ResourceManager.RESOURCES[resource_id]+" FINISHED - (object:"+object.toString()+")");		
	}

	public void resourceFailed(int resource_id, String error) {
		System.out.println("Application Manager - Resource:"+ResourceManager.RESOURCES[resource_id]+" FAILED - (error:"+error+")");
	}

	public void resourceInterrupted(int resource_id, String error) {
		System.out.println("Application Manager - Resource:"+ResourceManager.RESOURCES[resource_id]+" INTERRUPTED - (error:"+error+")");
	}

}
