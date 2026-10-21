<#
.SYNOPSIS
    Convenient developer testing and build tool for QuickGlance.
.DESCRIPTION
    Allows running automated tests, building all mod jars, or launching dev Minecraft clients.
.EXAMPLE
    .\test-mod.ps1 -Test
    .\test-mod.ps1 -BuildAll
    .\test-mod.ps1 -RunClient -Version 26.3 -Loader fabric
    .\test-mod.ps1 -RunClient -Version 1.21.1 -Loader neoforge
#>

[CmdletBinding()]
param (
    [switch]$Test,
    [switch]$BuildAll,
    [switch]$RunClient,
    [switch]$Clean,
    [ValidateSet("26.3", "1.21.1", "1.20.1")]
    [string]$Version = "26.3",
    [ValidateSet("fabric", "neoforge")]
    [string]$Loader = "fabric"
)

$ErrorActionPreference = "Stop"

function Write-Header([string]$text) {
    Write-Host "`n=== $text ===" -ForegroundColor Cyan
}

if ($Clean) {
    Write-Header "Cleaning project"
    .\gradlew.bat clean
    exit 0
}

if ($Test) {
    Write-Header "Running Automated Unit Tests across all targets"
    .\gradlew.bat test
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`nAll automated tests passed successfully!" -ForegroundColor Green
    }
    exit $LASTEXITCODE
}

if ($BuildAll) {
    Write-Header "Building and collecting all mod JARs"
    .\gradlew.bat buildAndCollect
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`nBuild complete! Jars in build/libs:" -ForegroundColor Green
        Get-ChildItem build/libs/*.jar | Select-Object Name, Length, LastWriteTime | Format-Table -AutoSize
    }
    exit $LASTEXITCODE
}

if ($RunClient) {
    if ($Version -eq "1.20.1" -and $Loader -eq "neoforge") {
        Write-Error "1.20.1 only targets Fabric (Forge is not targeted)."
        exit 1
    }

    $projectPath = ":$Version-$Loader"
    $taskName = "$projectPath:runClient"
    Write-Header "Launching Minecraft $Version ($Loader) Client via $taskName"
    .\gradlew.bat $taskName
    exit $LASTEXITCODE
}

# Default action if no flags provided: display help & quick menu
Write-Host "QuickGlance Test & Build Tool" -ForegroundColor Yellow
Write-Host "Usage:"
Write-Host "  .\test-mod.ps1 -Test                                     # Run unit test suite"
Write-Host "  .\test-mod.ps1 -BuildAll                                 # Build all 5 target JARs"
Write-Host "  .\test-mod.ps1 -RunClient -Version 26.3 -Loader fabric   # Run 26.3 Fabric dev client"
Write-Host "  .\test-mod.ps1 -RunClient -Version 26.3 -Loader neoforge # Run 26.3 NeoForge dev client"
Write-Host "  .\test-mod.ps1 -RunClient -Version 1.21.1 -Loader fabric # Run 1.21.1 Fabric dev client"
Write-Host "  .\test-mod.ps1 -RunClient -Version 1.20.1 -Loader fabric # Run 1.20.1 Fabric dev client"
Write-Host "  .\test-mod.ps1 -Clean                                    # Clean build cache"
