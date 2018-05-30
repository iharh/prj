#!/usr/bin/env pwsh
# run with: TERM=xterm <this-script>.ps1
$newPath=$env:PATH

# dot-folders are automatically filtered out without -force
get-childitem -path "/data/wrk/bin/cmd" -recurse | where psiscontainer |
foreach-object {
    $dirname = $_.fullname
    $newPath="${newPath}:${dirname}"
    echo "dirname: ${dirname}"
}
$env:PATH=$newPath
echo "PATH: ${env:PATH}"
