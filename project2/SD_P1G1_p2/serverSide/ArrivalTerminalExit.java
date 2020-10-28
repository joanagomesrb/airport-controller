package serverSide;

import java.util.concurrent.locks.*;

import clientSide.*;

/**
 * This datatype implements the ArrivalTerminalExit shared memory region.
 * <p>
 * In this shared region, the Passengers wait for each others lifecycle to be
 * over before moving on to the next flight simulation or concluding the program
 * execution
 */

public class ArrivalTerminalExit{

    /**
     * A reentrant mutual exclArrivalTerminalExitusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */
    private final ReentrantLock rl;

    /**
     * Synchronization point where the {@link entities.Passenger}s wait for each other
     * to complete their lifecycle before moving on to the next flight
     */
    private final Condition waitingEndCV;

    /**
     * Departure Terminal Entrance {@link DepartureTerminalEntrance}
     */
    private DepartureTerminalEntranceStub departureTerminalEntranceStub;

    /**
     * Total number of {@link entities.Passenger}s who finished their execution in the Arrival Terminal Exit
     */  
    private int passengers = 0;

    /**
     * Total number of {@link entities.Passenger}s who finished their execution in the Departure Terminal Entrance
     */  
    private int departurePassengers = 0;

    /**
     * Total number of {@link entities.Passenger}s
     */  
    private int numPassengers;
    
    /**
     * General Information Repository {@link GenInfoRepo}
     */
    private GenInfoRepoStub rep;

    // Create lock and conditions
    public ArrivalTerminalExit(int numPassengers, GenInfoRepoStub rep) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
        this.rep = rep;
    }

    /**
     * Instantiate {@link DepartureTerminalEntrance} 
     * @param departureTerminalEntranceStub departure terminal entrance stub
     */
    public void setDepartureTerminal(DepartureTerminalEntranceStub departureTerminalEntranceStub) {

        this.departureTerminalEntranceStub = departureTerminalEntranceStub;
    }

    /**
     * Signal {@link serverSide.DepartureTerminalEntrance} that all {@link clientSide.Passenger} have completed their lifecycle
     */
    public void signalCompletion() {
        rl.lock();
        try {
            passengers = 0;
            departurePassengers = 0;
            waitingEndCV.signalAll();
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } finally {
            rl.unlock();
        }

    }

    /**
     * Signal {@link serverSide.DepartureTerminalEntrance} that a {@link clientSide.Passenger} has entered the Arrival Terminal Exit
     */
    public void signalPassenger() {
        departurePassengers++;
    }


    /**
     * Passengers enter a lock state while waiting for every Passenger to finish their lifecycle
     * @param flight_number
     * @param passengerID
     * @param passengerState
     */
    public void goHome(int flight_number, int passengerID, PassengerState passengerState) {
        rl.lock();
        try {
            rep.passengerState(passengerID, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
            passengers++;
            departureTerminalEntranceStub.signalPassenger();
            if (passengers + departurePassengers == numPassengers) {
                passengers = 0;
                departurePassengers = 0;
                departureTerminalEntranceStub.signalCompletion();
                waitingEndCV.signalAll();
            } else {
                waitingEndCV.await();
            }
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } finally {
            rl.unlock();
        }
    }
}