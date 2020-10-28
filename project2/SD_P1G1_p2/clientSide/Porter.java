package clientSide;

import global.*;

import java.util.*;


/**
 * Entity Porter.
 */
public class Porter extends Thread {

    /**
     * {@link Bag}
     */
    Bag bag;
    /**
    * Boolean variable set to false to signal when the {@link Porter} has completed 
    * his lifecycle
     */
    private boolean rest = true;
    /*
    * Porter state
    * {@link PorterState}
    */
    PorterState state;
    /**
     * Arrival Lounge
     * {@link serverSide.ArrivalLounge}
     */
    private final ArrivalLoungeStub arrivalLoungeStub;
    /**
     * Temporary Storage Area
     * {@link serverSide.TempStorageArea}
     */
    private final TempStorageAreaStub tempStorageAreaStub;
    /**
     * Baggage Collection Point
     * {@link serverSide.BaggageCollectionPoint}
     */
    private final BaggageCollectionPointStub baggageCollectionPointStub;
    /**
     * General Information Repository {@link serverSide.GenInfoRepo}.
     */
    private GenInfoRepoStub repoStub;

    /**
     * Instantiates entity {@link Porter}
     * @param {@link serverSide.ArrivalLounge}
     * @param {@link serverSide.TempStorageArea}
     * @param {@link serverSide.BaggageCollectionPoint}
     */
    public Porter(ArrivalLoungeStub arrivalLoungeStub, TempStorageAreaStub tempStorageAreaStub,
            BaggageCollectionPointStub baggageCollectionPointStub, GenInfoRepoStub repoStub) {
        this.arrivalLoungeStub = arrivalLoungeStub;
        this.tempStorageAreaStub = tempStorageAreaStub;
        this.baggageCollectionPointStub = baggageCollectionPointStub;
        this.repoStub = repoStub;
    }

    /**
     * This method defines the life-cycle of the Porter.
     */
    @Override
    public void run() {
        while (rest) {
            char choice = arrivalLoungeStub.takeARest();
            repoStub.porterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
            if (choice == 'W') {
                bag = arrivalLoungeStub.tryToCollectBag();
            
                while (bag != null) {
                    Random r = new Random();
                    int answer = r.nextInt(Global.LOST_BAG_PERCENTAGE);
                    if(answer==9){ 
                        repoStub.lessBagsOnPlanesHold(bag);
                    }
                    else if (answer < 9) {
                        // if bag is in trasit
                        if (bag.getDestination() == 'T') {
                            tempStorageAreaStub.carryItToAppropriateStore(bag);
                        } else {
                            // bag is at final aeroport
                            baggageCollectionPointStub.carryItToAppropriateStore(bag);
                        }
                    }
                    bag = arrivalLoungeStub.tryToCollectBag();
                }
                baggageCollectionPointStub.noMoreBagsToCollect();
            } else if (choice == 'E') {
                rest = false;
            }
        }
    }

    /**
     * Gets {@link Porter} state
     * @return {@link PorterState}
     */
    public PorterState getPorterState() {
        return this.state;
    }

}