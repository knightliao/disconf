#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
Created on 2013-5-23

@author: liaoqiqi

打印zookeeper树

"""

from kazoo.client import KazooClient
import time

hosts = '127.0.0.1:3307'
zk = KazooClient(hosts=hosts)
zk.start()

print "hosts", hosts
print "version", zk.server_version()

#
#
#
def printTreeNode(basepath, node, i):
    if isinstance(node, list):

        for item in node:

            # current
            current_base_path = basepath + "/" + item
            data, stat = zk.get(current_base_path)
            ahead = ""
            j = 1
            while j < i:
                ahead += "\t"
                j += 1
            print ahead + "|----" + item + "\t" + data.decode("utf-8") + "\t" + \
                  time.strftime("%Y%m%d%H%M%S", time.localtime(stat.ctime / 1000)) + "\t" + \
                  time.strftime("%Y%m%d%H%M%S", time.localtime(stat.mtime / 1000))

            # recursive 
            items = zk.get_children(current_base_path)
            printTreeNode(current_base_path, items, i + 1)


#
# root
#
def printtreeRoot(path):
    # get children
    base_path = path
    children = zk.get_children(base_path)
    print base_path
    printTreeNode(base_path, children, 1)

#
#
#
if __name__ == '__main__':
    printtreeRoot("/")
    zk.stop()
