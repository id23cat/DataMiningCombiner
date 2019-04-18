import pandas as pd

def entry(fname):
    df = pd.read_csv(fname)
    return df
