#!/usr/bin/env python
# coding=utf8

import os
import shutil

import sys


reload(sys)
sys.setdefaultencoding('utf-8')  # @UndefinedVariable

# 输出文件夹
OUT_DIR = "output"


#
# 删除目录 (递归)
#
# Delete everything reachable from the directory named in "top",
# assuming there are no symbolic links.
# CAUTION:  This is dangerous!  For example, if top == '/', it
# could delete all your disk files.
def rmdir(top):
    import os

    for root, dirs, files in os.walk(top, topdown=False):
        for name in files:
            os.remove(os.path.join(root, name))
        for name in dirs:
            os.rmdir(os.path.join(root, name))


#
# 复制目录(递归)
# assets -> output/assets
# dep    -> output/dep
#
def copytree(src_dir, dest_dir, symlinks=False, ignore=None):
    for item in os.listdir(src_dir):
        s = os.path.join(src_dir, item)
        d = os.path.join(dest_dir, item)
        if os.path.isdir(s):
            shutil.copytree(s, d, symlinks, ignore)
        else:
            shutil.copy2(s, d)


#
# 复制类ShellPattern的文件至目录 (非递归)
#
def copyFilePattern(src_dir, dest_dir, pattern):
    import glob

    files = glob.iglob(os.path.join(src_dir, pattern))
    for file in files:
        if os.path.isfile(file):
            shutil.copy(file, dest_dir)


#
# 入口
#
if __name__ == '__main__':

    try:

        # 删除输出文件夹
        rmdir(OUT_DIR)

        # 
        if not os.path.exists(OUT_DIR):
            os.makedirs(OUT_DIR)

        #
        copytree("assets", OUT_DIR + "/assets")
        copytree("dep", OUT_DIR + "/dep")
        copyFilePattern(".", OUT_DIR, "*.html")

    except KeyboardInterrupt:
        pass



