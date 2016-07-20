#!/usr/bin/env python
# coding=utf8
"""
@author: liaoqiqi

93b9e2669a8965572610063d07559e6d

a195225bc1867d6a15aff7859ca076198d1d97b2

/* msoa	msoaSH*/
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`,`role_id`) VALUES (null, 'msoa', 'a195225bc1867d6a15aff7859ca076198d1d97b2', '3da70a83f9d386362fd5dfa369ddda3f95a86508','', '2');


"""

import hashlib
import uuid

if __name__ == '__main__':
    user_name = raw_input('name:')
    password = raw_input('password:')
    ownApps = raw_input('ownapps(空表示所有app):')
    role_id = raw_input('role(正常角色(1);管理员(2);只读管理员(3)):')

    sha1 = hashlib.sha1()
    sha1.update(password)
    new_password = sha1.hexdigest()

    dat = str(uuid.uuid4())
    sha1.update(dat + user_name)
    token = sha1.hexdigest()

    print "INSERT INTO `user` (`user_id`, `name`, `password`, `token`,`ownapps`,`role_id`) VALUES " \
          + "(null,'" + user_name + "' , '" + new_password + "' , '" + token + "' , '" + ownApps + "' ," + role_id + ")"
