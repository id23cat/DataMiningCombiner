import csv
from sklearn import preprocessing
from sklearn.cross_validation import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score
#from sklearn.pipeline import Pipeline
#from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
#import seaborn

# print info
def print_data(data):
    for row in data:
        print(row)

# read and parse IRIS data 
def read_iris():
    data = []
    with open('./Data/iris.data') as csvfile:
        iris_data = csv.reader(csvfile)
        # print_data(iris_data)
        for row in iris_data:
            data.append([float(c) for c in row[:-1]])
    return data

#def kNN_tweak():
#    knn_pipe = Pipeline([('scaler', StandardScaler()), ('knn', KNeighborsClassifier(n_jobs=-1))])
#    knn_params = {'knn__n_neighbors': range(1, 10)}
#    knn_grid = GridSearchCV(knn_pipe, knn_params, cv=5, n_jobs=-1, verbose=True)
#    knn_grid.fit(X_train, y_train)
    

X = list(read_iris())
Y = range(X.__len__())
print('Data length: ', X.__len__())

#plt.scatter(X,Y)
#plt.title('Relationship Between Temperature and Iced Coffee Sales')
#plt.show()

# normalize data
normalized_X = preprocessing.normalize(X, norm='l2')
# print row info
#print_data(normalized_X)

# train it
#    info:
#            Split arrays or matrices into random train and test subsets
#        test_size - the proportion of the dataset to include in the test split
#        train_size - represent the proportion of the dataset to include in the train split
#        random_state - pseudo-random number generator state used for random sampling
X_train, X_test, y_train, y_test = train_test_split(normalized_X, Y, test_size = 0.3, random_state = 20)

knn = KNeighborsClassifier(n_neighbors=3)
knn.fit(X_train, y_train)

knn_predict = knn.predict(X_test)
print(accuracy_score(y_test, knn_predict))
