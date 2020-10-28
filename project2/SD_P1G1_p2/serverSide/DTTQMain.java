package serverSide;

import java.io.IOException;
import java.net.SocketTimeoutException;

import global.Global;
import clientSide.*;

/**
 * This type of data simulates a server-side solution to the Airport Rapsody' Problem that implements the
 * type 2 client-server model (server replication) with static launch of the shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class DTTQMain {

    /**
     * activity signaling
     */
    public static boolean waitConnection;

    public static void main(String[] args) throws IOException {

        /**
         * Represents the service to be provided
         */
        DepartureTermTransfQuay dttq;
        /**
         * Arrival Lounge Interface
         */
        DepartureTermTransfQuayInterface dttqInt;
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**
         * Service provider thread
         */
        DTTQProxy dttqProxy;
        /**
         * General Information Repository Stub
         */
        GenInfoRepoStub repoStub;

        /* estabelecimento do servico */
        // Creation of the listening channel and its association with the public address
        scon = new ServerCom(Global.departureTermTransfQuayStub_PORT);
        scon.start();

        repoStub = new GenInfoRepoStub(Global.genRepo_HOSTNAME, Global.genRepo_PORT);
        // service activation
        dttq = new DepartureTermTransfQuay(repoStub); // activação do serviço
        // activation of the interface with the service
        dttqInt = new DepartureTermTransfQuayInterface(dttq);
        System.out.println("The service has been established!!");
        System.out.println("The server is listening.");

        /* processamento de pedidos */
        waitConnection = true;
        while (waitConnection)
            try {
                // entry into listening process
                sconi = scon.accept();
                // launch of the service provider
                dttqProxy = new DTTQProxy(sconi, dttqInt);
                dttqProxy.start();
            } catch (SocketTimeoutException e) {
            }
        // termination of operations
        scon.end();

        System.out.println("The server has been disabled.");
    }

}