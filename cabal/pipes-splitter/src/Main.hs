import System.IO(withFile, hPutStrLn, IOMode(..))
import Text.Printf(printf)
import Pipes
import qualified Pipes.Prelude as P

main :: IO ()
main = runEffect $ for (each ([1..]::[Int]) `P.zip` P.stdinLn) $ \(i, s) -> liftIO $ withFile (printf "%03d" i ++ ".out") WriteMode (\h -> hPutStrLn h s)

