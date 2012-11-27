package usermanager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import IIC2113.resource.manager.IUserManager;

import usermanager.Device;
import usermanager.ResourceState;
import usermanager.Sesion;
import usermanager.User;
import usermanager.Encoder;
import usermanager.Status;

import communication.Communication;
import communication.UMMessage;

public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Sesion> sesions = null;
	private Sesion currentSesion = null;

	private User currentUser;
	private Device currentDevice;	
	
	private Communication communication;


	public Communication getCommunication() {
		return communication;
	}

	public void setCommunication(Communication communication) {
		this.communication = communication;
	}

	private static UserManager um;

	public static UserManager getInstance() {
		return um;
	}
	
	public static UserManager init(Communication com , int id) {
		if (um == null) {
			um = new UserManager(com, id);
		}
		return um;
	}

	private int STATUS = Status.DISCONNECTED;

	/**
	 * Constructor of user manager, inititalizes the list of sesions and sets
	 * default status (disconnected).
	 */
	private UserManager(Communication com, int id) {
		
		
		
		
		sesions = new ArrayList<Sesion>();
		STATUS = Status.DISCONNECTED;

		currentUser = new User(id);

		List<ResourceState> lista_recursos = new ArrayList<ResourceState>();

		ResourceState r1 = new ResourceState();
		//ResourceState r2 = new ResourceState();
		r1.setId(1);
		r1.setUserId(currentUser.getId());
		r1.setType("Camara");
		//r2.setId(2);
		lista_recursos.add(r1);
		
		currentUser.setResources(lista_recursos);
		
		//lista_recursos.add(r2);

		
		
		
		
		
		
		communication = com;

		try {
			if (!this.communication.connectToSession()) {
				// Soy el primero en conectarme por lo que tengo que crear la
				// session
				joinSesion(new Sesion());
				sesions.add(currentSesion);
				currentSesion.getUsersList().add(currentUser);
			} else {
				// Envio un mensaje al primer usario de la sesion para que me
				// devuelva la sesion
				int first = this.communication.getNodos().get(0);
				UMMessage message = new UMMessage(this.currentUser.getId(), "get_session", null);
				this.communication.sendObject(message, first);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void recieveMessage(UMMessage message) {
		int sender = message.sender_id;
		if (message.action == "get_session") {
			UMMessage response = new UMMessage(this.currentUser.getId(), "set_session",
					getCurrentSesion());
			try {
				this.communication.sendObject(response, sender);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (message.action == "set_session") {
			joinSesion((Sesion) message.pack);
			currentSesion.getUsersList().add(currentUser);
			sesions.add(currentSesion);
			// Notificar al resto la incorporacion a la sesion
			UMMessage mess = new UMMessage(this.currentUser.getId(), "add_user", currentUser);
			try {
				this.communication.sendToAll(mess);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (message.action == "add_user") {
			currentSesion.getUsersList().add((User) message.pack);
		} else if (message.action == "remove_user") {
			currentSesion.getUsersList().remove((User) message.pack);
		}		
		else if( message.action == "update_state"){
			int[] new_state = (int[]) message.pack;
			this.updateState(new_state[0], new_state[1]);			
		}

	}

	/**
	 * Returns the current sesion.
	 * 
	 * @return the current sesion.
	 */
	public Sesion getCurrentSesion() {
		return currentSesion;
	}

	/**
	 * Returns the list of sesions available.
	 * 
	 * @return list of sesions.
	 */
	public Iterator<Sesion> getSesionsIterator() {
		return sesions.iterator();
	}

	public int getSesionCount() {
		return sesions.size();
	}

	public Sesion getSesion(int index) {
		return sesions.get(index);
	}

	/**
	 * Returns the current device.
	 * 
	 * @return current device.
	 */
	public Device getCurrentDevice() {
		return currentDevice;
	}

	/**
	 * Returns the current user.
	 * 
	 * @return the current user.
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * Adds a sesion for this user manager.
	 * 
	 * @param sesion
	 *            sesion to add.
	 */
	public void addSesion(Sesion sesion) {
		sesions.add(sesion);
	}

	/**
	 * Switches the current sesion for the sesion given.
	 * 
	 * @param sesion
	 *            sesion given to switch.
	 */
	public void joinSesion(Sesion sesion) {
		this.currentSesion = sesion;
		STATUS = Status.CONNECTED;

		//Falta metodo de agregar lista de recursos de ResourceManager (Setear ID recursos y usuario)
	}

	/**
	 * Leaves the current sesion.
	 * 
	 * @param sesion
	 *            sesion to leave.
	 */
	public void leaveSesion(Sesion sesion) {
		this.currentSesion = null;
		STATUS = Status.DISCONNECTED;

		UMMessage mess = new UMMessage(this.currentUser.getId(), "remove_user", currentUser);
		try {
			this.communication.sendToAll(mess);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Disconnects the device.
	 * 
	 * @param device
	 *            device for disconnect.
	 * @throws NoSuchMethodException
	 *             not implemented yet.
	 */
	public void disconnectDevice(Device device) throws NoSuchMethodException {
		// TODO complete this.
		throw new NoSuchMethodException();
	}

	/**
	 * Returns the status of user manager.
	 * 
	 * @return status code.
	 * @see usermanager.util.Status usermanager.util.Status for values.
	 */
	public int getStatus() {
		return STATUS;
	}

	/**
	 * Returns a string status of the user manager (for logging).
	 * 
	 * @return String indicating the status of the user manager.
	 */
	public String getUserMangerStatus() {
		String sesion = "null";
		String user = currentUser.getUsername();
		String device = currentDevice.getMacAddress();
		return "current sesion name: " + sesion + ", current username: " + user
				+ "(" + STATUS + ")" + ", current device: " + device;
	}

	/**
	 * Returns a string indicating the status of the user manager.
	 * 
	 * @return status string.
	 */
	public String getStringStatus() {
		switch (STATUS) {
		case Status.CONNECTED:
			return "connected";
		case Status.DISCONNECTED:
			return "disconnected";
		case Status.CONNECTING:
			return "connecting";
		case Status.UPDATED:
			return "updated";
		case Status.UPDATING:
			return "updating";
		}
		return null;
	}

	public String getSerializedString() {
		String serial = null;
		try {
			serial = Encoder.toString(this);
		} catch (IOException e) {
			System.out.println("An error ocurred when trying to serialize");
		}
		return serial;
	}

	// actualizar estado recurso

	private void updateState(int resource_id, int state) {

		for (User usr : currentSesion.getUsersList()) {
			for (ResourceState rsrc : usr.getResources()) {
				// FIXME here
				if (rsrc.getId() == resource_id) {
					rsrc.setStatus(state);
					return;
				}

			}
		}

	}

	/* Metodos relacionados con resource Manager */

	public void consumptionFinished(int resource_id, String path) {
		// Dejar recurso como inactivo en la lista
		updateState(resource_id, 0);
		//Avisar a todos
		int[] state_data = { resource_id , 0  };		
		UMMessage mess = new UMMessage(this.currentUser.getId(), "update_state", state_data);
		try {
			this.communication.sendToAll(mess);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void consumptionFailed(int resource_id, String error) {
		// informar error

	}

	public void consumptionInterrupted(int resource_id, String error) {

	}

	public void consumptionStarted(int resource_id, String[] details) {
		updateState(resource_id, 1);
		int[] state_data = { resource_id , 1  };		
		UMMessage mess = new UMMessage(this.currentUser.getId(), "update_state", state_data);
		try {
			this.communication.sendToAll(mess);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Sesion getCurrentSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public void disconnectUser(int user_id) {
		// TODO Auto-generated method stub

	}

	public void updateUser(User updatedUser) {
		// TODO Auto-generated method stub

	}


	public List<ResourceState> getResourceList(){  
		List<ResourceState> lista_recursos = new ArrayList<ResourceState>();    

		for( User usr : currentSesion.getUsersList()){        
			for( ResourceState rsrc : usr.getResources()){        
				if(rsrc.getStatus() == 0)
					lista_recursos.add(rsrc);       
				}      
		} 
		
		return currentUser.getResources();   

	}

}
