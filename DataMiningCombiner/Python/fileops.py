import pandas as pd

def readCSV(fname):
    df = pd.read_csv(fname)
    return df
