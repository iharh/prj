include_recipe 'yum'

s1_ids = node['cb']['s1']['ids']

s1_ids.each do |s1_id|
    file "/home/vagrant/hello#{s1_id}.txt" do
        content "hello #{s1_id}"
    end
end
