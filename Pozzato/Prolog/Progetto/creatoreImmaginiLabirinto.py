# -*- coding: utf-8 -*-
"""
Created on Thu Nov 12 11:15:31 2020

@author: Paolo Sanfilippo
"""
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
import matplotlib
import numpy as np


cmap = matplotlib.colors.LinearSegmentedColormap.from_list("", ["white","green","red","black"])

maze = []
numRighe=0
numColonne=0
iniziale=()
finale=()
occupate=[]

pathLabirinto= input("Inserisci il path del labirinto: ") 
with open(pathLabirinto, 'r') as file:
    for line in file:
        if line.split('(')[0]=='num_colonne':
            numColonne=int(line.split('(')[1].split(')')[0])
        elif line.split('(')[0]=='num_righe':
            numRighe=int(line.split('(')[1].split(')')[0])
        elif line.split('(')[0]=='occupata':
            numx=int(line.split(',')[0].split('(')[2])-1
            numy=int(line.split(',')[1].split(')')[0])-1
            occupate.append((numx,numy))
        elif line.split('(')[0]=='iniziale':
            numx=int(line.split(',')[0].split('(')[2])-1
            numy=int(line.split(',')[1].split(')')[0])-1
            iniziale=(numx,numy)
        elif line.split('(')[0]=='finale':
            numx=int(line.split(',')[0].split('(')[2])-1
            numy=int(line.split(',')[1].split(')')[0])-1
            finale=(numx,numy)
            
maze=np.zeros((numRighe,numColonne))
if finale[0]<numRighe and finale[1]<numColonne:
    maze[finale[0],finale[1]]=0.6    
maze[iniziale[0],iniziale[1]]=0.3

for occupata in occupate:
    maze[occupata[0],occupata[1]]=1
        
plt.pcolor(maze,edgecolors ='black',cmap=cmap)
plt.axes().set_aspect('equal')
plt.xticks([])
plt.yticks([])
plt.axes().invert_yaxis() 
nameImage=pathLabirinto.split('.')[0]+'.png'
plt.savefig(nameImage)
plt.show()
