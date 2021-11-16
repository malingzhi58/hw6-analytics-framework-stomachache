# Domain

Pokemon Analysis Framework

We will implement a Pokemon query and visualization platform, where Pokemons’ characters including moves, abilities, heights and weights will be present. There are more information we can visualize. It can show the number of pokemons in each type, such as flying, fire, water, and so on. Besides, it can show the top 10 pokemons with high attack or high defense.

For data source, we will use PokeAPI ( https://pokeapi.co/), which is a full RESTful API linked to an extensive database detailing everything about the Pokémon main game series, and requires no authentication. It returns Json through URLs. For example, to query Ditto’s (a Pokemon) data, we can simply enter https://pokeapi.co/api/v2/pokemon/ditto, which returns a JSON object including all Ditto related data.

Also there are many wrapper libraries using this API. Since we use TypeScript, we will import a Node Server-side with an auto caching library called Pokedex Promisev2 https://github.com/PokeAPI/pokedex-promise-v2.

# Generality vs specificity

For the dataplugin interface, it has getData, parseData, and showChart methods. For each dataplugin concrete class, all its methods are specific.

Framework can know which dataplugin to use and it calls this dataplugin to show the chart. It will register every dataplugin, asks the data, and uses React to show the chart.

In this way, if we want to add more dataplugs, we just need to implement more dataplugin classes, and add more components in the web page. This shows extensibility.

# Project structure

To utilize express library, we divide the code to two parts, one is client and the other is server. The server part takes charge of using apis to get data. The client part will decide which chart will be shown and call the server to fetch corresponding data.
![alt text](./pics/pic1.png)

# Plugin interfaces

Data plugins may include plugins to fetch different kinds of bar chart data.

Visualization plugins will use x,y-axis data to draw the graph. Here is an example shows the number of pokemons from different types.
![alt text](./pics/pic2.png)

# Reference:

https://pokeapi.co/api/v2/pokemon/charizard
https://covid19api.com/
https://polygon.io/docs/get_v1_marketstatus_upcoming_anchor
