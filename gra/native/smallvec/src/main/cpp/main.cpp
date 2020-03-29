#include <iostream>

int
main(void)
{
    int *pI = new int;
    *pI = 3;
    std::wcout << L"Hello Smallvec: " << *pI << std::endl;
    return 0;
}
