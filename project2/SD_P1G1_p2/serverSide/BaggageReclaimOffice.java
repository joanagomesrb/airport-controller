package serverSide;

import clientSide.*;

/**
 * Implements the {@link serverSide.BaggageReclaimOffice} In which {@link clientSide.Passenger}s with missed
 * bags come to post their complaint.
 */

public class BaggageReclaimOffice {

	/**
	 * Instance fo General repository of Information.
	 */
	private GenInfoRepoStub rep;

	/**
	 * Initiates the {@link BaggageReclaimOffice}.
	 * @param rep {@link GenInfoRepoStub}.
	 */
	public BaggageReclaimOffice(GenInfoRepoStub rep){
		this.rep = rep;
	}

	/**
	 * {@link clientSide.Passenger}s with missed report them here.
	 * <p>
	 * This method increments the total number of missing {@link clientSide.Bag}s
	 * @param i number of bags lost.
	 * @param passengerID {@link clientSide.Passenger} identification.
	 */
	public void reportMissingBags(int i, int passengerID) {
		rep.passengerState(passengerID, PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		rep.missingBags(i, passengerID);
	}

}