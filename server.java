/*programme serveur qui écoute sur le port p (passé en paramètre) et qui renvoie la longueur
de la chaîne de caractères que lui envoie un client. La chaîne envoyée se termine par un newline.*/
import java.net.*;
import java.io.*;
import java.util.*;

public class server {
	static Vector<ClientHandler> clients = new Vector<>();
	static int counter=0;
	
	static class ClientHandler extends Thread {
		int num;
		Socket s;
		BufferedReader entree;
		DataOutputStream sortie;

		ClientHandler(int n,Socket s, BufferedReader in, DataOutputStream out){
			this.num=n;
			this.s=s;
			this.entree=in;
			this.sortie=out;
		}

		public void run() {
			while(!s.isClosed())
				try {

					String ch; // la chaîne reçue
					ch = entree.readLine(); // on lit ce qui arrive
					System.out.println(ch);
					for(ClientHandler oc : server.clients){
						if(oc.isAlive() && oc != this){
							System.out.println("sending "+ch);
							oc.sortie.writeUTF("client "+num+" :"+ch+"\n");
						}
					}
					
				} catch (IOException e) {
					System.out.println("problème\n"+e);
				}

		}

	}

	
	public static void main(String[] argu) {
		int p; // le port d’écoute
		ServerSocket ecoute;
		Socket so;



//		BufferedReader entree;
//		DataOutputStream sortie;
	
		if (argu.length == 1) {
		
			p=Integer.parseInt(argu[0]); // on récupère le port
			try {
				ecoute = new ServerSocket(p); // on crée le serveur
				System.out.println("serveur mis en place ");
				
				while (true) {// le serveur va attendre qu’une connexion arrive
					System.out.println("waiting for new Client ");
					so = ecoute.accept();
					System.out.println("new Client ");	
					ClientHandler client = new ClientHandler(counter++,so,
						new BufferedReader(new InputStreamReader(so.getInputStream())),
						new DataOutputStream (so.getOutputStream()));

					clients.add(client);
					client.start();
					client.sortie.writeUTF("connected\n");

					//remove dead threads
					for(ClientHandler c: clients)
						if(!c.isAlive())
							clients.remove(c);

				}
			} catch (Exception e){
				System.out.println("something went wrong");
			}
		
		} else { System.out.println("syntaxe d’appel java servTexte port\n"); } 
	} 
}