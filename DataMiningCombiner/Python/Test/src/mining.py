# based on
#    http://scikit-learn.org/stable/auto_examples/cluster/plot_kmeans_silhouette_analysis.html
#    http://scikit-learn.org/stable/auto_examples/cluster/plot_kmeans_digits.html
#    http://flothesof.github.io/k-means-numpy.html
from __future__ import print_function

from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

from sklearn.preprocessing import scale
import sklearn as sk

# noramlization of numeric columns in DafaFrame
# data -- Pandas DataFrame
# result -- numpy.ndarray
def norm_stdscore(data):
    numerical_columns = [c for c in data.columns if data[c].dtype.name != 'object']
    data_numerical = data[numerical_columns]
    result=(data[numerical_columns] - data[numerical_columns].mean())/data[numerical_columns].std()
    return result

def norm_sklearn(data):
    numerical_columns = [c for c in data.columns if data[c].dtype.name != 'object']
    data_numerical = data[numerical_columns]
    result=sklearn.preprocessing.normalize(data_numerical)
    return result

def data_reduction(data):
    pca = PCA(n_components=2).fit(data)
    return pca.transform(data)

def get_numerical_data(data):
    numerical_columns = [c for c in data.columns if data[c].dtype.name != 'object']
    return data[numerical_columns].values

#def get_scaled(df, attributes):
def get_scaled(df):
    # only for beginning testing
    attributes = ['Total day minutes'] + ['Total eve minutes'] + ['Total night minutes']        
    return scale(df[attributes])

#       KMeans parameters:
# n_jobs       - If -1 all CPUs are used. If 1 is given, no parallel computing code is used at all,
#                which is useful for debugging. For n_jobs below -1, (n_cpus + 1 + n_jobs) are used.
#                Thus for n_jobs = -2, all CPUs but one are used.
# random_state - The generator used to initialize the centers. If an integer is given, it fixes the
#                seed. Defaults to the global numpy random number generator.
# max_iter     - Maximum number of iterations of the k-means algorithm for a single run.
#
#        Output:
# centers : array, [n_clusters, n_features] - Coordinates of cluster centers
# cluster_indexes : ???? - Predicted cluster index for each sample
# sum_of_distances : float - Sum of distances of samples to their closest cluster center.
def k_means(data, number_of_clusters, max_iter=300):
    clusterer = KMeans(n_clusters=number_of_clusters, max_iter=max_iter, random_state=10, init='k-means++')
    # compute cluster centers and predict cluster index for each sample
    cluster_indexes = clusterer.fit_predict(data)
    return clusterer.cluster_centers_,  cluster_indexes, clusterer.inertia_

