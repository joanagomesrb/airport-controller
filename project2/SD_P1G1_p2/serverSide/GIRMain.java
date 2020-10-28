package serverSide;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import global.*;

/**
 * This type of data simulates a server-side solution to the Airport Rapsody' Problem that implements the
 * type 2 client-server model (server replication) with static launch of the shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class GIRMain {

    /**
     * Activity sinalization
     */
    public static boolean waitConnection; 

    public static void main(String[] args) throws IOException{

        /**
         * General Information Repository - service to be provided
         */
        GenInfoRepo repo;
        /**
         * General Information Repository Interface
         */
        GenInfoRepoInterface repoInt;
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**
         * Service provider thread
         */  
        GIRProxy repoProxy;

        /* service establishment */
        scon = new ServerCom(Global.genRepo_PORT);
        scon.start();

        // creates new logger
		File logger = new File("logger.txt");
		if (logger.createNewFile()){
		}else{
			logger.delete();
			logger.createNewFile();
        }
        
        repo = new GenInfoRepo(logger);
        repoInt = new GenInfoRepoInterface(repo);
        System.out.println("The service has been established!");
        System.out.println("The server is listening.");

        /* request processing */
        waitConnection = true;
        while(waitConnection)
            try{   
                sconi = scon.accept ();                       
                repoProxy = new GIRProxy (sconi, repoInt);  
                repoProxy.start ();
            }
            catch (SocketTimeoutException e){  }
        scon.end ();                                         
        repo.finalReport();
        System.out.println("The server has been disabled.");
    }
}