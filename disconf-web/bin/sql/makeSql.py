#!/usr/bin/env python
# coding=utf8
"""
Created on 2014年2月15日

@author: liaoqiqi
"""

myfile = open("url_resources.txt")

lines = myfile.readlines()

print "use disconf;"
print 'delete from role_resource;'

for line in lines:

    line = line.strip('\n')

    if not line:
        continue

    if line[0] == '#':
        continue

    data = line.split()

    if len(data) != 5 and len(data) != 3:
        # print "cannot process this: " + line
        continue

    url = data[0]
    desc = data[1]

    if len(data) == 3:
        role1_mask = data[2]
        role2_mask = role1_mask
        role3_mask = role1_mask
    else:
        role1_mask = data[2]
        role2_mask = data[3]
        role3_mask = data[4]

    URL_PREFIX = "/api"

    print "INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES"
    print "(1,'" + URL_PREFIX + url + "' , '" + desc + "' , '" + role1_mask + "'),"
    print "(2,'" + URL_PREFIX + url + "' , '" + desc + "' , '" + role2_mask + "'),"
    print "(3,'" + URL_PREFIX + url + "' , '" + desc + "' , '" + role3_mask + "');"
