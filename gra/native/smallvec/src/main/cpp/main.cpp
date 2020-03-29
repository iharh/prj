#include "SmallVector.h"

#include <iostream>

struct my_data
{
    my_data()
    {
    }

    int m_i;
    long m_l;
    float m_f;
};

typedef std::shared_ptr<my_data> my_data_ptr;

int
main(void)
{
    typedef llvm::SmallVector<my_data_ptr, 64> t_small_vec;

    t_small_vec vActiveInstances, vTmpActiveInstances;

    std::wcout << L"Hello Smallvec: 1" << std::endl;

    vActiveInstances.push_back(std::make_shared<my_data>());

    std::wcout << L"Hello Smallvec: 2" << std::endl;

    for (size_t j = 0; j < 65; ++j)
    {
        vTmpActiveInstances.push_back(std::make_shared<my_data>()); // grow called
    }

    std::wcout << L"Hello Smallvec: 3" << std::endl;

    vActiveInstances.swap(vTmpActiveInstances); // grow called

    std::wcout << L"Hello Smallvec: 4" << std::endl;

    vTmpActiveInstances.clear(); // clear is really important here !!!

    std::wcout << L"Hello Smallvec: 5" << std::endl;

    vActiveInstances.swap(vTmpActiveInstances);

    std::wcout << L"Hello Smallvec: 6" << std::endl;

    return 0;
}
