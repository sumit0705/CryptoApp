This is cryptocurrency tracking Android app that utilizes the CoinPaprika API <https://api.coinpaprika.com/v1/coins> to fetch and display a list of cryptocurrencies. 
The app adheres to the Kotlin, MVVM architecture using repository, Kotlin Coroutines and include additional features such as the ability to view coin details, search for coins, and implemented swipe-to-refresh functionality.

Features:
1. Fetching Coin Data
 - Fetched a list of cryptocurrencies from the CoinPaprika API <https://api.coinpaprika.com/v1/coins>.
 - Displayed the list of coins on the main screen using RecyclerView and Viewholder pattern.
2. Displaying Coin Details
 - Allowed users to click on a coin in the list to view its details.
 - Displayed detailed information about the selected coin, including its name, symbol, type etc.
3. Search Feature
 - Implementd a search feature using SearchView that allows users to search for coins by name.
 - The search filters the local list of coins, and no additional API calls were used for searching.
4. Swipe-to-Refresh feature
 - Added a swipe-to-refresh feature that allows users to manually refresh the list of coins.
 - When users swipe down on the coin list, the app refreshes the data from theCoinPaprika API.
5. Test case
 - Added test cases for ViewModel.
