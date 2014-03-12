{-# OPTIONS -fno-warn-missing-signatures -fno-warn-unused-binds #-}
-- -fno-warn-unused-matches 
import Pipes
--import qualified Pipes.Prelude as P

-- The original version is
main01 = do { putStrLn "s1"; putStrLn "s2" }
--
-- Desugaring the do-notation:
main02 = putStrLn "s1" >> putStrLn "s2"
--
-- Using the definition from Monad:  m >> k = m >>= \_ -> k
main03 = putStrLn "s1" >>= \_ -> putStrLn "s2"
--

p1 :: Producer String IO ()
p1 = do
  yield ("str1"::String)
  yield ("str2"::String)

c1 :: Consumer String IO ()
c1 = do
     s1 <- await
     lift $ putStrLn s1
     s2 <- await
     lift $ putStrLn s2

main :: IO ()
main = runEffect $ p1 >-> c1

--main = runEffect $ for (each ([1..]::[Int]) `P.zip` P.stdinLn) $ \(i, s) -> liftIO $ withFile (printf "%03d" i ++ ".out") WriteMode (\h -> hPutStrLn h s)

