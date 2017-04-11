import pandas as pd
import sklearn as sk

# noramlization of numeric columns in DafaFrame
# data -- Pandas DataFrame
# result -- numpy.ndarray
def normalization.stdscore(data):
    numerical_columns = [c for c in data.columns if data[c].dtype.name != 'object']
    data_numerical = data[numerical_columns]
    result=(data[numerical_columns] - data[numerical_columns].mean())/data[numerical_columns].std()
    return result

def normalization.sklearn(data):
    numerical_columns = [c for c in data.columns if data[c].dtype.name != 'object']
    data_numerical = data[numerical_columns]
    result=sklearn.preprocessing.normalize(data_numerical)
    return result

