# -*- coding: utf-8 -*-
"""
Created on Thu Nov 12 18:25:59 2020

@author: Paolo Sanfilippo
"""
from pyswip import Prolog
from PyQt5.QtWidgets import QApplication,QFileDialog,QMessageBox
from PyQt5 import uic
import sys,os
import matplotlib.pyplot as plt
import matplotlib
import numpy as np
from PyQt5.QtGui import QPixmap

app =QApplication([])

win = uic.loadUi("GuiUi.ui")
maze=[]
cmap = matplotlib.colors.LinearSegmentedColormap.from_list("", ["white","green","yellow","red","black"])

prolog = Prolog()
prolog.consult("labirinti/azioniLabirinto.pl")

queryString=None  
mazeChoosed=False


def buildImageMaze(pathLabirinto):
    global maze
    numRighe=0
    numColonne=0
    iniziale=()
    finale=()
    occupate=[]
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
        maze[finale[0],finale[1]]=0.7   
    maze[iniziale[0],iniziale[1]]=0.3
    
    for occupata in occupate:
        maze[occupata[0],occupata[1]]=1
    plt.clf()       
    plt.pcolor(maze,edgecolors ='black',cmap=cmap)
    plt.axes().set_aspect('equal')
    plt.xticks([])
    plt.yticks([])
    plt.axes().invert_yaxis() 
    return plt
def rebuildMazeImage(sol):
    global maze
    iniziale= np.where(maze ==0.3)
    currentX=iniziale[0][0]
    currentY=iniziale[1][0]
    for move in sol:
        print(move)
        if str(move)=='est':
            currentY+=1
        elif str(move)=='ovest':
            currentY-=1
        elif str(move)=='sud':
            currentX+=1
        elif str(move)=='nord':
            currentX-=1
        if maze[currentX,currentY]!=0.7:
            maze[currentX,currentY]=0.5
    plt.pcolor(maze,edgecolors ='black',cmap=cmap)
    plt.axes().set_aspect('equal')
    plt.xticks([])
    plt.yticks([])
    plt.savefig('testplot.png')
    pixmap = QPixmap('testplot.png')
    label=win.label
    label.setPixmap(pixmap)
    win.setCentralWidget(label)
    win.resize(pixmap.width(), pixmap.height())
    os.remove('testplot.png')
            
def solveButtonFunction():
    global queryString,mazeChoosed,maze
    if not mazeChoosed:
       msgBox=QMessageBox()
       msgBox.setWindowTitle("Error!")
       msgBox.setText("Choose a maze")
       msgBox.exec()
    else:
        if not queryString:
            msgBox=QMessageBox()
            msgBox.setWindowTitle("Error!")
            msgBox.setText("Choose an algorithm")
            msgBox.exec()
        else:
            if 0.5 in maze:
                np.where(maze==0.5,0,maze)
            sol=list(prolog.query(queryString))
            if sol:
                rebuildMazeImage(sol[0]['Soluzione'])
            else:
                 msgBox=QMessageBox()
                 msgBox.setWindowTitle("Error!")
                 msgBox.setText("No solution founded")
                 msgBox.exec()
def loadLabyrinthFunction():
    global prolog,mazeChoosed,maze
    dlg = QFileDialog()
    fname = QFileDialog.getOpenFileName(dlg,'Open file','labirinti',"Prolog file (*.pl*)")
    labyrinthPath=fname[0]
    if mazeChoosed:
        maze=[]
    if labyrinthPath:
        prolog.consult(labyrinthPath)
        mazeImage=buildImageMaze(labyrinthPath)
        mazeImage.savefig('testplot.png')
        pixmap = QPixmap('testplot.png')
        label=win.label
        label.setPixmap(pixmap)
        win.setCentralWidget(label)
        win.resize(pixmap.width(), pixmap.height())
        os.remove('testplot.png')
        mazeChoosed=True

def chooseIterativeDeepiningFunction():
    global prolog,queryString
    prolog.consult("algoritmi/iterative_deepening.pl")
    queryString="iterative_deepening(Soluzione)."
def chooseAStarDeepiningFunction():
    global prolog,queryString
    prolog.consult("algoritmi/a_star.pl")
    queryString="a_star(Soluzione)."
def chooseIdaStarFunction():
    global prolog,queryString
    prolog.consult("algoritmi/ida_star.pl")
    queryString="ida_star(Soluzione)."
    

win.actionLoad_labyrinth.triggered.connect(loadLabyrinthFunction)
win.actionIterative_deepining.triggered.connect(chooseIterativeDeepiningFunction)    
win.actionAstar.triggered.connect(chooseAStarDeepiningFunction)   
win.actionIdaStar.triggered.connect(chooseIdaStarFunction)   
win.actionSolve.triggered.connect(solveButtonFunction)



win.show()
sys.exit(app.exec())