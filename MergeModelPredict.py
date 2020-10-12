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

#Because we need to call python scripts in java programs, we must use the absolute path.
with open('/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt', 'w') as writer:     
    writer.write("{'Intra': [],\n 'Inter': [],\n 'Size1': [],\n 'Size2': []}")

#putting three kinds of feature values in a dictionary
file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "r" )  
content = file.read() 
file_add1 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Intra.txt","r")  

content_add1 = file_add1.read()  
pos1 = content.find( "'Intra': [")
if pos1 != -1:
        content1 = content[:pos1+10] + content_add1 + content[pos1+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "w" ) 
        file.write( content1 )
        file.close()  
        file_add1.close()

file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "r" )  
content = file.read() 
file_add2 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Inter.txt","r")
content_add2 = file_add2.read()
pos2 = content.find( "'Inter': [")
if pos2 != -1:
        content2 = content[:pos2+10] + content_add2 + content[pos2+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "w" ) 
        file.write( content2 )
        file.close()  
        file_add2.close()



file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "r" )  
content = file.read() 
file_add4 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Size1.txt","r")
content_add4 = file_add4.read()
pos4 = content.find( "'Size1': [")
if pos4 != -1:
        content4 = content[:pos4+10] + content_add4 + content[pos4+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "w" ) 
        file.write( content4 )
        file.close()  
        file_add4.close()

file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "r" )  
content = file.read() 
file_add5 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Size2.txt","r")
content_add5 = file_add5.read()
pos5 = content.find( "'Size2': [")
if pos5 != -1:
        content5 = content[:pos5+10] + content_add5 + content[pos5+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "w" ) 
        file.write( content5 )
        file.close()  
        file_add5.close()

#Start running logistic regression model


file = open("/Users/binbingu/Documents/Codes/Python/MLmodel/NewRecordTrain.txt", "r")
contents = file.read()
dictionary = ast.literal_eval(contents)
# print(type(dictionary))
# print(dictionary)

candidates = dictionary
df = pd.DataFrame(candidates,columns= ['Intra', 'Inter', 'Size1','Size2'])
print (len(df))

import joblib

#load the model from the file
logistic_from_joblib = joblib.load('/Users/binbingu/Documents/Codes/Python/MLmodel/logistic.pkl')
#use the loaded model to make predictions
y_pred = logistic_from_joblib.predict_proba(df)
#print(y_pred)
#print(logistic_from_pickle.predict_proba(X_test))

end = time.time()
print ("Running time for MergeModelPredict= ", end - start)
print(len(y_pred))


#write the prediction probability of lable "1" into a file by line
with open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/merge1output.txt",'w') as file1:
    for i in range(y_pred.shape[0]):
        print(i, y_pred[i][1], file=file1)     #end=","
        #print()
        