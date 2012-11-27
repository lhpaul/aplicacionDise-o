package cl.puc.dds.appmgr.resources;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import IIC2113.resource.manager.ResourceManager;

import usermanager.ResourceState;
import usermanager.UserManager;

import communication.Communication;

import cl.puc.dds.appmgr.external.IApplication;
import cl.puc.dds.appmgr.external.ICommunicationMgr;
import cl.puc.dds.appmgr.external.IPersistenceMgr;
import cl.puc.dds.appmgr.external.IResource;
import cl.puc.dds.appmgr.external.IResourceMgr;


/**
 * @author Maquina
 *
 */
public abstract class Application{

	String appID; /*Permite reconocer la misma aplicación en diferentes dispositivos. Ejemplo: Angry birds siempre tendrá id "AngryBirds01" */
	String version; 

	ApplicationListener listener; /*Maneja peticiones de recursos de otros dispositivos a este*/
	DeviceState state; /*Contiene el dispositivo LOCAL donde corre esta app*/
	ArrayList<IResource> resources = new ArrayList<IResource>(); /*Lista de dispositivos locales*/



	// Acceso a los managers externos, Ojo: igual son los de ESTE dispotivo.



	Communication communicationMgr;
	IPersistenceMgr persistenceMgr;
	ResourceManager resourceMgr;

	UserManager userMgr;

	ApplicationDaemon deamon; /*Thread que corre métodos de rutina (persistencia, revisar dispositivos que se caen, etc.)*/

	public Application() 
	{


		try {

			SecureRandom random = new SecureRandom();    	
		    int userid = random.nextInt(20)+6000;
			
			resourceMgr = new ResourceManager();
			resourceMgr.setAppObserver(this.listener);
			
			communicationMgr = new Communication(userid);
			new Thread(communicationMgr).start();
			
			userMgr = UserManager.init(communicationMgr, userid);
			



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	// Lista con recursos de otros dispotivos.
	List<ResourceState> foreignResources = new ArrayList<ResourceState>();


	// Lista de dispositivos actualmente conectados a la "red" de la aplicación
	// Key: ID del dispositivo, Value: El dispositivo mismo (más su estado en esta aplicación)
	HashMap<String, DeviceState> connectedDevices = new HashMap<String, DeviceState>(); 



	/**
	 * Devuelve la lista con referencias 
	 * @return ArrayList con todos los recursos
	 */
	public ArrayList<ResourceState> getAllForeignResources(){
		setForeignResources();
		return (ArrayList<ResourceState>)foreignResources;		
	}


	/**
	 * Este método debe ser llamado por el UserMgr para actualizar nuestra lista
	 * local de recursos foráneos (de otros dispositivos).
	 * @param res Nueva lista de resources
	 */
	public void setForeignResources(){
		this.foreignResources = userMgr.getResourceList();
	}


	public boolean userResource(ResourceState r){

		String id = state.getDevice().getId();

		Object amm = new AMMessage(id, "CONSUME" , r); 
		// CONSUME indica que se va a pedir "usar" el recurso. Aquí podrían ir otros parámetros
		// dependiendo del recurso. Ejemplo "burdo": Consumir foto en baja resolución. "CONSUME_LOWRES".

		// TODO CommMgr lo implementa lanzando una excpetion o nos devuelve un boolean?
		// por ahora asumimos exception:

		try{
			this.communicationMgr.sendObject(amm , r.getUserId());
			return true;
		}catch(Exception e){
			return false;
		}


	}


	public HashMap<IResource, Object> resourcesFlags = new HashMap<IResource, Object>();

	public HashMap<IResource, Object> getResourcesFlags() {
		return resourcesFlags;
	}


	public void setResourcesFlags(HashMap<IResource, Object> resourcesFlags) {
		this.resourcesFlags = resourcesFlags;
	}


	public Object recieveMesagge(AMMessage amm){


		if(amm.action.equals("CONSUME")){

			IResource r = (IResource) amm.pack;

			// En este ejemplo sacaremos una foto
			resourceMgr.resourceAction(r.getId(), 0, null);

			if(!resourcesFlags.containsKey(r)){
				resourcesFlags.put(r, null);
			}

			Object respond = resourcesFlags.get(r);

			while(respond == null){				
			}

			return respond;			
		}

		return null;
	}


	public ICommunicationMgr getCommunicationMgr() {
		return communicationMgr;
	}

	public void setCommunicationMgr(Communication communicationMgr) {
		this.communicationMgr = communicationMgr;
	}

	public IPersistenceMgr getPersistenceMgr() {
		return persistenceMgr;
	}

	public void setPersistenceMgr(IPersistenceMgr persistenceMgr) {
		this.persistenceMgr = persistenceMgr;
	}

	public void setResourceMgr(ResourceManager resourceMgr) {
		this.resourceMgr = resourceMgr;
	}

	public HashMap<String, DeviceState> getConnectedDevices() {
		return connectedDevices;
	}

	public void setConnectedDevices(HashMap<String, DeviceState> connectedDevices) {
		this.connectedDevices = connectedDevices;
	}
	// Lista con todos los dispositivos que tienen algún recurso asignado
	// Key: El disp. mismo (más su estado en esta app.) , Value: ArrayList con todos los disp. asignados al disp. del key.
	HashMap< DeviceState , ArrayList<IResource> > assignedDevices = new HashMap<DeviceState, ArrayList<IResource>>();


	// Agregar-remover dispositivos al entorno de la aplicación
	public void addDevice(DeviceState deviceState){
		this.connectedDevices.put(deviceState.getDevice().getId() , deviceState);
	}

	public boolean removeDevice(DeviceState deviceState){
		for( String i : connectedDevices.keySet() ){
			if(i.equals(deviceState.getDevice().getId())){
				connectedDevices.remove(i);
				return true;
			}			
		}
		return false;
	}




	//Guardar y recuperar estado de la aplicación
	public void saveAppState(){
		state.setToken(persistenceMgr.save(state.getUserID(), state.getDevice().getId(), state.getData()));
	}

	public void loadAppState(byte[] token){
		persistenceMgr.retriveByDigest(token);
	}



	// Getters & Setters	
	public HashMap<DeviceState, ArrayList<IResource>> getAssignedDevices() {
		return assignedDevices;
	}	
	public ArrayList<IResource> getResources() {
		return resources;
	}
	public ResourceManager getResourceMgr() {
		return resourceMgr;
	}	
	public ApplicationListener getApplicationListener(){
		return listener;
	}	
	public void setApplicationListener(ApplicationListener al){
		listener = al;
	}






}
