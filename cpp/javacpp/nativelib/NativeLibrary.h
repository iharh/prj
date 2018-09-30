// NativeLibrary.h
#include <string>

namespace NativeLibrary {

class NativeClass {
public:
    std::wstring getWString() { return std::wstring(L"abc"); }
};

}
