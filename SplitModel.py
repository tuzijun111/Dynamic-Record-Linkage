import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn import metrics
import seaborn as sn
import matplotlib.pyplot as plt
import pandas as pd
import ast
import os  
import numpy as np
import time

start = time.time()

with open('/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt', 'w') as writer:
    writer.write("{'Intra': [],\n 'Inter': [],\n 'Size1': [],\n 'change': []}")

#delete the last character (i.e. ","), but it does not matter if the last character is a comma ","

# with open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Intra.txt", 'rb+') as filehandle:
#     filehandle.seek(-1, os.SEEK_END)
#     filehandle.truncate()

# with open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Inter.txt", 'rb+') as filehandle:
#     filehandle.seek(-1, os.SEEK_END)
#     filehandle.truncate()

# with open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/change.txt", 'rb+') as filehandle:
#     filehandle.seek(-1, os.SEEK_END)
#     filehandle.truncate()

#putting three kinds of feature values in a dictionary
file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r" )  
content = file.read() 
file_add1 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Intra_S.txt","r")  

content_add1 = file_add1.read()  
pos1 = content.find( "'Intra': [")
if pos1 != -1:
        content1 = content[:pos1+10] + content_add1 + content[pos1+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "w" ) 
        file.write( content1 )
        file.close()  
        file_add1.close()

file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r" )  
content = file.read() 
file_add2 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Inter_S.txt","r")
content_add2 = file_add2.read()
pos2 = content.find( "'Inter': [")
if pos2 != -1:
        content2 = content[:pos2+10] + content_add2 + content[pos2+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "w" ) 
        file.write( content2 )
        file.close()  
        file_add2.close()

file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r" )  
content = file.read() 
file_add3 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/change_S.txt","r")
content_add3 = file_add3.read()
pos3 = content.find( "'change': [")
if pos3 != -1:
        content3 = content[:pos3+11] + content_add3 + content[pos3+11:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "w" ) 
        file.write( content3 )
        file.close()  
        file_add3.close()

file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r" )  
content = file.read() 
file_add4 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size_S.txt","r")
content_add4 = file_add4.read()
pos4 = content.find( "'Size1': [")
if pos4 != -1:
        content4 = content[:pos4+10] + content_add4 + content[pos4+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "w" ) 
        file.write( content4 )
        file.close()  
        file_add4.close()


#Start running logistic regression model

file = open("/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r")
contents = file.read()
dictionary = ast.literal_eval(contents)

file.close()

# print(type(dictionary))
# print(dictionary)

candidates = dictionary

df = pd.DataFrame(candidates,columns= ['Intra', 'Inter', 'Size1', 'change'])
print (len(df))

X = df[['Intra', 'Inter', 'Size1']]
y = df['change']
print("the number of 1 is:", np.count_nonzero(y==1))

X_train,X_test,y_train,y_test = train_test_split(X,y,test_size=0.2,random_state=0)       #train is based on 75% of the dataset, test is based on 25% of dataset

logistic_regression= LogisticRegression()
logistic_regression.fit(X_train,y_train)

print(len(X_test))

y_pred=logistic_regression.predict(X_test)
y_prob=logistic_regression.predict_proba(X_test)

for prediction, label, probablility, intra1, inter1, size1 in zip(y_pred, y_test, y_prob, np.array(X_test)[:,0], np.array(X_test)[:,1], np.array(X_test)[:,2]):
  #if (prediction != label):
  if (prediction != label) & (label == 1):
    print('prediction', prediction, 'label',label, 'The probability ', probablility, intra1, inter1, size1) 

# performance plot
confusion_matrix = pd.crosstab(y_test, y_pred, rownames=['Actual'], colnames=['Predicted'])
sn.heatmap(confusion_matrix, annot=True)

print('Accuracy: ',metrics.accuracy_score(y_test, y_pred))
plt.show()


# # prediction for new data 
# file = open("new.txt", "r")
# contents = file.read()
# new_candidates = ast.literal_eval(contents)
 
# df2 = pd.DataFrame(new_candidates,columns= ['gmat', 'gpa','work_experience'])
# y_pred=logistic_regression.predict(df2)

# print (df2)
# print (y_pred)

#save model to string using pickle
# import pickle
# saved_model = pickle.dumps(logistic_regression)
# logistic_from_pickle = pickle.loads(saved_model)
# print(logistic_from_pickle.predict(X_test))

#pickled model as a file using joblib

import joblib
#save the model as a pickle in a file
joblib.dump(logistic_regression, '/Users/binbingu/Documents/Codes/Python/MLmodel/logisticSplit.pkl')

#load the model from the file
# logistic_from_joblib = joblib.load('logistic.pkl')
#use the loaded model to make predictions
# print(logistic_from_joblib.predict(X_test))
end = time.time()
print ("Running time = ", end - start)

print(logistic_regression.coef_)