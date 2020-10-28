package entities;

/**
 * Implementation of Bag. Represents the passengers' luggage.
 */

public class Bag extends Thread{
    
  /**
   * Bag's destination
   */
  private char destination;
  /**
   * Bag's identification
   */
  private int id;
  /**
   * Bag's flight number
   */
  private int flightNR;

  /**
   * Creates a Bag with identification, destination, and number of flight.
   * @param destination destination of Bag: 'T' - in transit, 'H' - final destination.
   * @param id identification of the Bag. Connects a Bag with the owner/{@link Passenger}.
   * @param flight number in which this Bag corresponds.
   */
  public Bag (char destination, int id, int flight){
      this.destination = destination;
      this.id = id;
      this.flightNR = flight;
  }


  /** 
   * Gets this bag's destination
   * @return bag's destination
   */
  public char getDestination() {
    return this.destination;
  }
  
  /** 
   * Gets this bag's identitification
   * @return bag's identification
   */
  public int getID() {
    return this.id;
  }

  /** 
   * Gets this bag's flight number
   * @return bag's flight number
   */
  public int getFlightNR() {
    return this.flightNR;
  }

}