#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jan 24 15:17:33 2020

@author: student
"""

import sys
sys.path.append('/home/student/Desktop/ProjectData')
from ProjectAlgo001_Loc_Cost_Regressor import *
from S01_DataRetrival_toHadoop import *

## Calling ETL Class
zomato_Obj = ZomatoEtl()
#zomato_Obj.dataTransformer()
zomato_Obj.createConn()
#zomato_Obj.dumpToHadoop()
#zomato_Obj.createTables()
#zomato_Obj.loadData()

## Calling Analysis Class
zomato_analysis = ZomatoAnalysis() 

ResultofReg = zomato_analysis.regAlgo()
print(ResultofReg)

Resultofclass = zomato_analysis.classificationAlgo()
print(Resultofclass)

ResultofSimpleRecomdation = zomato_analysis.simpleRecommendationAlgo()
print(ResultofSimpleRecomdation)

Result=zomato_analysis.restoRecommendationAlgo()
print(Result)

zomato_analysis.kMeansAlgo()


