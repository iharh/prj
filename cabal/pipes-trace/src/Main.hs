{-# OPTIONS -fno-warn-missing-signatures -fno-warn-unused-binds #-}
module Main(main, main01, main02, main03) where
-- -fno-warn-unused-matches 
import Pipes
-- import Pipes.Core
import Pipes.Internal
--import qualified Pipes.Prelude as P

--
-- The original version is
main01 = do { putStrLn "s1"; putStrLn "s2" }
--
-- Desugaring the do-notation:
main02 = putStrLn "s1" >> putStrLn "s2"

{-| Using the definition from 'Monad':

@
('>>') :: 'Monad' m => m a -> m b -> m b
@

> m >> k = m >>= \_ -> k

    We will get:
-}
main03 = putStrLn "s1" >>= \_ -> putStrLn "s2"

{-| Let's put some basic 'pipes' stuff into play
-}
main04 :: IO ()
main04 = runEffect $ p1 >-> c1
    where
	p1 :: Producer String IO ()
	p1 = do
	    yield ("st1"::String)
	    yield ("st2"::String)

	c1 :: Consumer String IO ()
	c1 = do
	     s1 <- await
	     lift $ putStrLn s1
	     s2 <- await
	     lift $ putStrLn s2

main :: IO ()
-- main = runEffect $ for (yield "str0") (lift . putStrLn)
-- main = runEffect $ for (yield "str0") (\str -> lift $ putStrLn str)
-- main = runEffect $ for (yield "str0") (lift . putStrLn)
-- main = runEffect $ (Respond "str0" Pure) //> (\str -> liftIO $ putStrLn str)
-- main = runEffect $ (Respond "str0" Pure) //> (\str -> M (putStrLn str >>= \r -> return (Pure r)))
-- main = runEffect $ M (putStrLn "str0" >>= \r -> return (Pure r)) >>= \b' -> Pure b'
main = runEffect $ M (putStrLn "str0" >>= \r -> return (Pure r))



--main = runEffect $ for (each ([1..]::[Int]) `P.zip` P.stdinLn) $ \(i, s) -> liftIO $ withFile (printf "%03d" i ++ ".out") WriteMode (\h -> hPutStrLn h s)

