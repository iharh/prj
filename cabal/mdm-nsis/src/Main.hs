{-# LANGUAGE OverloadedStrings #-}

import Development.NSIS

main :: IO ()
main = writeFile "mdm.nsi" $ nsis $ do
    _ <- constantStr "MANUFACT" "Clarabridge"
    _ <- constantStr "PRODUCTNAME" "$MANUFACT CMP"

    _ <- constantStr "MDM" "mdm"
    mdmName <- constantStr "MDMNAME" "MADAMIRA"

    name mdmName                      -- The name of the installer
    outFile "$MDM.exe"                -- Where to produce the installer
    -- [HKEY_LOCAL_MACHINE\Software\Wow6432Node\Microsoft\Windows\CurrentVersion\Uninstall\Clarabridge CMP] "UninstallString"="D:\\clb\\inst\\uninst.exe"
    installDir "$PROGRAMFILES64\\$MANUFACT\\$PRODUCTNAME" -- The default installation directory

    -- Registry key to check for directory (so if you install again, it will overwrite the old one automatically)
    installDirRegKey HKLM "Software/$MANUFACT/$MDMNAME" "InstallLocation"

    requestExecutionLevel Admin      -- Request application privileges for Windows Vista

    -- Pages to display
    page Directory                   -- Pick where to install
    page InstFiles                   -- Give a progress bar while installing

    -- The root stuff to install
    section mdmName [Required] $ do
        -- Write the installation path into the registry
        writeRegStr HKLM "Software/$MANUFACT/$MDMNAME" "InstallLocation" "$INSTDIR"

        -- Write the uninstall keys
        writeRegStr SHCTX "Software/Microsoft/Windows/CurrentVersion/Uninstall/$MDMNAME" "DisplayName"     "$MDMNAME Analyzer"
        writeRegStr SHCTX "Software/Microsoft/Windows/CurrentVersion/Uninstall/$MDMNAME" "UninstallString" "\"$INSTDIR/uninstall_$MDM.exe\""

        -- Set output path to the installation directory.
	setOutPath "$INSTDIR"        -- Where to install files in this section
	-- Write Uninstaller
        writeUninstaller "uninstall_$MDM.exe"

    -- The thirdparty stuff to install
    section "thirdparty" [Required] $ do
        -- Set output path to the installation directory.
	setOutPath "$INSTDIR\\thirdparty\\$MDMNAME"        -- Where to install files in this section

	-- Macro
	wordReplaceS "abc_$MDMNAME"

        -- Put file there
	-- SetCompress off
        file [Recursive] "D:\\clb\\src\\main\\lib\\MADAMIRA\\release-11072013-1.0-beta\\*"

    -- Uninstaller
    uninstall $ do
        -- Remove registry keys
        deleteRegKey SHCTX "Software/Microsoft/Windows/CurrentVersion/Uninstall/$MDMNAME"
        deleteRegKey HKLM "Software/$MANUFACT/$MDMNAME"

        -- Remove directories used
        rmdir [Recursive] "$INSTDIR\\thirdparty\\$MDMNAME"

        -- Remove files and uninstaller
        delete [] "$INSTDIR/uninstall_$MDM.exe"
