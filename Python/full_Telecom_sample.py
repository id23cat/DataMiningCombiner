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

df = pd.read_csv('Data/telecom_churn.csv')

# график 1: обычная гистограмма
df['Churn'].value_counts().plot(kind='bar', label='Churn')
plt.legend()
#.decode('utf-8')) для корректного представления символов в интерпретаторе
plt.title('Пример построения гистограммы (сумма по значению для некоторого столбца)')
plt.savefig("telecom_churn_info.png")

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
#pd.crosstab(df['Churn'], df['International plan'], margins=True)
#plt.savefig("telecom_crosstab1.png")
#
# число обращений в сервисный центр (Customer service calls)
#pd.crosstab(df['Churn'], df['Customer service calls'], margins=True)
#plt.savefig("telecom_crosstab2.png")

# График 2: корреляция количественных признаков

# пример распределения выборки по категориям и числовым данным
categorical_columns = [c for c in df.columns if df[c].dtype.name == 'object']
numerical_columns   = [c for c in df.columns if df[c].dtype.name != 'object']
print(categorical_columns)
print(numerical_columns)

#data_describe = df.describe(include=[object])
#binary_columns    = [c for c in categorical_columns if data_describe[c]['unique'] == 2]
#nonbinary_columns = [c for c in categorical_columns if data_describe[c]['unique'] > 2]

# пример нормализации данных
data_numerical = df[numerical_columns]
data_numerical = (data_numerical - data_numerical.mean()) / data_numerical.std()
print(data_numerical.describe())

col1 = 'Churn'
col2 = 'Total intl minutes'
col3 = 'Number vmail messages'
plt.figure(figsize=(10, 6))
plt.scatter(df[col1], df[col3], alpha=0.75, color='red', label=col1)
plt.scatter(df[col2], df[col3], alpha=0.75, color='blue', label=col2)
plt.xlabel(col1)
plt.ylabel(col2)
plt.legend(loc='best')
plt.title('Пример построения диаграммы рассеяния 2 произвольных признаков')
plt.savefig("telecom_scatter2.png")

corr_matrix = df.drop(['State', 'International plan', 'Voice mail plan', 'Area code'], axis=1).corr()
sns.heatmap(corr_matrix)
plt.title('Пример корреляции количественных признаков')
plt.savefig("telecom_heatmap.png")

# гистограмма и диаграмма рассеивания
scatter_matrix(df, alpha=0.05, figsize=(20, 12))
plt.title('Пример гистограммы и диаграммы рассеивания')
plt.savefig("telecom_scatter.png")

# распределение количественных признаков
features = list(set(df.columns) - set(['State', 'International plan', 'Voice mail plan',  'Area code',
                                       'Total day charge',  'Total eve charge',   'Total night charge',
                                       'Total intl charge', 'Churn']))

df[features].hist(figsize=(20,12))
#df.hist(figsize=(20,12))
plt.title('Пример распределения количественных признаков');
plt.savefig("telecom_hist.png")

# на главной диагонали рисуются распредления признаков, а вне главной диагонали – диаграммы рассеяния для пар признаков
#sns.pairplot(df[features + ['Churn']], hue='Churn');
#plt.legend()
#plt.title('Распределение количественных признаков');
#plt.savefig("telecom_pairplot.png")

df['International plan'] = pd.factorize(df['International plan'])[0]
df['Voice mail plan'] = pd.factorize(df['Voice mail plan'])[0]
df['Churn'] = df['Churn'].astype('int')
states = df['State']
y = df['Churn']
df.drop(['State', 'Churn'], axis=1, inplace=True)

X_train, X_holdout, y_train, y_holdout = train_test_split(df.values, y, test_size=0.3, random_state=17)

tree = DecisionTreeClassifier(max_depth=5, random_state=17)
knn = KNeighborsClassifier(n_neighbors=10)

tree.fit(X_train, y_train)
knn.fit(X_train, y_train)

tree_pred = tree.predict(X_holdout)
print(accuracy_score(y_holdout, tree_pred)) # 0.94

knn_pred = knn.predict(X_holdout)
print(accuracy_score(y_holdout, knn_pred)) # 0.88

#xx, yy = get_grid(X, eps=.05)
#predicted = knn.predict(np.c_[xx.ravel(), yy.ravel()]).reshape(xx.shape)
#plt.pcolormesh(xx, yy, predicted, cmap='autumn')
#plt.scatter(X[:, 0], X[:, 1], c=y, s=100,
#cmap='autumn', edgecolors='black', linewidth=1.5);
#plt.title('Easy task, kNN. Not bad');
