module HuskSpec (spec) where

import Husk (husk)
import Test.Hspec
import Test.Hspec.QuickCheck

spec :: Spec
spec = do
    describe "husk" $ do
        it "returns the unit value" $ do
            husk `shouldBe` ()

        prop "equals the unit value" $
            \ x -> husk == x
