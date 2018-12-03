import sys
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

if len(sys.argv) < 2:
    print("Not enough arguments.\n")
    sys.exit()

keys = []
data = []

df = pd.read_csv(sys.argv[1], delimiter = ',')
print(df[:2])
print('...')

data = df.values

fig = plt.figure()
ax = plt.axes(projection='3d')

xdata = data[:, 0]
ydata = data[:, 1]
zdata = data[:, 2]
cdata = data[:, 3]

ax.scatter3D(xdata, ydata, zdata, c=cdata, cmap='Paired')

name = sys.argv[1] + '.plot' + '.png'

fig.savefig(name)
