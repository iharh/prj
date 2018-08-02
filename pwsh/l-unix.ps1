#!/usr/bin/env pwsh
# run with: TERM=xterm <this-script>.ps1
$newPath=$env:PATH

# dot-folders are automatically filtered out without -force
# "/data/wrk/bin/cmd"
get-childitem -path "/data/wrk/notes/util" -recurse | where psiscontainer |
foreach-object {
    $dirname = $_.fullname
    $newPath="${newPath}:${dirname}"
    echo "dirname: ${dirname}"
}
# echo "newPath: ${newPath}"
# $env:PATH=$newPath
# echo "PATH: ${env:PATH}"
