# -*- coding: utf-8 -*-
"""
Created on Tue Dec 29 10:46:04 2020

@author: Paolo Sanfilippo
"""
import clips 
from PyQt5.QtWidgets import QApplication,QFileDialog,QMessageBox,QTableWidgetItem
import sys,os
from PyQt5 import uic,QtTest
from PyQt5.QtGui import QColor
import numpy as np


mapPath=None
environment=None
mainPath='0_Main.clp'
envPath='1_Env.clp'
agentPath='3_Agent_v2.clp'

grid=np.zeros((10,10))

app =QApplication([])

win = uic.loadUi("GuiUi.ui")
table=win.tableWidget
win.textEdit.setReadOnly(True)

def writeDat():
    f = open("go.bat", "w")
    f.write("(load "+mainPath+")\n")
    f.write("(load "+envPath+")\n")
    f.write("(load "+mapPath+")\n")
    f.write("(load "+agentPath+")\n")
    f.write("(reset)\n(set-break game-over)\n(run)\n(run 2)\n(focus AGENT)\n(facts)")
    f.close()

def buildGrid():
    global mapPath,table,grid
    with open(mapPath, 'r') as file:
        for line in file:
            if '(' in line:
                fact=line.split('(')[1]
                if fact.replace(" ","")=='cell' or fact.replace(" ","")=='k-cell':
                    x=int(line.split(')')[0].split('(')[2][2])
                    y=int(line.split(')')[1].split('(')[1][2])
                    if 'water' in line:
                        table.setItem(x, y, QTableWidgetItem())
                        table.item(x,y).setBackground(QColor(170,255,255))
                    else:
                        table.setItem(x, y, QTableWidgetItem())
                        table.item(x,y).setBackground(QColor(0,0,0))
                        grid[x,y]=1
                if fact.replace(" ","")=='k-cell':
                    table.item(x,y).setForeground(QColor(0,170,0))
                    table.item(x,y).setText('K')

def loadMapButtonFunction():
    global mapPath
    dlg = QFileDialog()
    path= QFileDialog.getOpenFileName(dlg,'Open file',os.getcwd(),"File Clips (*.clp*)")[0]
    if path:
        mapPath=os.path.relpath(path,os.getcwd()) 
        buildGrid()

def analyze_fact(fact):
    if fact.template.name=='exec':
        QtTest.QTest.qWait(1000)
        x=fact['x']
        y=fact['y']
        if fact['action']=='fire':
            if grid[x,y]==1:
                table.item(x,y).setForeground(QColor(0,170,0))
                win.textEdit.append('Fire('+str(x)+','+str(y)+'): ✔️')
            else:
                table.item(x,y).setForeground(QColor(255,0,0))
                win.textEdit.append('Fire('+str(x)+','+str(y)+'): ❌')
            table.item(x,y).setText('Fire')
        elif fact['action']=='guess':
            if grid[x,y]==1:
                table.item(x,y).setForeground(QColor(0,170,0))
                win.textEdit.append('Guess('+str(x)+','+str(y)+'): ✔️')
            else:
                table.item(x,y).setForeground(QColor(255,0,0))
                win.textEdit.append('Guess('+str(x)+','+str(y)+'): ❌')
            table.item(x,y).setText('Guess')
    elif fact.template.name=='score':
        msgBox=QMessageBox()
        msgBox.setWindowTitle("Score!")
        msgBox.setText("Your score is")
        msgBox.exec()
        win.textEdit.append('Score: ')
        
        
    
def runButtonButtonFunction():
    global environment
    if not mapPath:
        msgBox=QMessageBox()
        msgBox.setWindowTitle("Error!")
        msgBox.setText("Load a map!")
        msgBox.exec()
    else:
        writeDat()
        environment=clips.Environment()
        environment.batch_star('go.bat')
        environment.run()
        for fact in environment.facts():
            analyze_fact(fact)
        
def actionChange_mainFunction():
    global mainPath
    dlg = QFileDialog()
    path= QFileDialog.getOpenFileName(dlg,'Open file',os.getcwd(),"File Clips (*.clp*)")[0]
    if path:
        mainPath=os.path.relpath(path,os.getcwd()) 

def actionChange_AgentFunction():
    global agentPath
    dlg = QFileDialog()
    path= QFileDialog.getOpenFileName(dlg,'Open file',os.getcwd(),"File Clips (*.clp*)")[0]
    if path:
        agentPath=os.path.relpath(path,os.getcwd()) 
    
def actionChange_environmentFunction():
    global envPath
    dlg = QFileDialog()
    path= QFileDialog.getOpenFileName(dlg,'Open file',os.getcwd(),"File Clips (*.clp*)")[0]
    if path:
        envPath=os.path.relpath(path,os.getcwd()) 

    

win.LoadMapButton.clicked.connect(loadMapButtonFunction)
win.RunButton.clicked.connect(runButtonButtonFunction)
win.actionChange_main.triggered.connect(actionChange_mainFunction)
win.actionChange_Agent.triggered.connect(actionChange_AgentFunction)
win.actionChange_environment.triggered.connect(actionChange_environmentFunction)

win.show()
sys.exit(app.exec())


