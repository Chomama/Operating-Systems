The goal of this lab is to do resource allocation using both an optimistic resource manager and the banker’s
algorithm of Dijkstra. The optimistic resource manager is simple: Satisfy a request if possible, if not make the
task wait; when a release occurs, try to satisfy pending requests in a FIFO manner.
