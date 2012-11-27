package cl.puc.dds.appmgr.external;

import java.util.ArrayList;
import java.util.HashMap;

public interface IApplication {

	ArrayList<IResource> getResources();
	HashMap<IDevice, ArrayList<IResource>> getAssignedDevices();
	IResourceMgr getResourceMgr();
	HashMap<IResource, Object> getResourcesFlags();
	
}
