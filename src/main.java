import usermanager.ResourceState;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		try {
			while (true) {
				/*Thread.currentThread().sleep(5000);
				System.out.println("imprimiendo lista de recursos:");
				ListaRecursos = app.getAllForeignResources();
				for (ResourceState r : ListaRecursos) {
					System.out.println(r.getType());
				}*/
				ListaRecursos = app.getAllForeignResources();
				
				if (ListaRecursos.size() > 0)
				{
					for (ResourceState r : ListaRecursos) {
						System.out.println(r.getType());
					}
					System.out.println("Escriba el recurso a pedir");
					int indice = Integer.parseInt(br.readLine());
					if(app.userResource(ListaRecursos.get(indice)))
					{
						System.out.println("Peticion exitosa");
					} else {
						System.out.println("Peticion fallo");
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
