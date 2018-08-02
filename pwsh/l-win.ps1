#!/usr/bin/env pwsh
# run with: TERM=xterm <this-script>.ps1
$newPath=$env:PATH

# dot-folders are automatically filtered out without -force
# "/data/wrk/bin/cmd"
get-childitem -path "d:/dev/bin/cmd" -recurse | where psiscontainer |
foreach-object {
    $dirname = $_.fullname
    $newPath="${newPath}:${dirname}"
    echo "dirname: ${dirname}"
}
$env:PATH=$newPath
echo "PATH: ${env:PATH}"
