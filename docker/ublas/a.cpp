#include <iostream>
#include <chrono>
#include <random>
#include <thread>

#include "boost/numeric/ublas/matrix.hpp"
#include "boost/numeric/ublas/vector.hpp"

using namespace boost::numeric::ublas;

typedef matrix<float> t_matrix;
typedef vector<float> t_vector;

void
printM(const std::wstring &sText, const t_matrix &m)
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
printV(const std::wstring &sText, const t_vector &v)
{
    size_t s = v.size();

    std::wcout << sText << L" [" << s << L"]" << std::endl;
    for (size_t i = 0; i < s; ++i)
    {
        std::wcout << L" " << v(i);
    }
    std::wcout << std::endl;
}

//static size_t s1 = 3000000;
static size_t s1 = 300000;
static size_t s2 = 300;
static t_matrix m(s1, s2);

static std::mt19937_64 gen;
static std::uniform_real_distribution<float> dist(0.0, 1.0);

void
doMulti(size_t idx)
{
    t_vector v(s2);
    for (size_t i = 0; i < s2; ++i)
    {
        v(i) = dist(gen);
    }
    //printV(L"v:", v);

    std::chrono::high_resolution_clock::time_point tstart = std::chrono::high_resolution_clock::now();
    t_vector z = prod(m, v);
    //printV(L"z:", z);
    std::chrono::high_resolution_clock::time_point tfinish = std::chrono::high_resolution_clock::now();

    std::wcout << L"Matrix-vector multiplication [" << idx << L"] took "
        << std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart).count() << L" seconds." << std::endl;
}

int
main()
{
    size_t num_threads = 3;

    std::wcout << L"Matrix generation started..." << std::endl;
    std::chrono::high_resolution_clock::time_point tstart = std::chrono::high_resolution_clock::now();

    for (size_t i = 0; i < s1; ++i)
    {
        for (size_t j = 0; j < s2; ++j)
        {
            m(i, j) = dist(gen);
        }
    }
    //printM(L"m:", m);

    //std::chrono::duration<double> time_span = std::chrono::duration_cast<std::chrono::duration<double>>(t2 - t1);
    std::chrono::high_resolution_clock::time_point tfinish = std::chrono::high_resolution_clock::now();
    std::wcout << L"Matrix generation took " << std::chrono::duration_cast<std::chrono::duration<double>>(tfinish - tstart).count() << L" seconds." << std::endl;

    typedef std::vector<std::thread> t_thread_vec;

    t_thread_vec threads;
    for (size_t t = 0; t < num_threads; ++t)
    {
        auto r = std::bind(doMulti, t);
        threads.emplace_back(r);
    }

    for (t_thread_vec::iterator itr = threads.begin(); itr != threads.end(); ++itr)
    {
        itr->join();
    }

    return 0;
}
