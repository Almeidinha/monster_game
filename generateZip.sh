#!/bin/bash

# Variable to track unauthorized changes
UNAUTHORIZED_CHANGE=false
# Color Variables
GREEN='\033[0;32m'
RED='\033[0;31m'
WHITE='\033[1;37m'

# Get the list of changed files
while IFS= read -r file; do
    # Check if the file has one of the specified extensions
    if echo "$file" | grep -qE "\.(kt|tsx|py|go|ts|rb|js|java|ex|cs|dart|vue|rs|swift|php|scala)$"; then
        if ! echo "$file" | grep -qi "extended"; then
            echo -e "${RED}Error: You have modified $file, which is not allowed."
            UNAUTHORIZED_CHANGE=true
        fi
    fi
done < <(git diff --name-only HEAD)

# Check if unauthorized changes were found and exit accordingly
if [ "$UNAUTHORIZED_CHANGE" = true ]; then
    echo -e "${RED}Unauthorized changes detected, please remove changes of files above to create ZIP file."
    exit 1
else
    echo -e "${GREEN}No unauthorized changes detected, ALL GOOD."

    # Remove generated folders for various technologies
    echo -e "${WHITE}Removing unnecessary autogenerated folders and files..."

    # Javascript libraries and frameworks
    rm -rf node_modules dist build out tmp

    # Python libraries and frameworks
    rm -rf venv __pycache__ staticfiles mediafiles instance

    # C++, Go, .Net libraries and frameworks
    rm -rf bin obj pkg

    # Java libraries and frameworks
    rm -rf target

    # Ruby on Rails libraries and frameworks
    rm -rf log tmp vendor/bundle

    # Swift
    rm -rf .build DerivedData

    # Android
    rm -rf app/intermediates out

    echo -e "${GREEN}Unnecessary autogenerated folders and files removed successfully"

    # Get the current directory name
    CURRENT_DIR_NAME=$(basename "$PWD")

    # Define the name of the zip file with full path
    FILE_NAME="$CURRENT_DIR_NAME.zip"
    ZIP_FILE_FULL_PATH="$PWD/$FILE_NAME"

    echo -e "${WHITE}Compressing challenge into $FILE_NAME..."

    # Compress the remaining files into a zip file using zip
    zip -rq "$ZIP_FILE_FULL_PATH" . -x '*.git*'

    echo -e "${GREEN}Compression complete: $ZIP_FILE_FULL_PATH"


    # Calculate the SHA256 hash of the ZIP file
    # Determine the operating system
    OS_TYPE=$(uname)
    if [ "$OS_TYPE" == "Linux" ]; then
        SHA256_HASH=$(sha256sum "$ZIP_FILE_FULL_PATH" | awk '{ print $1 }')
    elif [ "$OS_TYPE" == "Darwin" ]; then
        SHA256_HASH=$(shasum -a 256 "$ZIP_FILE_FULL_PATH" | awk '{ print $1 }')
    else
        echo "Unsupported OS: $OS_TYPE"
        exit 1
    fi

    echo -e "${WHITE}SHA256 hash: $SHA256_HASH"

    exit 0
fi