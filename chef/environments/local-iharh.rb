name 'local-iharh'
description 'iharh descr'
default_attributes(
    'yum' => { 'main'            => { 'proxy'          => 'http://192.168.235.101:3128'                      } },
    #
     'cb' => { 'templateService' => { 'yumRepoURL'     => 'http://192.168.235.101:8080/yum',
                                      'contentRepoURL' => 'http://192.168.235.101:8081/content/repositories' } }
)
