@echo off
docker build -t aiit-csv-server .
docker run -d -p 8080:8080 -v "%cd%\plots_data:/data/plots" --name csv-server aiit-csv-server
pause
