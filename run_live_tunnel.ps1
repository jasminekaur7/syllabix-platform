# ==============================================================================
# Syllabix - Live Public Deployment Runner
# ==============================================================================

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host "  SYLLABIX - Launching Live Public Web Link                      " -ForegroundColor Yellow
Write-Host "=================================================================" -ForegroundColor Cyan

# 1. Stop any stale background java/ssh processes holding port 8080
$portInUse = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host "Releasing port 8080..." -ForegroundColor Gray
    Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force
    Start-Sleep -Seconds 1
}

# 2. Start Spring Boot Server
Write-Host "[1/3] Starting Spring Boot Server on port 8080..." -ForegroundColor White
$serverProc = Start-Process java -ArgumentList "-jar","target\syllabix-platform-1.0.0.jar","--server.port=8080" -RedirectStandardOutput "boot_live_out.log" -RedirectStandardError "boot_live_err.log" -PassThru

# 3. Wait for HTTP 200 on /login
$serverReady = $false
for ($i = 0; $i -lt 35; $i++) {
    Start-Sleep -Seconds 1
    try {
        $res = Invoke-WebRequest -Uri "http://localhost:8080/login" -UseBasicParsing -TimeoutSec 2 -ErrorAction SilentlyContinue
        if ($res -and $res.StatusCode -eq 200) {
            $serverReady = $true
            break
        }
    } catch {}
}

if (-not $serverReady) {
    Write-Host "Server failed to start. Last log lines:" -ForegroundColor Red
    Get-Content "boot_live_out.log" -Tail 15
    exit 1
}

Write-Host " -> Spring Boot Backend is LIVE & HEALTHY (HTTP 200 OK)" -ForegroundColor Green

# 4. Launch SSH Tunnel
Write-Host "[2/3] Establishing secure public HTTPS tunnel via localhost.run..." -ForegroundColor White
Remove-Item -Path "tunnel_live.log", "tunnel_live_err.log" -Force -ErrorAction SilentlyContinue
$tunnelProc = Start-Process ssh -ArgumentList "-o","StrictHostKeyChecking=no","-R","80:localhost:8080","nokey@localhost.run" -RedirectStandardOutput "tunnel_live.log" -RedirectStandardError "tunnel_live_err.log" -PassThru

# 5. Extract public URL
$publicUrl = ""
for ($j = 0; $j -lt 20; $j++) {
    Start-Sleep -Seconds 1
    if (Test-Path "tunnel_live.log") {
        $content = Get-Content "tunnel_live.log" -Raw
        if ($content -match "(https:\/\/[a-zA-Z0-9\-\.]+\.lhr\.life)") {
            $publicUrl = $matches[1]
            break
        }
    }
}

if ($publicUrl) {
    Write-Host "=================================================================" -ForegroundColor Green
    Write-Host "  LIVE RUNNING DEPLOYED LINK IS READY!                           " -ForegroundColor Green
    Write-Host "  $publicUrl" -ForegroundColor Cyan
    Write-Host "=================================================================" -ForegroundColor Green
    Write-Host "Access it now from any phone, laptop, or browser anywhere in the world!"
    Write-Host "Login: jkaur_be24@thapar.edu | Password: password123"
} else {
    Write-Host "Tunnel output:" -ForegroundColor Yellow
    Get-Content "tunnel_live.log" -Tail 10
}
