import sys

import yfinance as yf
# print("calling python function with parameters:")
# print(sys.argv[1])
company = yf.Ticker(sys.argv[1])
company_historical = company.history(start="2020-06-02", end="2021-06-07", interval="1d")
company_historical.loc[:,'symbol']= sys.argv[1]
newdf=company_historical[['symbol',"Open", 'Close', "High",'Low']]
newdf.to_csv("src/main/resources/tmp"+sys.argv[1]+".csv", sep=',')
print(newdf)
