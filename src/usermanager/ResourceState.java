package usermanager;

import java.io.Serializable;

import IIC2113.resource.manager.IConsumptionObs;
import cl.puc.dds.appmgr.external.IResource;

public class ResourceState implements IResource, Serializable {
    // 0 1 2 3 4 5
    public static final String[] RESOURCES = { "AVAILABLE", "IN USE", "NOT PRESENT", "RECORDING", "STREAMING",
            "NOT AVAILABLE" };
    private int id;
    private int status = 0;
    private IConsumptionObs observer;
    private int pic_counter = 0;
    private int vid_counter = 0;
    private int user_id = 0;
    private String type;

    public void setType(String type) {
		this.type = type;
	}

	public boolean isAvailable() {
        return true;
    }

    public void cancelConsumption() {

    }

    public void setObserver(IConsumptionObs _observer) {

    }

    public boolean recieveAction(int action_id, String[] param) {

        return true;
    }

    public int getStatus() {
        return status;
    }

    public void setId(int _id) {
        this.id = _id;
    }
    
    public int getId(){
    	return this.id;    	
    }
    
    
    public void setStatus(int stat){
    	
    	this.status = stat;
    }
    
    public void setUserId(int id){
    	this.user_id = id;
    }
    
    public int getUserId(){
    	return this.user_id;
    }
    
    public String getType(){
    	return type;
    }

	public int getOwnerId() {
		return this.user_id;
	}

}