# coding=UTF-8
from __future__ import (absolute_import, division,
                        print_function, unicode_literals)

# отключим предупреждения Anaconda
import warnings

warnings.simplefilter('ignore')

import pandas as pd
import numpy as np

df = pd.read_csv('Data/telecom_churn.csv')

#print(df.head()) # вывести первые 5 строк для ознакомления
#print(df.shape) # размер данных
#print(df.columns) # названия столбцов
#print(df.info()) # общая информация по данным

#df['Churn'] = df['Churn'].astype('int64') # приведение к заданному типу последнего столбцп

#print(df.describe()) # базовая статистика
#print(df.describe(include=['object', 'bool'])) # статистика, но для выбранных типов

#print(df['Churn'].value_counts()) # выведет информацию какое значение и сколько раз встретилось
#print(df['Churn'].value_counts(normalize=True)) # то же самое, но с нормализацией

#df.sort_values(by=['Churn', 'Total day charge'],
#        ascending=[True, False]).head() # отсортировать значения по возрастания для 'Churn' и
                                        # по убыванию для 'Total day charge'

# Какова максимальная длина международных звонков среди лояльных пользователей (Churn == 0),
#   не пользующихся услугой международного роуминга ('International plan' == 'No')?
#print(df[(df['Churn'] == 0) & (df['International plan'] == 'No')]['Total intl minutes'].max())

# формируем сводные данные
#print(pd.crosstab(df['Churn'], df['Voice mail plan'], normalize=True))

# как отток связан с признаком "Подключение международного роуминга" (International plan)
pd.crosstab(df['Churn'], df['International plan'], margins=True)

# число обращений в сервисный центр (Customer service calls)
pd.crosstab(df['Churn'], df['Customer service calls'], margins=True)

