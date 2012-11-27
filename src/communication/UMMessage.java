package communication;


import java.io.Serializable;


public class UMMessage implements Serializable{
	public int sender_id;
	public String action;
	public Object pack;

	public UMMessage(int id, String act, Object obj)
	{
		this.sender_id = id;
		this.action = act;
		this.pack = obj;
	}

}