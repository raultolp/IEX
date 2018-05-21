## +++ IEX Stock Exchange Game for Beginner Level Traders - Version 1.0 +++
###### (C) 2018 Renata Siimon, Helena Rebane, Raul Tölp. All rights reserved.

### GAME INTRO

This game is simulating stock portfolio management in US stock exchange with
real online prices and stock information. If you are already here, it means
your game client is connected to game server that does all the transactions
and provides all the information.

You have created a user that will have one stock portfolio where you can
buy or sell stocks:
   - There is limited amount of free money you can use.
   - There is limited amount of stocks you can buy.
   - Buy/Sell will be done with current MARKET price. No other transaction
     methods like LIMITED or STOP etc. orders are not implemented yet.
   - Current system does not count Bid/Ask price difference.
   - You can make your decisions based on information you can get from menu options.

From menu you can choose Game Top List that shows who has the best performance.

This game is for educational purposes only and no real money or nerves will be lost.

Be smart or lucky!

---------------------------------------------------------------------------

# IEX Stock Exchange Game for Beginners  
_Authors: Raul Tölp, Õie Renata Siimon and Helena Rebane_

This program has been written as a student project in the Object-Oriented Programming course
by students of University of Tartu, Estonia. The authors are thankful to their instructors
Märt Bakhoff and Simmo Saan for their guidance. 


## DESCRIPTION 

The program is a stock-exchange game using real stock data downloaded from IEX stock exchange (US) 
API [https://iextrading.com/developer/docs/#getting-started] at regular time intervals (10 sec), 
to simulate price data feed used in actual trading platforms. The players can ‘buy’ and ‘sell’ stocks 
and see information (such as price, PE, EPS, market cap, etc.) about them, as well as information on 
the respective companies (sector, description and news). 

Users’ portfolios contain stocks they currently hold, with their current price, volume (number of 
stocks held), total value, unrealized profit (increase in value of current positions) and realized profit 
(from stocks already sold). The user can also view his/her transactions report, containing information 
on all transactions s/he has made over time. 

A ranking of users is done during the game based on their current portfolio value, and at any point, 
a top list of users can be drawn up. The player who has attained the highest portfolio value can be 
declared the winner of the game. 

## IMPLEMENTATION

The menu system of the program has been built up by using handlers. ***_/add something on IU, MyUtils, 
StaticData, managing users, admin?/_***

At the start of the program, Master Portfolio is initiated by performing a batch download from IEX API. 
This portfolio contains all stocks available for users in the game. It also serves as the source of 
all information on the stocks. When a user buys or sells stocks, the prices are taken from the 
Master Portfolio. When a user asks information about a stock (prices, financial ratios, market cap, etc.) , 
this information is also retrieved from the Master Portfolio. Also, the stock instances added to users’ 
portfolios are taken from there. This approach helps to retain control over the number of enquiries 
made to the API.

There are two threads in the program. One is used for downloading price data at regular time intervals 
from IEX API. In order to eliminate time lag and avoid making excessive enquiries, prices of all stocks 
are downloaded with just a single batch enquiry. The information received is then used for updating 
the prices in the Master Portfolio and all users’ portfolios, as well as users’ current position values, 
unrealized profits, portfolio total value etc. Between downloading the data, this thread sleeps for 
a certain period. The second thread deals with everything else going on in the program.

For saving/loading data on users and their portfolios, JSON data structure is used. When saving, data on 
all users contained in the user list of the game, with their respective portfolios, positions and 
transactions are converted into a single JSON object. This object is then turned into a String and saved. 
When loading, JSON is converted back into user, portfolio, position and transaction instances. 
This JSON string  can also be easily used for sending data between Client and Server _(to be implemented 
in next stage of the project)._  

In order to reduce the amount of data downloaded from web, general information on companies (CEO, sector,
 description, etc.), which is stable enough for the purpose of this game, is stored in a file. 
 When information about a company is enquired, this information is first searched from this file, and 
 only if not found from there, it is downloaded from the API (and then also added to this file).  

The classes related to stocks owned by the user are centered around the Portfolio class. The user’s 
portfolio is created together with the user (portfolio is a field in the User class). A portfolio
 includes a HashMap of stocks included in the user’s portfolio, as well as a HashMap of positions 
 in the stocks the user owns. Each position represents one stock in the portfolio and includes also 
 an ArrayList of transactions the user has made with this stock. The data in Position class (together with 
 data in the Portfolio class itself) are used when viewing the user’s portfolio, while the data in 
 Transaction class are used for generating the report on all transactions the user has made.

JavaFX has been used for designing the User Interface. ***_/still on progress .. tests made/_***


## IMPLEMENTING SERVER-CLIENT:

### MUUDATUSED KLASSIDES:

##### SERVER:

- Lisatud on kliendiga suhtlemiseks klassid ***Server, ThreadForClientCommands*** ja ***ThreadForDataUpdates***.

- Peaklassi Iu on üsna palju muudetud ja osa sellest on tõstetud klassi Server.

- Kõigis klassides on muudetud I/O. Täpsemalt on igale poole, kus varem kasutati skännerit ja 
väljaprintimist, lisatud tingimus, et kui kasutajaks on admin, siis prindib ja skännib, aga kui 
kasutajaks on klient, siis saadetakse väljund DataOutputStreami /loetakse sisend DataInputStreamist.
 
- Pisem muudatus: Klassi Portfolio alt on hindade update’imisega tegelevad meetodid tõstetud ümber 
klassi UpdatingPrices, et kogu update’imise loogika oleks ühes kohas koos.


##### CLIENT:

- Lisatud on Serveriga suhtlemisega tegelevad klassid ***Client, ReceivingFromServer*** ja 
***SendingUserInput***.

- Ülejäänud klassid (st Portfolio/Transaction/Position...) on võetud varasemast, et FX saaks kasutada
 sisendina sama struktuuriga andmeid mis varem. Alternatiivina saaks FX (kui see ikka tuleb) võtta 
 portfellide andmed otse JSON stringidest ja teha nendega mida iganes vaja (st mitte luua/uuendada 
 vahepeal Portfolio jm isendeid, vaid otse oma andmetabeleid). Mõnes neist klassidest on tehtud 
 väikseid muutusi (nt Portfoliost kustutatud Buy/Sell meetod ja ülejäänutest muid meetodeid, mida 
 kliendil vaja ei ole). 
 
- UpdatingPrices klassi (samuti vajalik vaid FX jaoks) on kliendi puhul rohkem muudetud, sest see 
peab tegelema Serverilt JSON stringina saadud MasterPortfolio hinna update’ide ja kasutaja portfelli 
update’ide põhjal mõlema portfelli uuendamisega. (Kasutaja portfelli update’imise osa on sealt hetkel 
veel puudu. Käsurea versiooni jaoks ei ole seda ka vaja, sest kasutaja saab serverilt info oma 
portfelli kohta väljatrükkidena niigi.) 



### SERVER-KLIENT LOOGIKA – LÕIMED jm:

##### SERVER
 Serveris luuakse hulk lõimesid. 
 
- Kõigepealt luuakse lõim ***DataCollector*** (UpdatingPrices isend), mis tegeleb teatud intervalli 
(nt 10 sek) tagant IEX APIst andmete allalaadimisega ja portfellide uuendamisega, et uusi turuhindu
 arvesse võtta. 
 
- Siis luuakse admini (serveri haldaja ja ühtlasi endiselt ka kasutaja, kellele kuulub MasterPortfolio)
 jaoks lõim ***adminThread*** ja antakse adminile tema käskude haldamiseks kasutada Iu isend masterHandler.
  Adminil on sama menüü nagu enne (menüüpunktide järjestust on veidi muudetud), v.a lisatud on 
  menüüpunkt “Accept Client Connections” (vastav klass (“AcceptClientConnections”) on lisatud ka 
  “actions” kausta ja vastav väli (“acceptConnections”) Iu-sse).  
  
- Seejärel hakatakse vastu võtma klientide ühendusi – sellega tegeleb lõim ***UserThreadFactory***. Iga 
kliendi jaoks luuakse kaks lõime:

b) ***ThreadForClientCommands*** kliendi käskude haldamiseks. Selles lõimes tuvastatakse kasutajanimi, 
luuakse kas uus kasutaja või tuvastatakse olemasolev, luuakse kliendile tema käskude haldamiseks 
Iu isend handler ning saadetakse talle JSONina tema portfell ja masterportfolio (hiljem 
ThreadForClientUpdates saadab kliendile vaid masterportfolio aktsiahindasid, mitte kogu infot). 
Seejärel hakkab see thread kliendi handleri kaudu antud käskusid haldama.


a) ***ThreadForDataUpdates*** kliendile regulaarsete portfolio ja hinna update’ide saatmiseks (saadetakse 
json stringidena). See lõim kontrollib 1 sek tagant, kas kasutaja portfell on muutunud (kas 
ostude/müükide tõttu või IEX-ist allalaetud hinnauuenduste tulemusel). Kasutaja portfelli muutuse 
kontrollimiseks on Portfoliosse lisatud väli “portfolioHasChanged” – kui see on “true”, siis 
saadetakse kasutajale korraga JSON stringina nii tema enda portfell kui ka masterPortfolio 
hinnauuendused ning muudetakse “portfolioHasChanged” tagasi false’iks. Märkus: Uuenduste saatmine 
kliendile ei ole käsurea versiooni jaoks tegelikult vajalik, sest selle puhul saab klient oma 
portfelli seisu teada serverilt saadava ShowUserPortfolio väljatrükina.

- Kuna mõlemad threadid kasutavad sama socketit, siis selleks, et nende vahel konflikte ei tekiks,
 on ühele antud suurem prioriteet.
 
- Kõigile klientidele loodud threadid pannakse listidesse, et neid oleks võimalik korraga sulgeda. 

- Kliendi ühenduse loomisel antakse kliendile clientId, et ThreadForDataUpdates saaks selle järgi 
(pärast seda, kui kliendile vastav User on loodud/olemasolev tuvastatud) kindlaks teha, millise 
kliendi isendiga ta seotud on. (Selleks on Iu-sse lisatud vastavad mapid “clientThreads” ja 
“clientIds”.) 

##### CLIENT: 
Klassis Client luuakse kaks lõime, millest üks ***(ReceivingFromServer)*** tegeleb serveri väljundi 
vastuvõtmisega ja teine ***(SendingUserInput)*** serverile sisendi saatmisega. Kogu andmevahetus käib 
ainult stringidega. Sisendi vastuvõtmisel kontrollitakse, kas tegu on hinnauuenduse stringiga (JSON 
string algab “{“-ga), menüü trükkimise stringiga (sel juhul trükib klient endale ise menüü, et 
säilitada selle viisakas vormistus), väljumist tähistava stringiga (“Quit...”) – sel juhul lõpetab 
lõim oma tegevuse, või muu stringiga. Vaid juhul kui tegu on muu stringiga, prindib klient selle 
endale välja nii nagu ta selle serverilt sai. 

//-------------------------------------

### TODOs: Server ega Client ei sulgu praegu korrektselt... 

Veel todo-sid:

- Lisama peaks kliendi ühenduste lõpetamise loogika, kui klient valib “Quit”, või kui admin valib “Quit”._
 
- Kui teeme ka FX-i, siis tuleks lisada kliendi UpdatingPrices /klassi kasutaja portfelli update’imise 
osa, mis sealt hetkel on veel puudu. (Käsurea versiooni jaoks ei ole seda ka vaja, sest kasutaja saab 
serverilt info oma portfelli kohta väljatrükkidena niigi.)
