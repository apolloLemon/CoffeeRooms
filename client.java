/* Application cliente du serveur servTexte1, syntaxe d’appel java cliTexte1 s p c, elle envoie la
chaîne de caractères c (terminée par un newline) à la machine s sur le port p et elle reçoit la
longueur de c sous la forme d’un entier */
import java.net.*;
import java.io.*;
import java.util.*;

public class client {
	static class Sender extends Thread {
		Scanner scn = new Scanner(System.in);
		PrintWriter send;
		Sender(PrintWriter send){
			this.send = send;
		}

		public void run(){
			while(true){
				String msg = scn.nextLine();
				try {
					send.println(msg);
					if(msg.equals("logout")) {
						//send.close();
						return;
					}
				} catch (Exception e){
					System.out.println("Sender PB");
					return;
				}
			}
		}
	}
	static class Reciever extends Thread {
		BufferedReader recieve;
		Reciever(BufferedReader recieve){
			this.recieve=recieve;
		}

		public void run(){
			while(true){
				try {
					String msg = recieve.readLine();
					if(msg.equals("logout")) {
						recieve.close();
						return;
					}
					/*
					*/
					System.out.println(msg);
				} catch (IOException e){
					System.out.println("Reciever PB: "+e);
					return;
				}
			}
		}
	}


	public static void main(String[] argu) {
	
		Socket so;
		Reciever entree;
		Sender sortie;
		String s; // le serveur
		int p; // le port de connexion
		int l; // et sa longueur reçue
		
		if (argu.length == 2) { // on récupère les paramètres
			s=argu[0];
			p=Integer.parseInt(argu[1]);
			
			try{// on connecte un socket
				so = new Socket(s, p);
				sortie = new Sender		(new PrintWriter(so.getOutputStream(), true));
				entree = new Reciever	(new BufferedReader(new InputStreamReader(so.getInputStream())));
				
				entree.start();
				sortie.start();

				try {
					sortie.join();
					entree.join();
					//entree.stop(); //depreciated
				} catch (Exception e) {
				}
				//System.out.println("Closing reciever");
				//entree.recieve.close();
				//System.out.println("Closing sender");
				//sortie.send.close();
				//System.out.println("Closing socket");
				so.close();
				System.out.println("Closed");

			} catch(UnknownHostException e) { 
				System.out.println(e);
			} catch (IOException e) {
				System.out.println("Aucun serveur n’est rattaché au port ");
			}
		} else {
			System.out.println("syntaxe d’appel java client serveur port\n");
		} 
	} 
}