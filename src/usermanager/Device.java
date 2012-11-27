package usermanager;

import java.io.Serializable;

public class Device implements Serializable {

  private static final long serialVersionUID = 1L;    
  protected String type = null;
	protected String uid = null;
	protected String ipAddress = null;
	protected String macAddress = null;
	
	public Device(){
		//TODO: check if need this.
	}
	
	/**
	 * Sets the type of the device.
	 * @param type the type of the device.
	 */
	public void setType(String type){
		this.type = type;
	}
	
	/**
	 * Sets the unique id of the device.
	 * @param uid the unique id of the device.
	 */
	public void setUid(String uid){
		this.uid = uid;
	}
	
	/**
	 * Returns the type of the device.
	 * @return the type of the device.
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Returns the unique id of the device.
	 * @return the unique id of the device.
	 */
	public String getUid(){
		return uid;
	}
	
	/**
	 * Returns the resource manager for this device.
	 * @return interface of the resource manager for this device.
	 */
	public Object getResourceManager(){
		//TODO complete this.
		return null;
	}
	
	/**
	 * Returns the device ip address as a string.
	 * @return the ip address as a string.
	 */
	public String getIpAddress(){
	    return ipAddress;
	}
	
	/**
	 * Returns the device's mac address.
	 * @return mac address of the device.
	 */
	public String getMacAddress(){
	    return macAddress;
	}
}
