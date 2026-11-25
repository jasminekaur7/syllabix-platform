# ==============================================================================
# Syllabix - Production Live Server & Cloud Tunnel Daemon
# ==============================================================================

Write-Host ">>> Initializing Syllabix Live Server on Port 8080..." -ForegroundColor Cyan

# 1. Clean stale processes
Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 1

# 2. Start Spring Boot Server
$javaProc = Start-Process java -ArgumentList "-jar","target\syllabix-platform-1.0.0.jar","--server.port=8080" -RedirectStandardOutput "boot_live.log" -RedirectStandardError "boot_err.log" -PassThru

# 3. Wait for HTTP 200 on /login
$ready = $false
for ($i = 0; $i -lt 40; $i++) {
    Start-Sleep -Seconds 1
    try {
        $res = Invoke-WebRequest -Uri "http://localhost:8080/login" -UseBasicParsing -TimeoutSec 2 -ErrorAction SilentlyContinue
        if ($res -and $res.StatusCode -eq 200) {
            $ready = $true
            break
        }
    } catch {}
}

if (-not $ready) {
    Write-Host ">>> ERROR: Server failed to start on port 8080." -ForegroundColor Red
    Get-Content "boot_live.log" -Tail 20
    exit 1
}

Write-Host ">>> [SUCCESS] Spring Boot Application is LIVE on port 8080!" -ForegroundColor Green
Write-Host ">>> Launching public HTTPS tunnel via localhost.run..." -ForegroundColor Cyan

# 4. Run SSH tunnel in foreground so process remains active indefinitely
ssh -o StrictHostKeyChecking=no -o ServerAliveInterval=30 -R 80:localhost:8080 nokey@localhost.run
