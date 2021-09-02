# documents_manager

- Swagger UI: http://localhost:8080/swagger-ui/

- Command line example to call endpoint to analyze log file:  
    curl -X POST \ 
    http://localhost:8080/document/analyze \ 
    -H 'cache-control: no-cache' \ 
    -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \ 
    -H 'postman-token: bc5690f7-7dca-06dc-e1d5-e7519860616d' \ 
    -F logFile=@server1.txt