package serverSide;

import clientSide.*;
import global.Global;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * This datatype implements the Arrival Lounge shared memory region. In this
 * shared region, the Passengers decide on their course of action and the Porter
 * comes here to try to collect the passengers' bags.
 */
public class ArrivalLounge {

    /**
     * A reentrant mutual exclusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */
    protected final ReentrantLock rl;

    /**
     * Synchronization point where the {@link clientSide.Porter} waits for all {@link clientSide.Passenger}s to make their decision
     * before starting to collect {@link clientSide.Bag}s
     */
    protected final Condition planeHoldEmptyCV;

    /**
     * Current flight count
     */
    private int flightCount;
    /**
     * Total number of flights to be simulated
     */
    private int maxFlights;
    /**
     * Total number of {@link entities.Passenger}s
     */    
    private int numPassengers;
    /**
     * Count of the {@link entities.Passenger}s that have left the plane
     */
    private int passengerCount = 0;

    /**
     * Stack of {@link Bag}s which the {@link entities.Porter} will attempt to collect
     */
    private Stack<Bag> bags = new Stack<>();

    /**
     * General Information Repository {@link GenInfoRepo}
     */
    private GenInfoRepoStub rep;

    /**
     * Instantiates ArrivalLounge shared region
     * 
     * @param rep {@link GenInfoRepo}.
     */
    public ArrivalLounge(GenInfoRepoStub rep) {
        rl = new ReentrantLock(true);
        planeHoldEmptyCV = rl.newCondition();
        this.numPassengers = Global.NR_PASSENGERS;
        this.maxFlights = Global.MAX_FLIGHTS;
        this.rep = rep;
    }

    /**
     * This method updates internal flight count.
     * @param flight_number
     */
    public void setFlight(int flight_number) {
        flightCount = flight_number + 1;
    }

    /**
     * Passengers decide what to do based on their final destination and number of bags
     * @param flight_nr
     * @param passengerID
     * @param bags
     * @param finalDestination
     * @return char
     */
    public char whatShouldIDo(int flight_nr, int passengerID, Bag[] bags, boolean finalDestination) {
        rl.lock();
        
        try {    
            rep.passengerState(flight_nr, passengerID, PassengerState.AT_THE_DISEMBARKING_ZONE, finalDestination, bags.length);
            
            passengerCount++;
            if (passengerCount == numPassengers) {
                passengerCount = 0;
                planeHoldEmptyCV.signal();
            }

            if(bags.length == 0 && finalDestination) return 'a';

            for (int b = 0; b < bags.length; b++) {
                this.bags.push(bags[b]);
            }

            return finalDestination ? 'c' : 'b';
        } catch (NullPointerException e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
            System.exit(1);
            return 'z';
        } finally {
            rl.unlock();
        }
    }

    /**
     * This method checks if the porter stays resting or not.
     * <p>
     * Returns true in case of rest; false in case of wake up.
     * 
     * @return char
     */
    public char takeARest() {
        rl.lock();
        try {
            if (bags.empty() && flightCount == maxFlights)
                return 'E';
            
            rep.porterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
            planeHoldEmptyCV.await();

            if (bags.empty() && flightCount == maxFlights)
                return 'E';

            return 'W';

        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
            System.exit(1);
            return 'z';
        } finally {
            rl.unlock();
        }
    }
    
    /**
     * This method returns a Bag.
     * <p>
     * Returns null if there is not any bags to pick.
     * 
     * @return Bag
     */
    public Bag tryToCollectBag() {
        rl.lock();
        try {
            if (bags.empty()){
                return null;
            }
            else{
                rep.porterState(PorterState.AT_THE_PLANES_HOLD);
                return bags.pop();
            }

        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
            System.exit(1);
            return null;
        } finally {
            rl.unlock();
        }
    }
}