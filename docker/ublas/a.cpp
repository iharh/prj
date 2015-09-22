#include <iostream>

#include "boost/numeric/ublas/matrix.hpp"
#include "boost/numeric/ublas/vector.hpp"

using namespace boost::numeric::ublas;

typedef matrix<float> t_matrix;
typedef vector<float> t_vector;

void
printM(const std::wstring &sText, t_matrix m)
{
    size_t s1 = m.size1();
    size_t s2 = m.size2();

    std::wcout << sText << L" [" << s1 << L", " << s2 << L"]" << std::endl;
    for (size_t i = 0; i < s1; ++i)
    {
        for (size_t j = 0; j < s2; ++j)
        {
            std::wcout << L" " << m(i, j);
        }
        std::wcout << std::endl;
    }
    std::wcout << std::endl;
}

void
printV(const std::wstring &sText, t_vector v)
{
    size_t s = v.size();

    std::wcout << sText << L" [" << s << L"]" << std::endl;
    for (size_t i = 0; i < s; ++i)
    {
        std::wcout << L" " << v(i);
    }
    std::wcout << std::endl;
}

int
main()
{
    t_matrix a(3, 2);
    a(0, 0) = 1.;
    a(0, 1) = 2.;
    a(1, 0) = 3.;
    a(1, 1) = 4.;
    a(2, 0) = 5.;
    a(2, 1) = 6.;
    printM(L"a:", a);

    t_vector v(2);
    for (size_t i = 0; i < v.size(); ++i)
    {
        v(i) = i + 1.;
    }
    printV(L"v:", v);

    t_vector z = prod(a, v);
    printV(L"z:", z);

    return 0;
}
