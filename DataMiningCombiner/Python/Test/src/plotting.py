import matplotlib.pyplot as plt
import matplotlib.cm as cm

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
