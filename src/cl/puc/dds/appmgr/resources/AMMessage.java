package cl.puc.dds.appmgr.resources;


import java.io.Serializable;

public class AMMessage implements Serializable{
	public String sender_id;
	public String action;
	public Object pack;
	
	/** 
	 * @param id sender id
	 * @param action datos de la acción a realizar 
	 * @param obj puede ser, por ejemplo, un resource.
	 */
	public AMMessage(String sender_id, String action, Object obj)
	{
		this.sender_id = sender_id;
		this.action = action;
		this.pack = obj;
	}
	
}
