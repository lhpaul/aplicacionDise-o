package communication;
import java.io.*; 
import java.net.*;
import java.util.*;

import usermanager.UserManager;

import cl.puc.dds.appmgr.external.ICommunicationMgr;
import cl.puc.dds.appmgr.resources.AMMessage;
import cl.puc.dds.appmgr.resources.Application;

public class Communication implements Runnable, ICommunicationMgr
{
	private int id;
	private ArrayList<Integer> nodos;
	private ServerSocket welcomeSocket;
	private boolean envioDeObjeto = false; 
	private Application application;
	
	

	public Communication(int id) throws Exception
	{
		nodos = new ArrayList<Integer>();
		this.id = id;	
		welcomeSocket = new ServerSocket(this.id);
	}
		

	//Metodo que retorna true si logra conectarse a la red y false si no es posible
	public boolean connectToSession() throws Exception
	{
		boolean flag = false;
		//Buscamos la senal de broadcast en los 20 puertos que soporta el sistema
		System.out.println("Buscando se�al de broadcast");
		for(int i=6000 ; i<=6020 ;i++)
		{			
			if(i!=id)
			{
				System.out.println("Buscando en puerto: "+i);
				if(testPort("0",i))
				{
					flag=true;
					break;
				}
			}
		}
		if(flag)
			System.out.println("Nodo "+id+" te has conectado exitosamente a la red");
		else
		{
			nodos.add(id);
			System.out.println("Eres el primero en la red");
		}
		return flag;
	}

	//M�todo que env�a la actual lista (actualizada) a todos los nodos que estan en la lista
	public void updateEveryonesList() throws Exception
	{
		Iterator<Integer> i = nodos.iterator();
		while(i.hasNext())
		{
			//Como enviamos la lista debe ser un mensaje de caracter update --> 3
			String mensaje = "3_"+listaNodosToString();
			int next = i.next();
			sendMessage(mensaje,next);
		}
	}

	//M�todo que recibe la lista de nodos separados por comas, limpia la actual lista y luego la actualiza con la nueva info
	public void updateList(String listaNodos)
	{
		nodos.clear();
		getListaNodos(listaNodos);
	}

	//M�todo que toma la lista de nodos (atributo) y lo transforma en un string en donde
	//los id de los nodos est�n separados por comas.
	public String listaNodosToString()
	{
		String listString = "";

		for (int s : nodos)
		{
			listString += Integer.toString(s) + ",";
		}

		return listString;
	}

	//M�todo que env�a mensajes. Recibe como input el string del mensaje que se desea enviar y el id del nodo
	//al que se desea mandar el mensaje.
	public void sendMessage(String Message, int idDestino) throws Exception
	{
		//Si el nodo de destino esta dentro de la lista de nodos...
		if(nodos.contains(idDestino)||Message.substring(0, 1).compareTo("1") == 0)
		{
			try
			{
				Socket clientSocket = new Socket("127.0.0.1", idDestino);

				DataOutputStream outToServer = 
						new DataOutputStream(clientSocket.getOutputStream());

				outToServer.writeBytes(id+"_"+Message);		

				clientSocket.close(); 
			}
			catch(ConnectException e)
			{
				System.out.println("No fue posible enviar el mensaje");
			}
		}
		else
			System.out.println("El nodo "+idDestino+" no est� conectado");
	}
	
	//Manda el objeto a todos los de la lista
	public void sendToAll(Object object) throws Exception
	{
		for(int n : nodos)
		{
			sendObject(object, n);
		}
	}
	//M�todo que env�a objetos. Recibe como input el objeto que se desea enviar y el id del nodo
	//al que se desea mandar.
	public void sendObject(Object object, int idDestino) throws Exception
	{
		
		sendMessage("4", idDestino);
		//Si el nodo de destino esta dentro de la lista de nodos...
		if(nodos.contains(idDestino))
		{
			try
			{
				Socket clientSocket = new Socket("127.0.0.1", idDestino);

				ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

				outToServer.writeObject(object);
				outToServer.flush();

				clientSocket.close(); 
			}
			catch(ConnectException e)
			{
				System.out.println("No fue posible enviar el objeto");
			}
		}
		else
			System.out.println("El nodo "+idDestino+" no est� conectado");
	}

	//M�todo que prueba si el puerto idDestino est� activo. Se le env�a un mensaje de caracter Solicitar Llegada --> 0
	//Retorna true si existe un servidor en el puerto especificado
	public boolean testPort(String Message, int idDestino) throws Exception
	{
		try
		{
			Socket clientSocket = new Socket("127.0.0.1", idDestino);

			DataOutputStream outToServer = 
					new DataOutputStream(clientSocket.getOutputStream());

			outToServer.writeBytes(id+"_"+Message);		

			clientSocket.close();
			return true;
		}
		catch(ConnectException e)
		{
			return false;
		}
	}

	//Recibe un string de nodos y los transforma en ArrayList
	public void getListaNodos(String listaString)
	{
		String[] split = listaString.split(",");
		for(int i=0; i<split.length;i++)
		{
			nodos.add(Integer.parseInt(split[i]));
		}
	}

	public ArrayList<Integer> getNodos() {
		return nodos;
	}

	//M�todo para desconectar el nodo de la red. Elimina el id del nodo de la lista y luego
	//actualiza las listas de todos. Finalmente termina el programa.
	public void desconection() throws Exception
	{
		nodos.remove(id);
		updateEveryonesList();
		System.exit(0);
	}

	//M�todo principal de caracter runnable. En el main al hacer "new Thread(c).start()" se abre
	//un nuevo thread que corre �nicamente este m�todo. Lo que hace es quedarse en un ciclo infinito
	//con un servidor levantado que est� constantemente escuchando. Luego investiga el mensaje recibido
	//de acuerdo al mensaje que es: 0,1,2 o 3. (info en la documentacion)
	public void run()
	{
		try
		{
			String clientSentence;
			while(true) { 
				welcomeSocket.setSoTimeout(0);
				Socket connectionSocket = welcomeSocket.accept();
				
				if(!envioDeObjeto){
					BufferedReader inFromClient = 
							new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));				
					clientSentence = inFromClient.readLine();
	
					try
					{
						if(Integer.parseInt(clientSentence.substring(0,4))!=id)
						{
							//[Solicitud llegada] Llegada de un nuevo Nodo, debemos responder su solicitud y enviar la actual lista de nodos
							if(clientSentence.substring(5, 6).compareTo("0") == 0)
							{	
								int nuevoID = Integer.parseInt(clientSentence.substring(0,4));
								sendMessage("1"+"_"+listaNodosToString(), nuevoID); //1 = Llegada Aceptada
								System.out.println("Se ha agregado el nodo "+nuevoID+" a la red");
							}
							//[Llegada aceptada] Me responden la solicitud de llegada
							if(clientSentence.substring(5, 6).compareTo("1") == 0)
							{	
								System.out.println("Te has unido a "+clientSentence.substring(0,4));
								getListaNodos(clientSentence.substring(7));
								System.out.println("Agregando nodo: "+id);
								nodos.add(id);
								System.out.println("Tama�o lista nodos: "+nodos.size());
								updateEveryonesList();
							}
							//[Mensaje normal] Lo imprime en consola
							else if(clientSentence.substring(5, 6).compareTo("2") == 0)
							{	
								System.out.println("From "+clientSentence.substring(0, 4)+": "+clientSentence.substring(7));
							}
							//[Update de la lista] Invoca el metodo pertinente
							else if(clientSentence.substring(5, 6).compareTo("3") == 0)
							{	
								updateList(clientSentence.substring(7));
							}
							//primeramente para enviar objeto
							else if(clientSentence.substring(5, 6).compareTo("4") == 0)
							{	
								envioDeObjeto = true;
							}
						}
					}
					catch(IllegalArgumentException e)
					{
						System.out.println("Error en el formato del mensaje");
					}
				}
				else{
					//Recibimos objeto
					envioDeObjeto = false;
					try
					{
						ObjectInputStream oi = new ObjectInputStream(connectionSocket.getInputStream());
						Object objetoRecibido = oi.readObject();
						
						if(objetoRecibido instanceof UMMessage)
						{
							UserManager.getInstance().recieveMessage((UMMessage) objetoRecibido);
						} else if (objetoRecibido instanceof AMMessage) {
							this.application.recieveMesagge((AMMessage)objetoRecibido);
						}
	
					}
					catch(IOException ioe)
					{
	
					}
				}
			} 
		}
		catch (Exception e) 
		{
			System.out.println("Error al recibir el mensaje"+e);
		}
	}
/*
	public Object getObject()
	{
		return objetoRecibido;
	}
	*/
	public void setApp(Application a)
	{
		this.application = a;
	}
	
	public ArrayList<Object> requestDevicesOnline() {
		return null;
	}

	
	public boolean isDeviceOnline(String id) {
		return false;
	}
}