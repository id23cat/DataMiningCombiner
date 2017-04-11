# based on
#    http://scikit-learn.org/stable/auto_examples/cluster/plot_kmeans_silhouette_analysis.html
#    http://scikit-learn.org/stable/auto_examples/cluster/plot_kmeans_digits.html
#    http://flothesof.github.io/k-means-numpy.html

from __future__ import print_function

from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

import matplotlib.cm as cm
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.preprocessing import scale

def data_reduction(data):
    pca = PCA(n_components=2).fit(data)
    return pca.transform(data)

def get_numerical_data(data):
    numerical_columns = [c for c in data.columns if data[c].dtype.name != 'object']
    return data[numerical_columns].values

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

def save_features_2D(fname, X, Y):
    plt.figure('Source features (based on PCA convertion to 2D and scaling)')
    plt.scatter(X, Y)
    plt.savefig(fname)
    return
	
def get_color_code(values, color_groups):
    return cm.spectral(values / color_groups)
	
def save_clusters_2D(fname, values, colors, centers):
    fig, ax1 = plt.subplots(1, 1)
    ax1.scatter(values[:, 0], values[:, 1], marker='.', s=30, lw=0, alpha=0.7, c=colors)
	
    # Draw white circles at cluster centers
    ax1.scatter(centers[:, 0], centers[:, 1], marker='o', c="white", alpha=1, s=200)

    for i, c in enumerate(centers):
        ax1.scatter(c[0], c[1], marker='$%d$' % i, alpha=1, s=50)

    ax1.set_title("The visualization of the clustered data.")
    ax1.set_xlabel("Features")
    ax1.set_ylabel("Features")

    plt.savefig(fname)
    return
	
print(__doc__)

df = pd.read_csv('Data/telecom_churn.csv')
data = scale(df[['Total day minutes'] + ['Total eve minutes'] + ['Total night minutes']])
reduction = data_reduction(data)

save_features_2D('telecom_input.png', reduction[:, 0], reduction[:, 1])

range_n_clusters = [3, 4, 5]
for number_of_clusters in range_n_clusters:
    centers, cluster_labels, sum_of_distances = k_means(data, number_of_clusters)
    colors = get_color_code(cluster_labels.astype(float), number_of_clusters)
    save_clusters_2D('cluster_res_' + str(number_of_clusters) + '.png', reduction, colors, centers)