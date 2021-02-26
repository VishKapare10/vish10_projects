#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jan 23 14:42:53 2020
@author: student
"""

import requests
import pandas as pd
from pandas.io.json import json_normalize
from hdfs import *
from hdfs import InsecureClient
from hdfs3 import HDFileSystem
from pyhive import hive
import sasl

host_name = "localhost"
port = 10000
user = "hduser"
database = "default"
conn = hive.Connection(host = host_name, port = port, username=user, database = database, auth='NOSASL')
cur = conn.cursor() 

class ZomatoEtl:

   
    def dataTransformer(self):
        
        keyList = ['b6bc4fd166565a88c0861351bd31cd7e', 'e9523f398c0a4ea3afa12fdd4242bfb2', '883d77a39ba93be241da58810ea585fe']
        lstCityIds = [34,11,24,22,25,4,26,29,12,7,30,35,1,1,13,1,21,6,14,10,23,9,2,8,20,20,31,12,3,36,33,16,1,1,12,40,37,5,27,6,38,32,39,28]
        df = pd.DataFrame()
        df1 = pd.DataFrame()
        #df = pd.read_csv('/home/student/Desktop/24_RestaurantData.csv')
        df = pd.DataFrame(df)
        count = 0
        Key = keyList[0]
        k = 0
        
        for l in lstCityIds:
            for i in range(0, 200, 20):
                url ="https://developers.zomato.com/api/v2.1/search?entity_id={0}&entity_type=city&start={1}&count=19".format(l,i) 
                if __name__ == '__main__':
                    if count == 1000:
                        k = k + 1
                        Key = keyList[k]
                        count = 0
                    r = requests.get(url, headers={'user-key': Key})
                    count = count + 1
                    if r.ok:
                        data = r.json()
                        df = df.append(json_normalize(data['restaurants']))
                        print(i)
                        print(df.shape)
                        print(url)
        
        df1 = df.loc[:, ["restaurant.R.res_id", "restaurant.all_reviews_count", "restaurant.average_cost_for_two", "restaurant.cuisines",  "restaurant.establishment","restaurant.has_online_delivery", "restaurant.has_table_booking","restaurant.highlights","restaurant.location.city", "restaurant.location.city_id", "restaurant.location.latitude", "restaurant.location.locality","restaurant.location.locality_verbose","restaurant.location.longitude", "restaurant.name","restaurant.opentable_support", "restaurant.phone_numbers", "restaurant.price_range", "restaurant.user_rating.aggregate_rating", "restaurant.user_rating.rating_text","restaurant.user_rating.votes"]] 
                    
        df1.to_csv('/home/student/Desktop/26_RestaurantData.csv',index=False,sep=':')
        print(df.shape)        
    
        
    def dumpToHadoop(self):
       
        try:
            file1="/home/student/Desktop/26_RestaurantData.csv"
        except:
            print("File Not Found")
        hdfsclient=InsecureClient("http://localhost:50070",user="hduser")
        hdfs_path="/27_RestaurantData.csv"
        hdfsclient.upload(hdfs_path,file1)
    
    def createConn(self):
        host_name = "localhost"
        port = 10000
        user = "hduser"
        database = "default"
        conn = hive.Connection(host = host_name, port = port, username=user, database = database, auth='NOSASL')
        cur = conn.cursor()        
        return conn
        
    def createTables(self):
       # ZomatoEtl.createConn()
       
        createQuery = "create table IF NOT EXISTS zomato02(restoId int,reviewCount int,restoCost int,restoCuisins string,restoEstablish string, restoOnlineDel int,restoTableBooing int,restoHighlites string, restoCity string, restoCityId int, restoLatitude float,restoLocality string,restoLocalityVer string,restoLongitude float,restoName string,restoopentable int,restoPhoneNo string,restoPriceRange int,restoUserRating float, restoRatingText string, restoVotes int) row format delimited fields terminated by ':' stored as textfile tblproperties ('skip.header.line.count'='1')" 
        cur.execute(createQuery)
             
    def loadData(self):
        
        #ZomatoEtl.createConn()
       
        loadQuery = "load data inpath '/27_RestuarantData.csv' into table zomato01"
        cur.execute(loadQuery)
        
        zomatoTableQuery ="create table IF NOT EXISTS zomatoData01 as select restoID, reviewCount, restoCost, restoCuisins, regexp_replace(restoEstablish,'[^a-zA-Z 0-9]+','') as restoEstablish, restoOnlineDel,restoTableBooing,regexp_replace(restoHighlites,'[^a-zA-Z 0-9]+','') as restoHighlites, restoOnlineDel, restoCity, restoCityId, restoLatitude,restoLocality, restoLocalityVer, restoLongitude, restoName,restoopentable, restoPhoneNo, restoPriceRange,restoUserRating,restoRatingText,restoVotes from zomato02"
        cur.execute(zomatoTableQuery)
        
        zomatoRatings ="create table IF NOT EXISTS zomatoRating as select restocost,restolocality,restocuisins,restouserrating from zomatoData01"
        cur.execute(zomatoRatings) 
        
        zomatoCity = "create table zomatoCity as select restoCost, restouserrating, restocity  from zomatoData01"
        cur.execute(zomatoCity)

        zomatocuisines="create table zomatoCuisines as select restoId,restoCost, restoCuisins,restoLocalityVer,restoName,restoUserRating from zomatoData01;"
        cur.execute(zomatocuisines)
        
#zomato_Obj = ZomatoEtl()
#zomato_Obj.dataTransformer()
#zomato_Obj.createConn()
#zomato_Obj.dumpToHadoop()


