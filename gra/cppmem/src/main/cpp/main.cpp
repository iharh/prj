#include <iostream>

int
main(void)
{
    std::cout << "start" << std::endl;
    //while (true) {
        int *pI = new int;
        delete pI;
    //}
    std::cout << "finish" << std::endl;
    return 0;
}
