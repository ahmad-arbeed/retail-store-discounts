#!/bin/bash

open_html_report() {
    local report_path="./target/site/jacoco/index.html"
    if [ -f "$report_path" ]; then
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            xdg-open "$report_path"
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            open "$report_path"
        elif [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "win32" ]]; then
            start "$report_path"
        else
            echo "Unsupported OS. Please open $report_path manually."
        fi
    else
        echo "JaCoCo report not found for $module_path"
    fi
}

echo "Building..."
mvn clean compile

echo "Run Test Cases with Jacoco"
mvn test

echo "Opening Jacoco Report"
mvn jacoco:report
open_html_report