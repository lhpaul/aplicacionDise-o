package IIC2113.resource.manager;

public interface IUserManager {
	public void consumptionFinished(int resource_id, String path);
	public void consumptionFailed(int resource_id, String error);
	public void consumptionInterrupted(int resource_id, String error);
	public void consumptionStarted(int resource_id,String[] details);
}
