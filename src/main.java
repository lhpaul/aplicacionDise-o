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
		ArrayList<ResourceState> ListaRecursos;
		try {
			while (true) {
				Thread.currentThread().sleep(5000);
				System.out.println("imprimiendo lista de recursos:");
				ListaRecursos = app.getAllForeignResources();
				for (ResourceState r : ListaRecursos) {
					System.out.println(r.getType());
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
