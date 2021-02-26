#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jan 24 11:30:10 2020

@author: student
"""
import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
 
import sys
sys.path.append('/home/student/Desktop/ProjectData')

from S01_DataRetrival_toHadoop import *

dcthObj = ZomatoEtl()

class ZomatoAnalysis:
    
    def regAlgo(self):
        
        dfZomatoRating = pd.DataFrame()
        dfZomatoRating = pd.read_sql("select restocost,restolocality,restocuisins,restouserrating from zomatoRating", dcthObj.createConn())
        
        dfZomatoRating['restocuisins'] = dfZomatoRating['restocuisins'].str.replace(',' , '') 
        dfZomatoRating['restocuisins'] = dfZomatoRating['restocuisins'].astype(str).apply(lambda x: ' '.join(sorted(x.split())))
        dfZomatoRating['restocuisins'].value_counts().head()
        
        dfZomatoRating['restolocality'] = dfZomatoRating['restolocality'].str.replace(',' , '') 
        dfZomatoRating['restolocality'] = dfZomatoRating['restolocality'].astype(str).apply(lambda x: ' '.join(sorted(x.split())))
        dfZomatoRating['restolocality'].value_counts().head()
        
        from sklearn.preprocessing import LabelEncoder
        T = LabelEncoder()                 
        dfZomatoRating['restolocality'] = T.fit_transform(dfZomatoRating['restolocality'])
        dfZomatoRating['restocuisins'] = T.fit_transform(dfZomatoRating['restocuisins'])
        
        X = dfZomatoRating.loc[:, :].values
        Y =  dfZomatoRating.iloc[:, -1].values
        
        X = X[:, :-1]
        from sklearn.model_selection import train_test_split
        X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size = 0.30, random_state = 0)
        
        from sklearn.ensemble import RandomForestRegressor
        regressor = RandomForestRegressor(n_estimators=5, random_state = 0)
        regressor.fit(X_train, Y_train)
        
        y_pred = regressor.predict(X_test)
        
        from sklearn.metrics import mean_squared_error, mean_absolute_error, accuracy_score
        Meansqrerror = np.sqrt(mean_squared_error(Y_test, y_pred))
        Meanabsoluteerror = mean_absolute_error(Y_test, y_pred)
        return [(Meansqrerror,Meanabsoluteerror)]  
    
    
    def classificationAlgo(self):
        
        dfZomatoData = pd.DataFrame()
        dfZomatoData = pd.read_sql("select restoCost, restouserrating, restocity  from zomatoCity", dcthObj.createConn())
        
        labelencoder=LabelEncoder()
        dfZomatoData.iloc[:,-1]=labelencoder.fit_transform(dfZomatoData.iloc[:,-1])
        
        X = dfZomatoData.iloc[:,[0,1]].values
        Y = dfZomatoData.iloc[:,-1].values
        
        X_train, X_test, Y_train, Y_test =  train_test_split(X,Y,test_size = 0.25, random_state= 0)
        sc_X = StandardScaler()
        
        from sklearn.decomposition import KernelPCA
        kpca=KernelPCA(n_components= 1,kernel='rbf')
        X_train=kpca.fit_transform(X_train)
        X_test=kpca.transform(X_test)
        X_train = sc_X.fit_transform(X_train)
        X_test = sc_X.transform(X_test)
        X_train.shape
        
        from sklearn.model_selection import cross_val_score
        from sklearn.ensemble import RandomForestClassifier
        from sklearn.neighbors import KNeighborsClassifier
        from sklearn.svm import SVC
        from sklearn.naive_bayes import GaussianNB
        
        dictModel = {'SVC':  SVC(kernel = 'lin', random_state = 0), 'Gaussian' : GaussianNB(), 'KNN': KNeighborsClassifier(n_neighbors=8, metric="minkowski", p=2), 'RandomForest': RandomForestClassifier(n_estimators=25, criterion = "entropy", random_state=0)}
        finalScores = 0.0
        finalModel = ""
        for i, j in dictModel.items():
            print(j)
            scores = np.average(cross_val_score(j, X_train, Y_train, cv=5))
            
            if scores >= finalScores:
                finalScores = scores
                finalModel = i
                
        return [(finalScores, finalModel)]
        
    
    def simpleRecommendationAlgo(self):
         
         dfZomatoData3 = pd.DataFrame()
         dfZomatoData3 = pd.read_sql("select restoname, restocuisins, restolocality, restouserrating, restovotes from zomatoData01", dcthObj.createConn())        
         
         C = dfZomatoData3['restouserrating'].mean()
         m = dfZomatoData3['restovotes'].quantile(0.90)
         
         q_restaurant = dfZomatoData3.copy().loc[dfZomatoData3['restovotes'] >= m]
         def weighted_rating(x, m=m, C=C):
             v = x['restovotes']
             R = x['restouserrating']
             # Calculating the score
             return np.round((v/(v+m) * R) + (m/(m+v) * C))
        
         q_restaurant['score'] = q_restaurant.apply(weighted_rating, axis=1)
         q_restaurant = q_restaurant.sort_values('score',ascending=False)
         return q_restaurant[['restoname','restocuisins', 'restolocality','restovotes', 'restouserrating', 'score']].head(10)         
         
    
    def restoRecommendationAlgo(self):
        from nltk.corpus import stopwords
        from nltk.tokenize import word_tokenize
        import re 
        
        city = "'New Delhi'"
        locality = "'Connaught Place'"
        restotype = 'Dining Hall'
        cuisine = 'NorthIndian Continental Italian'
        
        Querry = "select restoname, restocity, restolocality, restocuisins, restoestablish, restouserrating, restovotes from zomatoData01 where restocity = {0} and restolocality = {1} order by restouserrating desc, restovotes desc".format(city, locality)
        
        data_sample = pd.DataFrame()
        data_sample = pd.read_sql(Querry, dcthObj.createConn())
        print(data_sample)
        
        data_sample['Split']="X"
        data_sample
        
        for i in range(0,data_sample.index[-1]):
            split_data=re.split(r'[,]', data_sample['restocuisins'][i])
            for k,l in enumerate(split_data):
                split_data[k]=(split_data[k].replace(" ", ""))
            split_data=' '.join(split_data[:])
            data_sample['Split'].iloc[i]=split_data
        data_sample
        
        data_sample['Similarity'] = 0.0
        X = cuisine
        
        X_list = set(word_tokenize(X))  
        for i in range(0, data_sample.index[-1]):
            Y = data_sample['Split'][i]
            Y_list = set(word_tokenize(Y))
            
            l1 =[];l2 =[] 
            rvector = X_list.union(Y_list)
            
            for w in rvector: 
                if w in X_list: l1.append(1) # create a vector 
                else: l1.append(0) 
                if w in Y_list: l2.append(1) 
                else: l2.append(0) 
            c = 0
            
            for l in range(len(rvector)): 
                c+= l1[l]*l2[l] 
            cosine = c / float((sum(l1)*sum(l2))**0.5)     
    
            data_sample['Similarity'].iloc[i] = float(np.round(cosine, 2)) 
        
        otp = data_sample.sort_values('Similarity', ascending=False)
        return otp
    
    def kMeansAlgo(self):
        import numpy as np
        import matplotlib.pyplot as plt 
        import pandas as pd
        import seaborn as sns

        city = "'New Delhi'"
        
        Querry = "select restoLongitude, restoLatitude, restoCity, restouserrating, restovotes from zomatoData01 where restocity = {0} ".format(city)
        
        data = pd.DataFrame()
        data = pd.read_sql(Querry, dcthObj.createConn())        
        print(data.columns)
        
        from sklearn.cluster import KMeans
        kmeans = KMeans(n_clusters=7, random_state=0).fit(data[['restolongitude', 'restolatitude']])
        data['pos'] = kmeans.labels_
        
        pop_local = data.groupby('pos')['restolongitude', 'restolatitude', 'restouserrating'].agg({'restolongitude':np.mean, 'restolatitude':np.mean, 'restouserrating':np.median}).reset_index()
       
        get_ipython().run_line_magic('matplotlib', 'inline')
        with plt.style.context('bmh', after_reset=True):
           pal = sns.color_palette('Spectral', 7)
           plt.figure(figsize = (8,6))
           for i in range(7):
               ix = data.pos == i
               plt.scatter(data.loc[ix, 'restolatitude'], data.loc[ix, 'restolongitude'], color = pal[i], label = str(i))
               plt.text(pop_local.loc[i, 'restolatitude'], pop_local.loc[i, 'restolongitude'], str(i) + ': '+str(pop_local.loc[i, 'restouserrating'].round(2)), fontsize = 14, color = 'brown')
           plt.title('KMeans Citywise Median Rating')
           plt.legend()
           plt.show()
           
        votes_area = data.groupby('pos').agg({'restovotes': [np.sum, np.mean]})
        votes_area.columns = votes_area.columns.droplevel(0)
        votes_area.reset_index(inplace = True)
        plt.figure(figsize = (8,4))
        ax = plt.subplot(1,2,1)
        sns.barplot(x = 'pos', y = 'sum', data =votes_area, palette = sns.cubehelix_palette(n_colors = 7, start = 2.4, rot = .1), ax = ax)
        ax.set_title('Summation Votes')
        
        ax = plt.subplot(1,2,2)
        sns.barplot(x = 'pos', y = 'mean', data =votes_area, palette = sns.cubehelix_palette(n_colors = 7, start = 3, rot = .1), ax = ax)
        ax.set_title('Mean Votes')
        plt.show()
        
    
'''zomato_analysis = ZomatoAnalysis()

ResultofReg = zomato_analysis.regAlgo()
print(ResultofReg)

Resultofclass=zomato_analysis.classAglo()
print(Resultofclass)

ResultofCosine = zomato_analysis.cosineAlgo()
print(ResultofCosine)'''
