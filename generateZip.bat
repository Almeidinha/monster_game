@echo off
setlocal enabledelayedexpansion

REM Variable to track unauthorized changes
set UNAUTHORIZED_CHANGE=false
REM Define the list of allowed extensions
set ALLOWED_EXTENSIONS=kt tsx py go ts rb js java ex cs dart vue rs swift php scala

REM Get the list of changed files
for /f "tokens=*" %%f in ('git diff --name-only HEAD') do (
    set "file=%%f"
    set match=false

    REM Check if the file has one of the specified extensions
    for %%e in (%ALLOWED_EXTENSIONS%) do (
        REM Output file with a "." to ensure end of the string i.e: "API/appsettings.json." for some reason "$" not working on the regex
        echo "!file!." | findstr /RI "\.%%e\." >nul
        if !errorlevel! equ 0 (
            set match=true
        )
    )
    
    if !match! equ true (
        REM Check if the file does not contain "extended" or "Extended"
        echo "!file!" | findstr /i "extended" >nul
        if !errorlevel! neq 0 (
            echo [31mError: You have modified !file!, which is not allowed.[0m
            set UNAUTHORIZED_CHANGE=true
        )
    )
)

REM Check if unauthorized changes were found and exit accordingly
if !UNAUTHORIZED_CHANGE! equ true (
    echo [31mUnauthorized changes detected, please remove changes of files above to create ZIP file.[0m
    exit /b 1
) else (
    echo [32mNo unauthorized changes detected, ALL GOOD.[0m

    REM Remove generated folders for various technologies
    echo Removing unnecessary autogenerated folders and files

    REM Javascript libraries and frameworks
    rmdir /s /q node_modules 2>nul
    rmdir /s /q dist 2>nul
    rmdir /s /q build 2>nul
    rmdir /s /q out 2>nul
    rmdir /s /q tmp 2>nul

    REM Python libraries frameworks
    rmdir /s /q venv 2>nul
    rmdir /s /q __pycache__ 2>nul
    rmdir /s /q staticfiles 2>nul
    rmdir /s /q mediafiles 2>nul
    rmdir /s /q instance 2>nul

    REM C++ Go .Net libraries frameworks
    rmdir /s /q bin 2>nul
    rmdir /s /q obj 2>nul
    rmdir /s /q pkg 2>nul

    REM Java libraries frameworks
    rmdir /s /q target 2>nul

    REM Ruby on Rails libraries frameworks
    rmdir /s /q log 2>nul
    rmdir /s /q tmp 2>nul
    rmdir /s /q vendor\bundle 2>nul

    REM Swift
    rmdir /s /q .build 2>nul
    rmdir /s /q DerivedData 2>nul

    REM Android
    rmdir /s /q app\intermediates 2>nul
    rmdir /s /q out 2>nul

    echo [32mUnnecessary autogenerated folders and files removed successfully[0m

    REM Get the current directory name
    for %%i in ("%CD%") do set "CURRENT_DIR_NAME=%%~nxi"

    REM Define the name of the zip file with full path
    set FILE_NAME=!CURRENT_DIR_NAME!.zip
    set ZIP_FILE_FULL_PATH=%CD%\!FILE_NAME!

    echo Compressing challenge into !FILE_NAME!

    REM Compress the remaining files into a zip file using
    REM By default Windows do not include hidden folders like .git
    powershell -command "& {Compress-Archive -Path * -DestinationPath '!ZIP_FILE_FULL_PATH!' -Force}"

    echo [32mCompression complete: !ZIP_FILE_FULL_PATH![0m

    REM Calculate the SHA256 hash of the ZIP file
    certUtil -hashfile "!FILE_NAME!" SHA256

    exit /b 0
)
