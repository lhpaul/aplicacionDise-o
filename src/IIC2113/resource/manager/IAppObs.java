package IIC2113.resource.manager;

public interface IAppObs {
	public void resourceFinished(int resource_id, Object object);
	public void resourceFailed(int resource_id, String error);
	public void resourceInterrupted(int resource_id, String error);
}
