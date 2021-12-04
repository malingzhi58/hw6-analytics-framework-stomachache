import requests
import sys
import pandas as pd
key = sys.argv[1]
company =sys.argv[2].upper()
api_result = requests.get('http://api.marketstack.com/v1/eod?access_key='+key+'&symbols='+company+'&date_from=2021-01-23&date_to=2021-12-03')

api_response = api_result.json()
df = pd.DataFrame(columns=['date','symbol',"open", 'close', "high",'low'])
for i in range(len(api_response['data'])):
    df.loc[i]=[api_response['data'][i]['date'][:10],
               api_response['data'][i]['symbol'].lower(),
               api_response['data'][i]['open'],
               api_response['data'][i]['close'],
               api_response['data'][i]['high'],
               api_response['data'][i]['low']
              ]
#     print(api_response['data'][i])
df=df.set_index('date')
df.to_csv("src/main/resources/tmp2"+company+".csv", sep=',')
print(df)
