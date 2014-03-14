module HuskBench (benchmarks) where

import Criterion (Benchmark, bench, nf)
import Husk (husk)

benchmarks :: [Benchmark]
benchmarks =
    [ bench "husk" (nf (const husk) ())
    ]
