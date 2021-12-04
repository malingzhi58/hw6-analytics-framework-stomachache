import sys
import yfinance as yf
# import subprocess
# import sys
#
# def install(package):
#     subprocess.check_call([sys.executable, "-m", "pip", "install", package])
from pip._internal import main as pipmain
packname='yfinance'
pipmain(['install', packname])
# Example
# install('yfinance')
# print("calling python function with parameters:")
# print(sys.argv[1])
company = yf.Ticker(sys.argv[1])
company_historical = company.history(start="2020-06-02", end="2021-06-07", interval="1d")
company_historical.loc[:,'symbol']= sys.argv[1]
newdf=company_historical[['symbol',"Open", 'Close', "High",'Low']]
newdf.to_csv("src/main/resources/tmp"+sys.argv[1]+".csv", sep=',')
print(newdf)
