#include <iostream>

#include "boost/numeric/ublas/matrix.hpp"
#include "boost/numeric/ublas/vector.hpp"

using namespace boost::numeric::ublas;

typedef matrix<float> t_matrix;

void
print_matrix_2_2(const std::wstring &sText, t_matrix a)
{
    std::wcout << sText << std::endl;
    std::wcout << a(0, 0) << L" " << a(0, 1) << std::endl;
    std::wcout << a(1, 0) << L" " << a(1, 1) << std::endl;
    std::wcout << std::endl;
}
int
main()
{
    t_matrix a(2, 2);
    a(0, 0) = 1.;
    a(0, 1) = 2.;
    a(1, 0) = 3.;
    a(1, 1) = 4.;
    print_matrix_2_2(L"a:", a);    

    return 0;
}
