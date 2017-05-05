https://www.conan.io/source/zlib/1.2.11/lasote/testing

conan install zlib/1.2.11@lasote/stable -b -s arch="x86_64" -s build_type="Release" -s compiler="Visual Studio" -s compiler.runtime="MT" -s compiler.version="10" -s os="Windows" -o shared="False" 

??? x86_64

conan install zlib/1.2.11@lasote/stable -s arch="x86_64" -s build_type="Release" -s compiler="Visual Studio" -s compiler.runtime="MD" -s compiler.version="12" -s os="Windows" -o shared="True" 
