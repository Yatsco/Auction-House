## Auction House

## Notes
Ensure you have zulu11.43.55-ca-fx-jdk11.0.9.1-win_x64 JDK installed on the machines you will use and that your JAVA_HOME and PATH are set accordingly.

## Running the program
Step 1: Start the Bank on machine 1
1. locate where the bank jar and run with the following command `java -jar proto4.jar 8000`
2. Ensure you see "listening on port 8000" in the console

Step 2: Starting an Auction House
1. locate where the House jar and run with the following command `java -jar proto4.jar 8001 <bank public ip>`
2. Ensure you see "listening on port 8001" in the console.

Step 3: Start an agent
1. Locate the Agent Jar and run `java -jar proto4.jar 8002 <agent name no spaces> <agent balance numerical>`
2. A window will pop up make sure you click the following buttons [Connect Bank, whoami, Get Houses List, Get House Server Location, Connect House].
3. There are a variety of more buttons to help find an house to connect to or items you want to bid on we can click
[Get Items List] to view items to bid on.
4. enter an items id to bid on and hit [Get Item Info] to see the items status "Bad Air is AVAILABLE." and the current items bid price.
5. if the high bid agent is "none" this is the starting bid value the agents bid will be autofilled to this value and one can hit [Make bid].
6. after 30s of a bid not being outbid the agent with the highest bid will have won the item in our case Steve won Bad Air.

## Known Issues
:warning: Error handling is lacking on the start up. Please make sure you enter the
`java -jar` arguments exactly. Meaning, of course, that you replace the values inside 
< >'s with the appropriate values.

:warning: I have seen occasional issues with the displayed 'Available Balance' being incorrect. 
I believe this is due to thread timing. Click `whoami` to get the most recent values if they seem wrong. 
You may have to click it twice to get to the most recent update from the Bank.

:warning: Agent Disconnect is implemented sort of jankily. If you have an open bid 
then you have to wait 35 or so seconds for the process of bidding to complete 
before your agent will close. Please do not click anything else in that agent's 
GUI during that time. The agent will close after the hold time. You can confirm that 
your agent won the bid by starting a new agent and viewing the item info. 

:warning: House Disconnect is only partially implemented. Click the `Disconnect House` 
button will lock all of the house's items unavailable for bidding and prevent 
the creation of new items. It does not close the house and any existing bids will 
run to completion.

:warning: When an auction house closes, it is not removed from the list of auction houses. So, if you start a new House on the same server, then you will now be able to accesss both the closed and the new house.


