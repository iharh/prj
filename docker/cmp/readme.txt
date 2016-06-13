$SVN:
fx/fx
    ant clean compile
cmp/installer
    ant build-lp -Dbuild.lp=true

$GIT
cmp/installer
    ant clean dist

QAAutomation/ci/ci_cluster
    postgres_cb_release
