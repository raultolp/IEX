draft
# IEX Stock Exchange Game for Beginners  
_Authors: Raul Tölp, Õie Renata Siimon and Helena Rebane_

This program has been written as a student project in the Object-Oriented Programming course
by students of University of Tartu, Estonia. The authors are thankful to their instructors
Märt Bakhoff and Simmo Saan for their guidance. 


## DESCRIPTION 

The program is a stock-exchange game using real stock data downloaded from IEX stock exchange (US) 
API [https://iextrading.com/developer/docs/#getting-started](url) at regular time intervals (10 sec), 
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

JavaFX has been used for designing the User Interface. ***_/add something?/_***


