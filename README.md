# Airport Controller
Projects developed in the context of the curricular unit Distributed Systems at Universidade de Aveiro.

## Brief explanation of the problem

This assignments consisted in the implementation of an airport and the different activities it might happen there when passengers arrive from a flight.

There are eight main locations: the arrival lounge, the baggage collection point, the temporary storage area (for holding the luggage of passengers in transit), the baggage reclaim office, the terminal transfer quays, the arrival terminal exit and the departure terminal entrance.

There are three types of entities: the passengers, who terminate their voyage at the airport or are in transit, the porter, who unloads the the bags from a plane, when it lands, and carries them to the baggage collection point or to the temporary storage area, and the bus driver, who moves the passengers in transit between the arrival and the departure terminals.

K plane landings are assumed, each involving the arrival of N passengers. Each passenger carries 0 to M pieces of luggage in the plane hold. The bus, which moves the passengers between terminals, has a capacity of T seating places.

Activities are organized, for each plane landing, in the following way:

* the passengers walk from the arrival lounge to the baggage collection point, if their journey ends at this airport and have bags to collect; those without bags go directly to the arrival terminal exit and leave the airport; the remaining passengers, who are in transit, walk to the terminal transfer quay;

* after all the passengers have left the plane, the porter picks up the pieces of luggage, one by one, from the plane hold and carries them either to the baggage collection point, or to the temporary storage area, as they belong to local or in transit passengers, respectively;

* in the baggage collection point, the passengers wait for the arrival of their bags; upon taking possession of them, they walk to the arrival terminal exit and leave the airport; those with missed bags go first to the baggage reclaim office to post their complaint, before walking to the arrival terminal exit and leave the airport;

* on the terminal transfer quay, the passengers wait for the bus arrival, which will take them to the departure terminal for the next leg of the journey;

* the bus leaves the terminal transfer quay according to a predefined schedule, executing a circular path which has as another stop the terminal transfer quay of the departure terminal; however, if it happens that all seats are occupied prior to the predefined time to leave, the driver may depart sooner.

In the end of the day, a full report of the activities is issued. 

## Solutions

1. The first project is a pure implementation of the problem running in a single platform. It uses java’s reentrant locks for managing shared variables and concurrency. (explicit monitors) 

2. The second project is a distributed approach to the problem, based on message passing, running in multiple platforms. It is an client-server implementation type where active entities trade messages with passive entities. This solution tries to demonstrate a simple simulation of java’s RMI.

## Active entities life cycle

### Passenger life cycle
![Referee life cycle](https://github.com/joanagomesrb/airport-controller/blob/main/passenger_lifeCycle.png)
### Porter life cycle
![Referee life cycle](https://github.com/joanagomesrb/airport-controller/blob/main/porter_lifeCycle.png)
### Bus driver life cycle
![Referee life cycle](https://github.com/joanagomesrb/airport-controller/blob/main/busDriver_lifeCycle.png)

## Passive entities / Shared regions

* Arrival Lounge - where passengers arrive to;
* Baggage Collection Point - where passengers go to collect their bags
* Baggage Reclaim Office - where passengers go to reclaim about missing bags
* Temporary Storage Area - where bags are transfered to when this airport is not their final destination and, so, they're in transit to another destination
* Arrival Terminal Transfer Quay - where passengers wait for the bus to take them to another terminal
* Departure Terminal Transfer Quay - where the bus stops and leaves the passengers
* Arrival Terminal Exit - where passengers can leave the airport
* Departure Terminal Entrance - where passengers go to prepare for the next leg of their journey
* General Repository of Information - place where the visible internal state of the problem is stored. The visible internal state is defined by the set of variables whose value is printed in the logging file. Whenever an entity (porter, passenger, bus driver) executes an operation that changes the values of some of these variables, the fact must be reported so that a new line group is printed in the logging file.


## Requirements

These solutions were developed with Java 11.




