package sharedRegions;

import java.util.concurrent.locks.*;

import entities.*;

/**
 * A synchronization point for the {@link entities.BusDriver} where he blocks while waiting for
 * all the {@link entities.Passenger}s to leave the bus.
 */
public class DepartureTermTransfQuay{

    /**
     * A reentrant mutual exclusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */
    private final ReentrantLock rl;
    /**
     * Synchronization point where the {@link entities.Passenger}s wait for the {@link entities.BusDriver} to arrive.
     */
    private final Condition waitArrival;
    /**
     * Synchronization point where the {@link entities.BusDriver} waits for the {@link entities.Passenger}s to leave the bus.
     */
    private final Condition waitEmpty;
    /**
     * Count of {@link entities.Passenger}s inside the bus.
     */
    private int numPassengers = 0;
    /**
     * Instance fo General repository of Information.
     */
    private GenInfoRepo rep;


    /**
     * Instantiates the Departure Terminal Tranfer Quay.
     * @param rep {@link GenInfoRepo}.
     */
    public DepartureTermTransfQuay(GenInfoRepo rep) {
        rl = new ReentrantLock(true);
		waitEmpty = rl.newCondition();
        waitArrival = rl.newCondition();
        this.rep = rep;
    }

    /**
     * When the {@link entities.BusDriver} arrives at this shared region he stops and let the passengers get off the bus.
     * @param busPassengers Number of passengers inside the bus.
     */
    public void parkTheBusAndLetPassengerOff(int busPassengers) {
        rl.lock();
        try {
            numPassengers += busPassengers;
            rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
			waitArrival.signalAll();
            waitEmpty.await();
        } catch(Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }
        finally {
            rl.unlock();
        }
    }

    /**
     * {@link entities.Passenger}s leave the bus at this shared region.
     * @param passengerID {@link entities.Passenger}'s identification.
     */
    public void leaveTheBus(int passengerID) {
        rl.lock();
        try {
            rep.passengerState(passengerID, PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
            waitArrival.await();   
            numPassengers--;
            rep.leaveBus(passengerID);
            if(numPassengers == 0) {
                waitEmpty.signal();
            }
        } catch(Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }
        finally {
            rl.unlock();
        }
    }

    /**
     * This method tells the {@link GenInfoRepo} that the {@link entities.BusDriver} is driving forward during 50 miliseconds.
     */
    public void goToDepartureTerminal(){
        try {
            rep.busDriverState(BusDriverState.DRIVING_FORWARD); 
            Thread.sleep(50);
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }

    }
}