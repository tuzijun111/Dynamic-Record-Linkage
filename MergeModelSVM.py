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
from sklearn import svm

start = time.time()

with open('/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt', 'w') as writer:
    writer.write("{'Intra': [],\n 'Inter': [],\n 'Size1': [],\n 'Size2': [],\n 'change': []}")


#putting three kinds of feature values in a dictionary
file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r" )  
content = file.read() 
file_add1 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Intra.txt","r")  

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
file_add2 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Inter.txt","r")
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
file_add3 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/change.txt","r")
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
file_add4 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size1.txt","r")
content_add4 = file_add4.read()
pos4 = content.find( "'Size1': [")
if pos4 != -1:
        content4 = content[:pos4+10] + content_add4 + content[pos4+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "w" ) 
        file.write( content4 )
        file.close()  
        file_add4.close()

file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r" )  
content = file.read() 
file_add5 = open("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size2.txt","r")
content_add5 = file_add5.read()
pos5 = content.find( "'Size2': [")
if pos5 != -1:
        content5 = content[:pos5+10] + content_add5 + content[pos5+10:]
        file = open( "/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "w" ) 
        file.write( content5 )
        file.close()  
        file_add5.close()

#Start running logistic regression model

file = open("/Users/binbingu/Documents/Codes/Python/MLmodel/RecordTrain.txt", "r")
contents = file.read()
dictionary = ast.literal_eval(contents)

file.close()

# print(type(dictionary))
# print(dictionary)

candidates = dictionary

df = pd.DataFrame(candidates,columns= ['Intra', 'Inter', 'Size1','Size2','change'])
print (len(df))

X = df[['Intra', 'Inter', 'Size1', 'Size2']]
y = df['change']
print("the number of 1 is:", np.count_nonzero(y==1))

X_train,X_test,y_train,y_test = train_test_split(X,y,test_size=0.2,random_state=0)       #train is based on 75% of the dataset, test is based on 25% of dataset

# logistic_regression= LogisticRegression()
# logistic_regression.fit(X_train,y_train)

model1= svm.SVC(probability=True)
model1.fit(X_train,y_train)

print(len(X_test))

# y_pred=logistic_regression.predict(X_test)
# y_prob=logistic_regression.predict_proba(X_test)

y_pred= model1.predict(X_test)
y_prob=model1.predict_proba(X_test)

# print(np.array(X_test)[:,1])
# print(y_pred)
# print(np.array(y_test))
# print(y_prob)


for prediction, label, probablility, intra1, inter1, size1, size2 in zip(y_pred, y_test, y_prob, np.array(X_test)[:,0], np.array(X_test)[:,1], np.array(X_test)[:,2], np.array(X_test)[:,3]):
  #if (prediction != label):
  if (prediction != label) & (label == 1):
    print('prediction', prediction, 'label',label, 'The probability ', probablility, intra1, inter1, size1, size2) 
#print(logistic_regression.predict_proba(X_test))

end = time.time()
print ("Running time = ", end - start)
print('Accuracy: ',metrics.accuracy_score(y_test, y_pred))

# performance plot

confusion_matrix = pd.crosstab(y_test, y_pred, rownames=['Actual'], colnames=['Predicted'])
sn.heatmap(confusion_matrix, annot=True)
plt.show()



# # prediction for new data 
# file = open("new.txt", "r")
# contents = file.read()
# new_candidates = ast.literal_eval(contents)
 
# df2 = pd.DataFrame(new_candidates,columns= ['gmat', 'gpa','work_experience'])
# y_pred=logistic_regression.predict(df2)

# print (df2)
# print (y_pred)

# #save model to string using pickle
# import pickle
# saved_model = pickle.dumps(logistic_regression)
# logistic_from_pickle = pickle.loads(saved_model)
# print(logistic_from_pickle.predict(X_test))

# # #print the probablility of a label 0
# # print(logistic_from_pickle.predict_proba(X_test)[:,0])

# #print the probablility of a label 1
# print(logistic_from_pickle.predict_proba(X_test)[:,1])

# #pickled model as a file using joblib

import joblib
#save the model as a pickle in a file
joblib.dump(model1, '/Users/binbingu/Documents/Codes/Python/MLmodel/SVMlogistic.pkl')


# #load the model from the file
# logistic_from_joblib = joblib.load('logistic.pkl')
# #use the loaded model to make predictions
# print(logistic_from_joblib.predict(X_test))
# #print(logistic_from_pickle.predict_proba(X_test))

#print(model1.coef_)