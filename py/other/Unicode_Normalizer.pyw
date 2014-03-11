# -*- coding: utf-8 -*-
"""
Created on Thu Jan 30 16:09:31 2014

@author: Pavel_Shkadzko
"""

import ttk, os, sys, codecs, unicodedata
from Tkinter import Tk, Frame, StringVar
from tkFileDialog import askopenfilename

# we need utf-8 env
reload(sys)
sys.setdefaultencoding('utf-8')
sys.stdout = codecs.getwriter('utf8')(sys.stdout)
sys.stderr = codecs.getwriter('utf8')(sys.stderr)


class UnicodeNormalizer(Frame):
    def __init__(self, master):
        Frame.__init__(self, master)
        self.grid()
        self.makeGui()
        
    def openFile(self):
        '''This function loads input data for conversion from the input file'''
        initialdir_path = os.getcwd()
        fname = askopenfilename(initialdir=initialdir_path, filetypes=(("All files", "*.*"), ("txt file", "*.txt")))
        with codecs.open(fname, 'r', encoding = 'utf-8') as data_file:
            self.input_data = data_file.read()
            
        self.STATE1.set('OK')
        
    
    def converter(self):
        '''This function performs unicode composition using NFC form'''
        converted_list = []
        for item in self.input_data.split('\n'):
            print 'conversion loop', item
            converted_list.append(unicodedata.normalize('NFC',item)) # unicode 'NFC' normalization

        with codecs.open(os.path.join(os.getcwd(), 'CONVERTED_NFC.txt'), 'w+', encoding = 'utf-8') as output_file:
            for converted_item in converted_list:
                output_file.write(converted_item)
        
        self.STATE2.set('OK')
        
        
    def makeGui(self):
        '''This function creates main app gui'''
        self.mainFrame = Frame(self, borderwidth = '2', relief = 'flat')
        self.mainFrame.grid()
        
        self.openFrame = ttk.Frame(self.mainFrame, borderwidth = '2', relief = 'groove')
        self.openFrame.grid(row=0,column=1)
        self.spacerFrame = ttk.Frame(self.mainFrame, width = 20, borderwidth = '2', relief = 'flat')
        self.spacerFrame.grid(row=0,column=2)
        self.convertFrame = ttk.Frame(self.mainFrame, borderwidth = '2', relief = 'groove')
        self.convertFrame.grid(row=0,column=3)
        self.STATE1 = StringVar();
        self.stateLabel1 = ttk.Label(self.mainFrame, padding = (5,2), textvariable = self.STATE1)
        self.stateLabel1.grid(row=0,column=2)
        self.STATE1.set('--')
        self.STATE2 = StringVar();
        self.stateLabel2 = ttk.Label(self.mainFrame, padding = (5,2), textvariable = self.STATE2)
        self.stateLabel2.grid(row=0,column=4)
        self.STATE2.set('--')
        
        
        self.openFile = ttk.Button(self.openFrame, text = "Open", padding = (1,1), command = self.openFile)
        self.openFile.grid(row=0,column=1)    
        self.convertButton = ttk.Button(self.convertFrame, text = "Convert", padding = (1,1), command = self.converter)
        self.convertButton.grid(row=0,column=3)  


# starting main interface loop
if __name__ == "__main__":
    root = Tk()
    root.title('Unicode Normalizer 0.1')
    root.minsize(260,50)
    root.columnconfigure(0, weight=1)
    root.rowconfigure(0, weight=1)
    root.update()

    app = UnicodeNormalizer(root)
    app.mainloop()