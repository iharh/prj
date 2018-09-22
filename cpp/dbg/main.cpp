#include <iostream>
#include <iomanip>
#include <wchar.h>

int
main()
{
    const wchar_t *my_str = L"1234";
    size_t len = wcslen(my_str);

    std::cout << "len: " << len << std::endl;
    std::cout << "sizeof(char): " << sizeof(char) << std::endl;
    std::cout << "sizeof(wchar_t): " << sizeof(wchar_t) << std::endl;

    std::cout << std::hex << std::setfill('0');  // setfill needs to be set only once
    unsigned char *ptr = reinterpret_cast<unsigned char *>(const_cast<wchar_t *>(my_str));
    for (size_t i = 0; i < len * sizeof(wchar_t); ++i, ptr++)
    {
        std::cout << std::setw(2) << static_cast<unsigned>(*ptr) << " ";
    }
    std::cout << std::endl;

    return 0;
}
