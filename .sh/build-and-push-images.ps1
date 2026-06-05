param(
    [string]$RegistryUser = "timeflyflower",
    [string]$Tag = "latest",
    [switch]$NoPush,
    [switch]$SkipInstall
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Require-Command {
    param([string]$Name)
    if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
        throw "Missing command: $Name"
    }
}

function Run-Step {
    param([string]$Title, [scriptblock]$Action)
    Write-Host ""
    Write-Host "==> $Title" -ForegroundColor Cyan
    $global:LASTEXITCODE = 0
    & $Action
    if ($global:LASTEXITCODE -ne 0) {
        throw "Step failed: $Title (exit code: $global:LASTEXITCODE)"
    }
}

function Build-Backend {
    param([string]$Root, [string]$Tag)
    Push-Location (Join-Path $Root "blog-backend")
    try {
        Run-Step "Maven package (backend)" { mvn clean package -DskipTests }
        Run-Step "Docker build blog-backend:$Tag" { docker build -t "blog-backend:$Tag" . }
    }
    finally {
        Pop-Location
    }
}

function Build-Admin {
    param([string]$Root, [string]$Tag, [switch]$SkipInstall)
    Push-Location (Join-Path $Root "blog-frontend\\kuailemao-admin")
    try {
        $env:HUSKY = "0"
        if (-not $SkipInstall) {
            Run-Step "pnpm install (admin)" { pnpm install }
        }
        Run-Step "pnpm build (admin)" { pnpm build }
        Run-Step "Docker build blog-admin:$Tag" { docker build -t "blog-admin:$Tag" . }
    }
    finally {
        Pop-Location
    }
}

function Build-Front {
    param([string]$Root, [string]$Tag, [switch]$SkipInstall)
    Push-Location (Join-Path $Root "blog-frontend\\kuailemao-blog")
    try {
        $env:HUSKY = "0"
        if (-not $SkipInstall) {
            Run-Step "pnpm install (front)" { pnpm install }
        }
        Run-Step "pnpm build (front)" { pnpm build }
        Run-Step "Docker build blog-front:$Tag" { docker build -t "blog-front:$Tag" . }
    }
    finally {
        Pop-Location
    }
}

function Tag-And-Push {
    param([string]$LocalName, [string]$RemoteName, [string]$Tag)
    $remote = "$RemoteName`:$Tag"
    Run-Step "Tag ${LocalName}:$Tag -> $remote" { docker tag "${LocalName}:$Tag" $remote }
    Run-Step "Push $remote" { docker push $remote }
}

Require-Command mvn
Require-Command pnpm
Require-Command docker

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$root = if (Test-Path (Join-Path $scriptDir "blog-backend")) { $scriptDir } else { Split-Path -Parent $scriptDir }

Write-Host "Workspace: $root"
Write-Host "Registry user: $RegistryUser"
Write-Host "Tag: $Tag"

Build-Backend -Root $root -Tag $Tag
Build-Admin -Root $root -Tag $Tag -SkipInstall:$SkipInstall
Build-Front -Root $root -Tag $Tag -SkipInstall:$SkipInstall

if (-not $NoPush) {
    Write-Host ""
    Write-Host "If needed, login first: docker login -u $RegistryUser" -ForegroundColor Yellow
    Tag-And-Push -LocalName "blog-backend" -RemoteName "$RegistryUser/blog-backend" -Tag $Tag
    Tag-And-Push -LocalName "blog-admin" -RemoteName "$RegistryUser/blog-admin" -Tag $Tag
    Tag-And-Push -LocalName "blog-front" -RemoteName "$RegistryUser/blog-front" -Tag $Tag
}

Write-Host ""
Write-Host "Done." -ForegroundColor Green
