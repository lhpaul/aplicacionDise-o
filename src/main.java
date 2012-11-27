import usermanager.ResourceState;

import java.util.ArrayList;

import cl.puc.dds.appmgr.resources.Application;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MyApplication app = new MyApplication();
		ArrayList<ResourceState> ListaRecursos = app.getAllForeignResources();
		System.out.println(ListaRecursos.size());
		

	}

}
