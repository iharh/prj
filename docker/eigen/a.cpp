#include <iostream>
#include <iterator>
#include <algorithm>
#include <chrono>
#include <functional>
//include <random>
//include <thread>
#include <Eigen/Dense>

static size_t s1 = 3000000;
static size_t s2 = 300;

static size_t numThreads = 1;
static size_t numIters = 10;

std::vector<double> minTimes;
std::vector<double> maxTimes;

std::chrono::duration<double>
mulIter(const Eigen::MatrixXd &m)
{
    auto vec = Eigen::VectorXd::Random(s2);

    // std::chrono::high_resolution_clock::time_point
    auto tstart = std::chrono::high_resolution_clock::now();

    Eigen::VectorXd z = m * vec;
/*
    float maxRes = 0.;
    // assert(z.size() == s1)
    for (Eigen::VectorXd::InnerIterator itr(z); itr; ++itr)
    {
        // itr.index(); itr.value();
        float val = itr.value();
        //float val = z(i);
        maxRes = std::max(val, maxRes);
    }
*/
    auto tfinish = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> time_span = std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart);
    return time_span;
}

void
doMulti(const Eigen::MatrixXd &m, size_t idx)
{
    std::vector<float> times;

    for (size_t i = 0; i < numIters; ++i)
    {
        std::chrono::duration<double> time_span = mulIter(m);
        times.emplace_back(time_span.count());
    }

    std::sort(times.begin(), times.end());
    size_t numSkip = numIters / 10;
    double minTime = times[numSkip];
    double maxTime = times[numIters - numSkip * 2];
        //<< L"size: " << z.size() << std::endl
        //<< L"max: " << maxRes << std::endl
    //std::wcout << L"Matrix-vector multiplication [" << idx << L"] took seconds:" << std::endl;
    //std::copy(times.begin(), times.end(), std::ostream_iterator<float, wchar_t>(std::wcout, L" "));
    //std::wcout << std::endl;
    minTimes[idx] = minTime;
    maxTimes[idx] = maxTime;
}

void
testMmul()
{
    std::wcout << L"Start matrix generation ..." << std::endl;

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
    std::wcout << L"Matrix generation took " << std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart).count() << L" seconds." << std::endl;

    static size_t MAX_THREADS = 2;
    minTimes.resize(MAX_THREADS);
    maxTimes.resize(MAX_THREADS);

    for (size_t idx = 0; idx < MAX_THREADS; ++idx)
    {
        // std::ref, std::placeholders::_1
        auto boundMulti = std::bind(doMulti, std::cref(m), idx);
        boundMulti();
    }

    for (size_t idx = 0; idx < MAX_THREADS; ++idx)
    {
        std::wcout << L"Matrix-vector multiplication [" << idx << L"] took " << minTimes[idx] << L" - " << maxTimes[idx] << L" seconds" << std::endl;
    }
}

void
testSort()
{
    std::vector<float> vec;

    vec.emplace_back(5.1);
    vec.emplace_back(1.2);
    vec.emplace_back(1.);
    vec.emplace_back(3.2);

    std::wcout << L"vec: ";
    std::copy(vec.begin(), vec.end(), std::ostream_iterator<float, wchar_t>(std::wcout, L" "));
    std::wcout << std::endl;

    std::wcout << L"sorted vec: ";
    std::sort(vec.begin(), vec.end());
    std::copy(vec.begin(), vec.end(), std::ostream_iterator<float, wchar_t>(std::wcout, L" "));
    std::wcout << std::endl;
}

int
main()
{
    testMmul();
    return 0;
}
