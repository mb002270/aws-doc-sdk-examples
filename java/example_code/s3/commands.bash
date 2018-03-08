#!/bin/bash

sudo mkdir /mnt/ramdisk
sudo mount -t tmpfs -o size=8192m tmpfs /mnt/ramdisk
dd if=/dev/urandom of=/mnt/ramdisk/bigfile1.txt bs=10485760 count=100
dd if=/dev/urandom of=/mnt/ramdisk/bigfile2.txt bs=10485760 count=100
dd if=/dev/urandom of=/mnt/ramdisk/bigfile3.txt bs=10485760 count=100
dd if=/dev/urandom of=/mnt/ramdisk/bigfile4.txt bs=10485760 count=100
dd if=/dev/urandom of=/mnt/ramdisk/bigfile5.txt bs=10485760 count=100


./run_example.sh XferMgrUpload "mwaters-s3fuse-test /mnt/ramdisk"
