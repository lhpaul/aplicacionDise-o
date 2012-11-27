package cl.puc.dds.appmgr.resources;

import cl.puc.dds.appmgr.external.IDevice;

public class DeviceState {
	
	private IDevice device;
	private String data;
	private String userID;
	
	private byte[] token; // Permite recuperar los datos desde persistencia
	
	public byte[] getToken() {
		return token;
	}
	public void setToken(byte[] token) {
		this.token = token;
	}
	public IDevice getDevice() {
		return device;
	}
	public void setDevice(IDevice device) {
		this.device = device;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	



}
