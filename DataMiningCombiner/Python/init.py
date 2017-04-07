#!/usr/bin/python
# coding=UTF-8
from __future__ import (absolute_import, division,
                        print_function, unicode_literals)

# отключим предупреждения Anaconda
import warnings
warnings.simplefilter('ignore')

#увеличим дефолтный размер графиков
#from pylab import rcParams
#rcParams['figure.figsize'] = 8, 5

import pandas as pd
#import numpy as np
import seaborn as sns

from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score


import matplotlib.pyplot as plt
from pandas.tools.plotting import scatter_matrix

# local modules
import fileops
