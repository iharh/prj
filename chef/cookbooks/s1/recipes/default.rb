#
# Cookbook Name:: s1
# Recipe:: default
#
# Copyright 2016, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#
include_recipe 'yum'

file '/home/vagrant/hello-world.txt' do
    content 'hello world'
end
