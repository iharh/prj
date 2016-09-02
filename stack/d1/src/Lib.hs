module Lib
    ( someDiag
    ) where

import Diagrams.Prelude
import Diagrams.Backend.SVG.CmdLine

-- not exported
someFunc :: IO ()
someFunc = putStrLn "someFunc"

someDiag :: Diagram SVG
someDiag = c ||| strutX 1 ||| c
    where c = text "ab" <> circle 1 # fc blue

--square 3
--    # fc green
--    # lw 5.0
--    # clipBy (square 3.2 # rotateBy (1/10))
