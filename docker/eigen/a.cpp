#include <iostream>
#include <chrono>
//include <random>
//include <thread>
#include <Eigen/Dense>

static size_t s1 = 3000000;
static size_t s2 = 300;

void
doMulti(const Eigen::MatrixXd &m, size_t idx)
{
    auto v = Eigen::VectorXd::Random(s2);

    // std::chrono::high_resolution_clock::time_point
    auto tstart = std::chrono::high_resolution_clock::now();

    auto z = m * v;

    auto tfinish = std::chrono::high_resolution_clock::now();

    std::cout << "Matrix-vector multiplication [" << idx << "] took "
        << std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart).count() << " seconds." << std::endl;

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
    std::cout << "Matrix generation took " << std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart).count() << " seconds." << std::endl;

    doMulti(m, 0);

    return 0;
}
