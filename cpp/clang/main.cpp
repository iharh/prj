#include <iostream>
#include <random>
#include <algorithm>

#include <thread>

const int32_t seed_ = 1234;

int
main(void)
{
    std::minstd_rand rng(seed_);
    const int32_t n = 5;
    std::vector<int32_t> perm(n, 0);
    std::iota(perm.begin(), perm.end(), 0); // 0, 1, 2, ...
    std::shuffle(perm.begin(), perm.end(), rng);

    std::wcout << L"perm: "
        << perm[0] << L","
        << perm[1] << L","
        << perm[2] << L","
        << perm[3] << L","
        << perm[4]
        << std::endl;
    return 0;
}
