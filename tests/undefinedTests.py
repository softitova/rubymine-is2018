a = 0
b = None
if a + 1 == b:
    pass


# -- false --
if 1 - 1 + 1 == 2 - 2 + 1 and False and True:
    pass

if not True:
    pass

if True and True:
    pass

if True and False:
    pass

if not True and True:
    pass

if not (False or True):
    pass

# -- true --

if 1 - 1 + 1 == 2 - 2 + 1 and False or True:
    pass

if not False:
    pass

if True and True:
    pass

if True or False:
    pass
