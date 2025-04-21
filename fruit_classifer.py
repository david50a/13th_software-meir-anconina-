import pandas as pd
from sklearn.tree import DecisionTreeClassifier
data={'color':[1,0,1,0],'size':[7,4,6,3],'fruit':['apple','bananas','apple','bananas']}
df=pd.DataFrame(data)
x=df[['color','size']]
y=df['fruit']
model=DecisionTreeClassifier()
model.fit(x,y)
result=model.predict([[1,6]])
print(result)
