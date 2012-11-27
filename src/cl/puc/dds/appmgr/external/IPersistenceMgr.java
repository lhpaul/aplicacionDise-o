package cl.puc.dds.appmgr.external;

import cl.puc.dds.appmgr.resources.Application;
import cl.puc.dds.appmgr.resources.DeviceState;

public interface IPersistenceMgr {
	
	//boolean saveDeviceState(DeviceState ds);
	//DeviceState loadDeviceState(IDevice d);
	//void saveAppState(Application application);

	
	/**Contrato ofrecido por el equipo de Persistencia */
	public byte[] save(String userId, String deviceId, String message); // Devuelve un digest
	public String retriveByDigest(byte[] digest);
	/**Fin del contrato*/
}
