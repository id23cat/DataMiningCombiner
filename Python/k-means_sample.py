from __future__ import print_function

from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

import matplotlib.cm as cm
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.preprocessing import scale

print(__doc__)
# based on
#    http://scikit-learn.org/stable/auto_examples/cluster/plot_kmeans_silhouette_analysis.html
#    http://scikit-learn.org/stable/auto_examples/cluster/plot_kmeans_digits.html
#    http://flothesof.github.io/k-means-numpy.html

df = pd.read_csv('Data/telecom_churn.csv')
numerical_columns = [c for c in df.columns if df[c].dtype.name != 'object']
data = scale(df[numerical_columns].values)

range_n_clusters = [3, 4, 5]

pca = PCA(n_components=2).fit(data)
pca_2d = pca.transform(data)

plt.figure('Source features (based on PCA convertion to 2D)')
plt.scatter(pca_2d[:, 0], pca_2d[:, 1])

for n_clusters in range_n_clusters:
    fig, ax1 = plt.subplots(1, 1)

    clusterer = KMeans(n_clusters=n_clusters, random_state=10)
    cluster_labels = clusterer.fit_predict(data)

    colors = cm.spectral(cluster_labels.astype(float) / n_clusters)
    ax1.scatter(pca_2d[:, 0], pca_2d[:, 1], marker='.', s=30, lw=0, alpha=0.7, c=colors)

    # Labeling the clusters
    centers = clusterer.cluster_centers_

    # Draw white circles at cluster centers
    ax1.scatter(centers[:, 0], centers[:, 1], marker='o', c="white", alpha=1, s=200)

    for i, c in enumerate(centers):
        ax1.scatter(c[0], c[1], marker='$%d$' % i, alpha=1, s=50)

    ax1.set_title("The visualization of the clustered data.")
    ax1.set_xlabel("Features")
    ax1.set_ylabel("Features")

    plt.show()