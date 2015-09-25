#include <iostream>
#include <chrono>
//include <random>
//include <thread>
#include <Eigen/Dense>

static size_t s1 = 3000000;
static size_t s2 = 300;

//static size_t

void
doMulti(const Eigen::MatrixXd &m, size_t idx)
{
    auto v = Eigen::VectorXd::Random(s2);

    // std::chrono::high_resolution_clock::time_point
    auto tstart = std::chrono::high_resolution_clock::now();

    Eigen::VectorXd z = m * v;

    float maxRes = 0.;
    // assert(z.size() == s1)
    //for (size_t i = 0; i < s1; ++i)
    //for (Eigen::VectorXd::InnerIterator itr(z); itr; ++itr)
    //{
    //    // itr.index(); itr.value();
    //    float v = itr.value();
    //    float v = z(i);
    //    maxRes = std::max(v, maxRes);
    //}

    auto tfinish = std::chrono::high_resolution_clock::now();

    std::wcout << L"Matrix-vector multiplication [" << idx << L"] took "
        << std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart).count() << L" seconds." << std::endl
        //<< L"size: " << z.size() << std::endl
        //<< L"max: " << maxRes << std::endl
    ;

    //std::cout << "z:" << std::endl << z << std::endl;
}

int
main()
{
    auto tstart = std::chrono::high_resolution_clock::now();

    Eigen::MatrixXd m = Eigen::MatrixXd::Random(s1, s2);
/*  Eigen::MatrixXd m(2,2);
    m(0,0) = 3;
    m(1,0) = 2.5;
    m(0,1) = -1;
    m(1,1) = m(1,0) + m(0,1);
    std::cout << m << std::endl;
*/
    //std::chrono::duration<double> time_span = std::chrono::duration_cast<std::chrono::duration<double>>(t2 - t1);
    auto tfinish = std::chrono::high_resolution_clock::now();
    std::cout << L"Matrix generation took " << std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart).count() << L" seconds." << std::endl;

    doMulti(m, 0);

    return 0;
}
