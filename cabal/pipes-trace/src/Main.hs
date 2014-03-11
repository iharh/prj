import Pipes
--import qualified Pipes.Prelude as P

main :: IO ()
main = putStrLn "Hello World"

--main = runEffect $ for (each ([1..]::[Int]) `P.zip` P.stdinLn) $ \(i, s) -> liftIO $ withFile (printf "%03d" i ++ ".out") WriteMode (\h -> hPutStrLn h s)

