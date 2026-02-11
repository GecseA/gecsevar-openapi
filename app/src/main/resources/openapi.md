# Get OpenAPI cli

[cli 7.12.0] (https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/7.12.0/openapi-generator-cli-7.12.0.jar)

# Generate DEBUG
cd .\app\src\main\resources
java.exe -jar openapi-generator-cli-7.12.0.jar generate -g go -o out -i test_1.yml --global-property debugModels=true,debugOperations=true,debugSupportingFiles=true

# Install swagger-cli
npm install -g @apidevtools/swagger-cli
# Merge splitted to avoid java generater wrapper "problems"
.\openapi-mygenerator\gecsevar-openapi\app\src\main\resources> swagger-cli bundle .\splitted\SplittedExample.yml -o .\splitted\bundled-api.yml  --type=yaml --format 2