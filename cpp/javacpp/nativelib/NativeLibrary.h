// NativeLibrary.h
#include <string>

namespace NativeLibrary {

class NativeClass {
public:
    const std::string &get_property() { return property; }
    void set_property(const std::string &property) { this->property = property + "aaa"; }

    std::string property;
};

}
