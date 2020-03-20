#include <cstdlib> // getenv
#include <iostream>

int
main()
{
    const char *envVar = std::getenv("ENV_VAR");
    if (envVar != NULL)
    {
        std::cout << "Hello with ENV_VAR" << std::endl;
    }
    else
    {
        std::cout << "Hello without ENV_VAR" << std::endl;
    }

    return 0;
}
