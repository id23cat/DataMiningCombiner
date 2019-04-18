import sys
this = sys.modules[__name__]
for n in dir():
    if n[0]!='_': delattr(this, n)
