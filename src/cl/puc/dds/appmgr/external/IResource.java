package cl.puc.dds.appmgr.external;

import IIC2113.resource.manager.IConsumptionObs;

public interface IResource {
	
	String getType();
	
	
	/** EXTERNAL: Exigencias de IResource según ResourceMgr */	
	public boolean isAvailable();
	public void cancelConsumption();
	public void setObserver(IConsumptionObs observer);
	public boolean recieveAction(int action_id, String[] param);
	public int getStatus();
	public void setId(int id);
	/** Fin exigencias */
	
	public int getOwnerId();
	public int getId();
	
	
}
