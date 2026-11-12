param(
    [string]$MysqlHome = 'C:\Program Files\MySQL\MySQL Server 8.4'
)

$ErrorActionPreference = 'Stop'
$mysqlServer = Join-Path $MysqlHome 'bin/mysqld.exe'
$mysqlClient = Join-Path $MysqlHome 'bin/mysql.exe'
$mysqlAdmin = Join-Path $MysqlHome 'bin/mysqladmin.exe'
foreach ($binary in @($mysqlServer, $mysqlClient, $mysqlAdmin)) {
    if (-not (Test-Path -LiteralPath $binary)) { throw "Executável não encontrado: $binary" }
}

$scratch = Join-Path ([IO.Path]::GetTempPath()) ('seugame-jpa-' + [guid]::NewGuid().ToString('N'))
$dataDirectory = Join-Path $scratch 'data'
$backendDirectory = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$savedEnvironment = @{}
foreach ($name in @('TEST_DB_URL', 'TEST_HTTP_DB_URL', 'TEST_DB_USER', 'TEST_DB_PASSWORD')) {
    $savedEnvironment[$name] = [Environment]::GetEnvironmentVariable($name, 'Process')
}
$server = $null
$testPort = $null
$mavenExitCode = 1

try {
    New-Item -ItemType Directory -Path $scratch | Out-Null
    & $mysqlServer --no-defaults --initialize-insecure "--basedir=$MysqlHome" "--datadir=$dataDirectory" "--log-error=$scratch/initialize.err"
    if ($LASTEXITCODE -ne 0) { throw "Falha na inicialização. Consulte $scratch/initialize.err" }

    $listener = [Net.Sockets.TcpListener]::new([Net.IPAddress]::Loopback, 0)
    $listener.Start()
    $testPort = $listener.LocalEndpoint.Port
    $listener.Stop()
    $arguments = @('--no-defaults', "--basedir=`"$MysqlHome`"", "--datadir=`"$dataDirectory`"",
        '--bind-address=127.0.0.1', "--port=$testPort", '--mysqlx=0', '--skip-log-bin',
        '--local-infile=0', '--secure-file-priv=NULL', "--log-error=`"$scratch/server.err`"")
    $server = Start-Process -FilePath $mysqlServer -ArgumentList $arguments -WindowStyle Hidden -PassThru
    $connection = @('--no-defaults', '--protocol=TCP', '--host=127.0.0.1', "--port=$testPort", '--user=root')
    $ready = $false
    for ($attempt = 0; $attempt -lt 100; $attempt++) {
        if ($server.HasExited) { throw "MySQL temporário encerrou. Consulte $scratch/server.err" }
        & $mysqlAdmin @connection ping --silent 2>$null | Out-Null
        if ($LASTEXITCODE -eq 0) { $ready = $true; break }
        Start-Sleep -Milliseconds 200
    }
    if (-not $ready) { throw 'A instância temporária não respondeu em tempo hábil.' }

    $databaseName = 'seugame_jpa_it_' + [guid]::NewGuid().ToString('N')
    & $mysqlClient @connection --execute="CREATE DATABASE $databaseName CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci"
    if ($LASTEXITCODE -ne 0) { throw 'Não foi possível criar o banco temporário.' }
    $httpDatabase = $databaseName + '_http'
    & $mysqlClient @connection --execute="CREATE DATABASE $httpDatabase CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci"
    if ($LASTEXITCODE -ne 0) { throw 'Não foi possível criar o banco HTTP temporário.' }
    $env:TEST_HTTP_DB_URL = "jdbc:mysql://127.0.0.1:$testPort/${httpDatabase}?connectionTimeZone=UTC"
    $env:TEST_DB_URL = "jdbc:mysql://127.0.0.1:$testPort/${databaseName}?connectionTimeZone=UTC"
    $env:TEST_DB_USER = 'root'
    $env:TEST_DB_PASSWORD = ''
    Push-Location $backendDirectory
    try {
        & mvn -B -ntp -Pmysql-it verify
        $mavenExitCode = $LASTEXITCODE
    } finally { Pop-Location }
} finally {
    foreach ($name in $savedEnvironment.Keys) {
        [Environment]::SetEnvironmentVariable($name, $savedEnvironment[$name], 'Process')
    }
    if ($null -ne $server -and -not $server.HasExited) {
        & $mysqlAdmin --no-defaults --protocol=TCP --host=127.0.0.1 "--port=$testPort" --user=root shutdown 2>$null | Out-Null
        if (-not $server.WaitForExit(10000)) {
            Stop-Process -Id $server.Id
            $server.WaitForExit()
        }
    }
    $temporaryRoot = [IO.Path]::GetFullPath([IO.Path]::GetTempPath()).TrimEnd('\', '/')
    $resolvedScratch = [IO.Path]::GetFullPath($scratch)
    if ([IO.Path]::GetDirectoryName($resolvedScratch) -ne $temporaryRoot -or
        [IO.Path]::GetFileName($resolvedScratch) -notmatch '^seugame-jpa-[a-f0-9]{32}$') {
        throw 'Alvo de limpeza temporária inválido; nenhum diretório será removido.'
    }
    if (Test-Path -LiteralPath $resolvedScratch) {
        if ((Get-Item -LiteralPath $resolvedScratch).Attributes -band [IO.FileAttributes]::ReparsePoint) {
            throw 'O diretório temporário é um link; limpeza recusada.'
        }
        if (Test-Path -LiteralPath $dataDirectory) {
            if ((Get-Item -LiteralPath $dataDirectory).Attributes -band [IO.FileAttributes]::ReparsePoint) {
                throw 'O diretório de dados temporário é um link; limpeza recusada.'
            }
            Remove-Item -LiteralPath $dataDirectory -Recurse -Force
        }
        if ($mavenExitCode -eq 0) { Remove-Item -LiteralPath $resolvedScratch -Recurse -Force }
        else { Write-Output "Logs temporários preservados em: $resolvedScratch" }
    }
}
exit $mavenExitCode
